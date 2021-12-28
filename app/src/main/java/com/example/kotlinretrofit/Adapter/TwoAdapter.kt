package com.example.kotlinretrofit.Adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretrofit.Fragments.FragmentDetails
import com.example.kotlinretrofit.MainActivity
import com.example.kotlinretrofit.Model.Hits
import com.example.kotlinretrofit.Model.Labels
import com.example.kotlinretrofit.R
import com.squareup.picasso.Picasso

class TwoAdapter(private val context: Context, private val pictureList: Labels):RecyclerView.Adapter<TwoAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.image_two)

        fun bind(listItem: Hits) {
            itemView.setOnClickListener {
                Toast.makeText(it.context, "" + listItem.likes, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_two, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = pictureList.hits.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val listItem = pictureList.hits[position]
        holder.bind(listItem)
        Picasso.get().load(pictureList.hits[position].webformatURL).into(holder.image)
//        Log.d("TAG", (pictureList.hits[position].previewURL.toString()))

        holder.itemView.setOnClickListener {
            fragmentJump(pictureList.hits[position])
        }
    }

    private fun fragmentJump(mItemSelected: Hits){
        val fragmentDetails = FragmentDetails()
        val bundle = Bundle()
        bundle.putParcelable("item_key", mItemSelected)
        fragmentDetails.arguments = bundle
        switchContent(R.id.fragment_container_view, FragmentDetails(), bundle)
    }

    private fun switchContent(id: Int, fragment: Fragment, bundle: Bundle) {
        if (context == null) return
        if (context is MainActivity) {
            val mainActivity = context
            val frag: Fragment = fragment
            mainActivity.switchContent(id, frag, bundle)
        }
    }

    fun updateList(newlist: ArrayList<Hits>) {
        pictureList.hits.clear()
        pictureList.hits.addAll(newlist)
        this.notifyDataSetChanged()
    }

}
