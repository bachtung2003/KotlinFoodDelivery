package com.example.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.CategoryModel
import com.example.myapplication.Model.FoodModel
import com.example.myapplication.Repository.MainRepository

class MainViewModel:ViewModel() {
    private val repository=MainRepository()

    fun loadCategory(): LiveData<MutableList<CategoryModel>>{
        return repository.loadCategory()
    }

    fun loadPopular():LiveData<MutableList<FoodModel>>{
        return repository.loadPopular()
    }

    fun loadFoodByCategory(categoryId: String): LiveData<MutableList<FoodModel>> {
        return repository.loadFoodByCategory(categoryId)
    }
}