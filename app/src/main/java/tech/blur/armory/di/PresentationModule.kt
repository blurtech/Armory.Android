package tech.blur.armory.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.blur.armory.presentation.MainViewModel

val presentationModule = module {
    viewModel { MainViewModel(get()) }
}