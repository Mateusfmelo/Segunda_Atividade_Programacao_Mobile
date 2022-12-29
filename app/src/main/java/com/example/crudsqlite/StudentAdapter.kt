package com.example.crudsqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.ViewHolderEstudante>(){

    private var stdList: ArrayList<StudentModel> = ArrayList()
    private var onClickItem: ((StudentModel) -> Unit)? = null
    private var onClickDeleteItem: ((StudentModel) -> Unit)? = null

    fun addItems(items: ArrayList<StudentModel>){
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (StudentModel)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (StudentModel)->Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderEstudante(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolderEstudante, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener{ onClickDeleteItem?.invoke(std) }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class ViewHolderEstudante(var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var nome = view.findViewById<TextView>(R.id.tvNome)
        private var email = view.findViewById<TextView>(R.id.tvEmail)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std:StudentModel){
            id.text = std.id.toString()
            nome.text = std.nome
            email.text = std.email
        }
    }
}