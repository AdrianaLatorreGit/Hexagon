package com.example.hexagontest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.data.entity.UserEntity
import com.example.hexagontest.databinding.ActivityAddUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddUserActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddUserBinding.inflate(layoutInflater)
    }

    private lateinit var database: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Inicializa o banco de dados
        database = DataBase.getInstance(applicationContext)

        // Configura o botão "Salvar"
        binding.btnSave.setOnClickListener {
            // Captura os dados do formulário
            val name = binding.textName.text.toString()
            val birth = binding.textBirth.text.toString()
            val cpf = binding.textCpf.text.toString()
            val city = binding.textCity.text.toString()
            val isActive = binding.switchActive.isChecked // Captura o estado do Switch

            // Verifica se todos os campos estão preenchidos
            if (name.isNotEmpty() && birth.isNotEmpty() && cpf.isNotEmpty() && city.isNotEmpty()) {
                // Salva o usuário no banco de dados
                saveUser(name, birth, cpf, city, isActive)
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUser(name: String, birth: String, cpf: String, city: String, isActive: Boolean) {
        // Cria um objeto UserEntity
        val user = UserEntity(
            name = name,
            birth = birth,
            cpf = cpf,
            city = city,
            isActive = isActive
        )

        // Insere o usuário no banco de dados utilizando coroutines
        CoroutineScope(Dispatchers.IO).launch {
            val userId = database.userDao().salvar(user)

            withContext(Dispatchers.Main) {
                if (userId > 0) {
                    Toast.makeText(this@AddUserActivity, "Usuário salvo com sucesso", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a Activity após salvar
                } else {
                    Toast.makeText(this@AddUserActivity, "Erro ao salvar o usuário", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
