package com.iuh_ad.a19500551_tranhophilong_ad_todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var rcvJob:RecyclerView
    lateinit var jobAdapter: JobAdapter
    lateinit var btnAddJob: AppCompatButton
    lateinit var mlistJob:MutableList<Job>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        showEditTextJob()

    }

    fun initUI(){
        rcvJob=findViewById(R.id.rcv_job)
        btnAddJob=findViewById(R.id.btn_add_job)
        val linearLayout: LinearLayoutManager = LinearLayoutManager(this)
        rcvJob.layoutManager=linearLayout
        mlistJob= arrayListOf()
        jobAdapter= JobAdapter(mlistJob)
        rcvJob.setAdapter(jobAdapter)
//        addJobToFirebase()



    }
//    private fun addJobToFirebase(){
//        val database = Firebase.database("https://tolistapp-86b3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
//        val myRef = database.getReference("listjob")
//        myRef.setValue(mlistJob)
//    }

    private  fun showEditTextJob(){
        btnAddJob.setOnClickListener{
            val builder=AlertDialog.Builder(this)
            val inflater:LayoutInflater=layoutInflater
            val dialogLayout=inflater.inflate(R.layout.edt_layout_add_job,null)
            val editText:EditText=dialogLayout.findViewById<EditText>(R.id.edt_add_job)

            with(builder){
                setTitle("Add New Task")
                setPositiveButton("SAVE"){dialog,which ->
                    val textJobDescription=editText.text.toString()
                    val job=Job(textJobDescription,false)
                    mlistJob.add(job)

                }
                setNegativeButton("Cancel",null)
                setView(dialogLayout)
                show()


            }
        }
    }
}