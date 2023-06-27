package com.mobile.diary

import android.app.Application

class MyApplication : Application() {
    companion object {
        private const val DIARY_DATA_KEY = "diary_data"
    }

    override fun onCreate() {
        super.onCreate()

  
        val sharedPreferences = applicationContext.getSharedPreferences(
            "MyDiaryPreferences",
            MODE_PRIVATE
        )

  
        DiaryDataManager.sharedPreferences = sharedPreferences
    }
}

object DiaryDataManager {
    private const val DIARY_DATA_KEY = "diary_data"
    lateinit var sharedPreferences: SharedPreferences

    fun getDiaryList(): ArrayList<DiaryBean> {
        val data = sharedPreferences.getString(DIARY_DATA_KEY, "")
        val listType = object : TypeToken<ArrayList<DiaryBean?>?>() {}.type
        return if (data.isNullOrEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(data, listType)
        }
    }

    fun saveDiaryList(diaryList: ArrayList<DiaryBean>) {
        val toJson = Gson().toJson(diaryList)
        sharedPreferences.edit().putString(DIARY_DATA_KEY, toJson).apply()
    }
}
