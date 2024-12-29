package com.example.myapplication.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Model.CategoryModel
import com.example.myapplication.Model.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepository {
    private val firebaseDatabase=FirebaseDatabase.getInstance()

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        val listData=MutableLiveData<MutableList<CategoryModel>>()
        val ref=firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(CategoryModel::class.java)
                    item?.let { lists.add(it) }
                }
                listData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
            }
        })
        return listData
    }

    fun loadPopular(): LiveData<MutableList<FoodModel>> {
        val listData=MutableLiveData<MutableList<FoodModel>>()
        val ref=firebaseDatabase.getReference("Items")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val lists = mutableListOf<FoodModel>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(FoodModel::class.java)
                    item?.let { lists.add(it) }
                }
                listData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
            }
        })
        return listData
    }

    fun loadFoodByCategory(categoryId: String): LiveData<MutableList<FoodModel>> {
        val listData = MutableLiveData<MutableList<FoodModel>>()
        val ref = firebaseDatabase.getReference("All")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists = mutableListOf<FoodModel>()
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(FoodModel::class.java)
                        item?.let { lists.add(it) }
                    }
                    listData.value = lists
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })
        return listData
    }

}