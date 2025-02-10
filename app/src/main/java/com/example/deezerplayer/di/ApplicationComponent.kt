package com.example.deezerplayer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [DataModule::class, ViewModelModule::class, DataModule::class]
)
interface ApplicationComponent {

    @Component.Factory
    fun interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }

}