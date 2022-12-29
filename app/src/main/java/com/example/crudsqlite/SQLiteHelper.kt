package com.example.crudsqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "student.db"
        private const val TB_ESTUDANTE = "tb_estudante"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTbEstudante = ("CREATE TABLE "+ TB_ESTUDANTE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NOME + " TEXT, "
                +EMAIL + " TEXT"+")")

        db?.execSQL(createTbEstudante)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TB_ESTUDANTE")
        onCreate(db)
    }

    fun insertStudent(std:StudentModel):Long{
        val db= this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NOME, std.nome)
        contentValues.put(EMAIL, std.email)

        val sucess = db.insert(TB_ESTUDANTE, null, contentValues)
        db.close()
        return sucess
    }

    @SuppressLint("Range")
    fun getAllStudent():ArrayList<StudentModel> {
        val stdList : ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TB_ESTUDANTE"
        val db = this.writableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nome: String
        var email: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                nome = cursor.getString(cursor.getColumnIndex("nome"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                val std = StudentModel(id = id, nome = nome, email = email)
                stdList.add(std)

            }while (cursor.moveToNext())
        }
        return stdList
    }

    fun updateStudent(std: StudentModel):Int{
        val db= this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NOME, std.nome)
        contentValues.put(EMAIL, std.email)

        val sucess = db.update(TB_ESTUDANTE, contentValues, "id="+std.id,null)
        db.close()
        return sucess
    }

    fun deleteStudentById(id:Int):Int{
        val db= this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val sucess = db.delete(TB_ESTUDANTE, "id=$id", null)
        db.close()
        return sucess
    }
}