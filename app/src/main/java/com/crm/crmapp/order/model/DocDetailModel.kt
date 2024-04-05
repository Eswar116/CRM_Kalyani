package com.crm.crmapp.order.model

import java.io.Serializable

class DocDetailModel (uri :String , docName :String? =null, docSize :Double? =null,  docPath :String? =null)  :
    Serializable {
    var docName=docName
     var docSize=docSize
     var docPath=docPath
      var uri=uri

}




