package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2022/4/25 6:05 下午
 */
class MusicAdapter(val listener: Listener) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false), listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return MusicData.list.size
    }
}


class MyViewHolder(inflateView: View, val listener: Listener) : RecyclerView.ViewHolder(inflateView) {

    fun bind(i: Int) {
        val textview = itemView.findViewById<TextView>(R.id.tv)
        textview.text = MusicData.list[i].fileName
        itemView.setOnClickListener {
            listener.onclick(MusicData.list[i])
        }
    }

}

interface Listener {
    fun onclick(music: Music)
}