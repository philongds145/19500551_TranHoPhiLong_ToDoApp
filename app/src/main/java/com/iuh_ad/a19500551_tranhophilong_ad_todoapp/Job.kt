package com.iuh_ad.a19500551_tranhophilong_ad_todoapp

import java.text.SimpleDateFormat
import java.util.*

data class Job(var id:Int?=null,
               var description:String?=null,
               var status:Boolean?=null,
               val day:Int?=null,
               val month:Int?=null,
               val year:Int?=null){

    fun toMap(): Map<String, Any?> {
        return mapOf(

            "description" to description,

            )
    }
    fun toMapCheck():Map<String,Any?>{
        return mapOf(

            "status" to status,

            )
    }
}


