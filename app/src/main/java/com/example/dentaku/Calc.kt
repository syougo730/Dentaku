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
    val rpn_formula:String = getRpnFormula() //RPNでの式 1 2 +
    var rpn_result:String //RPNでの計算解

    init{
        rpn_result = rpnCalc()
    }

    /**
     * RPN式へ変換
     * @return String
     */
    private fun getRpnFormula(): String {

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
                ','-> ""//なにもしない
                // 数値の場合
                else -> resultBuilder.append(token)
            }
        }
        //スタックしたものを追加する
        while (!stack.isEmpty()) {
            resultBuilder.append(' ')
            resultBuilder.append(stack.removeFirst())
        }
        return resultBuilder.toString()
    }

    /**
     * RPN式を計算して返す
     * @return Double
     */
    fun rpnCalc(): String {
        var data = getRpnFormula().split(' ') //RPN式を分解して配列に
        val stack: Deque<Double> = ArrayDeque() //スタックしておく場所

        data.forEach{
            if(it.toDoubleOrNull() != null){
                stack.add(it.toDouble())
            }else {
                val val1 = stack.removeLast()
                val val2 = stack.removeLast()
                when(it){
                    "+"-> stack.add(val1 + val2)
                    "-"-> stack.add(val1 - val2)
                    "*"-> stack.add(val1 * val2)
                    "/"-> stack.add(val2 / val1)
                    else-> return "ERROR"
                }
            }
        }
        return "%.2f".format(stack.first)
    }


    fun debug():String{
        var text = "---------------------------\n"
            text += "式： $formula \n"
            text += "RPN式： $rpn_formula \n"
            text += "解： $rpn_result \n"
            text += "---------------------------\n"
        return text
    }
}