package com.example.hexagontest.layout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.databinding.ActivityMainBinding
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

        // Inicializa o banco de dados
        database = DataBase.getInstance(applicationContext)

        // Configura o botão para abrir AddUserActivity
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        // Configura o botão para abrir InactiveUsersActivity
        binding.btnInactiveUsers.setOnClickListener {
            val intent = Intent(this, InactiveUsersActivity::class.java)
            startActivity(intent)
        }

        // Carrega os dados dos usuários ao iniciar a activity
        loadUserData()
    }

    // Sobrescreve onResume para recarregar os dados sempre que a activity voltar ao foco
    override fun onResume() {
        super.onResume()
        loadUserData() // Recarrega os dados de usuários ao voltar para a MainActivity
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = database.userDao().getAllUsers() // Obtém a lista de usuários
            Log.d("MainActivity", "Lista de usuários carregada: ${userList.size}")

            withContext(Dispatchers.Main) {
                // Limpa o LinearLayout antes de adicionar novas tags
                binding.userTagsLayout.removeAllViews()

                // Adiciona uma tag para cada usuário ativo
                for (user in userList.filter { it.isActive }) {
                    // Usa ViewBinding para inflar item_user_tag
                    val itemUserTagBinding = ItemUserTagBinding.inflate(layoutInflater)

                    // Define o texto com as informações do usuário
                    "Usuário: ${user.name}\nData de Nascimento: ${user.birth}\nCPF: ${user.cpf}\nCidade: ${user.city}".also { itemUserTagBinding.textViewUserTag.text = it }

                    // Carrega a imagem do usuário usando o Picasso, caso exista uma imagem salva
                    if (!user.imageUri.isNullOrEmpty()) {
                        Picasso.get().load(user.imageUri).into(itemUserTagBinding.imageViewUser)
                    } else {
                        itemUserTagBinding.imageViewUser.setImageResource(0) // Remove qualquer imagem existente
                    }

                    // Configura o estado inicial do Switch com base no estado do usuário (ativo/inativo)
                    itemUserTagBinding.userSwitch.isChecked = user.isActive

                    // Listener para o Switch
                    itemUserTagBinding.userSwitch.setOnCheckedChangeListener { _, isChecked ->
                        CoroutineScope(Dispatchers.IO).launch {
                            // Atualiza o status ativo do usuário no banco de dados
                            database.userDao().updateUserActiveStatus(user.id, isChecked)

                            withContext(Dispatchers.Main) {
                                if (!isChecked) {
                                    // Remove a tag visualmente da interface quando o usuário for desativado
                                    binding.userTagsLayout.removeView(itemUserTagBinding.root)
                                }
                            }
                        }
                    }

                    // Adiciona a view ao layout apenas para usuários ativos
                    binding.userTagsLayout.addView(itemUserTagBinding.root)
                }

                Log.d("MainActivity", "Número de tags adicionadas: ${userList.filter { it.isActive }.size}")
            }
        }
    }


}

