package com.crm.crmapp.marketPlan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResponsiblePersonViewHolder(application: Application) : AndroidViewModel(application) {

    var data: MutableLiveData<List<ResponsiblePersoneResponse.Result>>? = null
    private val context = getApplication<Application>().applicationContext

    fun getHeroes(stateId: String): LiveData<List<ResponsiblePersoneResponse.Result>> {
        //if the list is null
        if (data == null) {
            data = MutableLiveData<List<ResponsiblePersoneResponse.Result>>()
            //we will load it asynchronously from server in this method
            setListApi(stateId)
        }
        //finally we will return the list
        return data as MutableLiveData<List<ResponsiblePersoneResponse.Result>>
    }


    private fun setListApi(stateId: String) {

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val preferencesHelper = PreferencesHelper(context)
        var int = preferencesHelper.userId?.toInt()
        var call: Call<ResponsiblePersoneResponse>? = apiInterface!!.responsiblePersonList(int!!, stateId)  // commented on 16-06-2020 due to to no API from backend
        //var call: Call<ResponsiblePersoneResponse>? = apiInterface!!.responsiblePersonList(int!!)

        call?.enqueue(object : Callback<ResponsiblePersoneResponse> {
            override fun onFailure(call: Call<ResponsiblePersoneResponse>, t: Throwable) {
                // progressDialog!!.dismiss()
                //tvApply?.let { ConstantVariable.onSNACK(it,"NO Record Found") }
            }

            override fun onResponse(
                call: Call<ResponsiblePersoneResponse>,
                response: Response<ResponsiblePersoneResponse>
            ) {
                // progressDialog!!.dismiss()
                if (response.body() != null) {
                    var complaintResultList =
                        response.body()?.getResult() as ArrayList<ResponsiblePersoneResponse.Result>
                    data!!.value = complaintResultList
                }
//                if (complaintResultList.size>0) {
//                    setRecyclerView(complaintResultList)
//                }
//                else{
//                   // Toast.makeText(activity,"No Record Found", Toast.LENGTH_SHORT).show()
//                }
            }
        })
    }
}