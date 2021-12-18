package com.iuh_ad.a19500551_tranhophilong_ad_todoapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import java.text.SimpleDateFormat

class JobAdapter(private  val mListJob :MutableList<Job>,private  var mClickItemListenr:IClickItemListener):RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    private var viewBinderHelper= ViewBinderHelper()
    class JobViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDescription=itemView.findViewById<TextView>(R.id.tv_description)
        val tvStatus=itemView.findViewById<TextView>(R.id.tv_status)
        val swipeRevealLayout=itemView.findViewById<SwipeRevealLayout>(R.id.SwipeRevealLayout)
        val btnEdit=itemView.findViewById<AppCompatButton>(R.id.btn_edit)
        val btnDelete=itemView.findViewById<AppCompatButton>(R.id.btn_delete)
        val btnCheck=itemView.findViewById<AppCompatButton>(R.id.btn_check)
        val tvDate=itemView.findViewById<TextView>(R.id.tv_date)

    }

    interface  IClickItemListener{
        fun onclickEdit(job:Job)
        fun onClickDelete(job:Job)
        fun onClickCheck(job:Job,appCompatButton: AppCompatButton)
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

        viewBinderHelper.bind(holder.swipeRevealLayout, job.id.toString())
        holder.tvDescription.text=job.description
        holder.btnEdit.setOnClickListener{
            mClickItemListenr.onclickEdit(job)
        }
        holder.btnDelete.setOnClickListener{
            mClickItemListenr.onClickDelete(job)

        }
        holder.btnCheck.setOnClickListener{
            mClickItemListenr.onClickCheck(job,holder.btnCheck)
        }
        val day=job.day.toString().trim()
        val month=job.month.toString().trim()
        val yearr=job.year.toString().trim()
        holder.tvDate.text=day+"/"+month+"/"+yearr

//        holder.tvDate.text=job.date.toString().trim()
        if(job.status ==false){
            holder.tvStatus.text="Not Completed"
            holder.tvStatus.setTextColor(Color.parseColor("#ff0000"))
            holder.btnCheck.setBackgroundResource(R.drawable.ic_baseline_uncheck_24)


        }else{
            holder.tvStatus.text="Completed"
            holder.tvStatus.setTextColor(Color.parseColor("#0000ff"))
            holder.btnCheck.setBackgroundResource(R.drawable.ic_baseline_check_24)
        }
    }

    override fun getItemCount(): Int {

        if(mListJob != null){
            return mListJob.size
        }
        return 0

    }
}