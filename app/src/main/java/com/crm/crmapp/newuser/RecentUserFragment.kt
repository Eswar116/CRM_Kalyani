package com.crm.crmapp.newuser

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.expense.model.GetUserDetailResponseResult
import com.crm.crmapp.order.activity.OfflineOrderActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.model.NewOrderResponse
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentUserFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewOfLayout: View
    private lateinit var llSync: LinearLayout
    private lateinit var alDialogData: ArrayList<RecentUserModel>
    private lateinit var recentUserModel: RecentUserModel
    private lateinit var tvAdd: TextView
    private lateinit var rvRecentUser: RecyclerView
    private lateinit var tvAllCustomer: TextView
    private var intent: Intent? = null
    var progressDialog: ProgressDialog? = null
    private lateinit var recentUserList: ArrayList<UserResult>
    var preferencesHelper: PreferencesHelper? = null
    private var mDb: CRMdatabase? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater?.inflate(R.layout.fragment_recent_user, container, false)
        alDialogData = ArrayList()
        recentUserList = ArrayList()
        setListener()
        // setRecyclerview()
        return viewOfLayout
    }

    override fun onResume() {
        super.onResume()
        try {
            if (ConstantVariable.verifyAvailableNetwork(activity as AppCompatActivity)) {
                getRecentUserList()
            } else {
                ConstantVariable.onSNACK(rvRecentUser, "Check your internet connection !!")
            }
        } catch (e: Exception) {
            ConstantVariable.onSNACK(rvRecentUser, "Oops something went wrong !!")
        }
        //checkOffLineData()
    }

    private fun getRecentUserList() {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<RecentUserGetterSetter>? = apiInterface!!.getRecentUsersList()
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<RecentUserGetterSetter> {
            override fun onFailure(call: Call<RecentUserGetterSetter>, t: Throwable) {
                progressDialog!!.dismiss()
                // ConstantVariable.onSNACK(rvRecentCustomer, "Something went wrong")
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(
                call: Call<RecentUserGetterSetter>,
                response: Response<RecentUserGetterSetter>
            ) {
                progressDialog!!.dismiss()
                //tvNoData.visibility=View.GONE
                //rvList.visibility=View.VISIBLE
                if (response.body() != null) {
                    recentUserList = response.body()?.getResult() as ArrayList<UserResult>

                    if (recentUserList.size > 0) {
                        // todo insert customer in db
                        /*val t = Thread {
                            insertCustomerInDB(recentUserList, progressDialog!!)
                        }
                        t.start()
                        t.join()*/
                        setRecyclerview(recentUserList)
                    } else {
                        Toast.makeText(activity, "No Record Found", Toast.LENGTH_SHORT).show()
                    }

                    ///  if (currentPage <= TOTAL_PAGES)
                    // else isLastPageScroll = true;
                }
            }
        })
    }

    private fun setRecyclerview(recentUserList: ArrayList<UserResult>) {
        rvRecentUser?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvRecentUser?.adapter = RecentUserAdapter(context!!, recentUserList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val bundle = Bundle()
                val userInfoFrag = UserInfoFragment()
                bundle.putString(ConstantVariable.TAG_FROM, "from_recent_user")
                bundle.putSerializable(ConstantVariable.TAG_OBJECT, recentUserList.get(position))
                userInfoFrag.setArguments(bundle)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, userInfoFrag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }, object : RecentUserAdapter.OnActivateClick{
            override fun getPositionOfList(position: Int) {
                updateCustomer(rvRecentUser, recentUserList.get(position).getUserId()!!,1)
            }

        })
    }


    override fun onClick(view: View?) {
        when (view) {

            tvAdd -> {
                ConstantVariable.animationEffect(tvAdd, activity!!)
                intent = Intent(activity, NewUserCreateActivity::class.java)
                intent!!.putExtra(ConstantVariable.TAG_FROM, "new")
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
            tvAllCustomer -> {
                ConstantVariable.animationEffect(tvAllCustomer, activity!!)
                intent = Intent(activity, SearchCustomer::class.java)
                //intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }

            llSync -> {
                ConstantVariable.animationEffect(this!!.llSync!!, activity!!)
                intent = Intent(activity, OfflineOrderActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }
    }

    private fun setListener() {
        rvRecentUser = viewOfLayout.findViewById(R.id.rvRecentCustomer) as RecyclerView
        tvAdd = viewOfLayout.findViewById(R.id.tvAdd) as TextView
        tvAllCustomer = viewOfLayout.findViewById(R.id.tvAllCustomer) as TextView
        preferencesHelper = PreferencesHelper(this!!.activity!!)
        llSync = viewOfLayout.findViewById(R.id.llSync) as LinearLayout
        tvAdd.setOnClickListener(this)
        tvAllCustomer.setOnClickListener(this)
        llSync.setOnClickListener(this)
    }

    private fun updateCustomer(view: View, userId: Int, isActive: Int) {
        val progressDialog = ConstantVariable.showProgressDialog(context as Activity, "Loading...")
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val result1 = GetUserDetailResponseResult()
        result1.setUserId(userId)
        result1.userName = ""
        result1.user_password = ""
        result1.category = ""
        result1.fName = ""
        result1.lName = ""
        result1.reportsTo = 0
        result1.mobileNo = ""
        result1.emailId = ""
        result1.isactive = isActive.toString()
        val statelist = ArrayList<GetUserDetailResponseResult.Result>()
        result1.allotedStates = statelist

        val call: Call<NewOrderResponse> = apiInterface!!.SaveUser(result1)

        call!!.enqueue(object : Callback<NewOrderResponse> {
            override fun onFailure(call: Call<NewOrderResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                ConstantVariable.onSNACK(view, "Something went wrong")
            }

            override fun onResponse(
                call: Call<NewOrderResponse>,
                response: Response<NewOrderResponse>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response?.body()?.getStatus().equals("Success", ignoreCase = true)) {
                        ConstantVariable.onToastSuccess(context as Activity, response.body()!!.getMessage()!!)
                        getRecentUserList()
                    } else {
                        ConstantVariable.onSNACK(view, response.body()!!.getMsg()!!)
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    ConstantVariable.onSNACK(view, "Something went wrong!!")
                }
            }
        })
    }

}