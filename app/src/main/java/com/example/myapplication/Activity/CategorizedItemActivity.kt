package com.example.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.example.myapplication.Model.FoodModel
import com.example.myapplication.R
import com.example.myapplication.ViewModel.MainViewModel

class CategorizedItemActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryId = intent.getStringExtra("categoryId") ?: return

        setContent {
            CategorizedItemScreen(categoryId,
                onFoodClick = {
                    food ->
                val intent = Intent(this,ShowItemActivity::class.java)
                intent.putExtra("object",food)
                startActivity(intent)
            })

        }
    }
}


@Composable
fun CategorizedItemScreen(
    categoryId: String,
    onFoodClick: (FoodModel) -> Unit = {}
) {
    val categoryTitles = mapOf(
        "0" to "Pizza",
        "1" to "Burgers",
        "2" to "Hotdog",
        "3" to "Drink",
        "4" to "Donut"
    )
    val categoryTitle = categoryTitles[categoryId] ?: "Unknown Category"

    val viewModel = MainViewModel()
    val foods = remember { mutableStateListOf<FoodModel>() }
    var showLoading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableStateOf(0) }

    val itemsPerPage = 6
    val paginatedFoods by remember {
        derivedStateOf { foods.chunked(itemsPerPage) }
    }

    LaunchedEffect(categoryId) {
        viewModel.loadFoodByCategory(categoryId).observeForever {
            foods.clear()
            foods.addAll(it)
            showLoading = false
        }
    }

    Scaffold(
        content = { paddingValues ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val (titleRef, gridRef, paginationRef) = createRefs()

                // Title
                Text(
                    text = categoryTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.red),
                    modifier = Modifier
                        .constrainAs(titleRef) {
                            top.linkTo(parent.top, margin = 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(8.dp)
                )

                if (showLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(gridRef) {
                                top.linkTo(titleRef.bottom, margin = 16.dp)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (paginatedFoods.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .constrainAs(gridRef) {
                                    top.linkTo(titleRef.bottom, margin = 16.dp)
                                    bottom.linkTo(paginationRef.top, margin = 8.dp)
                                    height = Dimension.fillToConstraints
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            val currentItems = paginatedFoods[currentPage]
                            items(currentItems) { food ->
                                FoodItem2(
                                    food = food,
                                    onFoodClick
                                )
                            }
                        }

                        // Pagination Controls
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(paginationRef) {
                                    bottom.linkTo(parent.bottom, margin = 16.dp)
                                }
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { if (currentPage > 0) currentPage-- },
                                enabled = currentPage > 0,
                            ) {
                                Text(text = "Previous",)
                            }

                            Text(
                                text = "Page ${currentPage + 1} of ${paginatedFoods.size}",
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Button(
                                onClick = { if (currentPage < paginatedFoods.size - 1) currentPage++ },
                                enabled = currentPage < paginatedFoods.size - 1,
                            ) {
                                Text(text = "Next",)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .constrainAs(gridRef) {
                                    top.linkTo(titleRef.bottom, margin = 16.dp)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No items available")
                        }
                    }
                }
            }
        }
    )
}




@Composable
fun FoodItem2(food: FoodModel, onFoodClick: (FoodModel) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .border(
                3.dp, colorResource(id= R.color.grey),
                shape = RoundedCornerShape(15.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(16.dp)
    ) {
        val(title, image, fee, dollar, addButton) = createRefs()
        Text(
            text = food.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff373b54),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(title){
                    top.linkTo(parent.top, margin=16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 4.dp)
        )
        AsyncImage(
            model = (food.picUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image){
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Text(
            text="%.2f".format(food.price),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff373b54),
            modifier = Modifier.constrainAs(fee){
                top.linkTo(image.bottom)
                start.linkTo(parent.start, margin = 5.dp)
                end.linkTo(parent.end)
            }
        )
        Text(
            text="$",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xffff3d00),
            modifier = Modifier.constrainAs(dollar){
                top.linkTo(image.bottom)
                end.linkTo(fee.start, margin = 3.dp)
                bottom.linkTo(fee.bottom)
            }
        )
        Text(
            text ="+ Add",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .background(color = colorResource(id=R.color.red),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 10.dp, vertical = 3.dp)
                .clickable { onFoodClick(food) }
                .constrainAs(addButton){
                    top.linkTo(fee.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

    }
}


