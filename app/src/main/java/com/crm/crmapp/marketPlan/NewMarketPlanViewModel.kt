package com.crm.crmapp.marketPlan

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import android.util.Log
import android.view.View


class NewMarketPlanViewModel(var activity: Activity?) : BaseObservable() {

   private var navigationEvent : ViewNavigation?=null
   private var context : Activity?=null

    @Bindable
    private var toastMessage :String ?=null

    @Bindable
    private var dueDate: String? = null

    @Bindable

    fun getToastMessage() :String?  {
        return toastMessage
    }

    fun getNavigationEvent() :ViewNavigation?  {
        return navigationEvent
    }


    fun addResponsiblePerson(view : View){
        Log.e("clicked","clicked>>")
//      var intent =Intent(view.context , ResponsiblePersonListActivity::class.java)
//             view.context.startActivity(intent)



    }




//     fun dueData(view: View){
//         ConstantVariable.animationEffect(view,context!!)
//         dueDate=(ConstantVariable.defaultTime(view.findViewById(R.id.tvDue), context!!))
//        // notifyPropertyChanged(BR.dueDate)
//
//     }

}