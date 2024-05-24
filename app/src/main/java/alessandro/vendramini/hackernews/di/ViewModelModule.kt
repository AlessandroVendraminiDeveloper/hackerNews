package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.presentation.viewmodels.DashboardViewModel
import alessandro.vendramini.hackernews.presentation.viewmodels.NewStoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { NewStoriesViewModel(get()) }
}
