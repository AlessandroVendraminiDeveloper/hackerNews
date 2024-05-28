package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.data.store.InternalDatastore
import alessandro.vendramini.hackernews.presentation.viewmodels.events.FavoriteStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.FavoriteStoriesViewModelState
import alessandro.vendramini.hackernews.util.gson
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteStoriesViewModel(
    private val repository: StoriesRepository,
    private val datastore: InternalDatastore,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = FavoriteStoriesViewModelState())
    val uiState: StateFlow<FavoriteStoriesViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: FavoriteStoriesViewModelEvent) {
        when (event) {
            is FavoriteStoriesViewModelEvent.FetchStoriesDetailByIds -> {
                fetchStoryDetail(listOfIds = event.listOfIds)
            }
            is FavoriteStoriesViewModelEvent.UpdatePreferredList -> {
                updatePreferredList(
                    id = event.id,
                    isAddedFromList = event.isAddFromList,
                )
            }
        }
    }

    private fun fetchStoryDetail(listOfIds: List<Long>) {
        viewModelScope.launch {
            val storyArrayList: ArrayList<StoryModel> = arrayListOf()

            val deferredItems = mutableListOf<Deferred<StoryModel?>>()
            listOfIds.forEachIndexed { index, id ->
                val deferredItem = async {
                    var story: StoryModel? = null
                    repository.getStoryDetail(
                        id = id,
                        onCallbackResource = { response ->
                            when (response) {
                                is ApiResource.Success -> {
                                    story = response.data
                                }
                                else -> {
                                    // There is an error
                                    story = null
                                }
                            }
                        }
                    )
                    story
                }
                deferredItems.add(index, deferredItem)
            }

            deferredItems.forEach { deferredItem ->
                val story = deferredItem.await()
                story?.let {
                    if (storyArrayList.none { it.id == story.id }) {
                        storyArrayList.add(story)
                    }
                }
            }

            _uiState.update { state ->
                state.copy(favoriteStories = storyArrayList)
            }
        }
    }

    private fun updatePreferredList(
        id: Long,
        isAddedFromList: Boolean,
    ) {
        viewModelScope.launch {
            try {
                val arrayOfIds = DashboardViewModel.preferredIds.toCollection(ArrayList())
                when (isAddedFromList) {
                    true -> {
                        if (!arrayOfIds.contains(id)) {
                            arrayOfIds.add(id)
                        }
                    }

                    false -> {
                        if (arrayOfIds.contains(id)) {
                            arrayOfIds.remove(id)
                        }
                    }
                }

                val typeToken = object : TypeToken<List<Long>>() {}.type
                val json = gson.toJson(arrayOfIds.toList(), typeToken)
                datastore.savePreferredStories(json = json)
            } catch (e: Exception) { }
        }
    }
}