package com.example.kotlinretrofit.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.android.parcel.Parcelize

@Parcelize
class Labels(
    @SerializedName("total")
    var total: Int? = null,
    @SerializedName("totalHits")
    var totalHits: Int? = null,
    @SerializedName("hits")
    var hits: List<Hits> = emptyList(),
    ) : Parcelable, @NonNull Consumer<Labels> {
    override fun accept(t: Labels?) {
        TODO("Not yet implemented")
    }
}

@Parcelize
data class Hits(
    var id: String? = null,
    var pageURL: String? = null,
    var type: String? = null,
    var tags: String? = null,
    var previewURL: String? = null,
    var previewWidth: String? = null,
    var previewHeight: String? = null,
    var webformatURL: String? = null,
    var webformatWidth: String? = null,
    var webformatHeight: String? = null,
    var largeImageURL: String? = null,
    var imageWidth: String? = null,
    var imageHeight: String? = null,
    var imageSize: String? = null,
    var views: String? = null,
    var downloads: String? = null,
    var collections: String? = null,
    var likes: String? = null,
    var comments: String? = null,
    var user_id: String? = null,
    var user: String? = null,
    var userImageURL: String? = null,
): Parcelable
