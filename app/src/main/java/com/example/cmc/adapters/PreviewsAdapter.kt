package com.example.cmc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cmc.databinding.DescriptionsCardviewBinding
import com.example.cmc.models.DescriptionsModel

class PreviewsAdapter() : RecyclerView.Adapter<PreviewsAdapter.ViewHolder>() {

    private var descriptionList: ArrayList<DescriptionsModel> = ArrayList()

    inner class ViewHolder(val binding: DescriptionsCardviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DescriptionsCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return descriptionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list = descriptionList[position]
        val bind = holder.binding
        val pos = position
         "${pos + 1}:- ".also {
             bind.index.text =it
        }
        bind.title.text = list.title
        bind.description.text = list.description
    }

    fun setData(list: ArrayList<DescriptionsModel>) {
        descriptionList = list
        notifyDataSetChanged()
    }
}