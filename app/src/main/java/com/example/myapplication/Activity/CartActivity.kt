package com.example.myapplication.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.myapplication.Helper.ChangeNumberItemsListener
import com.example.myapplication.Helper.ManagmentCart
import com.example.myapplication.Model.FoodModel
import com.example.myapplication.R

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CartScreen(
                ManagmentCart(this),
                onBackClick = {
                    finish()
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CartScreen(
    managementCart: ManagmentCart=ManagmentCart(LocalContext.current),
    onBackClick:()->Unit={}
){
    val cartItems = remember { mutableStateOf(managementCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }
    calculatorCart(managementCart, tax)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "My Cart",
            color = colorResource(R.color.red),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(top = 36.dp)
                .fillMaxWidth()
        )
        if(cartItems.value.isEmpty()){
            Text(
                text = "Cart Is Empty",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }else{
            CartList(cartItems=cartItems.value,managementCart){
                cartItems.value=managementCart.getListCart()
                calculatorCart(managementCart,tax)
            }

            CartSummary(
                itemTotal=managementCart.getTotalFee(),
                tax=tax.value,
                delivery=10.0
            )
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal+tax+delivery
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)){
            Text(
                text = "Delivery",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(text = "$$delivery")
        }
        Box(
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.black))
                .padding(top = 32.dp)
        )
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)){
            Text(
                text = "Total:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(text = "$${"%.2f".format(total)}")
        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.red)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Check Out",
                fontSize = 22.sp,
                color = Color.White
            )
        }
    }

}

@Composable
fun CartList(cartItems: ArrayList<FoodModel>, managementCart: ManagmentCart, onItemChange: () -> Unit) {
    LazyColumn(Modifier.padding(top = 16.dp)) {
        items(cartItems) { item ->
            CartItem(
                cartItems,
                managementCart = managementCart,
                item = item,
                onItemChange = onItemChange
            )
        }
    }
}

@Composable
fun CartItem(cartItems: ArrayList<FoodModel>,item:FoodModel, managementCart: ManagmentCart, onItemChange: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        val (pic, titleTxt, feeEachItem, totalEachItem, Quantity)= createRefs()
        AsyncImage(
            model = (item.picUrl),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .background(
                    colorResource(R.color.grey),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(
            text = item.title,
            modifier = Modifier
                .constrainAs(titleTxt) {
                    start.linkTo(pic.end)
                    top.linkTo(pic.top)
                }
                .padding(start = 8.dp, top = 8.dp)
        )
        Text(
            text = "$${item.price}",
            color = colorResource(R.color.red),
            modifier = Modifier
                .constrainAs(feeEachItem) {
                    start.linkTo(titleTxt.start)
                    top.linkTo(titleTxt.bottom)
                }
                .padding(start = 8.dp, top = 8.dp)
        )
        Text(
            text = "$${"%.2f".format(item.numberInCart*item.price)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalEachItem) {
                    start.linkTo(titleTxt.start)
                    top.linkTo(pic.bottom)
                }
                .padding(start = 8.dp)
        )
        ConstraintLayout(modifier = Modifier
            .width(100.dp)
            .constrainAs(Quantity) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .background(
                colorResource(R.color.grey),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            val(plusCartBtn,minusCartBtn,numberItemTxt) = createRefs()
            Text(
                text = item.numberInCart.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(numberItemTxt){
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                }
            )
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.red),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .constrainAs(plusCartBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.plusItem(
                            cartItems,
                            cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ){
                Text(
                    text = "+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.red),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .constrainAs(minusCartBtn) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.minusItem(
                            cartItems,
                            cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ){
                Text(
                    text = "-",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


fun calculatorCart(managementCart: ManagmentCart,tax:MutableState<Double>){
    val percentTax=0.02
    tax.value = Math.round((managementCart.getTotalFee()*percentTax)*100)/100.0
}