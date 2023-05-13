package com.comunidadedevspace.taskbeats.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.comunidadedevspace.taskbeats.data.Tarefas


@Dao
interface TarefasDao{
    //Insere os dados na tabela
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tarefa : Tarefas)

    //Tem a função de selecionar todas as tarefas na tabela e fazer a listagem de todas e mostrar na UI
    @Query("select * from tarefas")
    fun getAll(): List<Tarefas>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(tarefa: Tarefas)

    @Query("DELETE from tarefas")
    fun deleteAll()

    @Query("DELETE from tarefas WHERE id =:id")
    fun deleteById(id: Int)

}