package com.mobile.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobile.diary.databinding.ActivityListDiaryBinding

class ListDiaryActivity : AppCompatActivity() {
    private lateinit var inflate: ActivityListDiaryBinding
    private lateinit var diaryList: List<DiaryBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = ActivityListDiaryBinding.inflate(layoutInflater)
        setContentView(inflate.root)

        val sharedPreferences = getSharedPreferences("MyDiaryPreferences", MODE_PRIVATE)
        val data = sharedPreferences.getString("data", "")

        val listType = object : TypeToken<ArrayList<DiaryBean?>?>() {}.type
        diaryList = if (data.isNullOrEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(data, listType)
        }

        inflate.rv.layoutManager = LinearLayoutManager(this)
        inflate.rv.adapter = DiaryAdapter(this, diaryList)
    }
}
