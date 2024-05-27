package alessandro.vendramini.hackernews.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InternalDatastore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "internalDatastore",
        )
        val PREFERRED_STORIES = stringPreferencesKey("preferred_stories")
    }

    /**
     * PREFERRED_STORIES
     */
    val getPreferredStories: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PREFERRED_STORIES] ?: "[]"
    }

    suspend fun savePreferredStories(json: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERRED_STORIES] = json
        }
    }
}
