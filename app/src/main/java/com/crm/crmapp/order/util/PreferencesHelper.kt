package com.crm.crmapp.order.util

import android.content.Context
import android.preference.PreferenceManager
import com.crm.crmapp.order.model.DocDetailModel
import com.crm.crmapp.order.model.FilterModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.crm.crmapp.registration.LoginResponse


class PreferencesHelper(var context: Context) {

    companion object {
        val DEVELOP_MODE = false
        private const val DEVICE_TOKEN = "data.source.prefs.DEVICE_TOKEN"
        private const val BYSORT = "bySorting"
        private const val TYPE_SORT = "typeSoting"
        private const val BASE64_IMAGE = "BASE64_IMAGE"
        private const val FILTER_VALUE = "filter_value"
        private const val ORDER_ID = "order_id"
        private const val USER_ID = "user_id"
        private const val MOBILE_ID = "mobile_num"
        private const val USER_NAME = "user_name"
        private const val Base_url = ""
        const val SETTINGS_PAGE_VALUE = "setting_page_value"
        private const val device_firebase_token = "0"
        var PREFS_NAME = "MyPrefName"
        var PREFS_KEY = "MyPrefKey"


    }
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var bySort = preferences.getString(BYSORT, "")
        set(value) = preferences.edit().putString(BYSORT, value).apply()

    var typeOfSort = preferences.getString(TYPE_SORT, "")
        set(value) = preferences.edit().putString(TYPE_SORT, value).apply()

    var image = preferences.getString(BASE64_IMAGE, "")
        set(value) = preferences.edit().putString(BASE64_IMAGE, value).apply()


    var filterValue = preferences.getString(FILTER_VALUE, "")
        set(value) = preferences.edit().putString(FILTER_VALUE, value).apply()

    var orderId = preferences.getString(ORDER_ID, "")
        set(value) = preferences.edit().putString(ORDER_ID, value).apply()

    var userId = preferences.getString(USER_ID, "")
        set(value) = preferences.edit().putString(USER_ID, value).apply()

    var mobileId = preferences.getString(MOBILE_ID, "")
        set(value) = preferences.edit().putString(MOBILE_ID, value).apply()


    var Base_URL = preferences.getString(Base_url, "")
        set(value) = preferences.edit().putString(Base_url, value).apply()



    var userName = preferences.getString(USER_NAME, "")
        set(value) = preferences.edit().putString(USER_NAME, value).apply()








    var deviceToken = preferences.getString(device_firebase_token, "")
        set(value) = preferences.edit().putString(device_firebase_token, value).apply()

   /* var pagevalue = preferences.getInt(SETTINGS_PAGE_VALUE, 5)
        set(pagevalue) = preferences.edit().putInt(SETTINGS_PAGE_VALUE, pagevalue).apply()*/

    fun saveTasksToSharedPrefs(context: Context, array: ArrayList<FilterModel>) {
        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = appSharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(array) //tasks is an ArrayList instance variable
        prefsEditor.putString("currentTasks", json)
        prefsEditor.apply()
    }

    fun getTasksFromSharedPrefs(context: Context): ArrayList<FilterModel> {
        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = appSharedPrefs.getString("currentTasks", "")
        var tasks: ArrayList<FilterModel>? =
            gson.fromJson<ArrayList<FilterModel>>(json, object : TypeToken<ArrayList<FilterModel>>() {
            }.type)
        return tasks!!
    }

    fun saveObject(context: Context, myObject: LoginResponse.Result) {


        val preferencesReader = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferencesReader.edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        editor.putString(PREFS_KEY, json)
        editor.commit()
    }

    fun getObject(): LoginResponse.Result {
        val gson = Gson()
        var obj: LoginResponse.Result? = null
        val preferencesReader = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = preferencesReader.getString(PREFS_KEY, "")
        if (gson.fromJson(json, LoginResponse.Result::class.java) != null) {
            obj = gson.fromJson(json, LoginResponse.Result::class.java)
        }
        return obj!!
    }



    fun saveObjectImage(context: Context, myObject: DocDetailModel) {


        val preferencesReader = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferencesReader.edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        editor.putString(PREFS_KEY, json)
        editor.commit()
    }

    fun getObjectImage(): DocDetailModel {
        val gson = Gson()
        var obj: DocDetailModel? = null
        val preferencesReader = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = preferencesReader.getString(PREFS_KEY, "")
        if (gson.fromJson(json, DocDetailModel::class.java) != null) {
            obj = gson.fromJson(json, DocDetailModel::class.java)
        }
        return obj!!
    }




    fun removeSahredPre() {
        var preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.remove(PREFS_KEY)
        editor.remove("currentTasks")
        editor.remove(USER_ID)
        editor.remove(USER_NAME)
        editor.remove(MOBILE_ID)
        editor.remove(ORDER_ID)
        editor.remove(FILTER_VALUE)
        editor.remove(BASE64_IMAGE)
        editor.remove(TYPE_SORT)
        editor.remove(BYSORT)
        editor.commit()
    }

}