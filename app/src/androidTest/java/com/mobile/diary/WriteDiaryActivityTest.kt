package com.mobile.diary

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import io.mockk.mockkStatic
import io.mockk.any
import io.mockk.capture
import com.mobile.diary.DiaryBean

class WriteDiaryActivityTest {

    private lateinit var mockMMKV: MMKV

    private lateinit var writeDiaryActivity: WriteDiaryActivity

    @Before
    fun setup() {
        mockMMKV = mockkClass(MMKV::class)
        writeDiaryActivity = spyk(WriteDiaryActivity()) // Use spyk to partially mock the activity
        writeDiaryActivity.DiaryList(ArrayList())
        mockkStatic(MMKV::class)
        every { MMKV.defaultMMKV() } returns mockMMKV
    }

    @Test
    fun testSaveDiary_Success() {
        // Mock the necessary data
        val diaryBean = DiaryBean().apply {
            content = "Sample Content"
            date = "2023-06-25"
            location = "Sample Location"
        }

        // Mock the behavior of MMKV
        every { mockMMKV.encode(any<String>(), any<String>()) } just Runs

        // Call the method to be tested
        writeDiaryActivity.saveDiary(diaryBean)

        // Verify the expected behavior
        verify(exactly = 1) { mockMMKV.encode(any<String>(), any<String>()) }

        // Verify the interactions indirectly through the writeDiaryActivity object
        val capturedList = slot<ArrayList<DiaryBean>>()
        verify { writeDiaryActivity.captureDiaryList(capture(capturedList)) }
        assertEquals(listOf(diaryBean), capturedList.captured)
    }

    @Test
    fun testSaveDiary_EmptyFields() {
        // Call the method to be tested
        writeDiaryActivity.saveDiary(DiaryBean())

        // Verify the expected behavior
        verify(exactly = 0) { mockMMKV.encode(any<String>(), any<String>()) }

        // Verify the interactions indirectly through the writeDiaryActivity object
        val capturedList = slot<ArrayList<DiaryBean>>()
        verify { writeDiaryActivity.captureDiaryList(capture(capturedList)) }
        assertEquals(0, capturedList.captured.size)
    }

    // Helper method to capture the diaryList indirectly
    private fun WriteDiaryActivity.captureDiaryList(list: ArrayList<DiaryBean>) {
        setDiaryList(list)
    }
}