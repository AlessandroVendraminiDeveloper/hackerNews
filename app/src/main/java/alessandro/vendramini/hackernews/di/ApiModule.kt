package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.data.api.services.NewStoriesService
import alessandro.vendramini.hackernews.data.api.services.StoryDetailService
import org.koin.dsl.module

val apiModule = module {
    single { NewStoriesService(get()) }
    single { StoryDetailService(get()) }
}
