package com.example.hexagontest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.databinding.ActivityMainBinding
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

        // Carrega os dados dos usuários
        loadUserData()
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = database.userDao().getAllUsers() // Obtém a lista de usuários

            withContext(Dispatchers.Main) {
                // Limpa o LinearLayout antes de adicionar novas tags
                binding.userTagsLayout.removeAllViews()

                // Filtra os usuários ativos e cria tags
                val activeUsers = userList.filter { user -> user.isActive }

                // Adiciona uma tag para cada usuário ativo
                for (user in activeUsers) {
                    val userTagView = layoutInflater.inflate(R.layout.item_user_tag, null)
                    val textViewUserTag = userTagView.findViewById<TextView>(R.id.textViewUserTag)
                    textViewUserTag.text = "Usuário: ${user.name} Data de Nascimento: ${user.birth} CPF: ${user.cpf} Cidade: ${user.city}"
                    binding.userTagsLayout.addView(userTagView)
                }

                // Log para verificar o texto atualizado
                Log.d("MainActivity", "Número de tags adicionadas: ${activeUsers.size}")
            }
        }
    }
}
