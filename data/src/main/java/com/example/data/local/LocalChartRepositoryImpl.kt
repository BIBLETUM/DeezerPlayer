package com.example.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.data.local.mapper.LocalTrackMapper
import com.example.data.local.model.MusicFile
import com.example.domain.Track
import com.example.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class LocalChartRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val localTrackMapper: LocalTrackMapper,
) : ChartRepository {

    private val _tracksFlow = MutableSharedFlow<List<Track>>(replay = 1)

    override fun getTracksFlow(): Flow<List<Track>> {
        _tracksFlow.tryEmit(getMusicFiles().map { localTrackMapper.mapMusicFileToDomain(it) })
        return _tracksFlow.asSharedFlow()
    }

    override suspend fun searchTracks(query: String) {
        val filteredTracks = when (query.length) {
            0 -> {
                getMusicFiles().map { localTrackMapper.mapMusicFileToDomain(it) }
            }

            else -> {
                searchMusicFiles(query).map { localTrackMapper.mapMusicFileToDomain(it) }
            }
        }

        _tracksFlow.emit(filteredTracks)
    }

    private fun getMusicFiles(): List<MusicFile> {
        val cursor = queryMusicFiles() ?: return emptyList()
        return parseCursor(cursor)
    }

    private fun searchMusicFiles(query: String): List<MusicFile> {
        val selection = String.format(
            SELECTION_FORMAT,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM
        )

        val selectionArgs = arrayOf("%$query%", "%$query%", "%$query%")

        val cursor = queryMusicFiles(selection, selectionArgs) ?: return emptyList()
        return parseCursor(cursor)
    }

    private fun queryMusicFiles(
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): Cursor? {
        return contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            getProjection(),
            selection,
            selectionArgs,
            String.format(ORDER_BY_TITLE, MediaStore.Audio.Media.TITLE)
        )
    }

    private fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
    }

    private fun parseCursor(cursor: Cursor): List<MusicFile> {
        val musicList = mutableListOf<MusicFile>()
        cursor.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                musicList.add(
                    mapToMusicFile(
                        it,
                        idColumn,
                        titleColumn,
                        artistColumn,
                        albumColumn,
                        durationColumn,
                        albumIdColumn
                    )
                )
            }
        }
        return musicList
    }

    private fun mapToMusicFile(
        cursor: Cursor,
        idColumn: Int,
        titleColumn: Int,
        artistColumn: Int,
        albumColumn: Int,
        durationColumn: Int,
        albumIdColumn: Int
    ): MusicFile {
        val id = cursor.getLong(idColumn)
        val title = cursor.getString(titleColumn)
        val artist = cursor.getString(artistColumn)
        val album = cursor.getString(albumColumn)
        val duration = cursor.getLong(durationColumn)
        val albumId = cursor.getLong(albumIdColumn)
        val albumCoverUri = getAlbumArtUri(albumId)

        return MusicFile(
            id = id,
            title = title,
            artistName = artist,
            albumName = album,
            lengthInMillis = duration,
            albumCover = albumCoverUri.toString()
        )
    }

    private fun getAlbumArtUri(albumId: Long): Uri {
        val sArt = Uri.parse(URI_COVER_ART)
        return ContentUris.withAppendedId(sArt, albumId)
    }

    companion object {
        private const val URI_COVER_ART = "content://media/external/audio/albumart"
        private const val SELECTION_FORMAT = "%s LIKE ? OR %s LIKE ? OR %s LIKE ?"
        private const val ORDER_BY_TITLE = "%s ASC"
    }
}