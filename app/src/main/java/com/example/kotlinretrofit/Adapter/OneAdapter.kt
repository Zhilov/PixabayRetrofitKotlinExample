package com.example.kotlinretrofit.Adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretrofit.Fragments.FragmentDetails
import com.example.kotlinretrofit.MainActivity
import com.example.kotlinretrofit.Model.Hits
import com.example.kotlinretrofit.Model.Labels
import com.example.kotlinretrofit.R
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference


class OneAdapter(private val context: Context, private val pictureList: Labels):RecyclerView.Adapter<OneAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.image_one)
        val textUser: TextView = itemView.findViewById(R.id.text_user_one)
        val textLikes: TextView = itemView.findViewById(R.id.text_likes_one)
        val textComments: TextView = itemView.findViewById(R.id.text_comments_one)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_one, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = pictureList.hits.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(pictureList.hits[position].webformatURL).into(holder.image)
//        Log.d("TAG", (pictureList.hits[position].previewURL.toString()))
        if(pictureList.hits[position].user?.length!! > 9){
            holder.textUser.text = pictureList.hits[position].user?.substring(0, 9) + "..."
        } else{
            holder.textUser.text = pictureList.hits[position].user
        }
        holder.textLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
        holder.textComments.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_comment_24, 0, 0, 0)
        holder.textLikes.text = pictureList.hits[position].likes
        holder.textComments.text = pictureList.hits[position].comments

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
