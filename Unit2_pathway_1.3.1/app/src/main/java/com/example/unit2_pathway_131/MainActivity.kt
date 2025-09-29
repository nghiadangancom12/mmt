package com.example.unit2_pathway_131

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    fun main() {
        var favoriteActor: String? = "Sandra Oh"

        if (favoriteActor != null) {
            println("The number of characters in your favorite actor's name is ${favoriteActor.length}.")
        } else {
            println("You didn't input a name.")
        }

        favoriteActor = null
        if (favoriteActor != null) {
            println("The number of characters in your favorite actor's name is ${favoriteActor.length}.")
        } else {
            println("You didn't input a name.")
        }
    }

}