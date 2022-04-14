package com.example.dentaku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClicked(view: View){
        var result = findViewById<EditText>(R.id.result)
        val button = findViewById<Button>(view.id)

        when(button.text){
            "=" -> {
                if(result.text != null){
                    val calc = Calc((result.text).toString())
                    println("calc = ${calc.rpn_result}")
                    result.setText(calc.rpn_result)
                    findViewById<TextView>(R.id.debug_area).text = calc.debug()//デバッグ用
                }
            }
            "←" ->{
                result.text = result.text.dropLast(1) as Editable?//1文字戻る
            }
            "C" -> result.text = null
            else -> {
                result.text.append(button.text)
            }
        }

    }


}