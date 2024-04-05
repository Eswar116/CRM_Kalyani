package com.crm.crmapp.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewCustomerDB")
class NewCustomerDB(

    @ColumnInfo(name = "cust_name")
    var cust_name: String,

    @ColumnInfo(name = "user_Id")
    var user_Id: Int = 0,

    @ColumnInfo(name = "cust_category")
    var cust_category: String,

    @ColumnInfo(name = "cust_mobile")
    var cust_mobile: String,

    @ColumnInfo(name = "cust_email")
    var cust_email: String,

    @ColumnInfo(name = "cust_address")
    var cust_address: String,

    @ColumnInfo(name = "remarks")
    var remarks: String,

    @ColumnInfo(name = "sync")
    var sync: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var CustomerId: Int = 0
}



