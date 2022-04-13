package com.example.dentaku

import android.widget.EditText
import java.util.*

/**
 * 計算クラス
 * 逆ポーランド記法（RPN）を利用する
 * @param text formula 通常の計算式
 */
class Calc(text:String) {

    val formula:String = text //通常の計算式 1 + 2
    //var rpn_formula:String //RPNでの式 1 2 +
    var rpn_result:Int //RPNでの計算解

    init{
        //rpn_formula = getRpnFormula()
        rpn_result = rpnCalc()
    }

    /**
     * RPN式へ変換
     * @return String
     */
    private fun getRpnFormula(): String {

        println("formula = $formula")

        val sequenceList: CharArray = formula.toCharArray()
        val resultBuilder = StringBuilder()
        val stack: Deque<Char> = ArrayDeque()
        var calcFlag = false

        for (token:Char in sequenceList){
            when (token) {
                '+' , '-' -> {
                    resultBuilder.append(' ')//四則演算の値が来たところで桁を確定させる
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
                    if (calcFlag){//四則演算のあとに空白を追加する
                        resultBuilder.append(' ')
                        calcFlag = false
                    }
                }
                '*', '/', '(' -> {
                    resultBuilder.append(' ')
                    stack.addFirst(token)
                    calcFlag = true
                }
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
        //スタックしたものを追加する
        println(stack)
        while (!stack.isEmpty()) {
            resultBuilder.append(' ')
            resultBuilder.append(stack.removeFirst())
        }
        println("RPNformula = $resultBuilder")
        return resultBuilder.toString()
    }

    /**
     * RPN式を計算して返す
     * @return Int
     */
    fun rpnCalc(): Int {
        var data = getRpnFormula().split(' ') //RPN式を分解して配列に
        val stack: Deque<Int> = ArrayDeque() //スタックしておく場所

        data.forEach{
            if(it.toIntOrNull() != null){
                stack.add(it.toInt())
            }else {
                val val1 = stack.removeLast()
                val val2 = stack.removeLast()
                when(it){
                    "+"-> stack.add(val1 + val2)
                    "-"-> stack.add(val1 - val2)
                    "*"-> stack.add(val1 * val2)
                    "/"-> stack.add(val2 / val1)
                }
            }
        }
        return stack.first
    }

}