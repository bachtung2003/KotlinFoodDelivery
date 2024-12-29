package com.example.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.myapplication.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            IntroScreenPreview(onGetStartedClick = {
                startActivity(Intent(this,MainActivity::class.java))
            })
        }
    }
}

@Composable
fun IntroScreenPreview(onGetStartedClick: () -> Unit){
    IntroScreen(onGetStartedClick=onGetStartedClick
    )
}

@Composable
fun IntroScreen(onGetStartedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ConstraintLayout (
            modifier = Modifier
                .background(Color.White)
        ){
            val (backgroundImg, logoImg, titleTxt, subtitileTxt, buttonBox)=createRefs()
            Image(
                painter = painterResource(id= R.drawable.background_intro),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(backgroundImg){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxSize()
                    .height(700.dp),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text="KINGPIE",
                fontSize= 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .padding(top=110.dp)
                    .constrainAs(titleTxt){
                        bottom.linkTo(logoImg.top, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Image(
                painter = painterResource(id= R.drawable.foodlogo),
                contentDescription = null,
                modifier = Modifier
                        .run{
                    constrainAs(logoImg){
                        top.linkTo(backgroundImg.top)
                        bottom.linkTo(backgroundImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                        .width(350.dp)
                        .height(350.dp)
                }, contentScale = ContentScale.Fit
            )
            Text(
                text="Bite Into Bliss",
                fontSize= 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .padding(top=40.dp)
                    .constrainAs(subtitileTxt){
                        top.linkTo(logoImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            GetStartedButton(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .padding(top=48.dp)
                    .constrainAs(buttonBox){
                        top.linkTo(subtitileTxt.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun GetStartedButton(onClick:()->Unit,modifier: Modifier=Modifier){
    Button(onClick=onClick
    ,colors = ButtonDefaults.buttonColors(
        containerColor = colorResource(R.color.white)
    ),
    shape = RoundedCornerShape(50.dp),
    modifier = modifier
        .fillMaxWidth(0.7f)
        .height(50.dp)
    ){
        Text(
            text="Get Started",
            fontSize = 22.sp,
            color = Color.Black,
        )
    }
}
