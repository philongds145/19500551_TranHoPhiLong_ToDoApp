package com.iuh_ad.a19500551_tranhophilong_ad_todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(private  val mListJob :MutableList<Job>):RecyclerView.Adapter<JobAdapter.JobViewHolder>() {


    class JobViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDescription=itemView.findViewById<TextView>(R.id.tv_description)
        val tvStatus=itemView.findViewById<TextView>(R.id.tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_job,parent,false)

        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job= mListJob[position]

        if(job==null){
            return
        }


        holder.tvDescription.text=job.description
        if(job.status ==false){
            holder.tvStatus.text="Not Complete"

        }else{
            holder.tvStatus.text="Complete"
        }
    }

    override fun getItemCount(): Int {

        if(mListJob != null){
            return mListJob.size
        }
        return 0

    }
}