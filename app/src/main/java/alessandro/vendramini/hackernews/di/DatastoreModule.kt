package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.data.store.InternalDatastore
import org.koin.dsl.module

val dataStoreModule = module {
    single { InternalDatastore(get()) }
}