package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.TopStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.TopStoriesViewModelState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopStoriesViewModel(private val repository: StoriesRepository): ViewModel() {

    private val hitsPerPage = 30

    private val _uiState = MutableStateFlow(value = TopStoriesViewModelState())
    val uiState: StateFlow<TopStoriesViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: TopStoriesViewModelEvent) {
        when (event) {
            is TopStoriesViewModelEvent.FetchTopStoriesIds -> {
                _uiState.update { state ->
                    state.copy(isRefreshing = event.isRefreshing)
                }
                fetchTopStoriesIds()
            }
            is TopStoriesViewModelEvent.FetchStoriesByIds -> {
                val startHit = uiState.value.paginationState.page * hitsPerPage
                val isEndReached = startHit > event.listOfIds.size

                if (startHit != 0) {
                    _uiState.update { state ->
                        state.copy(
                            paginationState = state.paginationState.copy(
                                isLoading = !isEndReached,
                                endReached = isEndReached,
                            )
                        )
                    }
                }

                if (!isEndReached) {
                    fetchStoryDetail(
                        listOfIds = event.listOfIds.subList(
                            startHit,
                            minOf(startHit + hitsPerPage, event.listOfIds.size),
                        )
                    )
                }
            }
        }
    }

    private fun fetchTopStoriesIds() {
        viewModelScope.launch {
            repository.getTopStoriesIds(
                onCallbackResource = { response ->
                    when (response) {
                        is ApiResource.Success -> {
                            _uiState.update { state ->
                                state.copy(topStoriesIds = response.data)
                            }

                            if (uiState.value.isRefreshing) {
                                uiState.value.topStoriesIds?.let { ids ->
                                    // Reset pagination
                                    _uiState.update { state ->
                                        state.copy(
                                            paginationState = state.paginationState.copy(
                                                isLoading = false,
                                                page = 0,
                                                endReached = false
                                            )
                                        )
                                    }
                                    fetchStoryDetail(
                                        listOfIds = ids.subList(
                                            0,
                                            minOf(hitsPerPage, ids.size),
                                        )
                                    )
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }

    private fun fetchStoryDetail(listOfIds: List<Long>) {
        viewModelScope.launch {
            val storyArrayList: ArrayList<StoryModel> =
                uiState.value.topStories?.takeIf { !uiState.value.isRefreshing }?.toCollection(ArrayList())
                    ?: arrayListOf()

            val deferredItems = listOfIds.map { id ->
                async {
                    repository.getStoryDetail(
                        id = id,
                        onCallbackResource = { response ->
                            when (response) {
                                is ApiResource.Success -> {
                                    response.data?.let { story ->
                                        if (storyArrayList.none { it.id == story.id }) {
                                            storyArrayList.add(story)
                                        }
                                    }
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
                state.copy(
                    topStories = storyArrayList,
                    isRefreshing = false,
                    paginationState = state.paginationState.copy(
                        isLoading = false,
                        page = state.paginationState.page + 1,
                    )
                )
            }
        }
    }
}