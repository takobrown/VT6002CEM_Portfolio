package com.mobile.diary

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.github.gzuliyujiang.calendarpicker.CalendarPicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.utils.ToastUtils
import com.mobile.diary.databinding.ActivityWriteDiaryBinding
import com.tencent.mmkv.MMKV
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Calendar
import java.util.Date



class WriteDiaryActivity : AppCompatActivity() {
    private var diaryBean: DiaryBean = DiaryBean()
    private lateinit var inflate: ActivityWriteDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = ActivityWriteDiaryBinding.inflate(layoutInflater)
        setContentView(inflate.root)

        requestPer()

        inflate.takephoto.setOnClickListener {
            selectAblums()
        }
        inflate.selectAlbums.setOnClickListener {
            takePhoto()
        }
        inflate.clear.setOnClickListener {
            inflate.date.setText("")
            inflate.location.text = "*"
            Glide.with(this@WriteDiaryActivity).load(R.mipmap.ic_add).into(inflate.iv)
            inflate.content.setText("")

            diaryBean = DiaryBean()
        }

        inflate.shared.setOnClickListener {
            val sharedText = "this is pet diary!"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)

            startActivity(Intent.createChooser(shareIntent, "shared by diary"));
        }

        inflate.save.setOnClickListener {

            if (inflate.date.text.isNullOrEmpty() || inflate.location.text.equals("*") || diaryBean.photoPath.isNullOrEmpty() || inflate.content.text.isNullOrEmpty()) {
                ToastUtils.showToast(this@WriteDiaryActivity, "please input！")
                return@setOnClickListener
            }

            diaryBean.content = inflate.content.text.toString()
            diaryBean.date = inflate.date.text.toString()
            diaryBean.location = inflate.location.text.toString()


            diaryList?.add(0, diaryBean)

            val toJson = Gson().toJson(diaryList)

            MMKV.defaultMMKV().encode("data", toJson)
            ToastUtils.showToast(this@WriteDiaryActivity, "save success！")
            finish()
        }

        val data = MMKV.defaultMMKV().decodeString("data")

        val listType = object : TypeToken<ArrayList<DiaryBean?>?>() {}.type
        diaryList = if (data.isNullOrEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(data, listType)
        }

        inflate.date1.setOnClickListener {
            val picker = CalendarPicker(this)
            val currentDate = Date(System.currentTimeMillis())

            val maxCalendar: Calendar = DateUtils.calendar(currentDate)
            maxCalendar.add(Calendar.MONTH, 12)
            maxCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(maxCalendar.time))

            val minCalendar: Calendar = DateUtils.calendar(currentDate)
            minCalendar.add(Calendar.MONTH, -12)
            minCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(minCalendar.time))

            val minDate: Date = minCalendar.time
            val maxDate: Date = maxCalendar.time
            picker.setRangeDate(
                minDate, maxDate
            )
            picker.setSelectedDate(currentDate)
            picker.setOnSingleDatePickListener { date ->
                val timeToString = DateUtils.timeToString(date.time)
                inflate.date.setText(timeToString)
            }
            picker.show()
        }

        // set the diaryList
        fun setDiaryList(list: ArrayList<DiaryBean>) {
            diaryList = list
        }

    }

    private var diaryList: ArrayList<DiaryBean>? = null


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!Constants.PHOTO_PATH.isNullOrEmpty()) {
            Glide.with(this@WriteDiaryActivity).load(Constants.PHOTO_PATH).into(inflate.iv)
        }
    }


    private fun selectAblums() {
        PictureSelector.create(this).openCamera(SelectMimeType.ofImage())
            .setLanguage(LanguageConfig.ENGLISH)
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>) {
                    val realPath = result?.get(0)?.realPath
                    Glide.with(this@WriteDiaryActivity).load(realPath).into(inflate.iv)
                    diaryBean.photoPath = realPath

                    Constants.PHOTO_PATH = realPath
                }

                override fun onCancel() {}
            })
    }

    private fun takePhoto() {
        PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
            .setLanguage(LanguageConfig.ENGLISH).setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>) {
                    val realPath = result?.get(0)?.realPath
                    Glide.with(this@WriteDiaryActivity).load(realPath).into(inflate.iv)
                    diaryBean.photoPath = realPath

                    Constants.PHOTO_PATH = realPath
                }

                override fun onCancel() {}
            })
    }

    private fun requestPer() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
            return
        }
        getLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener
                    )
                }
            }
        }
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude

                val okHttpClient = OkHttpClient()

                val url =
                    "http://api.positionstack.com/v1/reverse?access_key=a6d21406ac24c45d11d8279441a24931&query=$latitude,$longitude"
                val build = Request.Builder().url(url).build()

                val newCall = okHttpClient.newCall(build)
                newCall.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        ToastUtils.showToast(this@WriteDiaryActivity, e.message)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val string = response.body()?.string()
                            val fromJson = Gson().fromJson(string, AddressBean::class.java)
                            val get = fromJson?.data?.get(0)
                            runOnUiThread {
                                inflate.location.text = get?.label
                            }
                        }
                    }
                })

                if (locationManager != null && locationListener != null) {
                    locationManager.removeUpdates(locationListener)
                }

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Constants.PHOTO_PATH = ""
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener)
        }
    }

    fun DiaryList(arrayList: Any) {

    }

}

