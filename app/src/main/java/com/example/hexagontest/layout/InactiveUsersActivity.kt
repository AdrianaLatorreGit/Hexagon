package com.example.hexagontest.layout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.databinding.ActivityInactiveUsersBinding
import com.example.hexagontest.databinding.ItemUserTagBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InactiveUsersActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityInactiveUsersBinding.inflate(layoutInflater)
    }

    private lateinit var database: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = DataBase.getInstance(applicationContext)

        loadInactiveUsers()
    }

    private fun loadInactiveUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = database.userDao().getAllUsers()
            val inactiveUsers = userList.filter { !it.isActive }

            withContext(Dispatchers.Main) {

                binding.userTagsLayout.removeAllViews()

                for (user in inactiveUsers) {
                    val itemUserTagBinding = ItemUserTagBinding.inflate(layoutInflater)

                    "Usuário: ${user.name}\nData de Nascimento: ${user.birth}\nCPF: ${user.cpf}\nCidade: ${user.city}".also { itemUserTagBinding.textViewUserTag.text = it }

                    if (!user.imageUri.isNullOrEmpty()) {
                        Picasso.get().load(user.imageUri).into(itemUserTagBinding.imageViewUser)
                    } else {
                        itemUserTagBinding.imageViewUser.setImageResource(0)
                    }

                    itemUserTagBinding.userSwitch.isChecked = false

                    itemUserTagBinding.userSwitch.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {

                            activateUser(user.id)

                            startActivity(Intent(this@InactiveUsersActivity, MainActivity::class.java))
                            finish()
                        }
                    }

                    binding.userTagsLayout.addView(itemUserTagBinding.root)
                }
            }
        }
    }

    private fun activateUser(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database.userDao().updateUserActiveStatus(userId, true)
        }
    }
}
