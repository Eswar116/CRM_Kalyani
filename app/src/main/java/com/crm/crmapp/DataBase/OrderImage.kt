package com.crm.crmapp.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity(
    tableName = "OrderImage",
    foreignKeys = [
        ForeignKey(
            entity = NewOrder::class,
            parentColumns = ["OrderId"],
            childColumns = ["OrderImageId"],
            onDelete = CASCADE
        )]
)


class OrderImage(
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var order_attachment_url: ByteArray? = null,
    @ColumnInfo(name = "OrderImageId")
    var OrderImageId: Int
) {

    @PrimaryKey(autoGenerate = true)
    var ImageId: Int = 0

}