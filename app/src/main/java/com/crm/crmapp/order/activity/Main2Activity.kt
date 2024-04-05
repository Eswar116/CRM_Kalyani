package com.crm.crmapp.order.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crm.crmapp.CRMDashboardModel.*
import com.crm.crmapp.R
import com.crm.crmapp.order.util.ConstantVariable
import com.crm.crmapp.aboutUS.AboutUsFragment
import com.crm.crmapp.attendence.fragment.AttendenceFragment
import com.crm.crmapp.cRMDashboardModel.adapter.CRMDashboardResult1GridAdapter
import com.crm.crmapp.cRMDashboardModel.adapter.CRMDashboardResult3GridAdapter
import com.crm.crmapp.competition.fragment.AllCompetitionFragment
import com.crm.crmapp.complaint.fragment.AllComplaintListFragment
import com.crm.crmapp.customer.fragment.CustomerInfoFragment
import com.crm.crmapp.customer.fragment.RecentCustomerFragment
import com.crm.crmapp.expense.fragment.ExpenseInfoFragment
import com.crm.crmapp.expense.fragment.ExpensePendingFragment
import com.crm.crmapp.expense.fragment.ExpenseRecentFragment
import com.crm.crmapp.expense.model.SearchExpenseRequestModel
import com.crm.crmapp.favourite.activity.FavouriteCustomerFragment
import com.crm.crmapp.fireBaseNotification.fragment.NotificationFragment
import com.crm.crmapp.marketPlan.fragment.MarketPlanListFragment
import com.crm.crmapp.newuser.RecentUserFragment
import com.crm.crmapp.order.fragment.RecentOrderFragment
import com.crm.crmapp.order.model.*
import com.crm.crmapp.order.util.PreferencesHelper
import com.crm.crmapp.registration.LoginActivity
import com.crm.crmapp.retrofit.ApiClient
import com.crm.crmapp.retrofit.ApiClient.Companion.checkClientActiveInActive
import com.crm.crmapp.retrofit.ApiInterface
import com.crm.crmapp.salesscheme.SalesSchemeFragment
import com.crm.crmapp.search.SearchMainActivity
import com.crm.crmapp.search.SearchMainResponse
import com.crm.crmapp.settings.SettingsFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity as Activity1

class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var searchCustomerobj: SearchCustomerResultModel? = null
    var marketPlanObj: SearchCustomerResultModel? = null
    var ivProfile: ImageView? = null
    var tvName: TextView? = null
    var email: TextView? = null
    var context: Context? = null
    lateinit var context1: Context

    private var preferencesHelper: PreferencesHelper? = null

    //    private lateinit var progressDialog: ProgressDialog?
    private var progressDialog: ProgressDialog? = null
    private var count: Int? = 0
    private var textCartItemCount: TextView? = null
    var doubleBackToExitPressedOnce = false
    lateinit var getActiveUser: ArrayList<CheckUserActiveResponse.Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        preferencesHelper = PreferencesHelper(this)
        context1 = this@Main2Activity
        setIds()
        var filter = IntentFilter(ConstantVariable.NotificationIntentFilter)
        LocalBroadcastManager.getInstance(this).registerReceiver(this!!.broadcastReceiver!!, filter)

        try {
//            if (Constants.CHECHACTIVEINACTIVE.equals("success", ignoreCase = true)) {
            if (preferencesHelper!!.getObject() != null) {
                val userData = preferencesHelper!!.getObject()
                tvName?.setText(userData.getUserName())
                email?.setText(userData.getEmailId())
                if (userData.getCategory().equals("admin", true)) {
                    nav_view.getMenu().findItem(R.id.nav_users).isVisible = true;
                    nav_view.getMenu().findItem(R.id.nav_sales_scheme).isVisible = true;
                } else {
                    nav_view.getMenu().findItem(R.id.nav_users).isVisible = false;
                    nav_view.getMenu().findItem(R.id.nav_sales_scheme).isVisible = false;
                }
                if (preferencesHelper!!.image != null && !preferencesHelper!!.image.equals(
                        "",
                        ignoreCase = true
                    )
                ) {
                    ivProfile?.setImageBitmap(ConstantVariable.StringToBitMap(preferencesHelper!!.image!!))
                } else if (userData.getProfilePicUrl() != null || !userData.getProfilePicUrl()!!
                        .contains(
                            "",
                            ignoreCase = true
                        )
                ) {
                    if (ConstantVariable.verifyAvailableNetwork(this@Main2Activity)) {
                        getRequestApiForImage(userData.getProfilePicUrl()!!) // Anil 02-11-2023
                    } else {
                        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT)
                    }
                }
            }
