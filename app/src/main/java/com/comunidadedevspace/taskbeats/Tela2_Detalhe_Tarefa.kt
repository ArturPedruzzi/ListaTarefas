package com.comunidadedevspace.taskbeats

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar


class Tela2_Detalhe_Tarefa : AppCompatActivity() {

    private var tarefa: Tarefas? = null

    private lateinit var btnDone: Button

    companion object {
        const val TAREFA_DETALHE = "TAREFA_DETALHE"

        fun start(context: Context, tarefa: Tarefas?): Intent {
            val intent = Intent(context, Tela2_Detalhe_Tarefa::class.java)
                .apply {
                    putExtra(TAREFA_DETALHE, tarefa)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela2_detalhe_tarefa)

        tarefa = intent.getSerializableExtra(TAREFA_DETALHE) as Tarefas?

        val edtTitulo = findViewById<EditText>(R.id.edt_titulo_detail_tela2)
        val edtDescricao = findViewById<EditText>(R.id.edt_descricao_detail_tela2)
        btnDone = findViewById<Button>(R.id.btn_tela2)

        if (tarefa != null) {
            edtTitulo.setText(tarefa!!.titulo)
            edtDescricao.setText(tarefa!!.descricao)
        }

        btnDone.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descricao = edtDescricao.text.toString()

            if (titulo.isNotEmpty() && descricao.isNotEmpty()) {
                if (tarefa == null) { // -> veio pelo floatingAction Button, tarefa n existe.
                    criarOUeditarTarefa(0, titulo, descricao, MainActivity.ActionType.CREATE)
                }else{
                    criarOUeditarTarefa(tarefa!!.id, titulo, descricao, MainActivity.ActionType.UPDATE)
                }
            } else {
                mensagem(it, "Preencha todos os campos")
            }
        }

        //titulo2 = findViewById<TextView>(R.id.tv_nome_tela2)
        //titulo2.text = tarefa?.titulo ?: "Adicione uma tarefa"
    }


    private fun criarOUeditarTarefa(
        id: Int,
        titulo: String,
        descricao: String,
        actionType: MainActivity.ActionType
    ) {
        val newTarefa = Tarefas(id, titulo, descricao)
        retornoResult(newTarefa, actionType)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_tela2_results, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {

                if (tarefa != null) {
                    retornoResult(tarefa!!, MainActivity.ActionType.DELETE)

                } else {
                    mensagem(btnDone, "Item não existe")

                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //Esse código pega o result ( delete, create, edit) e passa para a tela 1
    private fun retornoResult(tarefa: Tarefas, actionType: MainActivity.ActionType) {
        val intent = Intent()
            .apply {
                val tarefaAction = MainActivity.TarefaAction(tarefa, actionType.name)
                putExtra(TAREFA_ACTION_RESULT, tarefaAction)
            }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun mensagem(view: View, mensagem: String) {
        Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }
}
