package com.crm.crmapp.fireBaseNotification.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.expense.fragment.ExpenseInfoFragment
import com.crm.crmapp.fireBaseNotification.adapter.NotificationAdapter
import com.crm.crmapp.fireBaseNotification.model.NotificationResponseModel
import com.crm.crmapp.fireBaseNotification.model.NotificationStatusResponse
import com.crm.crmapp.marketPlan.fragment.MarketPlanInfoFragment
import com.crm.crmapp.order.model.RecentOrderRequest
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NotificationFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private var rvNotification: RecyclerView? = null
    private var progressDialog: ProgressDialog? = null
    private var notificationList: ArrayList<NotificationResponseModel.Result>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(R.layout.fragment_notification, container, false)!!
        setIds()
        setListener()
        getNotificationList()
        return viewOfLayout
    }

    private fun setAdapter() {
        rvNotification!!.layoutManager = LinearLayoutManager(context)
        rvNotification!!.adapter = NotificationAdapter(this!!.context!!, notificationList, object : onItemClick {
            override fun getPositionOfList(position: Int) {

                showNotificationStatus(position)
            }
        })
    }

    private fun showNotificationStatus(notiId: Int) {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var notificationRequest = RecentOrderRequest()

        notificationRequest?.setNoti(notificationList?.get(notiId)?.getId())
        var call: Call<NotificationStatusResponse>? = apiInterface!!.getNotificationStatus(notificationRequest)
        call?.enqueue(object : Callback<NotificationStatusResponse> {
            override fun onFailure(call: Call<NotificationStatusResponse>, t: Throwable) {
                progressDialog!!.dismiss()

                //ConstantVariable.onSNACK(rvNotification,"NO Record Found")

                Toast.makeText(activity, "Something went wrong !!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<NotificationStatusResponse>,
                response: Response<NotificationStatusResponse>
            ) {
                progressDialog!!.dismiss()
                try {

                    if (notificationList?.get(notiId)?.getBeanType()!!.equals("Market Plan")) {
                        val bundle = Bundle()
                        var marketPlanInfoFrag = MarketPlanInfoFragment()
                        bundle.putString(ConstantVariable.TAG_FROM, "from_notification_detail")
                        bundle.putInt(ConstantVariable.TAG_OBJECT, notificationList?.get(notiId)?.getBeanId()!!)
                        marketPlanInfoFrag.setArguments(bundle)

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(com.crm.crmapp.R.id.fragment_container, marketPlanInfoFrag)
                            ?.addToBackStack(null)
                            ?.commit()
                    } else {
                        val bundle = Bundle()
                        var expenseInfoFrag = ExpenseInfoFragment()
                        bundle.putString(ConstantVariable.TAG_FROM, "from_notification_detail")
                        bundle.putInt(ConstantVariable.TAG_OBJECT, notificationList?.get(notiId)?.getBeanId()!!)
                        expenseInfoFrag.setArguments(bundle)

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(com.crm.crmapp.R.id.fragment_container, expenseInfoFrag)
                            ?.addToBackStack(null)
                            ?.commit()
                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setListener() {
    }

    private fun setIds() {
        rvNotification = viewOfLayout.findViewById(R.id.rvNotification)
    }


    private fun getNotificationList() {
        notificationList = ArrayList<NotificationResponseModel.Result>()
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading...")
        progressDialog!!.show()
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var notificationRequest = RecentOrderRequest()

        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        if (preferencesHelper!!.userId != null) {
            notificationRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        }
        var call: Call<NotificationResponseModel>? = apiInterface!!.getNotificationApi(notificationRequest)
        call?.enqueue(object : Callback<NotificationResponseModel> {
            override fun onFailure(call: Call<NotificationResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<NotificationResponseModel>,
                response: Response<NotificationResponseModel>
            ) {
                progressDialog!!.dismiss()
                try {
                    notificationList = response.body()?.getResult() as ArrayList<NotificationResponseModel.Result>

                    if (notificationList!!.size > 0) {

                        setAdapter()
                    } else {

                        Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {

                }
            }
        })
    }
}