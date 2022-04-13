package com.example.dentaku

import android.widget.EditText
import java.util.*

/**
 * 計算クラス
 * 逆ポーランド記法（RPN）を利用する
 */
class Calc(text:String) {

    val formula:String = text
    var rpn_result:String

    init{
        rpn_result = getRpn()
    }
    /**
     * RPNへ変換
     */
    fun getRpn(): String {

        val sequenceList: CharArray = formula.toCharArray()
        val resultBuilder = StringBuilder()
        val stack: Deque<Char> = ArrayDeque()

        for (token:Char in sequenceList){
            println("$token")
            when (token) {
                '+' , '-' -> {
                    // スタックされた演算子の優先順位より低い場合は、スタックの演算子をバッファへ
                    while (!stack.isEmpty()) {
                        val c = stack.first
                        if (c == '*' || c == '/') {
                            resultBuilder.append(stack.removeFirst())
                        } else {
                            break
                        }
                    }
                    stack.addFirst(token)
                }
                '*', '/', '(' -> stack.addFirst(token)
                ')' -> {
                    // 「(」から「)」までの演算子をバッファへ
                    val list: List<Any> = Arrays.asList(stack.toTypedArray())
                    val index = list.indexOf('(')
                    val workStack: Deque<Char> = ArrayDeque()
                    var i = 0
                    while (i <= index) {
                        val c = stack.removeFirst()
                        if (c != '(') {
                            workStack.addFirst(c)
                        }
                        i++
                    }
                    while (!workStack.isEmpty()) {
                        resultBuilder.append(workStack.removeFirst())
                    }
                }

                // 数値の場合
                else -> resultBuilder.append(token)
            }
        }
        while (!stack.isEmpty()) {
            resultBuilder.append(stack.removeFirst())
        }
        println(resultBuilder.toString())
        return resultBuilder.toString()
    }



}