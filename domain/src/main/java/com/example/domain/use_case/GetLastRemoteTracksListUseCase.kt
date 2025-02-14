package com.example.domain.use_case

import com.example.domain.Track
import com.example.domain.repository.ChartRepository
import javax.inject.Inject
import javax.inject.Named

class GetLastRemoteTracksListUseCase @Inject constructor(
    @Named("RemoteRepository") private val repository: ChartRepository
) : IGetLastRemoteTracksListUseCase {

    override operator fun invoke(): List<Track> {
        return repository.getLastTracksList()
    }

}

interface IGetLastRemoteTracksListUseCase {
    operator fun invoke(): List<Track>
}