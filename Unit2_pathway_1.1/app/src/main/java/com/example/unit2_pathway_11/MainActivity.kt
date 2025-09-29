package com.example.unit2_pathway_11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent()
        {
            ManHinhChinh()
        }
    }
}

@Composable
fun ManHinhChinh()
{
    val a = 7
    val b = 3

    val ketQua1 = if (a > b) "a lon hon b" else "a khong lon hon b"

    val ketQua2 = if (a < b)
    {
        "a nho hon b"
    }
    else if (a == b)
    {
        "a bang b"
    }
    else
    {
        "a lon hon b"
    }

    val so = 2
    val ketQua3 = when (so)
    {
        1 -> "mot"
        2 -> "hai"
        3 -> "ba"
        else -> "khac"
    }

    val x: Any = "Xin chao"
    val ketQua4 = when (x)
    {
        is Int -> "x la so nguyen"
        is String -> "x la chuoi"
        else -> "khong biet"
    }

    val ketQua5 = when (a)
    {
        in 1..5 -> "a nam trong 1..5"
        in 6..10 -> "a nam trong 6..10"
        else -> "a ngoai khoang"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = "if else: $ketQua1", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "else if: $ketQua2", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "when so: $ketQua3", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "when is: $ketQua4", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "when range: $ketQua5", fontSize = 20.sp)
    }
}