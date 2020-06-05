package com.saurabhdk.countit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton

class MainActivity : AppCompatActivity() {

    lateinit var countInput: AppCompatEditText
    lateinit var start: AppCompatButton
    lateinit var helpButton: AppCompatImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countInput = findViewById(R.id.countInput)
        start = findViewById(R.id.startButton)
        helpButton = findViewById(R.id.helpButton)

        start.setOnClickListener {
            gotoCounter()
        }

        helpButton.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            with(alertBuilder) {
                setTitle("Count Limit")
                setMessage(getString(R.string.count_limit_help))
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                show()
            }
        }

        countInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    countInput.clearFocus()
                    countInput.hideKeyboard()
                    gotoCounter()
                    return@setOnEditorActionListener true;
                }
                else -> false
            }
        }
    }

    private fun gotoCounter() {
        val count = countInput.text.toString().toIntOrNull() ?: 0
        val intent = Intent(this, CounterActivity::class.java).apply {
            putExtra("COUNT", count)
        }
        startActivity(intent)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}