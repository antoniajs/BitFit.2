package com.example.bitfit2

import android.app.Application

class BitFitApplication : Application(){
    val db by lazy {AppDatabase.getInstance(this)}
}