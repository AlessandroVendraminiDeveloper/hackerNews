package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.data.api.repositories.CommentsRepository
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { StoriesRepository(get(), get()) }
    single { CommentsRepository(get()) }
}
