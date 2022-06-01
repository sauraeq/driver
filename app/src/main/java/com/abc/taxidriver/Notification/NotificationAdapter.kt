package com.abc.taxidriver.Notification

import android.content.Context
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.abc.taxidriver.R

import com.abc.taxidriver.databinding.Notification1Binding
/*

class NotificationAdapter(
    val context: Context,
    val list:ArrayList<String>?

): RecyclerView.Adapter<NotificationAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val v= LayoutInflater.from(parent.context).inflate(R.layout.company_rows,parent,false)
        //return Holder(v)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: Notification1Binding = Notification1Binding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return list!!.size
    }

    override  fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //    val data=list?.get(position)

        */
/*  if(data?.title!=null){
              holder.binding.tvTitle.setText(data?.title)
          }
          if(data?.body!=null){
              holder.binding.tvDes.setText(data?.body)
          }*//*

    }
    class ViewHolder(binding: Notification1Binding) : RecyclerView.ViewHolder(binding.getRoot())
    {
        var binding: Notification1Binding

        init {
            this.binding = binding
        }
    }
}*/

class NotificationAdapter(
    val mContext: Context,
    val users: ArrayList<notificationResponse.Data>

) : RecyclerView.Adapter<NotificationAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: Notification1Binding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.UserViewHolder {
        val rowBinding: Notification1Binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.notification_1, parent, false
        )
        return NotificationAdapter.UserViewHolder(rowBinding)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

       // holder.binding.tvTitle.setText(users[position].title)
        holder.binding.tvTime.setText(users[position].created_date)
        holder.binding.tvDesciption.setText(users[position].description)


    }

    override fun getItemCount(): Int {
        if (users == null)
            return 0
        return users.size
    }


}