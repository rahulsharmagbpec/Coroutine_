package com.rahul.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var text: TextView
    lateinit var progress: ProgressBar
    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000

    private lateinit var job: CompletableJob
    //Job vs CompletableJob
    //interface CompletableJob extends Job
    //mean CompletableJob have more features like Complete and CompleteExceptionally

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById<Button>(R.id.job_button)
        text = findViewById<TextView>(R.id.jobTextComplete)
        progress = findViewById<ProgressBar>(R.id.progressbar)
        button.setOnClickListener {
            if (!::job.isInitialized) {
                initJob()
            }
            progress.startJobOrCancel(job)
        }
    }

    fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            println("debug $job is already active, cancelling...")
            resetJob()
        } else {
            button.text = "cancel job #1"

            //new coroutine context
            CoroutineScope(Dispatchers.IO + job).launch {
                println("debug coroutine ${this} is activated with job ${job}")
                for(i in PROGRESS_START.. PROGRESS_MAX){
                    delay((JOB_TIME/PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete")
            }
        }
    }

    fun updateJobCompleteTextView(str:String){
        GlobalScope.launch(Dispatchers.Main ) {
            text.text = str
        }
    }

    fun resetJob() {
        if(job.isActive || job.isCompleted){
            job.cancel(CancellationException("resetting job"))
        }

        //because once job is cancelled we cannot new one, so creating new job
        initJob()
    }

    fun initJob() {
        button.text = "start job #1"
        updateJobCompleteTextView("")
        job = Job()

        //whether job is completed or exception to update the user
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "unknown error"
                }
                println("debug $job was cancelled. Reason: $msg")
                showToast(msg)
            }
        }
        progress.max = PROGRESS_MAX
        progress.progress = PROGRESS_START
    }

    fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
        }

    }
}