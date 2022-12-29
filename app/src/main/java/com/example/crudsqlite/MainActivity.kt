package com.example.crudsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edNome: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView:RecyclerView
    private var adapter: StudentAdapter? = null
    private var std: StudentModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerVew()
        sqLiteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener{
            addStudent()
        }
        btnView.setOnClickListener {
            getStudents()
        }
        btnUpdate.setOnClickListener{
            updateStudent()
        }
        adapter?.setOnClickItem {
            Toast.makeText(this, it.nome, Toast.LENGTH_SHORT).show()
            edNome.setText(it.nome)
            edEmail.setText(it.email)
            std = it
        }
        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun updateStudent() {
        val nome = edNome.text.toString()
        val email = edEmail.text.toString()
        if(std == null) return

        val std = StudentModel(id = std!!.id, nome = nome, email = email)
        val status = sqLiteHelper.updateStudent(std)
        if(status > -1){
            clearEditText()
            getStudents()
        }else{
            Toast.makeText(this,"Update Falhou", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getStudents() {
        val stdList = sqLiteHelper.getAllStudent()
        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val nome = edNome.text.toString()
        val email = edEmail.text.toString()

        if(nome.isEmpty()|| email.isEmpty()){
            Toast.makeText(this, "Insira algum valor ",Toast.LENGTH_SHORT).show()
        }else{
            val std = StudentModel(nome = nome, email = email)
            val status = sqLiteHelper.insertStudent(std)

            if(status>-1){
                Toast.makeText(this, "Estudante inserido com sucesso", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudents()
            }else {
                Toast.makeText(this, "Erro, consulte o administrador!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteStudent(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Tem certeza que deseja deletar?")
        builder.setCancelable(true)
        builder.setPositiveButton("Sim"){
                dialog, _ ->
            sqLiteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("Não"){
                dialog, _ -> dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()

    }

    private fun clearEditText() {
        edNome.setText("")
        edEmail.setText("")
        edNome.requestFocus()
    }

    private fun initRecyclerVew(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edNome = findViewById(R.id.edNome)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}