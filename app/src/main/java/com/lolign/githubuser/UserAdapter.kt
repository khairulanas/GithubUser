package com.lolign.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter internal constructor(private val context:Context): BaseAdapter() {
    internal var users = arrayListOf<User>()

    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(p0: Int): Any {
        return users[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup,false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }
}

private class ViewHolder constructor(view: View) {
    private val txtUsername:TextView = view.findViewById(R.id.txt_username)
    private val txtName:TextView = view.findViewById(R.id.txt_name)
    private val imgPhoto:CircleImageView = view.findViewById(R.id.img_photo)

    fun bind(user:User){
        txtUsername.text = user.username
        txtName.text = user.name
        imgPhoto.setImageResource(user.avatar!!)
    }

}
