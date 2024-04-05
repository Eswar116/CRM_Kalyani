package com.crm.crmapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(
        RecentCustomerTable::class,
        NewOrder::class,
        OrderImage::class,
        NewCustomerDB::class,
        CustomerImage::class,
        UserTable::class
    ), version = 1
)

abstract class CRMdatabase : RoomDatabase() {
    abstract fun crmDoa(): CRMDoa

    companion object {
        private var INSTANCE: CRMdatabase? = null
        fun getInstance(context: Context): CRMdatabase? {
            if (INSTANCE == null) {
                synchronized(CRMdatabase::class) {
                    INSTANCE = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        CRMdatabase::class.java, "CRM.db"
                    )
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
