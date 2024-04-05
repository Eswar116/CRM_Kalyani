package com.crm.crmapp.order.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.adapter.OfflineAdapterCustomer
import com.crm.crmapp.customer.model.SaveCustomerRequestModel
import com.crm.crmapp.customer.model.SaveCustomerResult1
import com.crm.crmapp.customer.model.SaveCustomerResult2
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.DataBase.NewCustomerDB
import com.crm.crmapp.DataBase.NewOrder
import com.crm.crmapp.order.adapter.OffLineOrderAdapter
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.order.util.doAsync
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfflineOrderActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var rvList: RecyclerView
    private lateinit var rvCustomer: RecyclerView
    private lateinit var tvHeader: TextView
    private lateinit var tvNoData: TextView
    private lateinit var llFilter: LinearLayout
    private lateinit var llSort: LinearLayout
    private lateinit var llNew: LinearLayout
    private lateinit var ivSync: ImageView
    private lateinit var llBottom: LinearLayout
    private lateinit var orderAllModelList: ArrayList<OrderDetailResult1>
    private lateinit var dataBaseOrderList: List<NewOrder>
    private lateinit var databaseCustomerList: List<NewCustomerDB>
    private lateinit var databaseModelCustomerList: ArrayList<SaveCustomerResult1>
    private var mDb: CRMdatabase? = null
    private var progressDialog: ProgressDialog? = null
    private var result1: NewOrderResult1? = null
    private var resultList1: ArrayList<NewOrderResult1>? = null
    private var result2: NewOrderResult2? = null
    private var resultList2: ArrayList<NewOrderResult2>? = null
    private var imageBase64: ArrayList<String>? = null
    private var resultCustomerList1: java.util.ArrayList<SaveCustomerResult1>? = null
    private var resultCustomerList2: java.util.ArrayList<SaveCustomerResult2>? = null
    private var saveCustomerRequest: SaveCustomerRequestModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offline_layout_activity)
        orderAllModelList = ArrayList<OrderDetailResult1>()
        dataBaseOrderList = ArrayList<NewOrder>()
        databaseCustomerList = ArrayList<NewCustomerDB>()
        resultList1 = ArrayList()
        resultList2 = ArrayList()
        resultCustomerList1 = ArrayList()
        resultCustomerList2 = ArrayList()
        databaseModelCustomerList = ArrayList()
        imageBase64 = ArrayList()
        findId()
        getDataFromDatabase()
        setListener()

    }


    private fun setListener() {
        ivSync.setOnClickListener {
            if (ConstantVariable.verifyAvailableNetwork(this@OfflineOrderActivity)) {

                progressDialog = ConstantVariable.showProgressDialog(this, "Loading...")

                progressDialog!!.show()
                var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
                mDb = CRMdatabase.getInstance(this@OfflineOrderActivity)

                if (databaseModelCustomerList.size > 0) {
                    createHandler(apiInterface, mDb, progressDialog!!)
                } else {
                    if (orderAllModelList.size > 0) {
                        // syncData(apiInterface, mDb,0)
                        for (i: Int in orderAllModelList.indices) {
                            createHandlerSyncOrder(
                                apiInterface,
                                mDb,
                                orderAllModelList.get(i).getCustId()!!,
                                orderAllModelList.get(i).getMarketPlanId()!!,
                                progressDialog!!
                            )
                        }
                    }

                }
            } else {
                ConstantVariable.onSNACK(ivSync, "Please check your internet connection.")
            }
        }
    }

    private fun setAdapterOrder() {
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = OffLineOrderAdapter(this, orderAllModelList, object : onItemClick {
            override fun getPositionOfList(orderId: Int) {
                val intent = Intent(this@OfflineOrderActivity, OrderDetailActivity::class.java)
                intent.putExtra(ConstantVariable.offline_order_id, orderId)
                startActivity(intent)
                this@OfflineOrderActivity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        })
    }

    private fun setAdapterCustomer() {
        rvCustomer.layoutManager = LinearLayoutManager(this)
        rvCustomer.adapter = OfflineAdapterCustomer(this, databaseModelCustomerList, object : onItemClick {
            override fun getPositionOfList(position: Int) {

            }
        })
    }

    private fun syncData(
        apiInterface: ApiInterface,
        mDb: CRMdatabase?,
        custId: Int?,
        marketPlanId: Int?,
        progressDialog: ProgressDialog
    ) {
        resultList1?.clear()
        resultList2?.clear()


        var orderDetailRequest = NewOrderRequest()
        orderDetailRequest.setMessage("Record Found")
        orderDetailRequest.setStatus("success")
        for (i: Int in orderAllModelList.indices) {
            result1 = NewOrderResult1()
            //todo set cust id 0 always when syncing with server
            if (orderAllModelList.get(i).getMarketPlanId() != 0) {
                result1?.setMarketPlanId(marketPlanId!!)
            } else {
                result1?.setMarketPlanId(marketPlanId!!)
            }

            if (orderAllModelList.get(i).getCustId() != 0) {
                result1?.setCustId(custId!!)
            } else {
                result1?.setCustId(custId!!)
            }
            val preferencesHelper = PreferencesHelper(this)
            result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
            result1?.setNoOfBoxes(orderAllModelList.get(i).getNoOfBoxes().toString())
            result1?.setOrderDate(orderAllModelList.get(i).getOrderDate().toString())
            if (orderAllModelList.get(i).getRemarks() != null) {
                result1?.setRemarks(orderAllModelList.get(i).getRemarks()!!)
            } else {
                result1?.setRemarks("")
            }

            if (orderAllModelList.get(i).getIsEnteredErp() == true) {
                result1?.setIsEnteredErp(0)
            } else {
                result1?.setIsEnteredErp(1)
            }
            if (orderAllModelList.get(i).getIsNoOrder() == true) {
                result1?.setIsNoOrder(0)
            } else {
                result1?.setIsNoOrder(1)
            }
            result1?.setStatusText("New")
            resultList1?.add(this!!.result1!!)
            orderDetailRequest.setResult1(this!!.resultList1!!)
            imageBase64 = (checkImageOffLine("order", this.mDb!!, orderAllModelList.get(i).getOrderId()!!))

            if (imageBase64?.size!! > 0) {
                for (i: Int in imageBase64!!.indices) {
                    result2 = NewOrderResult2()
                    result2!!.setOrderAttachmentUrl(
                        ConstantVariable.convertByteArray(
                            ConstantVariable.StringToBitMap(
                                imageBase64!!.get(i)
                            )!!
                        )
                    )
                    resultList2!!.add(result2!!)
                    orderDetailRequest.setResult2(this!!.resultList2!!)
                }
            } else {
                orderDetailRequest.setResult2(this!!.resultList2!!)

            }
        }

        var call: Call<NewOrderResponse>? = apiInterface!!.sendOrderDetail(orderDetailRequest)
        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(ivSync, t.toString())
            }

            override fun onResponse(call: Call<NewOrderResponse>, response: Response<NewOrderResponse>) {
                progressDialog!!.dismiss()

                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {

                        doAsync {

                            mDb!!.crmDoa().deleteImage()
                            mDb!!.crmDoa().deleteNewOrder()
                        }
                        ConstantVariable.onSNACKSuccess(ivSync, response.body()!!.getMessage()!!)
                        finish()
                    } else {
                        ConstantVariable.onSNACK(ivSync, response.body()!!.getMessage()!!)
                    }
                } catch (e: NullPointerException) {
                    progressDialog!!.dismiss()

                }
            }
        })
    }


    private fun createHandler(
        apiInterface: ApiInterface,
        mDb: CRMdatabase?,
        progressDialog: ProgressDialog
    ) {
        val thread = object : Thread() {
            override fun run() {
                Looper.prepare()

                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {

                        if (mDb!!.crmDoa().allCustomer().size > 0) {
                            syncCustomerDataApi(
                                apiInterface,
                                mDb!!.crmDoa().allCustomer(), mDb!!, progressDialog
                            )
                        }
                        handler.removeCallbacks(this)
                        Looper.myLooper()!!.quit()
                    }
                }, 2000)

                Looper.loop()
            }
        }
        thread.start()
    }


    private fun createHandlerSyncOrder(
        apiInterface: ApiInterface,
        mDb: CRMdatabase?,
        CustId: Int,
        marketPlanId: Int,
        progressDialog: ProgressDialog
    ) {
        val thread = object : Thread() {
            override fun run() {
                Looper.prepare()

                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        syncData(apiInterface, mDb, CustId, marketPlanId, progressDialog)
                        handler.removeCallbacks(this)
                        Looper.myLooper()!!.quit()
                    }
                }, 2000)

                Looper.loop()
            }
        }
        thread.start()
    }

    private fun syncCustomerDataApi(
        apiInterface: ApiInterface,
        allCustomer: List<NewCustomerDB>,
        mDb: CRMdatabase,
        progressDialog: ProgressDialog
    ) {
        resultCustomerList2!!.clear()
        resultCustomerList1!!.clear()


        for (i: Int in databaseModelCustomerList.indices) {
            val result1 = SaveCustomerResult1()

            val preferencesHelper = PreferencesHelper(this@OfflineOrderActivity)
            result1?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
            result1?.setCustName(allCustomer.get(i).cust_name)
            result1?.setCustType(allCustomer.get(i).cust_category)
            result1?.setAddress(allCustomer.get(i).cust_address)
            result1?.setMobileno(allCustomer.get(i).cust_mobile)
            result1?.setEmailid(allCustomer.get(i).cust_email)
            result1?.setRemarks(allCustomer.get(i).remarks)
            resultCustomerList1?.add(result1!!)
            saveCustomerRequest = SaveCustomerRequestModel()
            saveCustomerRequest!!.setResult1(this!!.resultCustomerList1!!)
            imageBase64 = (checkImageOffLine("customer", mDb, allCustomer.get(i).CustomerId))

            for (i: Int in imageBase64!!.indices) {
                var result2 = SaveCustomerResult2()
                if (imageBase64 != null)
                    result2!!.setAttachment(imageBase64!!.get(i))
                result2!!.setType("image")
                result2!!.setExtension(".jpg")
                resultCustomerList2!!.add(result2!!)
            }

            saveCustomerRequest!!.setResult2(this!!.resultCustomerList2!!)
        }
        var call: Call<NewOrderResponse>? = apiInterface!!.saveCustomer(this!!.saveCustomerRequest!!)

        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(ivSync, t.toString())
            }

            override fun onResponse(call: Call<NewOrderResponse>, response: Response<NewOrderResponse>) {
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onSNACK(ivSync, response.body()!!.getStatus()!!)
                        doAsync {
                            mDb!!.crmDoa().deleteNewCustomer()
                            mDb!!.crmDoa().deleteCustomerImage()
                        }


                        if (orderAllModelList.size > 0) {

                            val t = Thread {
                                createHandlerSyncOrder(
                                    apiInterface,
                                    mDb,
                                    response.body()!!.getCustId()!!,
                                    0,
                                    progressDialog
                                )
                            }
                            t.start()
                            t.join()

                        }


                    } else {
                        ConstantVariable.onSNACK(ivSync, response.body()!!.getStatus()!!)
                        progressDialog!!.dismiss()
                    }
                } catch (e: NullPointerException) {
                    ConstantVariable.onSNACK(ivSync, "Something went wrong!!")
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    private fun checkImageOffLine(value: String, mDb: CRMdatabase, orderId: Int): ArrayList<String>? {
        val t = Thread {
            imageBase64?.clear()
            if (value.equals("order", ignoreCase = true)) {
                var listOrderImage = mDb!!.crmDoa().allOrderImage(orderId)
                for (i: Int in listOrderImage.indices) {
                    imageBase64!!.add(ConstantVariable.convertByteArrayToBase64(listOrderImage.get(i).order_attachment_url!!)!!)
                }
            } else {
                var listOrderImage = mDb!!.crmDoa().allCustomerImage(orderId)
                for (i: Int in listOrderImage.indices) {
                    imageBase64!!.add(ConstantVariable.convertByteArrayToBase64(listOrderImage.get(i).order_attachment_url!!)!!)
                }
            }
        }
        t.start()
        t.join()
        return imageBase64
    }

    private fun findId() {
        orderAllModelList = ArrayList<OrderDetailResult1>()
        rvList = findViewById(R.id.rvList)
        rvCustomer = findViewById(R.id.rvCustomer)
        ivSync = findViewById(R.id.ivSync) as ImageView
        llFilter = findViewById(R.id.llFilter)
        llSort = findViewById(R.id.llSort)
        llBottom = findViewById(R.id.llBottom)
        llNew = findViewById(R.id.llNew)
        tvHeader = findViewById(R.id.tvHeader)
        tvNoData = findViewById(R.id.tvNoData)
        llBottom.visibility = View.GONE
        ivSync.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun getDataFromDatabase() {
        mDb = CRMdatabase.getInstance(this)
        orderAllModelList.clear()
        databaseModelCustomerList.clear()

        if (mDb != null) {
            progressDialog = ConstantVariable.showProgressDialog(this@OfflineOrderActivity, "Loading")
            AsyncTask.execute {
                dataBaseOrderList = mDb!!.crmDoa().allOrder()
                if (dataBaseOrderList.size > 0) {
                    for (item: Int in dataBaseOrderList.indices) {
                        var result = OrderDetailResult1()
                        result.setCustName(dataBaseOrderList.get(item).cust_name)
                        result.setOrderDate(dataBaseOrderList.get(item).order_date)
                        result.setUserName(dataBaseOrderList.get(item).user_name)
                        result.setOrderId(dataBaseOrderList.get(item).OrderId)
                        result.setNoOfBoxes(dataBaseOrderList.get(item).no_of_boxes)
                        result.setCustId(dataBaseOrderList.get(item).cust_id)
                        result.setMarketPlanId(dataBaseOrderList.get(item).market_plan_id)
                        result.setIsNoOrder(dataBaseOrderList.get(item).is_no_order)
                        result.setIsEnteredErp(dataBaseOrderList.get(item).isEnteredErp)
                        orderAllModelList.add(result)
                        progressDialog!!.dismiss()
                        setAdapterOrder()
                    }
                } else {
                    progressDialog!!.dismiss()
                }

                databaseCustomerList = mDb!!.crmDoa().allCustomer()
                if (databaseCustomerList.size > 0) {
                    for (item: Int in databaseCustomerList.indices) {
                        val result1 = SaveCustomerResult1()
                        result1.setCustName(databaseCustomerList.get(item).cust_name)
                        result1.setAddress(databaseCustomerList.get(item).cust_address)
                        result1.setEmailid(databaseCustomerList.get(item).cust_email)
                        result1.setRemarks(databaseCustomerList.get(item).remarks)
                        result1.setMobileno(databaseCustomerList.get(item).cust_mobile)
                        result1.setCustType(databaseCustomerList.get(item).cust_category)
                        result1.setUserId(databaseCustomerList.get(item).user_Id)
                        databaseModelCustomerList.add(result1)
                        progressDialog!!.dismiss()
                        setAdapterCustomer()
                    }
                } else {
                    progressDialog!!.dismiss()
                }
            }
        }
    }
}
