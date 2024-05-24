package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.data.api.repositories.NewStoriesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { NewStoriesRepository(get()) }
}
