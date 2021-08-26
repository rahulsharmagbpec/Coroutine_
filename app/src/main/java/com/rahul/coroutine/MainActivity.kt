package com.rahul.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button: Button = findViewById<Button>(R.id.button_1)
        button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest()
            }
        }
        text = findViewById(R.id.text)
    }

    private fun setNewText(input: String) {
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        //switch context of existing coroutine || can be called inside coroutine only.
        withContext(Dispatchers.Main) {
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        val result = getResultOneFromApi()
        setTextOnMainThread(result)

        val result2 = getResultTwoFromApi()
        setTextOnMainThread(result2)
    }

    private suspend fun getResultOneFromApi(): String {
        logThread("getResultOneFromApi")
        delay(1000) //cooroutine delay to delay coroutine not like Thread.sleep(1000)
        return "Result#1"
    }

    private suspend fun getResultTwoFromApi(): String {
        logThread("getResultTwoFromApi")
        delay(1000) //cooroutine delay to delay coroutine not like Thread.sleep(1000)
        return "Result#2"
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName: ${Thread.currentThread().name}")
    }
}