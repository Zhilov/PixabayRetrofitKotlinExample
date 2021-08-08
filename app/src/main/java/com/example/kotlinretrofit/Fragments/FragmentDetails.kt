package com.example.kotlinretrofit.Fragments

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kotlinretrofit.Model.Hits
import com.example.kotlinretrofit.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.io.File


class FragmentDetails : Fragment() {

    lateinit var imageView: ImageView
    lateinit var imageUser: ImageView
    lateinit var textUser: TextView
    lateinit var textTags: TextView
    lateinit var textLikes: TextView
    lateinit var textComments: TextView
    lateinit var textViews: TextView
    lateinit var fab: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_details, container, false)
        var hits: Hits?

        imageView = view.findViewById(R.id.image_details)
        imageUser = view.findViewById(R.id.image_user_details)
        textUser = view.findViewById(R.id.text_user_details)
        textTags = view.findViewById(R.id.text_tags_details)
        textLikes = view.findViewById(R.id.text_likes_details)
        textComments = view.findViewById(R.id.text_comments_details)
        textViews = view.findViewById(R.id.text_views_details)
        fab = view.findViewById(R.id.fab_details)

        textLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
        textComments.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_comment_24, 0, 0, 0)
        textViews.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_remove_red_eye_24, 0, 0, 0)

        var bundle: Bundle? = arguments
        if (bundle != null){
            hits = bundle.getParcelable("item_key")
            Picasso.get().load(hits?.largeImageURL).into(imageView)
            if (hits?.userImageURL != ""){
                Picasso.get().load(hits?.userImageURL).into(imageUser)
            }
            textUser.text = hits?.user
            textTags.text = hits?.tags
            textLikes.text = hits?.likes
            textViews.text = hits?.views
            textComments.text = hits?.comments

            fab.setOnClickListener(View.OnClickListener {
                download(hits!!)
                Snackbar.make(fab, "Downloading...", Snackbar.LENGTH_LONG).show()
            })
        } else{
            Log.d("TAG", "Fail")
        }

//        val requestPermissionLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted: Boolean ->
//                if (isGranted) {
//
//                } else {
//                    Toast.makeText(context, "Getting storage access denied", Toast.LENGTH_SHORT).show()
//                }
//            }
        return view
    }

    private fun download(hits: Hits){
        val filename = hits.id + ".jpg"
        val DIR_NAME = "pixabay"
        val downloadUrlOfImage = hits.largeImageURL
        val direct = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath + "/" + DIR_NAME + "/"
        )


        if (!direct.exists()) {
            direct.mkdir()
            Log.d("TAG", "dir created for first time")
        }

        val dm = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri: Uri = Uri.parse(downloadUrlOfImage)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(filename)
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + DIR_NAME + File.separator.toString() + filename
            )

        dm.enqueue(request)
    }

    }
