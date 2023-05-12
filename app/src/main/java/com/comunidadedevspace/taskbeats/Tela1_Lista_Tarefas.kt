package com.comunidadedevspace.taskbeats


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.Serializable


class MainActivity : AppCompatActivity() {


    // colocamos a imagem em cima, apra que ela possa aparecer tanto na fun onCreat, quanto na val startForResult
    private lateinit var conteudo_lista_vazia: LinearLayout

    // colocamos o adapter no topo para ficar disponível para a startForResult
    val adapterList = TarefasAdapter(::abrirTela2_click_item)


    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "tarefas-database"
        ).build()
    }

    private val dao by lazy {
        dataBase.tarefasDao()
    }


    // variável que tem a ação de receber o resultado(DELETE, CREATE, UPDATE) da tela2
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val tarefaAction = data?.getSerializableExtra(TAREFA_ACTION_RESULT) as TarefaAction
            val tarefa: Tarefas = tarefaAction.tarefa



            when (tarefaAction.actionType) {

                ActionType.CREATE.name -> { insertTarefa(tarefa)
                }
                ActionType.UPDATE.name -> { updateList(tarefa)

                }
                ActionType.DELETE.name -> { deleleByID(tarefa.id)
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela1_lista_tarefas)

        listDataBase()

        // recuperando Iu component imagem para a mainActivity
        conteudo_lista_vazia = findViewById(R.id.ctn_conteudo)


        // recuperando recyclerView
        val rvList = findViewById<RecyclerView>(R.id.rv_contatos)

        // juntando recycelrView com o adapter
        rvList.adapter = adapterList


        // recuperando float action button
        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            abrirTela2_btn(null)
        }
    }

    private fun listDataBase() {
        CoroutineScope(IO).launch {
            val listDataBase: List<Tarefas> = dao.getAll()
            adapterList.submitList(listDataBase)
        }
    }

    private fun insertTarefa(tarefa: Tarefas) {
        CoroutineScope(IO).launch {
            dao.insert(tarefa)
            listDataBase()
        }
    }

    private fun updateList(tarefa: Tarefas) {
        CoroutineScope(IO).launch {
            dao.update(tarefa)
            listDataBase()
        }
    }

    private fun deleleByID(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listDataBase()
        }
    }

    private fun delele_all() {
        CoroutineScope(IO).launch {
            dao.deleteAll()
            listDataBase()
        }
    }


    private fun mensagem(view: View, mensagem: String) {
        Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    // função que chama a tela2 para ser aberta
    fun abrirTela2_click_item(tarefa: Tarefas) {
        abrirTela2_btn(tarefa)
    }

    fun abrirTela2_btn(tarefa: Tarefas?) {
        val intent = Intent(Tela2_Detalhe_Tarefa.start(this, tarefa))
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.menu_delete_all, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_All -> {
                delele_all()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // classe que armazena os resultados do menu
    enum class ActionType {
        DELETE,
        UPDATE,
        CREATE
    }

    // classe que relaciona um item da lista com um resultado do menu
    data class TarefaAction(
        val tarefa: Tarefas,
        val actionType: String
    ) : Serializable

}

// variável que serve como validação para autorizar o resultado da tela2 ser passado para a tela1
const val TAREFA_ACTION_RESULT = "TAREFA_ACTION_RESULT"






