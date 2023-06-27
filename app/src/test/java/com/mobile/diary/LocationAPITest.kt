import com.google.gson.Gson
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import com.mobile.diary.AddressBean
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
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
    }

    @Test
    fun testGetLocation_Failure() {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))

        val latitude = 37.7749
        val longitude = -122.4194

        getLocation(latitude, longitude)
    }

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
                val responseBody = response.body
                if (response.isSuccessful && responseBody != null) {
                    val string = responseBody.string()
                    val fromJson = Gson().fromJson(string, AddressBean::class.java)
                    val get = fromJson?.data?.get(0)
                }
            }
        })
    }
}
