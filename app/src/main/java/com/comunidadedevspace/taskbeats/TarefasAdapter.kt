package com.comunidadedevspace.taskbeats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TarefasAdapter(
    private val abrirTela2 : (tarefa: Tarefas) -> Unit
) : ListAdapter<Tarefas, TarefasViewHolder>(TarefasAdapter) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefasViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa, parent, false)

        return TarefasViewHolder(view)
    }



    override fun onBindViewHolder(holder: TarefasViewHolder, position: Int) {
        val indexList = getItem(position)
        holder.bind(indexList, abrirTela2)
    }

    companion object : DiffUtil.ItemCallback<Tarefas>(){

        override fun areItemsTheSame(oldItem: Tarefas, newItem: Tarefas): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Tarefas, newItem: Tarefas): Boolean {
            return oldItem.titulo == newItem.titulo &&
                    oldItem.descricao == newItem.descricao
        }

    }
}

class TarefasViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val tvtitulo = view.findViewById<TextView>(R.id.tv_titulo)
    val tvdescricao = view.findViewById<TextView>(R.id.tv_descricao)

    fun bind(tarefa: Tarefas, abrirTela2: (tarefa: Tarefas) -> Unit) {
        tvtitulo.text = tarefa.titulo
        tvdescricao.text = tarefa.descricao

        view.setOnClickListener {
            abrirTela2.invoke(tarefa)
        }
    }
}