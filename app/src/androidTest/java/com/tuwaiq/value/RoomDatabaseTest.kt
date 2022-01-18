package com.tuwaiq.value

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueDao
import com.tuwaiq.value.database.ValueDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database : ValueDatabase
    private lateinit var dao : ValueDao


    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
        ValueDatabase::class.java).allowMainThreadQueries().build()
        dao = database.valueDao()
    }


    @After
    fun teardown(){
        database.close()
    }



    @Test
    fun insetValueMember() = runBlockingTest {
        val value = Value("1","2","3","4","5",
        "6","7","8","9","10","11","12","13",
        "14","15","16","17")
        dao.addNewUser(value)

        val allValue = dao.getAllUserInfo().getOrAwaitValue()

        assertThat(allValue).contains(value)
    }
}