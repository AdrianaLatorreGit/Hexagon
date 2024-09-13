package com.example.hexagontest

import android.os.Bundle
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

        // Busca os dados do usuário e verifica o estado do Switch
        loadUserData()
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = database.userDao().getAllUsers() // Obtém a lista de usuários

            withContext(Dispatchers.Main) {
                // Filtra os usuários ativos
                val activeUsers = userList.filter { user ->
                    user.isActive
                }

                // Converte a lista de usuários ativos em uma única string
                val userDataText = activeUsers.joinToString(separator = "\n") { user ->
                    "Usuário ativo: ${user.name}, ${user.birth}, ${user.cpf}, ${user.city}"
                }

                // Atualiza o TextView com a string formatada
                binding.textViewUserData.text = userDataText
            }
        }
    }
}
