package com.example.aso

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import java.lang.Exception

class Home : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        btnStudents.setOnClickListener{
            val intent = Intent(this, Students::class.java)
            startActivity(intent)
        }

        btnServices.setOnClickListener {
            val intent = Intent(this, Services::class.java)
            startActivity(intent)
        }

        btnReports.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            startActivity(intent)
        }

    }

    fun alerta(){
        try{
            val builder=AlertDialog.Builder(this)

            builder.setTitle("¿Estás seguro/a de salir?")
            builder.setMessage("La información no se verá compremetida")
            builder.setPositiveButton("Aceptar"){dialog, which ->
                finish()
            }
            builder.setNegativeButton("Cancelar"){dialog, which ->

            }
            builder.setIcon(R.drawable.info)

            val dialog : AlertDialog = builder.create()

            dialog.show()
        }catch (e: Exception){
            Toast.makeText(this,"error: "+e.toString(),Toast.LENGTH_LONG).show()
        }
    }


    override fun onBackPressed() {

        alerta()

    }
}
