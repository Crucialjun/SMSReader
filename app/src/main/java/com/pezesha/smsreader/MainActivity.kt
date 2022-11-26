package com.pezesha.smsreader

import android.R
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.pezesha.smsreader.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    var messagesListitems = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),111)
        }else{

                receiveSms()

        }
        val messagesArrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1,
            messagesListitems
        )
        binding.listview.adapter = messagesArrayAdapter


    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

                receiveSms()

        }
    }

    private fun receiveSms() {
        val cursor = contentResolver.query(Uri.parse("content://sms"),null,"address='MPESA'",null,"date DESC")
        try {
            cursor?.moveToFirst()

            while (cursor != null){
                val message = cursor.getString(12)
                    messagesListitems.add(message)
                
                if(cursor.isLast){
                    return
                }else{
                    cursor.moveToNext()
                }

            }
        }finally {
           cursor!!.close()
        }



    }
}