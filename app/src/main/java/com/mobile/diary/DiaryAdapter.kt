package com.mobile.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DiaryAdapter(private val context: Context, private val data: List<DiaryBean>) :
    RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var date: TextView = view.findViewById(R.id.date)
        var loc: TextView = view.findViewById(R.id.loc)
        var content: TextView = view.findViewById(R.id.content)
        var iv: ImageView = view.findViewById(R.id.iv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.ViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: DiaryAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.loc.text = "location:${item.location}"
        holder.content.text = "content:${item.content}"
        holder.date.text = "date:${item.date}"
        Glide.with(context).load(item.photoPath).into(holder.iv)
    }

    override fun getItemCount(): Int {
        return if (data == null) {
            0
        } else {
            data.size
        }
    }


}