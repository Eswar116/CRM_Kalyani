package com.crm.crmapp.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CustomerImage",
    foreignKeys = [
        ForeignKey(
            entity = NewCustomerDB::class,
            parentColumns = ["CustomerId"],
            childColumns = ["CustomerImageId"],
            onDelete = ForeignKey.CASCADE
        )]
)

class CustomerImage(
    @ColumnInfo(name = "order_attachment_url")
    var order_attachment_url: ByteArray?,
    @ColumnInfo(name = "CustomerImageId")
    var CustomerImageId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var ImageId: Int = 0
}
