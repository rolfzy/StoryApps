package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.data.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {

        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `when Get story Should Not Null and Return data `() = runTest {
        val dummyUserModel = UserModel("akm@gmail.com", "akm12345", true)
        val dummyStory = DataDummy.generateDummyStorieResponse()
        val data: PagingData<ListStoryItem> = storypagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        val token = ""
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(flowOf(dummyUserModel))
        Mockito.`when`(storyRepository.getPaging(dummyUserModel.token)).thenReturn(expectedStory)

        val actualStory : PagingData<ListStoryItem> = mainViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
        diffCallback = StoryAdapter.DIFF_CALLBACK ,
        updateCallback = noopListUpdateCallback,
        workerDispatcher = Dispatchers.Main,
     )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }


    @Test
    fun `when Get story Empty Should Return No Data `() = runTest {
        val dummyUserModel = UserModel("akm@gmail.com", "akm12345", true)
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        val token = ""

        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(flowOf(dummyUserModel))
        Mockito.`when`(storyRepository.getPaging(dummyUserModel.token)).thenReturn(expectedStory)
        val actualStory: PagingData<ListStoryItem> =
            mainViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }





        class storypagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
        companion object {
            fun snapshot(item: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(item)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int,LiveData<List<ListStoryItem>>>{
            return LoadResult.Page(emptyList(),0,1)
        }
    }
}
val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
