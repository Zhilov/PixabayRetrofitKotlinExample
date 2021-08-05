package com.example.kotlinretrofit.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretrofit.Model.Hits
import com.example.kotlinretrofit.Model.Labels
import com.example.kotlinretrofit.R
import com.squareup.picasso.Picasso

class TwoAdapter(private val context: Context?, private val movieList: Labels):RecyclerView.Adapter<TwoAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.image_two)

        fun bind(listItem: Hits) {
            itemView.setOnClickListener {
                Toast.makeText(it.context, "" + listItem.likes, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_layout_two, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = movieList.hits.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val listItem = movieList.hits[position]
        holder.bind(listItem)
        Picasso.get().load(movieList.hits.get(position).webformatURL).into(holder.image)
        Log.d("TAG", (movieList.hits.get(position).previewURL.toString()))
//        holder.txt_name.text = movieList[position].name
//        holder.txt_team.text = movieList[position].team
//        holder.txt_createdby.text = movieList[position].createdby
    }

}
