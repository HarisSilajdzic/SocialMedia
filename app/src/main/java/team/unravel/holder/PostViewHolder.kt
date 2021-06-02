package team.unravel.holder
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unravel.socialmedia.R
import team.unravel.post.Post

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var authorView: TextView = itemView.findViewById(R.id.uploadedText)
    private var messageView: TextView = itemView.findViewById(R.id.uploadedImage)

    fun bind(post: Post) {
        authorView.text = post.authorName
        messageView.text = post.message
    }

}