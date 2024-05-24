package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.NewStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.NewStoriesViewModelState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewStoriesViewModel(private val repository: StoriesRepository): ViewModel() {

    private val _uiState = MutableStateFlow(value = NewStoriesViewModelState())
    val uiState: StateFlow<NewStoriesViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: NewStoriesViewModelEvent) {
        when (event) {
            is NewStoriesViewModelEvent.FetchStoriesByIds -> {
                fetchStoryDetail(
                    listOfIds = event.listOfIds.subList(
                        0,
                        minOf(30, event.listOfIds.size),
                    )
                )
            }
        }
    }

    private fun fetchStoryDetail(listOfIds: List<Long>) {
        viewModelScope.launch {
            val storyArrayList: ArrayList<StoryModel> = arrayListOf()
            val deferredItems = listOfIds.map { id ->
                async {
                    repository.getStoryDetail(
                        id = id,
                        onCallbackResource = { response ->
                            when (response) {
                                is ApiResource.Success -> {
                                    response.data?.let { storyArrayList.add(it) }
                                }
                                else -> {

                                }
                            }
                        }
                    )
                }
            }
            deferredItems.awaitAll()

            _uiState.update { state ->
                state.copy(newStories = storyArrayList)
            }
        }
    }
}