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

    fun fakeApiRequest(){
        CoroutineScope(IO).launch {
            var executionTime = measureTimeMillis {
                var  result1:Deferred<String> = async {
                    getResultOneFromApi()
                }

                var result2:Deferred<String> = async {
                    getResultTwoFromApi()
                }

                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            println("debug : total time elapsed $executionTime")
        }

    }

    /*fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    println("debug launchijng job1 in thread ${Thread.currentThread().name}")
                    val result1 = getResultOneFromApi()
                    setTextOnMainThread("Got $result1")
                }
                println("debug completed job1 in $time1 ms")
            }

            val job2 = launch {
                val time2 = measureTimeMillis {
                    println("debug launchijng job2 in thread ${Thread.currentThread().name}")
                    val result2 = getResultTwoFromApi()
                    setTextOnMainThread("Got $result2")
                }
                println("debug completed job1 in $time2 ms")
            }
        }
    }*/

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

    suspend fun getResultTwoFromApi(): String {
        delay(1700)
        return "Result #2"
    }
}