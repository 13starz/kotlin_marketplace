package com.example.marketpalcev1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Locale

class AddItemActivity : AppCompatActivity() {
    private lateinit var itemImage: ImageView
    private lateinit var itemDesc: EditText
    private lateinit var itemPrice: EditText
    private lateinit var addButton: Button
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemImage = findViewById(R.id.item_image_add)
        itemDesc = findViewById(R.id.item_desc_add)
        itemPrice = findViewById(R.id.item_price_add)
        addButton = findViewById(R.id.button_add)

        itemImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        addButton.setOnClickListener {
            addItemToDatabase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            itemImage.setImageURI(imageUri) // устанавливаем image
        }
    }

    private fun saveImageAndGetName(): String?{
        if(imageUri == null) return null;

        try {
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val imageName = "item_${System.currentTimeMillis()}_${generateRandomString(10).lowercase(Locale.ROOT)}".filter { it.isLetterOrDigit() } + ".jpg"
            val imageFile = File(filesDir, imageName)

            val outputStream = FileOutputStream(imageFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            return imageName;

        } catch (e: IOException) {
            e.printStackTrace();
            return null
        }
    }

    private fun addItemToDatabase() {
        val desc = itemDesc.text.toString().trim()
        val price = itemPrice.text.toString().trim()
        val imageName = saveImageAndGetName();

        if (desc.isEmpty() || price.isEmpty() || imageName == null) {
            Toast.makeText(this, "Пожалуйста, заполните все поля и выберите изображение", Toast.LENGTH_SHORT).show()
            return
        }

        val priceInt = price.toIntOrNull()
        if (priceInt == null || priceInt <= 0) {
            Toast.makeText(this, "Неверный формат цены", Toast.LENGTH_SHORT).show()
            return
        }
        val item = Item(0, desc, priceInt, imageName) // 0 тк не имеет значения id в этом контексте
        val db = DbHelper(this, null)
        //TODO get user login from another class

        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val userLogin = sharedPreferences.getString("user_login", "");

        if (userLogin == ""){
            Toast.makeText(this, "Произошла ошибка. Пользователь не авторизован.", Toast.LENGTH_SHORT).show()
            return;
        }

        db.addItem(item, userLogin!!)

        Toast.makeText(this, "Товар добавлен", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (9..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}