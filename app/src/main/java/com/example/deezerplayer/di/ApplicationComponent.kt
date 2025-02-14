package com.example.deezerplayer.di

import android.content.ContentResolver
import android.content.Context
import com.example.deezerplayer.ViewModelFactory
import com.example.deezerplayer.player.service.DeezerAudioService
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [DataModule::class,
        ViewModelModule::class, DataModule::class,
        DomainModule::class,
        PlayerModule::class]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getPlayerScreenComponentFactory(): PlayerScreenComponent.Factory

    fun inject(service: DeezerAudioService)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance contentResolver: ContentResolver,
        ): ApplicationComponent
    }

}