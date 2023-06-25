package com.mobile.diary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class WriteDiaryActivityTest {

    private lateinit var mockMMKV: MMKV

    private lateinit var writeDiaryActivity: WriteDiaryActivity

    @Before
    fun setup() {
        mockMMKV = mockkClass(MMKV::class)
        writeDiaryActivity = WriteDiaryActivity()
        writeDiaryActivity.diaryList = ArrayList()
        mockkStatic(MMKV::class)
        every { MMKV.defaultMMKV() } returns mockMMKV
    }

    @Test
    fun testSaveDiary_Success() {
        // Mock the necessary data
        val diaryList = ArrayList<DiaryBean>()
        val diaryBean = DiaryBean().apply {
            content = "Sample Content"
            date = "2023-06-25"
            location = "Sample Location"
        }
        diaryList.add(diaryBean)

        val gson = Gson()
        val toJson = gson.toJson(diaryList)

        // Mock the behavior of MMKV
        every { mockMMKV.encode("data", toJson) } just Runs

        // Call the method to be tested
        writeDiaryActivity.saveDiary()

        // Verify the expected behavior
        verify(exactly = 1) { mockMMKV.encode("data", toJson) }
        assertEquals(diaryList, writeDiaryActivity.diaryList)
    }

    @Test
    fun testSaveDiary_EmptyFields() {
        // Call the method to be tested
        writeDiaryActivity.saveDiary()

        // Verify the expected behavior
        verify(exactly = 0) { mockMMKV.encode(any(), any()) }
        assertEquals(0, writeDiaryActivity.diaryList?.size)
    }

    @Test
    fun testLoadDiary_Success() {
        // Mock the necessary data
        val diaryList = ArrayList<DiaryBean>()
        val diaryBean = DiaryBean().apply {
            content = "Sample Content"
            date = "2023-06-25"
            location = "Sample Location"
        }
        diaryList.add(diaryBean)

        val gson = Gson()
        val toJson = gson.toJson(diaryList)

        // Mock the behavior of MMKV
        every { mockMMKV.decodeString("data") } returns toJson

        // Call the method to be tested
        writeDiaryActivity.loadDiary()

        // Verify the expected behavior
        verify(exactly = 1) { mockMMKV.decodeString("data") }
        assertEquals(diaryList, writeDiaryActivity.diaryList)
    }

    @Test
    fun testLoadDiary_NoData() {
        // Mock the behavior of MMKV
        every { mockMMKV.decodeString("data") } returns null

        // Call the method to be tested
        writeDiaryActivity.loadDiary()

        // Verify the expected behavior
        verify(exactly = 1) { mockMMKV.decodeString("data") }
        assertEquals(0, writeDiaryActivity.diaryList?.size)
    }

}


