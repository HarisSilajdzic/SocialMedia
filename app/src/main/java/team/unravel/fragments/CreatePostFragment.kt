package team.unravel.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.unravel.socialmedia.R
import kotlinx.android.synthetic.main.fragment_create_post.*
import team.unravel.model.UserModel


class CreatePostFragment : Fragment() {
//    val storage = Firebase.storage("gs://social-media-app-id.appspot.com/uploads")
//    val storageRef = storage.rbeference
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_create_post, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userModel = UserModel("123121", "hariss", "https://www.test.com/img.jpg")

        Firebase.database.reference.child("users").setValue(userModel)

        val imageView = view.findViewById<ImageView>(R.id.uploadImage)
        imageView.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1000)
        }

    }

    private fun uploadPostToFB() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val imageStream = selectedImage?.let {
                requireContext().contentResolver.openInputStream(
                    it
                )
            }
            val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
            uploadImage.setImageBitmap(yourSelectedImage)
            if (uploadPost != null || textContent != null) {
                uploadPost.setOnClickListener {
                    uploadPostToFB()

                    Log.d("TAG", "uploadPostClicked: $uploadImage ")
                }
            }
        }
    }


}