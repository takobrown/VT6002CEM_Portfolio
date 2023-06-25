package com.mobile.diary

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class LocationAPITest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetLocation_Success() {
        // Enqueue a mock response
        val responseFile = getFileFromResource("location_success_response.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getFileContent(responseFile))

        mockWebServer.enqueue(mockResponse)

        // Make API request
        val latitude = 37.7749
        val longitude = -122.4194
        val url = "http://api.positionstack.com/v1/reverse?access_key=a6d21406ac24c45d11d8279441a24931&query=$latitude,$longitude"

        // Perform the API request using the provided URL
        // Verify the request
        val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
        // Verify the recordedRequest and perform assertions

        // Verify the response

    }

    @Test
    fun testGetLocation_Failure() {
        // Enqueue a mock response
        val mockResponse = MockResponse()
            .setResponseCode(500)

        mockWebServer.enqueue(mockResponse)

        // Make API request
        val latitude = 37.7749
        val longitude = -122.4194
        val url = "http://api.positionstack.com/v1/reverse?access_key=a6d21406ac24c45d11d8279441a24931&query=$latitude,$longitude"

        // Perform the API request using the provided URL
        // Verify the request
        val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
        // Verify the recordedRequest and perform assertions

        // Verify the response

    }

    private fun getFileFromResource(fileName: String): File {
        val classLoader = javaClass.classLoader
        return File(classLoader.getResource(fileName)?.file!!)
    }

    private fun getFileContent(file: File): String {
        return file.readText()
    }

}
