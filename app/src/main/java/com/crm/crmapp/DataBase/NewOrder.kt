package com.crm.crmapp.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewOrder")
data class NewOrder(

    @ColumnInfo(name = "order_date")
    var order_date: String,
    @ColumnInfo(name = "user_name")
    var user_name: String,
    @ColumnInfo(name = "market_plan_name")
    var market_plan_name: String,
    @ColumnInfo(name = "cust_name")
    var cust_name: String,
    @ColumnInfo(name = "no_of_boxes")
    var no_of_boxes: Int,
    @ColumnInfo(name = "market_plan_id")
    var market_plan_id: Int,
    @ColumnInfo(name = "cust_id")
    var cust_id: Int,
    @ColumnInfo(name = "user_id")
    var user_id: Int,
    @ColumnInfo(name = "remarks")
    var remarks: String,
    @ColumnInfo(name = "is_no_order")
    var is_no_order: Boolean,
    @ColumnInfo(name = "isEnteredErp")
    var isEnteredErp: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var OrderId: Int = 0
}
