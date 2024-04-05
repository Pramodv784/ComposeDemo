package com.example.composedemo.utils

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    fun reset()
}