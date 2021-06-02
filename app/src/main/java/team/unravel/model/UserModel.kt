package team.unravel.model

import android.net.Uri

data class UserModel(val id: Int, val username: String, val imageRef: String, val userText: String){
    constructor(): this(0, "", "", ""){

    }
}