package com.badzohugues.staticlbcapp.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.badzohugues.staticlbcapp.data.db.DbAlbumItem
import com.badzohugues.staticlbcapp.data.db.room.StaticLBCDatabase
import com.badzohugues.staticlbcapp.getOrAwaitValueAndroid
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlbumItemDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: StaticLBCDatabase
    private lateinit var dao: AlbumItemDao
    private lateinit var dbAlbumItemA: DbAlbumItem
    private lateinit var dbAlbumItemB: DbAlbumItem
    private lateinit var dbAlbumItemC: DbAlbumItem

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StaticLBCDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.albumItemDao()
    }

    @Before
    fun setupDbItems() {
        dbAlbumItemA = DbAlbumItem(424242,
            42,
            "AlbumItemTest",
            "https://via.placeholder.com/600/e403d1",
            "https://via.placeholder.com/150/e403d1"
        )

        dbAlbumItemB = DbAlbumItem(242424,
            42,
            "AlbumItemTest",
            "https://via.placeholder.com/600/e403d1",
            "https://via.placeholder.com/150/e403d1"
        )

        dbAlbumItemC = DbAlbumItem(212142,
            24,
            "AlbumItemTest",
            "https://via.placeholder.com/600/e403d1",
            "https://via.placeholder.com/150/e403d1"
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAlbumItem() = runBlockingTest {
        dao.insert(dbAlbumItemA)
        val allAlbumItem = dao.getAll()
        assertThat(allAlbumItem).contains(dbAlbumItemA)
    }

    @Test
    fun insertAlbumItems() = runBlockingTest {
        dao.insertAll(listOf(dbAlbumItemA, dbAlbumItemB))
        val allAlbumItem = dao.getAll()
        assertThat(allAlbumItem).contains(dbAlbumItemB)
    }

    @Test
    fun getItemsOfAlbum() = runBlockingTest {
        dao.insertAll(listOf(dbAlbumItemA, dbAlbumItemB, dbAlbumItemC))
        val allAlbumItem = dao.getItemsOfAlbum(42)
        assertThat(allAlbumItem).isNotEmpty()
    }
}