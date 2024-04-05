package com.crm.crmapp.retrofit

import com.crm.crmapp.CRMDashboardModel.CRMDashboardResponseModel
import com.crm.crmapp.attendence.model.AttendanceDetailRequest
import com.crm.crmapp.attendence.model.AttendanceResponse
import com.crm.crmapp.competition.model.CompetitionDetailByIdResponseModel
import com.crm.crmapp.competition.model.CompetitionListResponseModel
import com.crm.crmapp.competition.model.SaveCompetitorRequestModel
import com.crm.crmapp.complaint.model.*
import com.crm.crmapp.customer.model.*
import com.crm.crmapp.expense.model.*
import com.crm.crmapp.favourite.activity.FavrtResponseModel
import com.crm.crmapp.fireBaseNotification.model.NotificationResponseModel
import com.crm.crmapp.fireBaseNotification.model.NotificationStatusResponse
import com.crm.crmapp.marketPlan.NewMarketPlanRequest
import com.crm.crmapp.marketPlan.NewMarketPlanResponse
import com.crm.crmapp.marketPlan.ResponsiblePersoneResponse
import com.crm.crmapp.marketPlan.models.*
import com.crm.crmapp.newuser.RecentUserGetterSetter
import com.crm.crmapp.order.model.*
import com.crm.crmapp.registration.LoginRequest
import com.crm.crmapp.registration.LoginResponse
import com.crm.crmapp.search.SearchMainResponse
import com.crm.crmapp.settings.SettingsSyncRequestResponseModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("so/OrderSearchAll/")
    fun getOrderList(@Body orderListRequest: OrderListRequest): Call<OrderAllModel>

    @POST("cs/CustomerSearch/")
    fun getSearchCustomerList(@Body searchCustomerRequestModel: SearchCustomerRequestModel): Call<SearchCustomerModel>

    @POST("so/OrderDetailById/")
    @Headers("Content-Type: application/json")
    fun orderDetail(@Body orderDetailRequest: OrderDetailRequest): Call<OrderDetailResponse>

    @POST("so/SaveOrder/")
    @Headers("Content-Type: application/json")
    fun sendOrderDetail(@Body orderDetailRequest: NewOrderRequest): Call<NewOrderResponse>

    @POST("ites/GetImage/")
    @Headers("Content-Type: application/json")
    fun getImage(@Body imageRequest: ImageRequest): Call<ImageRequestResponse>

    @POST("so/UpdateOrder/")
    @Headers("Content-Type: application/json")
    fun updateOrder(@Body orderDetailRequest: NewOrderRequest): Call<NewOrderResponse>

    @POST("Atten/AttendanceByDate/")
    @Headers("Content-Type: application/json")
    fun getAttendanceDetail(@Body attendanceDetailRequest: AttendanceDetailRequest): Call<AttendanceResponse>

    @POST("cust/RecentCustomers/")
    @Headers("Content-Type: application/json")
    fun getRecentCustomerList(@Body orderListRequest: RecentCustomerModel): Call<RecentCustomerGetterSetter>

    @POST("cust/CustomersDtlbyId/")
    @Headers("Content-Type: application/json")
    fun getCustomersDtlbyId(@Body orderListRequest: RecentCustomerModel): Call<CustomerDetailByIDModel>

    @POST("Atten/AttendanceSave/")
    @Headers("Content-Type: application/json")
    fun saveAttendance(@Body result: AttendanceResponse): Call<AttendanceResponse>

    @POST("so/RecentOrders/")
    @Headers("Content-Type: application/json")
    fun recentOrderApi(@Body recentOrderRequest: RecentOrderRequest): Call<RecentOrderResponse>

    @POST("cust/SaveCustomer/")
    @Headers("Content-Type: application/json")
    fun saveCustomer(@Body saveCustomerRequestModel: SaveCustomerRequestModel): Call<NewOrderResponse>


    @POST("Complaint/ComplaintType/")
    @Headers("Content-Type: application/json")
    fun complaintList(@Body jsonObject: JSONObject): Call<ComplaintTypeResponse>

    @POST("Complaint/SaveComplaint/")
    @Headers("Content-Type: application/json")
    fun saveComplaint(@Body complaintSaveRequest: ComplaintSaveRequest): Call<ComplaintSaveResponse>

    @POST("Complaint/ComplaintList/")
    @Headers("Content-Type: application/json")
    fun complainSearchtList(@Body complaint: ComplaintReqest): Call<ComplaintSearchListResponse>

    @POST("Complaint/ComplaintDetailById/")
    @Headers("Content-Type: application/json")
    fun getComplaintDetail(@Body jsonObject: ComplaintDetailRequest): Call<ComplaintDetailResponse>

    @POST("cust/UpdateCustomers/")
    @Headers("Content-Type: application/json")
    fun updateCustomer(@Body saveCustomerRequestModel: SaveCustomerRequestModel): Call<NewOrderResponse>

    @POST("login/userLogin/")
    @Headers("Content-Type: application/json")
    fun loginApi(@Body jsonObject: LoginRequest): Call<LoginResponse>

    @POST("cs/GlbSearch/")
    @Headers("Content-Type: application/json")
    fun mainSearchApi(@Body complaint: ComplaintReqest): Call<SearchMainResponse>


    @POST("exp/ExpenseType/")
    @Headers("Content-Type: application/json")
    fun getExpenseTypeList(): Call<ExpenseTypeResponseModel>

    @POST("exp/ExpenseList/")
    @Headers("Content-Type: application/json")
    fun getSearchExpenseList(@Body searchExpenseRequestModel: SearchExpenseRequestModel): Call<SearchExpenseResponseModel>

    @POST("exp/ExpenseDashboard/")
    @Headers("Content-Type: application/json")
    fun getExpenseDashboardList(@Body searchExpenseRequestModel: SearchExpenseRequestModel): Call<ExpenseDashBoardResponseModel>

    @POST("exp/SaveExpense/")
    @Headers("Content-Type: application/json")
    fun SaveExpense(@Body saveExpenseRequestModel: SaveExpenseRequestModel): Call<SaveExpenseResponseModel>

    @POST("exp/ExpenseDetailById/")
    @Headers("Content-Type: application/json")
    fun getExpenseDetailById(@Body searchExpenseRequestModel: SearchExpenseRequestModel): Call<ExpenseDetailByIDModel>

    @POST("compt/CompetitionList/")
    @Headers("Content-Type: application/json")
    fun getCompetitionList(@Body complaint: ComplaintReqest): Call<CompetitionListResponseModel>

    @POST("compt/CompetitionDetailById/")
    @Headers("Content-Type: application/json")
    fun getCompetitionDetailById(@Body orderListRequest: ComplaintDetailRequest): Call<CompetitionDetailByIdResponseModel>

    @POST("compt/SaveCompetition/")
    @Headers("Content-Type: application/json")
    fun saveCompetitor(@Body saveCustomerRequestModel: SaveCompetitorRequestModel): Call<NewOrderResponse>


    @POST("nf/NotiList/")
    @Headers("Content-Type: application/json")
    fun getNotificationApi(@Body saveCustomerRequestModel: RecentOrderRequest): Call<NotificationResponseModel>


    @POST("nf/NotiRead/")
    @Headers("Content-Type: application/json")
    fun getNotificationStatus(@Body saveCustomerRequestModel: RecentOrderRequest): Call<NotificationStatusResponse>

    @POST("exp/UpdateExpenseState/")
    @Headers("Content-Type: application/json")
    fun UpdateExpenseState(@Body updateExpenseStateRequestModel: UpdateExpenseStateRequestModel): Call<UpdateExpenseStateResponseModel>

    @POST("cs/GetUserDetail/")
    @Headers("Content-Type: application/json")
    fun GetUserDetail(@Body searchExpenseRequestModel: SearchExpenseResult): Call<GetUserDetailResponseModel>

    @POST("cust/CustOtherDetails/")
    @Headers("Content-Type: application/json")
    fun getCustomersOtherDtlbyId(@Body orderListRequest: RecentCustomerModel): Call<CustomerOtherDtlResponseModel>

    @POST("fs/FavList/")
    @Headers("Content-Type: application/json")
    fun getFavrtCustomer(@Body searchExpenseRequestModel: RecentOrderRequest): Call<FavrtResponseModel>

    @POST("fs/SaveFav/")
    @Headers("Content-Type: application/json")
    fun saveFavrtCustomer(@Body searchExpenseRequestModel: FavrtSaveRequest): Call<FavrtSaveResponseModel>


    @POST("exp/ExpenseListforApproval/")
    @Headers("Content-Type: application/json")
    fun getListPendingExpense(@Body searchExpenseRequestModel: RecentOrderRequest): Call<ExpensePendingResponse>

    @POST("cust/CustMobileCheck/")
    @Headers("Content-Type: application/json")
    fun getMobileValidation(@Body searchExpenseRequestModel: RecentOrderRequest): Call<MobileValidationResponse>

    @POST("so/OrderDashboard/")
    @Headers("Content-Type: application/json")
    fun getOrderDashboardData(@Body searchExpenseRequestModel: SearchExpenseRequestModel): Call<OrderDashboardResponseModel>

    @POST("cs/CRMDashboard/")
    @Headers("Content-Type: application/json")
    fun getCRMDashboardData(@Body searchExpenseRequestModel: SearchExpenseRequestModel): Call<CRMDashboardResponseModel>

    @POST("so/OrderSyncERP/")
    @Headers("Content-Type: application/json")
    fun getSyncDataWithERP(@Body recentOrderRequest: RecentOrderRequest): Call<Orders>

    @POST("mp/SaveMP/")
    @Headers("Content-Type: application/json")
    fun saveMP(@Body newMarketPlanRequest: NewMarketPlanRequest): Call<NewMarketPlanResponse>

    @GET("cs/GetUsers/{Id}/{StateCode}")
    fun responsiblePersonList(
        @Path("Id") userId: Int, @Path("StateCode") statecode: String
    ): Call<ResponsiblePersoneResponse>

    /*@GET("cs/GetUsers/{Id}")
    fun responsiblePersonList(@Path("Id") userId: Int): Call<ResponsiblePersoneResponse>*/

    @GET("cs/GetStates/{Id}")
    fun getStateList(@Path("Id") userId: Int): Call<StateListRequestResponseModel>

    @GET("cs/GetCities/{stateId}")
    fun getCityList(@Path("stateId") stateId: String): Call<CityListRequestResponseModel>


    @GET("chkUsrActv/{user_name}")
    suspend fun checkUserActiveSuspended(@Path("user_name") stateId: String): Call<CheckUserActiveResponse>

    @GET("chkUsrActv/{user_name}")
    fun checkUserActive(@Path("user_name") stateId: String): Call<CheckUserActiveResponse>


    @GET("logout/{user_name}")
    fun logoutUser(@Path("user_name") stateId: String): Call<LogoutMode>


    @GET("mp/GetCategory/{Id}")
    fun getCategoryList(@Path("Id") userId: Int): Call<CategoryListRequestResponseModel>

    @GET("mp/GetScheme/{Id}")
    fun getSchemeList(@Path("Id") userId: Int): Call<SchemeListRequestResponseModel>

    @GET("mp/MKPListByUser/{Id}")
    fun getMarketPlanList(@Path("Id") userId: Int): Call<MarketPlanListRequestResponse>

    @GET("mp/MKPDetailById/{mkt_plan_id}/{user_id}")
    fun getMarketPlanInfo(
        @Path("mkt_plan_id") marketPlanId: Int, @Path("user_id") userId: Int
    ): Call<MarketPlanInfoRequestResponse>




    @GET("mp/MKPDetailById/{mkt_plan_id}/{user_id}")
    suspend fun getMarketPlanInfoSus( @Path("mkt_plan_id") mkt_plan_id: Int, @Path("user_id") user_id: Int
    ): Response<MarketPlanInfoRequestResponse>


    @POST("mp/UpdateMktPlnStage/")
    @Headers("Content-Type: application/json")
    fun UpdateMarketPlanState(@Body updateMarketPlanStage: UpdateMarketPlanStateRequestResponseModel): Call<UpdateMarketPlanStateRequestResponseModel>

    @POST("mp/EditMP/")
    @Headers("Content-Type: application/json")
    fun UpdateMarketPlanToDate(@Body updateMarketPlanStage: UpdateMarketPlanStateRequestResponseModel): Call<UpdateMarketPlanStateRequestResponseModel>

    @POST("cust/SyncCustomers/")
    @Headers("Content-Type: application/json")
    fun syncCustomer(@Body saveCustomerRequestModel: SettingsSyncRequestResponseModel): Call<SettingsSyncRequestResponseModel>

    @POST("cs/CustomerSearchMP/")
    fun getCustomerSearchMP(@Body searchCustomerRequestModel: SearchCustomerRequestModel): Call<SearchCustomerModel>

    @GET("mp/MKPListByUser/{Id}/{SearchValue}/{Type}")
    fun getSearchMarketPlanList(
        @Path("Id") userId: Int,
        @Path("SearchValue") searchValue: String,
        @Path("Type") type: String
    ): Call<MarketPlanListRequestResponse>

    @GET("Atten/AttenTypeId/{Id}")
    fun getStatusList(@Path("Id") userId: Int): Call<ExpenseTypeResponseModel>

    @POST("cs/CustomerSearchMPbyState/")
    fun getCustomerSearchMPbyState(@Body searchCustomerRequestModel: SearchCustomerRequestModel): Call<SearchCustomerModel>

    @GET("cs/GetUserList")
    fun getRecentUsersList(): Call<RecentUserGetterSetter>

    @GET("cs/GetUserCategory")
    fun GetUserCategory(): Call<CategoryListRequestResponseModel>

    @GET("cs/GetReportingUser/{category}")
    fun GetReportingUser(@Path("category") category: String): Call<RecentUserGetterSetter>

    @POST("cs/SaveUser")
    @Headers("Content-Type: application/json")
    fun SaveUser(@Body saveUserRequestModel: GetUserDetailResponseResult): Call<NewOrderResponse>

    @GET("mp/GetSchemeList")
    fun GetSchemeList(): Call<SchemeListRequestResponseModel>

    @POST("mp/AddUpdateMarketSchema")
    @Headers("Content-Type: application/json")
    fun AddUpdateMarketSchema(@Body model: SchemeListRequestResponseModel.Result): Call<SchemeListRequestResponseModel>

}