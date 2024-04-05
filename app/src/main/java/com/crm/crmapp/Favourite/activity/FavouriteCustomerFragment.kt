package com.crm.crmapp.favourite.activity

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
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.fragment.CustomerInfoFragment
import com.crm.crmapp.order.model.RecentOrderRequest
import com.crm.crmapp.order.util.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteCustomerFragment :Fragment(){
    private var viewOfLayout:View ?=null
    private var rvFvrtCustomer:RecyclerView ?=null
    private var progressDialog: ProgressDialog ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(R.layout.fragment_favourite, container, false)

        setIds()
        if (ConstantVariable.verifyAvailableNetwork(this!!.activity!!)){
            getFavrtCustomerResult()
        }
        else{
            Toast.makeText(activity,"No Internet Connection !!",Toast.LENGTH_SHORT)
        }


        return viewOfLayout
    }

    private fun setIds() {
        rvFvrtCustomer=viewOfLayout!!.findViewById(R.id.rvFvrtCustomer)
    }

    private fun setRecyclerview(favrtList: ArrayList<FavrtResponseModel.Result>) {
        rvFvrtCustomer?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvFvrtCustomer?.adapter = FavrtCustomerAdapter(
            context!!,
            favrtList!!,
            object : onItemClick {
                override fun getPositionOfList(position: Int) {
                    val bundle = Bundle()
                    var customerInfoFrag = CustomerInfoFragment()
                    bundle.putString(ConstantVariable.TAG_FROM, "from_search_main_distributor")
                    bundle.putString(ConstantVariable.TAG_OBJECT, position.toString())
                    customerInfoFrag.setArguments(bundle)

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, customerInfoFrag)
                        ?.addToBackStack(null)
                        ?.commit()
                }
            })
    }

    private fun getFavrtCustomerResult(){

        var  favrtList: ArrayList<FavrtResponseModel.Result>
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!,"Loading...")
        progressDialog!!.show()
        var apiInterface  = ApiClient.getClient().create(ApiInterface::class.java)
        var notificationRequest= RecentOrderRequest()

        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        if(preferencesHelper!!.userId!=null) {
            notificationRequest?.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)
        }
        var call : Call<FavrtResponseModel>? = apiInterface!!.getFavrtCustomer(notificationRequest)
        call?.enqueue(object : Callback<FavrtResponseModel> {
            override fun onFailure(call: Call<FavrtResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(activity,"Something went wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<FavrtResponseModel>, response: Response<FavrtResponseModel>) {
                progressDialog!!.dismiss()
                try {
                    favrtList = response.body()?.getResult() as java.util.ArrayList<FavrtResponseModel.Result>

                    if (favrtList!!.size>0) {

                        setRecyclerview(favrtList)
                    }
                    else{

                        Toast.makeText(activity,"No Record Found", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {

                }
            }
        })
    }

}