package alessandro.vendramini.hackernews

import alessandro.vendramini.hackernews.di.apiModule
import alessandro.vendramini.hackernews.di.dataStoreModule
import alessandro.vendramini.hackernews.di.ktorModule
import alessandro.vendramini.hackernews.di.repositoryModule
import alessandro.vendramini.hackernews.di.viewModelModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp : Application() {
    companion object {
        lateinit var instance: MainApp
            private set
    }

    private val moduleList = listOf(
        apiModule,
        ktorModule,
        repositoryModule,
        dataStoreModule,
        viewModelModule,
    )

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(moduleList)
        }
        instance = this
    }
}
