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

        // Inicializa o banco de dados
        database = DataBase.getInstance(applicationContext)

        // Carrega os usuários inativos ao iniciar a activity
        loadInactiveUsers()
    }

    private fun loadInactiveUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = database.userDao().getAllUsers() // Obtém todos os usuários
            val inactiveUsers = userList.filter { !it.isActive } // Filtra os inativos

            withContext(Dispatchers.Main) {
                // Limpa o layout antes de adicionar as novas tags
                binding.userTagsLayout.removeAllViews()

                // Adiciona uma tag para cada usuário inativo
                for (user in inactiveUsers) {
                    val itemUserTagBinding = ItemUserTagBinding.inflate(layoutInflater)

                    // Define o texto com as informações do usuário
                    "Usuário: ${user.name}\nData de Nascimento: ${user.birth}\nCPF: ${user.cpf}\nCidade: ${user.city}".also { itemUserTagBinding.textViewUserTag.text = it }

                    if (!user.imageUri.isNullOrEmpty()) {
                        Picasso.get().load(user.imageUri).into(itemUserTagBinding.imageViewUser)
                    } else {
                        itemUserTagBinding.imageViewUser.setImageResource(0) // Remove qualquer imagem existente
                    }


                    // Desativa o Switch, pois o usuário está inativo
                    itemUserTagBinding.userSwitch.isChecked = false

                    // Listener para o Switch
                    itemUserTagBinding.userSwitch.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            // Se o usuário for reativado, atualiza o status no banco de dados
                            activateUser(user.id)

                            // Após ativar, volta para a MainActivity e finaliza esta Activity
                            startActivity(Intent(this@InactiveUsersActivity, MainActivity::class.java))
                            finish()
                        }
                    }

                    // Adiciona a view ao layout
                    binding.userTagsLayout.addView(itemUserTagBinding.root)
                }
            }
        }
    }

    // Função para ativar o usuário
    private fun activateUser(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database.userDao().updateUserActiveStatus(userId, true)
        }
    }
}
