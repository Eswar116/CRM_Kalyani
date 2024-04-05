package com.crm.crmapp.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        //var BASE_URL: String = "http://192.168.1.14:7003/CRMMobileAPI/webresources/" //Kalyani's server test
        //var BASE_URL: String = "http://192.168.1.100:7003/CRMMobileAPI/webresources/" //Kalyani's server test Linux
        //var BASE_URL: String = "http://192.168.1.22:9073/CRMMobileAPI/webresources/"//LOCAL IP Kalyani server live
        //var BASE_URL: String = "http://118.185.141.101:9073/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI SERVER LIVE
//        var BASE_URL: String = "http://118.185.196.11:9073/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI SERVER LIVE 06JAN2021
        //var BASE_URL: String = "http://118.185.196.12:7003/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI SERVER LIVE 06JAN2021
        //var BASE_URL: String = "http://180.151.31.122:7002/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI SERVER LIVE
        //var BASE_URL: String = "http://122.160.74.72:7003/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI Oshina

        //-------------------Anil----------------
//        var BASE_URL: String = "http://118.185.196.11:9073/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI SERVER LIVE 06JAN2021
        private var BASE_URL: String =
            "http://125.20.174.190:9073/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI  LIVE SERVER  26 Jan 2023
//        var BASE_URL: String = "http://125.20.174.189:9073/CRMMobileAPI/webresources/"//PUBLIC IP KALYANI TEST SERVER  26 Jan 2023

                var BASE_URL_TWO_ROOTS: String = "http://125.20.174.190:9073/TwoRootWS/resources/tworootws/"//PUBLIC IP KALYANI TEST SERVER  26 Jan 2023

        var retrofit: Retrofit? = null
        fun getClient(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY;
            val gson = GsonBuilder().setLenient().create()
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(60, TimeUnit.SECONDS)
            httpClient.writeTimeout(60, TimeUnit.SECONDS)
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            return retrofit
        }

          fun  checkClientActiveInActive(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY;
            val gson = GsonBuilder().setLenient().create()
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(50, TimeUnit.SECONDS)
            httpClient.writeTimeout(50, TimeUnit.SECONDS)
            httpClient.connectTimeout(50, TimeUnit.SECONDS)
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_TWO_ROOTS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            return retrofit
        }





        fun getClientForSyncCustomers(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            val gson = GsonBuilder().setLenient().create()
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(600, TimeUnit.SECONDS)
            httpClient.writeTimeout(600, TimeUnit.SECONDS)
            httpClient.connectTimeout(600, TimeUnit.SECONDS)
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            return retrofit
        }
    }
}