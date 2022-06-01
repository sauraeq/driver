package com.abc.taxidriver.YourEarning

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.abc.taxidriver.Dashboard.Model.TodayEarningResponse
import com.abc.taxidriver.Notification.NotificationAdapter
import com.abc.taxidriver.R
import com.abc.taxidriver.databinding.EarningBinding
import com.abc.taxidriver.databinding.Notification1Binding

class EarningAdapter(
    val mContext: Context,
    val users: ArrayList<TodayEarningResponse.Data>

) : RecyclerView.Adapter<EarningAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: EarningBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EarningAdapter.UserViewHolder {
        val rowBinding: EarningBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.earning, parent, false
        )
        return EarningAdapter.UserViewHolder(rowBinding)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.binding.tvride.setText(users[position].pickup_address)
        holder.binding.tvaAmount.setText(users[position].amount)


    }

    override fun getItemCount(): Int {
        if (users == null)
            return 0
        return users.size
    }


}