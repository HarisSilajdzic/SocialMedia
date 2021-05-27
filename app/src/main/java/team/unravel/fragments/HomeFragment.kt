package team.unravel.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unravel.socialmedia.R
import kotlinx.android.synthetic.main.fragment_home.*
import team.unravel.post.Post


class HomeFragment : Fragment(R.layout.fragment_home) {
    var mDatabase: DatabaseReference? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        mDatabase = FirebaseDatabase.getInstance().reference.child("users")


        val allUsers = storageReference?.child("users")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter()
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(10)
                .build()

        }
    }


}

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val kode = arrayOf(
        "123231",
        "dasdasd", "f5cf12312e78", "dasf3f13f1",
        "db8d14e", "9913dc4", "e120f96",
        "f13f3f13f"
    )


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_model, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemKode.text = kode[i]
        Glide.with(viewHolder.itemView.context)
            .asBitmap()
            .load("https://images.unsplash.com/photo-1604009506606-fd4989d50e6d?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzB8fGZvcmVzdHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80")
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    viewHolder.itemKategori.setImageBitmap(resource)
                }

            })

    }

    override fun getItemCount(): Int {
        return kode.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemKode: TextView
        var itemKategori: ImageView

//        private var authorText: TextView = itemView.findViewById(R.id.uploadedText)
//        private var imageView: TextView = itemView.findViewById(R.id.uploadedImage)
//
//        fun bind(post: Post) {
//            authorText.text = post.authorName
//            imageView.text = post.message
//        }

        init {
            itemKode = itemView.findViewById(R.id.uploadedText)
            itemKategori = itemView.findViewById(R.id.uploadedImage)
        }

    }
}
