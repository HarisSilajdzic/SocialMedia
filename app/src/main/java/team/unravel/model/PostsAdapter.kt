package team.unravel.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.unravel.socialmedia.R
import team.unravel.GlideApp

class PostsAdapter(internal var context: android.content.Context) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    internal var kode: MutableList<UserModel> = ArrayList()

    val lastItemId: Int?
    get() = kode[kode.size - 1].id

    fun addAll (newPosts: List<UserModel>){
        val init = kode.size
        kode.addAll(newPosts)
        notifyItemRangeChanged(init, newPosts.size)
    }

//    fun removeLastItem(){
//        kode.removeAt(kode.size - 1)
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.post_model, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val storage = Firebase.storage
        val storageReference = Firebase.storage.reference



        viewHolder.itemKode.text = kode[i].userText

        GlideApp.with(viewHolder.itemView.context)
            .asBitmap()
            .load(storage.getReferenceFromUrl(storageReference.toString()).child("images/").child(kode[i].imageRef))
//            .load(storageReference.child("images/").child(kode[i].imageRef))
//            .load(storageReference.child("images/" + kode[i].imageRef).downloadUrl)
            .into(object : CustomTarget<Bitmap>() {

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    viewHolder.itemKategori.setImageBitmap(resource)
                }
            })
    }

    override fun getItemCount(): Int {
      return kode.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemKode: TextView = itemView.findViewById(R.id.uploadedText)
        var itemKategori: ImageView = itemView.findViewById(R.id.uploadedImage)

    }
}