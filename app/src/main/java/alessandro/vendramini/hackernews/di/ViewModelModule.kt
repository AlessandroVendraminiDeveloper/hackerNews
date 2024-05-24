package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.presentation.viewmodels.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
}
