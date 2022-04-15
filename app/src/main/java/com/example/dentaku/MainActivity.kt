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
        //setContentView(R.layout.activity_main)
        //setContentView(R.layout.ipad_style)
        setContentView(R.layout.xperia_style)
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
                    //デバッグ用
                    val debug = calc.debug()
                    findViewById<TextView>(R.id.debug_area).text = debug
                    println(debug)
                }
            }
            "←" ->{
                result.text = result.text.dropLast(1) as Editable?//1文字戻る
            }
            "C" -> result.setText("0")
            else -> {
                when(result.text.toString()){
                    "0"->{//まだ何も入力してないとき
                        when(button.text){
                            "-"-> result.text.append(button.text)
                            "+","*","/",")"-> result.setText("0")
                            else-> result.setText(button.text)
                        }
                    }
                    "ERROR"-> result.setText(button.text) //ERROR表示の時は上書き
                    else-> result.text.append(button.text)
                }
            }
        }

    }


}