package com.example.fintracker_app.services.base

interface CrudService<T> {

    fun getAll(): MutableList<T>

    fun deleteById(id: Int): Boolean
}