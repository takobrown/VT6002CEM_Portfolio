import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class LocationUnitTest {

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
        // Prepare the mock response
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
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockResponseBody)
        )

        // Set up the test location
        val latitude = 37.7749
        val longitude = -122.4194

        // Invoke the method under test
        getLocation(latitude, longitude)

        // TODO: Add assertions to verify the behavior or state after invoking the method
        // For example, you can verify if the address is set correctly in the UI component
    }

    @Test
    fun testGetLocation_Failure() {
        // Prepare the mock response for failure scenario
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))

        // Set up the test location
        val latitude = 37.7749
        val longitude = -122.4194

        // Invoke the method under test
        getLocation(latitude, longitude)

        // TODO: Add assertions to verify the behavior or state after invoking the method
        // For example, you can verify if the appropriate error handling is done, like showing a toast message
    }

    // Other test cases can be added based on different scenarios, such as network errors, empty responses, etc.

    private fun getLocation(latitude: Double, longitude: Double) {
        val okHttpClient = OkHttpClient()

        val url = "http://${mockWebServer.hostName}:${mockWebServer.port}/v1/reverse?access_key=a6d21406ac24c45d11d8279441a24931&query=$latitude,$longitude"
        val request = Request.Builder().url(url).build()

        val newCall = okHttpClient.newCall(request)
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val string = response.body()?.string()
                    val fromJson = Gson().fromJson(string, AddressBean::class.java)
                    val get = fromJson?.data?.get(0)
                    // Update UI component with the address
                }
            }
        })
    }
}
