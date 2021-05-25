package team.unravel.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.unravel.socialmedia.R
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val navHostFragment =
//            parentFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
        mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser = mAuth?.currentUser!!
        username.text = currentUser.displayName
        email.text = currentUser.email
        Glide.with(this)
            .asBitmap()
            .load(currentUser.photoUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    profileImg.setImageBitmap(resource)
                }

            })
        logout.setOnClickListener {
            mAuth?.signOut()
//            clickExecuted()

            Navigation.findNavController(view).navigate(R.id.action_moreFragment_to_loginFragment)

//            Navigation.findNavController(view).navigate(R.id.action_moreFragment_to_loginFragment)
//           findNavController().navigate(R.id.loginFragment)
//            findNavController().popBackStack()
//            findNavController().popBackStack(R.id.loginFragment, true)
//            findNavController().popBackStack(R.id.loginFragment, false)
//        Navigation.findNavController(view).navigateUp()
//            Navigation.findNavController(view).navigateUp()
        }
    }
//    private fun getMyNavController(): NavController? {
//        val fragment: Fragment? =
//            parentFragmentManager.findFragmentById(R.id.nav_host_fragment)
//        check(fragment is NavHostFragment) { "Activity $this does not have a NavHostFragment" }
//        return fragment.navController
//    }
//    fun clickExecuted() {
//        getMyNavController()?.navigate(R.id.loginFragment)
//    }
}


