package team.unravel.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import kotlin.properties.Delegates


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {
    private lateinit var mAuth: FirebaseAuth
    var storage: FirebaseStorage? = null
    var maxId: Long = 0
    private var storageReference: StorageReference? = null
    lateinit var imageUri: Uri
    var imageUrl: String? = null
    lateinit var userText: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        imageUrl = storageReference?.child("posts")?.child("imageRef")!!.name
        val imageView = view.findViewById<ImageView>(R.id.uploadImage)
        imageView.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1000)
        }
        uploadPost.setOnClickListener {
            userText = textContent.text.toString()
            imageUri = Uri.parse("nothing")
            uploadPostToFB()
            Log.d("TAG", "uploadPostClicked: $uploadImage ")
        }

    }

    private fun uploadPostToFB() {
        var id: Int
        val databaseRef = FirebaseDatabase.getInstance().reference
        val query = databaseRef.child("Member")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                mAuth = FirebaseAuth.getInstance()
                val currentUser: FirebaseUser = mAuth.currentUser!!
                maxId = snapshot.childrenCount
                val userModel = UserModel(0, currentUser.displayName!!, imageUrl!!, userText)
                Firebase.database.reference.child("Member").child((maxId + 1).toString()).setValue(userModel)
                findNavController().navigate(R.id.homeFragment)
            }

        })


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
        imageUri.let {
            ref?.putFile(it)
                ?.addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                    override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                        Toast.makeText(requireContext(), "Upload Successful", Toast.LENGTH_SHORT)
                            .show()
                        imageUrl = ref.name
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
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")

                    }

                })
        }

    }


}