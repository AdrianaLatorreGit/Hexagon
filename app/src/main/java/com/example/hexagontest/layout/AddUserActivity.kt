package com.example.hexagontest.layout

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hexagontest.data.dataBase.DataBase
import com.example.hexagontest.data.entity.UserEntity
import com.example.hexagontest.databinding.ActivityAddUserBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddUserActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddUserBinding.inflate(layoutInflater)
    }

    private lateinit var database: DataBase
    private var selectedImageUri: Uri? = null

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = DataBase.getInstance(applicationContext)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it

                Picasso.get().load(selectedImageUri).into(binding.imageButton)
            }
        }

        binding.btnSave.setOnClickListener {

            val name = binding.textName.text.toString()
            val birth = binding.textBirth.text.toString()
            val cpf = binding.textCpf.text.toString()
            val city = binding.textCity.text.toString()
            val isActive = binding.switchActive.isChecked

            if (name.isNotEmpty() && birth.isNotEmpty() && cpf.isNotEmpty() && city.isNotEmpty()) {

                saveUser(name, birth, cpf, city, isActive)
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageButton.setOnClickListener {
            openImageChooser()
        }
    }

    private fun openImageChooser() {

        pickImageLauncher.launch("image/*")
    }

    private fun saveUser(name: String, birth: String, cpf: String, city: String, isActive: Boolean) {
        val user = UserEntity(
            name = name,
            birth = birth,
            cpf = cpf,
            city = city,
            isActive = isActive,
            imageUri = selectedImageUri?.toString() ?: ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            val userId = database.userDao().salvar(user)

            withContext(Dispatchers.Main) {
                if (userId > 0) {
                    Toast.makeText(this@AddUserActivity, "Usuário salvo com sucesso", Toast.LENGTH_SHORT).show()

                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@AddUserActivity, "Erro ao salvar o usuário", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
