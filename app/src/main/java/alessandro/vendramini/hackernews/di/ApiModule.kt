package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.data.api.services.NewStoriesService
import org.koin.dsl.module

val apiModule = module {
    single { NewStoriesService(get()) }
}
