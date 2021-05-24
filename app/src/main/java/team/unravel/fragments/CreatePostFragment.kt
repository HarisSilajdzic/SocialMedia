package team.unravel.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.unravel.socialmedia.R
import kotlinx.android.synthetic.main.fragment_create_post.*
import team.unravel.model.UserModel
import java.util.*


class CreatePostFragment : Fragment() {
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var imageUri: Uri
    lateinit var userText: String

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


        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference

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

        val userModel = UserModel("2", "fark", imageUri.toString(), userText)
        Firebase.database.reference.child("users").push().setValue(userModel)
        findNavController().navigate(R.id.homeFragment)
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
            if (uploadImage.drawable != null || textContent.text.toString().isNotEmpty()) {
                uploadPost.setOnClickListener {
                    userText = textContent.text.toString()
                        imageUri = data.data!!
                        uploadImageToFB()
//comment
                    Log.d("TAG", "uploadPostClicked: $uploadImage ")
                }
            }
        }
    }

    private fun uploadImageToFB() {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
        val ref = storageReference?.child(
            "images/"
                    + UUID.randomUUID().toString()
        )
        imageUri?.let {
            ref?.putFile(it)
                ?.addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                    override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                        Toast.makeText(requireContext(), "Upload Successful", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                        uploadPostToFB()
                    }
                })
                ?.addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
                    }

                })
                ?.addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
                    override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                        val progress: Double = (100.0
                                * taskSnapshot.bytesTransferred
                                / taskSnapshot.totalByteCount)
                        progressDialog.setMessage(
                            "Uploaded "
                                    + progress.toInt() + "%"
                        )
                    }

                })
        }

    }
}