package com.rahul.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        text = findViewById(R.id.text)

        button.setOnClickListener {
            setNewText("Clicked!")
            fakeApiRequest()
        }

    }

    fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            var executionTime = measureTimeMillis {
                var result1 = async {
                    getResultOneFromApi()
                }.await()

                var result2= async {
                    getResultTwoFromApi("sdfs")
                }.await()

            }
            println("debug : total time elapsed $executionTime")
        }

    }

    fun setNewText(input: String) {
        val newText = text.text.toString() + "\ninput"
        text.text = newText
    }

    suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            setNewText(input)
        }
    }

    suspend fun getResultOneFromApi(): String {
        delay(1000)
        return "Result #1"
    }

    suspend fun getResultTwoFromApi(result1: String): String {
        delay(1700)
        if(result1.equals("Result #1")){
            return "Result #2"
        }
        throw CancellationException("job2 cancelled")

    }
}