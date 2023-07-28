package com.example.mobco

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mobco.Util.CommunicationData
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeReader

class MainActivity : AppCompatActivity() {
    companion object {
        private var application :Application? =null
        fun setApplication (application: Application){
            this.application=application
        }
        var BASE_URL = ""

        var barcodeReader: BarcodeReader? = null
    }

    private var manager: AidcManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setApplication(application)
        val protocol = CommunicationData.getProtocol(application)
        val ip       = CommunicationData.getIpAddress(application)
        val portNo   = CommunicationData.getPortNumber(application)
        val apiLocation = "/api/GBSWMS/"
        BASE_URL = "$protocol://$ip:$portNo$apiLocation"

        AidcManager.create(this) { aidcManager: AidcManager ->
            manager = aidcManager
            barcodeReader = manager!!.createBarcodeReader()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item);
    }
}