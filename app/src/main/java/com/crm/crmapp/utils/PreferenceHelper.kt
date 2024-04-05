package com.crm.crmapp.utils


import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private val PREF_NAME = "PreLoginPreferences"
    private lateinit var preferences: SharedPreferences

    fun setPreLoginPreferences(context: Context, KEY: String, value: String) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY, value)
        editor.apply()
    }




    fun getPreLoginPreferences(context: Context, KEY: String): String? {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences.getString(KEY, "")
    }
 }