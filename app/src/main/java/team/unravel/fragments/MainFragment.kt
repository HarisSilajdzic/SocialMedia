package team.unravel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unravel.socialmedia.R
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(){
    lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        NavigationUI.setupWithNavController(menu_items, findNavController())
        val navHostFragment = childFragmentManager.findFragmentById(R.id.my_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.menu_items)
        bottomNavigationView.setupWithNavController(navController)
//        bottomNavigationView.setupWithNavController(navController)
//        NavigationUI.onNavDestinationSelected(R.menu.home, navController)
//        NavigationUI.setupWithNavController(menu_items, findNavController())
    }


}