//            }
//            else {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null
            && intent.getStringExtra(ConstantVariable.TAG_FROM)
                .equals("from_search_customer", ignoreCase = true)
        ) {
            searchCustomerobj =
                intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as SearchCustomerResultModel
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            var bundle = Bundle();
            bundle.putString(ConstantVariable.TAG_FROM, "from_search_customer")
            bundle.putSerializable(ConstantVariable.TAG_OBJECT, searchCustomerobj)
            var fragment: Fragment = CustomerInfoFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null
            && intent.getStringExtra(ConstantVariable.TAG_FROM)
                .equals("from_search_main_customer", ignoreCase = true)
        ) {
            var result =
                intent.getSerializableExtra(ConstantVariable.TAG_OBJECT) as SearchMainResponse.Result
            //var custId = intent.getStringExtra(ConstantVariable.TAG_OBJECT) as String
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            var bundle = Bundle()
            bundle.putString(ConstantVariable.TAG_FROM, "from_search_main_customer")
            bundle.putSerializable(ConstantVariable.TAG_OBJECT, result)
            var fragment: Fragment = CustomerInfoFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            drawer_layout.closeDrawer(GravityCompat.START)

        } else if (intent.getStringExtra(ConstantVariable.TAG_FROM) != null
            && intent.getStringExtra(ConstantVariable.TAG_FROM)
                .equals("from_new_order", ignoreCase = true)
        ) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            var bundle = Bundle();
            bundle.putString(ConstantVariable.TAG_FROM, "from_new_order")
            var fragment: Fragment = MarketPlanListFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (intent.getStringExtra(ConstantVariable.firebaseType) != null && intent.getStringExtra(
                ConstantVariable.firebaseType
            ).equals("Expenses", ignoreCase = true)
        ) {

            var bundle = Bundle()
            bundle.putString(
                ConstantVariable.firebase_bean_id,
                intent.getStringExtra(ConstantVariable.firebase_bean_id)
            )
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            var fragment: Fragment = ExpenseInfoFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (intent.getStringExtra(ConstantVariable.firebaseType) != null && intent.getStringExtra(
                ConstantVariable.firebaseType
            ).equals("Customer", ignoreCase = true)
        ) {
            var bundle = Bundle()
            bundle.putString(
                ConstantVariable.firebase_bean_id,
                intent.getStringExtra(ConstantVariable.firebase_bean_id)
            )
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            var fragment: Fragment = CustomerInfoFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            var fragment: Fragment = WalletFragment()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


    }

    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            count = intent.getIntExtra("NotificationCount", 0)
            Log.e("receiver", "Got message: " + count)
            if (count!! > 0) {
                textCartItemCount!!.setText(count.toString())
            } else {
                textCartItemCount!!.setText("")
                textCartItemCount!!.visibility = View.GONE
            }
            ConstantVariable.NotificationCount = count
            textCartItemCount!!.isEnabled = true
        }
    }

    private fun getRequestApiForImage(docDetailList: String) {
        var progressDialog = ConstantVariable.showProgressDialog(this@Main2Activity, "Loading...")


        var apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        var imageRequest = ImageRequest()
        imageRequest.setImgPath(docDetailList!!)
        var call: Call<ImageRequestResponse> = apiInterface!!.getImage(imageRequest)
        call!!.enqueue(object : Callback<ImageRequestResponse> {
            override fun onFailure(call: Call<ImageRequestResponse>, t: Throwable) {
                progressDialog!!.dismiss()
            }

            override fun onResponse(
                call: Call<ImageRequestResponse>,
                response: Response<ImageRequestResponse>
            ) {
                progressDialog!!.dismiss()
                if (response?.body()?.getMsg().equals("success")) {
                    var imageUrl = response.body()?.getImgString() as String
                    preferencesHelper?.image = imageUrl
                    ivProfile?.setImageBitmap(ConstantVariable.StringToBitMap(imageUrl!!))
                }
            }
        })
    }

    private fun setIds() {
        context = this;
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        tvName = headerView.findViewById(R.id.tvName) as? TextView
        email = headerView.findViewById(R.id.email) as? TextView
        ivProfile = headerView.findViewById(R.id.ivProfile) as? ImageView
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStackImmediate()
            } else if (fragment is WalletFragment) {

                if (!doubleBackToExitPressedOnce) {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT)
                        .show()
                    Handler().postDelayed({
                        run {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000)

                } else {
                    super.onBackPressed()
                    return
                }
            } else {
                super.onBackPressed()
                return
            }
        }
    }

    override fun onStart() {
        super.onStart()
        var filter = IntentFilter(ConstantVariable.NotificationIntentFilter)
        LocalBroadcastManager.getInstance(this).registerReceiver(this!!.broadcastReceiver!!, filter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main2, menu)
        val menuItem = menu.findItem(R.id.notification)
        val menuFavrt = menu.findItem(R.id.favrt)
        val actionView = MenuItemCompat.getActionView(menuItem)
        textCartItemCount = actionView.findViewById(R.id.cart_badge) as TextView

        var frameLayout = actionView.findViewById(R.id.ivFrameLayout) as FrameLayout
        textCartItemCount!!.isEnabled = true
        if (ConstantVariable.NotificationCount != null) {
            textCartItemCount!!.setText(ConstantVariable.NotificationCount.toString()!!)
        } else {
            textCartItemCount!!.setText("")
            textCartItemCount!!.visibility = View.GONE
        }
        frameLayout!!.setOnClickListener {
            var fragment: Fragment? = null
            var fragmentManager = supportFragmentManager
            var fragmentTransaction = fragmentManager!!.beginTransaction()

            fragment = NotificationFragment()
            if (fragment != null) {
                fragmentTransaction!!.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                    .replace(R.id.fragment_container, fragment!!)
                    ?.addToBackStack(null)
            }

            if (textCartItemCount != null) {
                textCartItemCount!!.setText("")
                textCartItemCount!!.visibility = View.GONE
            }

            fragmentTransaction!!.commit()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> /*{}return true
             else -> return super.onOptionsItemSelected(item)*/ {
                var intent = Intent(this@Main2Activity, SearchMainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.pending -> {
                var fragment: Fragment? = null
                var fragmentManager = supportFragmentManager
                var fragmentTransaction = fragmentManager!!.beginTransaction()
                fragment = ExpensePendingFragment()
                if (fragment != null) {
                    fragmentTransaction!!.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .replace(R.id.fragment_container, fragment!!)
                        ?.addToBackStack(null)
                }
                fragmentTransaction!!.commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager!!.beginTransaction()

        when (item.itemId) {
            R.id.nav_home -> {
                fragment = WalletFragment()
            }
            R.id.favrt -> {
                fragment = FavouriteCustomerFragment()
            }
            R.id.nav_customers -> {
                fragment = RecentCustomerFragment()
            }
            R.id.nav_sales_orders -> {
                fragment = RecentOrderFragment()
            }
            R.id.nav_collections -> {

            }
            R.id.nav_expense -> {
                fragment = ExpenseRecentFragment()
            }
            R.id.nav_attendance -> {
                fragment = AttendenceFragment()
            }
            R.id.nav_marketPlan -> {
                fragment = MarketPlanListFragment()
            }

            R.id.nav_complaint -> {
                fragment = AllComplaintListFragment()
            }
            R.id.nav_competition -> {
                fragment = AllCompetitionFragment()
            }
            R.id.nav_users -> {
                fragment = RecentUserFragment()
            }
            R.id.nav_sales_scheme -> {
                fragment = SalesSchemeFragment()
                //ConstantVariable.onToast(context!!,"On development")
            }
            R.id.nav_about -> {
                fragment = AboutUsFragment()
            }
            R.id.nav_settings -> {
                fragment = SettingsFragment()
            }
            R.id.nav_logout -> {
                logoutUser(preferencesHelper!!.getObject().getUserName()!!)

                preferencesHelper?.removeSahredPre()
                var intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
                ConstantVariable.animationEffect(this.tvName!!, context as Activity1)
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish()
            }
        }

        if (fragment != null) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
            fragmentTransaction.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    class WalletFragment : Fragment() {
        private var preferencesHelper: PreferencesHelper? = null
        private var result3_recycler: RecyclerView? = null
        private var result1_recycler: RecyclerView? = null
        private var tvAmount: TextView? = null
        private var tvexpdate: TextView? = null
        private var tvcustname: TextView? = null
        private var tvcusttype: TextView? = null
        private var tvgrpcustName: TextView? = null
        private var tvPhone: TextView? = null
        private var tvEmailid: TextView? = null
        private var tvPunchIn: TextView? = null
        private var tvPunchOut: TextView? = null
        private var enter_plan_days: LinearLayout? = null
        private var tvdate: TextView? = null
        private var tvDays: TextView? = null
        private var btnSubmit: Button? = null
        private var attendanceCard: CardView? = null
        private var textCartItemCount2: TextView? = null

        @SuppressLint("MissingInflatedId")
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            var viewOfLayout =
                inflater.inflate(R.layout.activity_dashboard_wallet, container, false)
            preferencesHelper = PreferencesHelper(requireContext())
            result3_recycler = viewOfLayout.findViewById(R.id.result3_recycler) as RecyclerView
            result1_recycler = viewOfLayout.findViewById(R.id.result1_recycler) as RecyclerView
            tvAmount = viewOfLayout.findViewById(R.id.tvAmount) as TextView
            tvexpdate = viewOfLayout.findViewById(R.id.tvexpdate) as TextView
            tvcustname = viewOfLayout.findViewById(R.id.tvcustname) as TextView
            tvcusttype = viewOfLayout.findViewById(R.id.tvcusttype) as TextView
            tvgrpcustName = viewOfLayout.findViewById(R.id.tvgrpcustName) as TextView
            tvPhone = viewOfLayout.findViewById(R.id.tvPhone) as TextView
            tvEmailid = viewOfLayout.findViewById(R.id.tvEmailid) as TextView
            tvPunchIn = viewOfLayout.findViewById(R.id.tvPunchIn) as TextView
            tvPunchOut = viewOfLayout.findViewById(R.id.tvPunchOut) as TextView
            tvdate = viewOfLayout.findViewById(R.id.tvdate) as TextView
            attendanceCard = viewOfLayout.findViewById(R.id.attendanceCard) as CardView
            enter_plan_days = viewOfLayout.findViewById(R.id.enter_plan_days) as LinearLayout
            tvDays = viewOfLayout.findViewById(R.id.tvDays) as TextView
            btnSubmit = viewOfLayout.findViewById(R.id.btnSubmit) as Button

//            if (preferencesHelper!!.getObject().getCategory().equals("Admin")) {
//                enter_plan_days!!.visibility = View.VISIBLE
//            } else
//                enter_plan_days!!.visibility = View.GONE

            //textCartItemCount2 = Main2Activity.
            getCRMDashboarddData()
//            if (ConstantVariable.checkActiveInActive.equals("success")){
//                getCRMDashboarddData()
//            }else{
//                val intent = Intent(context, LoginActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(intent)
//            }


            attendanceCard!!.setOnClickListener {
                var fragment: Fragment? = null
                var fragmentManager = requireActivity().supportFragmentManager
                var fragmentTransaction = fragmentManager!!.beginTransaction()
                fragment = AttendenceFragment()
                if (fragment != null) {
                    fragmentTransaction!!.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .replace(R.id.fragment_container, fragment!!)
                        ?.addToBackStack(null)
                    fragmentTransaction.commit()
                }

            }

            btnSubmit!!.setOnClickListener {

//                var progressDialog =
//                    ConstantVariable.showProgressDialog(context as Activity, "Loading...")

            }


            return viewOfLayout
        }

        private fun getCRMDashboarddData() {
            var progressDialog =
                ConstantVariable.showProgressDialog(context as Activity1, "Loading...")
            val searchExpenseRequestModel = SearchExpenseRequestModel()
//            val useId=(preferencesHelper!!.userId!!)
            searchExpenseRequestModel.user_Id = preferencesHelper?.userId?.toIntOrNull()


            val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
            val call: Call<CRMDashboardResponseModel>? =
                apiInterface!!.getCRMDashboardData(searchExpenseRequestModel)
            Log.e("REQUEST >>", "BODY DASHBOARD : " + searchExpenseRequestModel.toString())
            Log.e("Orderrequest>>", "DASH BOARD DATA : " + call?.request()?.url())
            call!!.enqueue(object : Callback<CRMDashboardResponseModel> {
                override fun onFailure(call: Call<CRMDashboardResponseModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<CRMDashboardResponseModel>,
                    response: Response<CRMDashboardResponseModel>
                ) {
                    progressDialog.dismiss()

                    if (response.body() != null) {
                        if (response.body()?.status!! == "success") {
                            val result1List = (response.body()?.result1 as ArrayList<Result1>)
                            val result2List = (response.body()?.result2 as ArrayList<Result2>)
                            val result3List = (response.body()?.result3 as ArrayList<Result3>)
                            val result4List = (response.body()?.result4 as ArrayList<Result4>)
                            val result5List = (response.body()?.result5 as ArrayList<Result5>)
                            var result7List = (response.body()?.result7 as ArrayList<Result7>)

                            if (result1List.size > 0) {
                                setGridRecyclerViewResult1(result1List)
                            }
                            if (result2List.size > 0) {
                                tvAmount!!.setText(result2List.get(0).amt.toString())
                                tvexpdate!!.setText("Exp Date : " + result2List.get(0).expDate.toString())
                            }
                            if (result3List.size > 0) {
                                setGridRecyclerViewResult3(result3List)
                            }
                            if (result4List.size > 0) {
                                tvPunchIn!!.setText(result4List.get(0).punchInTime.toString())
                                tvPunchOut!!.setText(result4List.get(0).punchOutTime.toString())
                                tvdate!!.setText(result4List.get(0).attenDate.toString())
                            }
                            if (result5List.size > 0) {
                                tvcustname!!.setText(result5List.get(0).custName.toString())
                                tvcusttype!!.setText("(" + result5List.get(0).custType.toString() + ")")
                                tvgrpcustName!!.setText(result5List.get(0).grpcustname.toString())
                                if (!result5List.get(0).grpcustname.toString()
                                        .equals("")
                                ) tvgrpcustName!!.visibility =
                                    View.VISIBLE
                                tvPhone!!.setText(result5List.get(0).mobileno.toString())
                                if (!result5List.get(0).mobileno.toString()
                                        .equals("")
                                ) tvPhone!!.visibility =
                                    View.VISIBLE
                                tvEmailid!!.setText(result5List.get(0).emailid.toString())
                                if (!result5List.get(0).emailid.toString()
                                        .equals("")
                                ) tvEmailid!!.visibility =
                                    View.VISIBLE
                            }

                            /* if (result7List.size > 0) {
                                 textCartItemCount2!!.text = ("" + result7List.get(0).count)
                             } else {
                                 textCartItemCount2!!.text = ("")
                             }*/
                        } else {
                            Toast.makeText(
                                activity,
                                response.body()!!.getMessage()!!,
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                }
            })
        }

        private fun setGridRecyclerViewResult1(result1List: ArrayList<Result1>) {
            result1_recycler!!.layoutManager = GridLayoutManager(context, 2)
            var result1Adapter = CRMDashboardResult1GridAdapter(context, result1List)
            result1_recycler!!.adapter = result1Adapter
        }

        private fun setGridRecyclerViewResult3(result3List: ArrayList<Result3>) {
            result3_recycler!!.layoutManager = GridLayoutManager(context, 2)
            var result3Adapter = CRMDashboardResult3GridAdapter(context, result3List)
            result3_recycler!!.adapter = result3Adapter
        }

    }


    private fun logoutUser(user_name: String) {

        var apiInterface = ApiClient.checkClientActiveInActive().create(ApiInterface::class.java)
        var call: Call<LogoutMode>? =
            apiInterface!!.logoutUser(user_name)
        Log.e("CHECK USER INACTIVE>>", "" + call?.request()?.url())
        call!!.enqueue(object : Callback<LogoutMode> {
            override fun onFailure(call: Call<LogoutMode>, t: Throwable) {


            }

            override fun onResponse(
                call: Call<LogoutMode>,
                response: Response<LogoutMode>
            ) {
                if (response.body() != null) {
                    var status = response.body()?.getStatus()
                    var message = response.body()?.getMessage()


                }
            }
        })
    }

    fun checkActiveInactive(user_name: String) {
        val apiInterface = checkClientActiveInActive().create(ApiInterface::class.java)
        val call: Call<CheckUserActiveResponse> =
            apiInterface!!.checkUserActive(user_name)
        call.enqueue(object : Callback<CheckUserActiveResponse> {
            override fun onFailure(call: Call<CheckUserActiveResponse>, t: Throwable) {


            }

            override fun onResponse(
                call: Call<CheckUserActiveResponse>,
                response: Response<CheckUserActiveResponse>
            ) {
                try {
                    if (response.body() != null) {
                        val message = response.body()!!.getMessage()
                        if (message.equals("success")) {
                            ConstantVariable.checkActiveInActive = "success"
                        } else {
                            ConstantVariable.checkActiveInActive = "fail"
                        }

                    }
                } catch (e: NullPointerException) {

                }
            }
        })


    }


}


interface ExpensePendingInterface {
    var textViewinterface: TextView
}




