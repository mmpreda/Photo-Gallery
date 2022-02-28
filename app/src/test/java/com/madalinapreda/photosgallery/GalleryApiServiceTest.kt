package com.madalinapreda.photosgallery

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.madalinapreda.photosgallery.network.GalleryApi
import com.madalinapreda.photosgallery.network.GalleryApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
class GalleryApiServiceTest {

    private val apiService = GalleryApiService()

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val mockApi = mock(GalleryApi::class.java)
        `when`(apiService.api).thenReturn(mockApi)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testAlbumsCall() = runBlockingTest {
        val albums = apiService.getAlbums()
        verify(apiService.api, times(1)).getAlbums()
        assertEquals(100, albums.size)
        verifyNoMoreInteractions(apiService.api)
    }

    @Test
    fun testUsersCall() = runBlockingTest {
        val users = apiService.getUsers()
        verify(apiService.api, times(1)).getUsers()
        assertEquals(10, users.size)
        verifyNoMoreInteractions(apiService.api)
    }

    @Test
    fun testPhotosCall() = runBlockingTest {
        val photos = apiService.getPhotosInAlbum(2)
        verify(apiService.api, times(1)).getPhotosInAlbum(2)
        assertEquals(50, photos.size)
        verifyNoMoreInteractions(apiService.api)
    }
}