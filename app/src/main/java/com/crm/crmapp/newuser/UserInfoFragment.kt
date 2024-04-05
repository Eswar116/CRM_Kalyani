package com.crm.crmapp.newuser

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.expense.model.GetUserDetailResponseModel
import com.crm.crmapp.expense.model.GetUserDetailResponseResult
import com.crm.crmapp.expense.model.SearchExpenseResult
import com.crm.crmapp.marketPlan.adapters.StateListAdapter
import com.crm.crmapp.marketPlan.models.StateListRequestResponseModel
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoFragment : Fragment() {

    var userDetailResult: GetUserDetailResponseResult? = null
    lateinit var tvName: TextView
    lateinit var tvMob: TextView
    lateinit var tvEmail: TextView
    lateinit var tvUsername: TextView
    lateinit var tvPassword: TextView
    lateinit var tvType: TextView
    lateinit var tvCategory: TextView
    lateinit var tvDepartment: TextView
    lateinit var tvReportsTo: TextView
    lateinit var rvStates: RecyclerView
    var bundle: Bundle? = null
    var userResult: UserResult? = null
    lateinit var stateList: ArrayList<StateListRequestResponseModel.Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stateList = ArrayList();
        bundle = this.arguments
        if (bundle != null && bundle!!.getString(ConstantVariable.TAG_FROM) != null) {
            if (bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) != null
            ) {
                userResult = bundle!!.getSerializable(ConstantVariable.TAG_OBJECT) as UserResult
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_user_info, container, false)
        declaration(view)
        if (userResult != null) {
            getUserDetailByUserId(userResult!!.getUserId()!!)
            getStateList(userResult!!.getUserId()!!)
        }
        return view;
    }

    fun getUserDetailByUserId(userId: Int) {
        var progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var searchExpenseRequestModel = SearchExpenseResult()
        searchExpenseRequestModel.setUserId(userId)
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<GetUserDetailResponseModel>? =
            apiInterface!!.GetUserDetail(searchExpenseRequestModel)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<GetUserDetailResponseModel> {
            override fun onFailure(call: Call<GetUserDetailResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(rvStates, "Something went wrong")
            }

            override fun onResponse(
                call: Call<GetUserDetailResponseModel>,
                response: Response<GetUserDetailResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {

                    if (response?.body()?.getStatus().equals("success")) {
                        var userDetails =
                            response.body()?.getResult() as List<GetUserDetailResponseResult>
                        if (userDetails.size > 0) {
                            userDetailResult = userDetails.get(0)
                            tvName?.setText(userDetailResult!!.usr_fullname)
                            tvMob?.setText(userDetailResult!!.mobileNo)
                            tvEmail?.setText(userDetailResult!!.emailId)
                            tvUsername?.setText(userDetailResult!!.userName)
                            tvPassword?.setText(userDetailResult!!.user_password)
                            tvType?.setText(userDetailResult!!.userType)
                            tvCategory?.setText(userDetailResult!!.category)
                            tvDepartment?.setText(userDetailResult!!.department)
                            tvReportsTo?.setText(userDetailResult!!.reportsToFullname)
                            //getRequestApiForProfileImage(userDetailResult!!.getProfilePicUrl())
                        }
                    } else {
                        ConstantVariable.onSNACK(rvStates, "Something went wrong")
                    }

                }
            }
        })
    }

    private fun getStateList(userId: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<StateListRequestResponseModel>? =
            apiInterface!!.getStateList(userId)
        call!!.enqueue(object : Callback<StateListRequestResponseModel> {
            override fun onFailure(call: Call<StateListRequestResponseModel>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(rvStates, "Something went wrong")
            }

            override fun onResponse(
                call: Call<StateListRequestResponseModel>,
                response: Response<StateListRequestResponseModel>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    stateList = response.body()
                        ?.getResult() as ArrayList<StateListRequestResponseModel.Result>
                    if (stateList != null && stateList.size > 0) {
                        rvStates.layoutManager =
                            LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                        rvStates.adapter =
                            StateListAdapter(
                                context,
                                stateList,
                                object : StateListAdapter.BtnClickListener {
                                    override fun onBtnClick(
                                        position: Int,
                                        llRow: LinearLayout,
                                        type: String
                                    ) {
                                    }
                                })
                    }

                }
            }
        })
    }


    fun declaration(view: View) {
        tvName = view.findViewById(R.id.tvName)
        tvMob = view.findViewById(R.id.tvMob)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvPassword = view.findViewById(R.id.tvPassword)
        tvType = view.findViewById(R.id.tvType)
        tvCategory = view.findViewById(R.id.tvCategory)
        tvDepartment = view.findViewById(R.id.tvDepartment)
        tvReportsTo = view.findViewById(R.id.tvReportsTo)
        rvStates = view.findViewById(R.id.rvStates)
    }

}