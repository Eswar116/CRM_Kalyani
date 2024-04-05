package com.crm.crmapp.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CRMDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(newOrder: NewOrder): Long

    @Insert()
    fun insertImage(orderImage: OrderImage)

    @Query("SELECT * FROM NewOrder")
    fun allOrder(): List<NewOrder>

    @Query("SELECT * FROM NewOrder WHERE OrderId IN (:OrderId)")
    fun orderDetail(OrderId: Int): NewOrder

    @Query("SELECT * FROM OrderImage WHERE OrderImageId IN (:OrderId) ")
    fun allOrderImage(OrderId: Int): List<OrderImage>

    @Insert()
    fun insertCustomer(newCustomerDB: NewCustomerDB): Long

    @Insert()
    fun insertRecentCustome(recentCustomerTable: RecentCustomerTable)


    @Insert()
    fun insertCustomerImage(customerImage: CustomerImage)


    @Query("SELECT * FROM NewCustomerDB")
    fun allCustomer(): List<NewCustomerDB>

    @Query("SELECT * FROM RecentCustomerTable")
    fun allRecentCustomer(): List<RecentCustomerTable>

    @Query("SELECT * FROM CustomerImage WHERE CustomerImageId IN (:CustomerId) ")
    fun allCustomerImage(CustomerId: Int): List<CustomerImage>

    @Query("DELETE FROM NewOrder")
    fun deleteNewOrder()

    @Query("DELETE FROM OrderImage")
    fun deleteImage()


    @Query("DELETE FROM NewCustomerDB")
    fun deleteNewCustomer()

    @Query("DELETE FROM RecentCustomerTable")
    fun deleteRecentCustomer()

    @Query("DELETE FROM CustomerImage")
    fun deleteCustomerImage()


    @Insert()
    fun insertUser(userTable: UserTable)

    @Query("SELECT * FROM UserTable")
    fun allUserData(): List<UserTable>

}
