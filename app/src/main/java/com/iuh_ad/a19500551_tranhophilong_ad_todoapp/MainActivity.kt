package com.iuh_ad.a19500551_tranhophilong_ad_todoapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var rcvJob:RecyclerView
    lateinit var jobAdapter: JobAdapter
    lateinit var btnAddJob: AppCompatButton
    lateinit var mlistJob:MutableList<Job>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        getListJobFromFirebase()
        showEditTextJob()


    }

    fun initUI(){
        rcvJob=findViewById(R.id.rcv_job)
        btnAddJob=findViewById(R.id.btn_add_job)
        val linearLayout: LinearLayoutManager = LinearLayoutManager(this)
        rcvJob.layoutManager=linearLayout
        val dividerItemDecoration=DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        rcvJob.addItemDecoration(dividerItemDecoration)
        mlistJob=arrayListOf()
        jobAdapter= JobAdapter(mlistJob,object :JobAdapter.IClickItemListener{
            override fun onclickEdit(job: Job) {
                openAlterDialogUpdate(job)
            }

            override fun onClickDelete(job: Job) {
                openAlterDiaglogDelete(job)
            }

            override fun onClickCheck(job: Job, appCompatButton: AppCompatButton) {
                if(job.status==false){
                    job.status=true
                }else{
                    job.status=false
                }
                onclickCheckJob(job)
            }


        })
        rcvJob.setAdapter(jobAdapter)




    }
    private fun onclickCheckJob(job:Job){
        val database = Firebase.database("https://tolistapp-86b3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("listjob")
        myRef.child(job.id.toString()).updateChildren(job.toMapCheck())
    }
    private fun openAlterDialogUpdate(job:Job){
        val builder=AlertDialog.Builder(this)
        val inflater:LayoutInflater=layoutInflater
        val dialogLayout=inflater.inflate(R.layout.edt_layout_update_job,null)
        val editText:EditText=dialogLayout.findViewById<EditText>(R.id.edt_update_job)
        editText.setText(job.description)
        with(builder){
            setTitle("Edit Task")
            setPositiveButton("SAVE"){dialog,which ->
                val database = Firebase.database("https://tolistapp-86b3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val myRef = database.getReference("listjob")
                val textJobDescription=editText.text.toString()
                job.description=textJobDescription
                myRef.child(job.id.toString()).updateChildren(job.toMap(),object :DatabaseReference.CompletionListener{
                    override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                        Toast.makeText(this@MainActivity,"Update data success",Toast.LENGTH_SHORT).show()
                    }
                })

            }
            setNegativeButton("Cancel",null)
            setView(dialogLayout)
            show()


        }
    }
    private fun openAlterDiaglogDelete(job: Job) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Delete Task")
            setMessage("Are you sure you want to delete ?")
            setPositiveButton("SAVE") { dialog, which ->
                val database = Firebase.database("https://tolistapp-86b3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val myRef = database.getReference("listjob")
                myRef.child(job.id.toString()).removeValue(object :DatabaseReference.CompletionListener{
                    override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                        Toast.makeText(this@MainActivity,"Delete data success",Toast.LENGTH_SHORT).show()
                    }

                })

            }
            setNegativeButton("Cancel", null)
            show()
        }
    }


    private fun addJobToFirebase(job:Job){
        val database = Firebase.database("https://tolistapp-86b3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("listjob")
        val pathObject:String=job.id.toString()
        myRef.child(pathObject).setValue(job,object :DatabaseReference.CompletionListener{
            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                Toast.makeText(this@MainActivity,"add user success",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private  fun showEditTextJob(){
        btnAddJob.setOnClickListener{
            val builder=AlertDialog.Builder(this)
            val inflater:LayoutInflater=layoutInflater
            val dialogLayout=inflater.inflate(R.layout.edt_layout_add_job,null)
            val editText:EditText=dialogLayout.findViewById<EditText>(R.id.edt_add_job)

            with(builder){
                setTitle("Add New Task")
                setPositiveButton("NEXT"){dialog,which ->
                    val textJobDescription=editText.text.toString()
                    var id=0
                    if(mlistJob.isNotEmpty()) {
                        id = mlistJob.last().id!! + 1
                    }
                    val cal = Calendar.getInstance()
                    val y = cal.get(Calendar.YEAR)
                    val m = cal.get(Calendar.MONTH)
                    val d = cal.get(Calendar.DAY_OF_MONTH)
                    val datepickerdialog:DatePickerDialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                        val date:String=year.toString().trim()+"/"+monthOfYear.toString().trim()+"/"+dayOfMonth.toString().trim()
//                       val formatter=DateFormat().format()
                        val job=Job(id,textJobDescription,false,dayOfMonth,monthOfYear,year)
                        addJobToFirebase(job)
                    }, y, m, d)
                    datepickerdialog.show()

                }
                setNegativeButton("Cancel",null)
                setView(dialogLayout)
                show()


            }
        }
    }

    private fun getListJobFromFirebase(){
        val database = Firebase.database("https://tolistapp-86b3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("listjob")
        val query:Query=myRef.orderByChild("day")
        query.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val job: Job? =snapshot.getValue(Job::class.java)
                    mlistJob.add(job!!)
                    jobAdapter.notifyDataSetChanged()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val job: Job? =snapshot.getValue(Job::class.java)
                if(job==null ||mlistJob.isEmpty()){
                    return
                }
                for (i in 0..mlistJob.size-1 ){
                    if(job.id ==mlistJob[i].id){
                        mlistJob.set(i, job)
                        break
                    }
                }
                jobAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val job: Job? =snapshot.getValue(Job::class.java)
                if(job==null ||mlistJob.isEmpty()){
                    return
                }
                for (i in 0..mlistJob.size-1 ){
                    if(job.id ==mlistJob[i].id){
                        mlistJob.removeAt(i)
                        break
                    }
                }
                jobAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}