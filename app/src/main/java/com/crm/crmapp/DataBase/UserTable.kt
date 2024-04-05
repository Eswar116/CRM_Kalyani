package com.crm.crmapp.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
class UserTable(

    @ColumnInfo(name = "email_id")
    var email_id: String,

    @ColumnInfo(name = "reports_to_fullname")
    var reports_to_fullname: String,

    @ColumnInfo(name = "user_name")
    var user_name: String,

    @ColumnInfo(name = "l_name")
    var l_name: String,

    @ColumnInfo(name = "mobile_no")
    var mobile_no: String,

    @ColumnInfo(name = "reports_to_id")
    var reports_to_id: Int = 0,

    @ColumnInfo(name = "user_type")
    var user_type: String,

    @ColumnInfo(name = "user_id")
    var user_id: Int = 0,


    @ColumnInfo(name = "f_name")
    var f_name: String,

    @ColumnInfo(name = "reports_to_username")
    var reports_to_username: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "department")
    var department: String,


    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var profile_pic_url: ByteArray? = null
) {
    @PrimaryKey(autoGenerate = true)
    var UserId: Int = 0
}

