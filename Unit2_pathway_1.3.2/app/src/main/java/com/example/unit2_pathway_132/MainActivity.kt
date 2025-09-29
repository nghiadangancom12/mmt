package com.example.unit2_pathway_132

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var number: Int? = 10
        Log.d("NullableDemo", number.toString())

        number = null
        Log.d("NullableDemo", number.toString())
    }
}