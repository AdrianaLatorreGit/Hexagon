package com.example.hexagontest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

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

        // Busca os dados do usuário e verifica o estado do Switch
        loadUserData()

        // Configura o botão para abrir AddUserActivity
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            // Obtém a lista de usuários
            val userList = database.userDao().getAllUsers()

            // Log para verificar o conteúdo da lista
            Log.d("MainActivity", "Total de usuários obtidos: ${userList.size}")

            // Filtra os usuários ativos
            val activeUsers = userList.filter { user ->
                // Log para verificar o estado de isActive
                Log.d("MainActivity", "Usuário: ${user.name}, Ativo: ${user.isActive}")
                user.isActive
            }

            // Converte a lista de usuários ativos em uma única string
            val userDataText = activeUsers.joinToString(separator = "\n") { user ->
                "Usuário ativo: ${user.name}, ${user.birth}, ${user.cpf}, ${user.city}"
            }

            withContext(Dispatchers.Main) {
                // Atualiza o TextView com a string formatada
                binding.textViewUserData.text = userDataText

                // Log para verificar o texto atualizado
                Log.d("MainActivity", "Texto atualizado no TextView: $userDataText")
            }
        }
    }

}
