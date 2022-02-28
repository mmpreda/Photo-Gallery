package com.madalinapreda.photosgallery

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.madalinapreda.photosgallery.database.GalleryDao
import com.madalinapreda.photosgallery.database.GalleryDatabase
import com.madalinapreda.photosgallery.model.Photo
import com.madalinapreda.photosgallery.model.User
import com.madalinapreda.photosgallery.model.UserAddress
import com.madalinapreda.photosgallery.model.UserCompany
import com.madalinapreda.photosgallery.repository.GalleryRepository
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
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class GalleryDaoTest {

    private lateinit var database: GalleryDatabase
    private lateinit var galleryDao: GalleryDao
    private lateinit var galleryRepository: GalleryRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room
            .inMemoryDatabaseBuilder(context, GalleryDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        galleryDao = database.getDao()
        galleryRepository = GalleryRepository(galleryDao)
    }

    @After
    fun cleanup() {
        database.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInsertAndCountUsers() = runBlockingTest {
        galleryRepository.saveUsers(testUsers)
        assertEquals(3, galleryRepository.getUsersCount())
    }

    @Test
    fun testInsertAndCountPhotos() = runBlockingTest {
        galleryRepository.savePhotos(testPhotos)
        assertEquals(2, galleryRepository.getPhotosCountForAlbum(albumId = 11))
    }

    @Test
    fun testInsertAndRetrievePhotos() = runBlockingTest {
        galleryRepository.savePhotos(testPhotos)
        val photosInAlbum = galleryRepository.getPhotosForAlbum(albumId = 11)
        assertEquals(3, photosInAlbum.size)
        assertEquals(photo1, photosInAlbum[0])
        assertEquals(photo2, photosInAlbum[1])
        assertEquals(photo3, photosInAlbum[2])
    }

    companion object {
        private val company = UserCompany("Test", "some slogan", "qwerty")
        private val user1 = User(
            100, "John Smith", "josmith", "johnsmith@mail.com",
            UserAddress("", "", "Nice", "06000", null), "555-123-456", "johnswebsite.com", company
        )
        private val user2 = User(
            101, "Jean Smith", "jesmith", "jeansmith@mail.com",
            UserAddress("", "", "Nice", "06100", null), "555-123-789", "jeanswebsite.com", company
        )
        private val user3 = User(
            102, "Jack Smith", "jasmith", "jacksmith@mail.com",
            UserAddress("", "", "Nice", "06200", null), "555-123-000", "jackswebsite.com", company
        )
        private val testUsers = arrayListOf(user1, user2, user3)

        private val photo1 = Photo(10, 1, "title-photo-1", "https://via.placeholder.com/600/7b37f9", "")
        private val photo2 = Photo(11, 2, "title-photo-2", "https://via.placeholder.com/600/ff5673", "")
        private val photo3 = Photo(11, 3, "title-photo-3", "https://via.placeholder.com/600/bcaaed", "")
        private val testPhotos = arrayListOf(photo1, photo2, photo3)

    }
}