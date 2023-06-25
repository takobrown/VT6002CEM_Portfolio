package com.mobile.diary

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobile.diary.databinding.ActivityMainBinding
import java.security.AccessController.getContext

class MainActivity : ComponentActivity() {
    private lateinit var inflate: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = ActivityMainBinding.inflate(layoutInflater)
        setContentView(inflate.root)


        inflate.llWrite.setOnClickListener {
            startActivity(Intent(this, WriteDiaryActivity::class.java))
        }
        inflate.llList.setOnClickListener {
            startActivity(Intent(this, ListDiaryActivity::class.java))
        }
        inflate.version.text = "V${getVersionName()}"
    }

    private fun getVersionName(): String? {
        val context = this@MainActivity
        val manager = context.packageManager
        try {

            val packageInfo =
                manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            val activities = packageInfo.activities
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}
