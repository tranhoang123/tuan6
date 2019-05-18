package com.example.myapplication

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.user_screen.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.myapplication.room.AppDatabase
import com.example.myapplication.room.User
import com.example.myapplication.room.UserDAO
import java.util.*
import kotlin.concurrent.schedule


class UserScreen : AppCompatActivity() {
    var users: ArrayList<User> = ArrayList()
    lateinit var userAdapter: UserAdapter
    lateinit var dao: UserDAO
    var user = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_screen)
        initRoomDatabase()
        setupRecyclerView()
        getUser()
        btnAdd.setOnClickListener{
            val textTask = edtUserID.text.toString()
            if(textTask.trim() != "") {
                user.name = edtUserID.text.toString()
                dao.insert(user)
                userAdapter.appenData(user)
                edtUserID.setText("")
            }
            Log.d("hahah", "ahiahas")
        }
    }
    private fun setupRecyclerView(){
        rvUser.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        userAdapter = UserAdapter(users, this)
        rvUser.adapter = userAdapter
        userAdapter.setListener(userClickListener)

    }
    private val userClickListener = object : UserCLickListener {
        override fun onItemCLicked(position: Int) {
            Log.d("hasdas", "asdajsdioj")
            removeItem(position)
        }

        override fun onItemLongCLicked(position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onEditIconClicked(position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    private fun removeItem(position: Int) {
        dao.delete(users[position]) // remove from Room database  //

        users.removeAt(position) // remove user list on RAM

        userAdapter.notifyItemRemoved(position) // notify data change
//        Timer(false).schedule(100) {
            runOnUiThread {
                userAdapter.setData(users)
                userAdapter.notifyDataSetChanged()
           }
//        }
    }
    private fun getUser(){
        val users = dao.getAll() // get Tasks from ROOM database
        Log.i("Task: ", users.toString())
        this.users.addAll(users) // add to task list
        userAdapter.notifyDataSetChanged() // notify data changed
    }
    private fun initRoomDatabase(){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME).allowMainThreadQueries()
            .build()
        dao = db.userDAO()
    }
}