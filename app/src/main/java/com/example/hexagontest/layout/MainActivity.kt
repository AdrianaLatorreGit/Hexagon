package com.example.hexagontest.layout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.data.entity.UserEntity
import com.example.hexagontest.databinding.ActivityMainBinding
import com.example.hexagontest.databinding.DialogEditUserBinding
import com.example.hexagontest.databinding.ItemUserTagBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = DataBase.getInstance(applicationContext)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        binding.btnInactiveUsers.setOnClickListener {
            val intent = Intent(this, InactiveUsersActivity::class.java)
            startActivity(intent)
        }

        loadUserData()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = database.userDao().getAllUsers()

            withContext(Dispatchers.Main) {

                binding.userTagsLayout.removeAllViews()

                for (user in userList.filter { it.isActive }) {

                    val itemUserTagBinding = ItemUserTagBinding.inflate(layoutInflater)

                    "Usuário: ${user.name}\nData de Nascimento: ${user.birth}\nCPF: ${user.cpf}\nCidade: ${user.city}".also {
                        itemUserTagBinding.textViewUserTag.text = it
                    }

                    if (!user.imageUri.isNullOrEmpty()) {
                        Picasso.get().load(user.imageUri).into(itemUserTagBinding.imageViewUser)
                    } else {
                        itemUserTagBinding.imageViewUser.setImageResource(0)
                    }

                    itemUserTagBinding.userSwitch.isChecked = user.isActive

                    itemUserTagBinding.userSwitch.setOnCheckedChangeListener { _, isChecked ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.userDao().updateUserActiveStatus(user.id, isChecked)
                            withContext(Dispatchers.Main) {
                                if (!isChecked) {
                                    binding.userTagsLayout.removeView(itemUserTagBinding.root)
                                }
                            }
                        }
                    }

                    itemUserTagBinding.root.setOnClickListener {
                        showEditUserDialog(user)
                    }

                    binding.userTagsLayout.addView(itemUserTagBinding.root)
                }
            }
        }
    }

    private fun showEditUserDialog(user: UserEntity) {
        val dialogBinding = DialogEditUserBinding.inflate(layoutInflater)

        dialogBinding.editName.setText(user.name)
        dialogBinding.editBirth.setText(user.birth)
        dialogBinding.editCpf.setText(user.cpf)
        dialogBinding.editCity.setText(user.city)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Editar Usuário")
            .setView(dialogBinding.root)
            .setPositiveButton("Salvar") { _, _ ->

                val newName = dialogBinding.editName.text.toString()
                val newBirth = dialogBinding.editBirth.text.toString()
                val newCpf = dialogBinding.editCpf.text.toString()
                val newCity = dialogBinding.editCity.text.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    database.userDao().updateUserFields(user.id, newName, newBirth, newCpf, newCity)
                    withContext(Dispatchers.Main) {
                        loadUserData()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
