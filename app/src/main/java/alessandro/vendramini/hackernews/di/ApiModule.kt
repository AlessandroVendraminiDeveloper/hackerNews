package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.data.api.services.StoriesService
import alessandro.vendramini.hackernews.data.api.services.StoryDetailService
import org.koin.dsl.module

val apiModule = module {
    single { StoriesService(get()) }
    single { StoryDetailService(get()) }
}
