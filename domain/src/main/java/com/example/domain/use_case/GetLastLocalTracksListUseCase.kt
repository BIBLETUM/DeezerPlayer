package com.example.domain.use_case

import com.example.domain.Track
import com.example.domain.repository.ChartRepository
import javax.inject.Inject
import javax.inject.Named

class GetLastLocalTracksListUseCase @Inject constructor(
    @Named("LocalRepository") private val repository: ChartRepository
) : IGetLastLocalTracksListUseCase {

    override operator fun invoke(): List<Track> {
        return repository.getLastTracksList()
    }

}

interface IGetLastLocalTracksListUseCase {
    operator fun invoke(): List<Track>
}