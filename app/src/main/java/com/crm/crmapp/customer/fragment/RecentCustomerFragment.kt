package com.crm.crmapp.customer.fragment

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
import com.crm.crmapp.DataBase.CRMdatabase
import com.crm.crmapp.DataBase.RecentCustomerTable
import com.crm.crmapp.R
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.order.util.ConstantVariable.Companion.TAG_OBJECT
import com.crm.crmapp.customer.Interface.onItemClick
import com.crm.crmapp.customer.activity.NewCustomerActivity
import com.crm.crmapp.customer.adapter.RecentCustomerAdapter
import com.crm.crmapp.customer.model.CustomerResult
import com.crm.crmapp.customer.model.RecentCustomerGetterSetter
import com.crm.crmapp.customer.model.RecentCustomerModel
import com.crm.crmapp.order.activity.OfflineOrderActivity
import com.crm.crmapp.order.activity.SearchCustomer
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.order.util.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecentCustomerFragment : Fragment(), View.OnClickListener, onItemClick {
    override fun getPositionOfList(position: Int) {

    }

    private lateinit var viewOfLayout: View
    private lateinit var llSync: LinearLayout
    private lateinit var alDialogData: ArrayList<RecentCustomerModel>
    private lateinit var recentCustomerModel: RecentCustomerModel
    private lateinit var tvAdd: TextView
    private lateinit var rvRecentCustomer: RecyclerView
    private lateinit var tvAllCustomer: TextView
    private var intent: Intent? = null
    var progressDialog: ProgressDialog? = null
    private lateinit var recentCustomerList: ArrayList<CustomerResult>
    var preferencesHelper: PreferencesHelper? = null
    private var mDb: CRMdatabase? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater?.inflate(R.layout.fragment_recent_customer, container, false)
        alDialogData = ArrayList()
        recentCustomerList = ArrayList()
        initVariable()
        setListener()
        // setRecyclerview()
        return viewOfLayout
    }

    override fun onResume() {
        super.onResume()
        try {
            if (ConstantVariable.verifyAvailableNetwork(activity as AppCompatActivity)) {
                getRecentCustomerList()
            } else {
                ConstantVariable.onSNACK(rvRecentCustomer, "Check your internet connection !!")
            }
        } catch (e: Exception) {
            ConstantVariable.onSNACK(rvRecentCustomer, "Oops something went wrong !!")
        }
        checkOffLineData()
    }


    private fun getRecentCustomerList() {
        progressDialog = ConstantVariable.showProgressDialog(this!!.activity!!, "Loading")
        var customerListRequest = RecentCustomerModel()
        customerListRequest.setUserId(preferencesHelper!!.userId!!.toIntOrNull()!!)

        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var call: Call<RecentCustomerGetterSetter>? = apiInterface!!.getRecentCustomerList(customerListRequest)
        Log.e("Orderrequest>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<RecentCustomerGetterSetter> {
            override fun onFailure(call: Call<RecentCustomerGetterSetter>, t: Throwable) {
                progressDialog!!.dismiss()
               // ConstantVariable.onSNACK(rvRecentCustomer, "Something went wrong")
                Toast.makeText(activity,"Something went wrong!!", Toast.LENGTH_SHORT).show()

                //tvNoData.visibility=View.VISIBLE
                //rvList.visibility=View.GONE
            }

            override fun onResponse(
                call: Call<RecentCustomerGetterSetter>,
                response: Response<RecentCustomerGetterSetter>
            ) {
                progressDialog!!.dismiss()
                //tvNoData.visibility=View.GONE
                //rvList.visibility=View.VISIBLE
                if (response.body() != null) {
                    recentCustomerList = response.body()?.getResult() as ArrayList<CustomerResult>

                    if (recentCustomerList.size>0) {
                        // todo insert customer in db

                        val t = Thread {
                            insertCustomerInDB(recentCustomerList, progressDialog!!)
                        }
                        t.start()
                        t.join()

                        setRecyclerview(recentCustomerList)


                    }
                    else{
                        Toast.makeText(activity,"No Record Found", Toast.LENGTH_SHORT).show()
                    }

                    ///  if (currentPage <= TOTAL_PAGES)
                    // else isLastPageScroll = true;
                }
            }
        })
    }

    private fun insertCustomerInDB(
        recentCustomerList: ArrayList<CustomerResult>,
        progressDialog: ProgressDialog
    ) {
        mDb = activity?.let { CRMdatabase.getInstance(it) }
        val preferencesHelper = PreferencesHelper(this!!.activity!!)
        if (mDb != null) {
            val t = Thread {
                mDb!!.crmDoa().deleteRecentCustomer()
            }
            t.start()
            t.join()

        for (i: Int in recentCustomerList.indices) {
            var customer = RecentCustomerTable(
                recentCustomerList.get(i).custName,
                preferencesHelper!!.userId!!.toIntOrNull()!!,
                recentCustomerList.get(i).custType,
                recentCustomerList.get(i).mobileno!!,
                recentCustomerList.get(i).emailid,

                "",
                "",
                false,
                recentCustomerList.get(i).custId
            )

                doAsync {
                    mDb!!.crmDoa().insertRecentCustome(customer)
                }
            }
        }
        progressDialog.dismiss()
    }


    private fun checkOffLineData() {
        doAsync {
            try {
                var mDb = CRMdatabase.getInstance(this!!.activity!!)
                if (mDb != null) {

                    if (mDb!!.crmDoa().allOrder().size > 0 || mDb!!.crmDoa().allCustomer().size > 0) {
                        activity!!.runOnUiThread {
                            llSync.visibility = View.VISIBLE
                        }
                    } else {
                        activity!!.runOnUiThread {
                            llSync.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("exception", "e" + e.message)
            }
        }
    }

    private fun setRecyclerview(recentCustomerList: ArrayList<CustomerResult>) {
        rvRecentCustomer?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvRecentCustomer?.adapter = RecentCustomerAdapter(context!!, recentCustomerList, object : onItemClick {
            override fun getPositionOfList(position: Int) {
                val bundle = Bundle()
                var customerInfoFrag = CustomerInfoFragment()
                bundle.putString(ConstantVariable.TAG_FROM, "from_recent_customer")
                bundle.putSerializable(TAG_OBJECT, recentCustomerList.get(position))
                customerInfoFrag.setArguments(bundle)

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, customerInfoFrag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        })
    }

    private fun setListener() {
        rvRecentCustomer = viewOfLayout.findViewById(R.id.rvRecentCustomer) as RecyclerView
        tvAdd = viewOfLayout.findViewById(R.id.tvAdd) as TextView
        tvAllCustomer = viewOfLayout.findViewById(R.id.tvAllCustomer) as TextView
        preferencesHelper = PreferencesHelper(this!!.activity!!)
        llSync = viewOfLayout.findViewById(R.id.llSync) as LinearLayout
        tvAdd.setOnClickListener(this)
        tvAllCustomer.setOnClickListener(this)
        llSync.setOnClickListener(this)
    }

    private fun initVariable() {
        for (i: Int in 1..5) {
            recentCustomerModel = RecentCustomerModel()
            recentCustomerModel.setName("srikant")
            alDialogData.add(recentCustomerModel)
        }
    }

    override fun onClick(view: View?) {
        when (view) {

            tvAdd -> {
                ConstantVariable.animationEffect(tvAdd, activity!!)
                intent = Intent(activity, NewCustomerActivity::class.java)
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
}
