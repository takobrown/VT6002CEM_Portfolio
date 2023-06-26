package com.mobile.diary

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import com.google.gson.Gson
import com.mobile.diary.databinding.ActivityWriteDiaryBinding
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class WriteDiaryActivityTest {

    @Mock
    private lateinit var mockBinding: ActivityWriteDiaryBinding

    @Mock
    private lateinit var mockLocationManager: LocationManager

    @Mock
    private lateinit var mockLocation: Location

    private lateinit var mockWebServer: MockWebServer

    private lateinit var writeDiaryActivity: WriteDiaryActivity

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        `when`(mockBinding.location).thenReturn(mockBinding)
        `when`(mockBinding.text).thenReturn("")

        writeDiaryActivity = WriteDiaryActivity().apply {
            inflate = mockBinding
            locationManager = mockLocationManager
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getLocation_Success() {
        val latitude = 37.7749
        val longitude = -122.4194

        `when`(mockLocation.latitude).thenReturn(latitude)
        `when`(mockLocation.longitude).thenReturn(longitude)
        `when`(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
            .thenReturn(mockLocation)

        val mockResponseBody = """
            {
                "data": [
                    {
                        "label": "Sample Address"
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseBody)
        )

        writeDiaryActivity.getLocation()

        // Perform assertions or verifications based on the expected behavior
    }

    @Test
    fun getLocation_Failure() {
        val latitude = 37.7749
        val longitude = -122.4194

        `when`(mockLocation.latitude).thenReturn(latitude)
        `when`(mockLocation.longitude).thenReturn(longitude)
        `when`(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
            .thenReturn(mockLocation)

        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        writeDiaryActivity.getLocation()


    }

    @Test
    fun onRequestPermissionsResult_GrantPermission() {
        val requestCode = 1
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)

        writeDiaryActivity.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }

    @Test
    fun onRequestPermissionsResult_DenyPermission() {
        val requestCode = 1
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)

        writeDiaryActivity.onRequestPermissionsResult(requestCode, permissions, grantResults)

        
    }


}
