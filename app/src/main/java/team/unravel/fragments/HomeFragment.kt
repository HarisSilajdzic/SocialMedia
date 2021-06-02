package team.unravel.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unravel.socialmedia.R
import kotlinx.android.synthetic.main.fragment_home.*
import team.unravel.model.PostsAdapter
import team.unravel.model.UserModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    val ITEM_COUNT = 5
    var total_items = 0
    var last_visible_item = 0
    var isLoading = false
    var isMaxData = false
    var lastKey: String = ""


    var mDatabase: DatabaseReference? = null
    var paginatedDatabase: Query? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var adapter: PostsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = PostsAdapter(requireContext())
        recyclerView.adapter = adapter
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        mDatabase = FirebaseDatabase.getInstance().getReference("Member")

        paginatedDatabase =
            FirebaseDatabase.getInstance().getReference("Member").orderByKey().limitToLast(1)

        getLastKey()

        getPosts()
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                total_items = layoutManager.itemCount
                last_visible_item = layoutManager.findLastVisibleItemPosition()

                if (total_items <= last_visible_item + ITEM_COUNT) {
                    getPosts()
                    isLoading = true
                }

            }
        })
        val genericTypeIndicator: GenericTypeIndicator<HashMap<String, UserModel>> =
            object : GenericTypeIndicator<HashMap<String, UserModel>>() {

            }
        // Attach a listener to read the data at our posts reference
//        mDatabase?.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val post = dataSnapshot.getValue(genericTypeIndicator)!!.values
//                adapter.kode = post.toList()
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//            }
//        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        isMaxData = false
//        lastNode = adapter.lastItemId.toString()
//        adapter.removeLastItem()
        adapter.notifyDataSetChanged()
        getLastKey()
        getPosts()
        return true

    }

    private fun getPosts() {
        if (!isMaxData) {
            val query: Query
//            if (TextUtils.isEmpty(lastNode))
//                query = FirebaseDatabase.getInstance().reference
//                    .child("posts")
//                    .orderByKey()
//                    .startAt("")
//                    .limitToFirst(ITEM_COUNT)
//            else
//                query = FirebaseDatabase.getInstance().reference
//                    .child("posts")
//                    .orderByKey()
//                    .startAt(lastNode)
//                    .limitToFirst(ITEM_COUNT)


            val startFrom = 1

            query = FirebaseDatabase.getInstance().reference
                .child("Member")
//                    .orderByChild("id")
//                    .startAt(startFrom.toString())
//                    .limitToFirst(startFrom + ITEM_COUNT)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        val userList = ArrayList<UserModel>()
                        for (item in snapshot.children)
                            userList.add(item.getValue(UserModel::class.java)!!)
//                          lastNode = userList[userList.size - 1].id
//                        if (lastNode != lastKey)
//                            userList.removeAt(userList.size - 1)
//                        else
//                            lastNode = "end"

                        adapter.addAll(userList)
                        isLoading = false
                    } else {
                        isLoading = false
                        isMaxData = true

                    }
                }

            })
        }
    }

    private fun getLastKey() {
        val getLastKey = FirebaseDatabase.getInstance().reference
            .child("Member")
            .orderByKey()
            .limitToFirst(1)
        getLastKey.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapShot in snapshot.children) {
                    lastKey = userSnapShot.key.toString()
                }
            }

        })
    }

}