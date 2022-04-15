package com.example.dentaku

import android.widget.EditText
import java.math.BigDecimal
import java.util.*

/**
 * 計算クラス
 * 逆ポーランド記法（RPN）を利用する
 * @param formula 通常の計算式 例) 1 + 1
 */
class Calc(val formula:String ) {

    val rpn_formula:String = getRpnFormula() //RPNでの式 1 2 +
    val rpn_result:String  = rpnCalc()//RPNでの計算解

    companion object {//定数を定義する
        const val SPACE:Char = ' '//
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
                    resultBuilder.append(SPACE)//四則演算の値が来たところで桁を確定させる
                    while (!stack.isEmpty()) {// スタックされた演算子の優先順位より低い場合は、スタックの演算子をバッファへ
                        when(stack.first){
                            '*','/' -> resultBuilder.append(stack.removeFirst())
                            else -> break
                        }
                    }
                    stack.addFirst(token)
                    if (calcFlag){//四則演算のあとに空白を追加する
                        resultBuilder.append(SPACE)
                        calcFlag = false
                    }
                }
                '*', '/' -> {
                    resultBuilder.append(SPACE)
                    stack.addFirst(token)
                    calcFlag = true
                }
                '('->{
                    stack.addFirst(token)
                }
                ')' -> {
                    while (stack.isNotEmpty()) {
                        if(stack.first != '(') {
                            resultBuilder.append(SPACE)
                            resultBuilder.append(stack.removeFirst())
                        }else{
                            stack.remove('(')
                            break
                        }
                    }
                }
                ','-> {}//「,」は外す
                // 数値の場合
                else -> resultBuilder.append(token)
            }
        }
        //スタックしたものを追加する
        while (!stack.isEmpty()) {
            resultBuilder.append(SPACE)
            resultBuilder.append(stack.removeFirst())
        }
        return resultBuilder.toString()
    }

    /**
     * RPN式を計算して返す
     * @return Double
     */
    fun rpnCalc(): String {
        var data = getRpnFormula().split(SPACE) //RPN式を分解して配列に
        val stack: Deque<Double> = ArrayDeque() //スタックしておく場所
        data.forEach{
            if(it.toDoubleOrNull() != null){
                stack.add(it.toDouble())
            }else if(it.isNotEmpty()) {
                val val1:Double
                val val2:Double
                if (!stack.isEmpty()){
                    val1 = stack.removeLast()
                }else return "ERROR"
                if (!stack.isEmpty()){
                    val2 = stack.removeLast()
                }else return "ERROR"
                when(it){
                    "+"-> stack.add(val2 + val1)
                    "-"-> stack.add(val2 - val1)
                    "*"-> stack.add(val2 * val1)
                    "/"-> stack.add(val2 / val1)
                    else-> return "ERROR"
                }
            }
        }
        if(stack.isNotEmpty()) {
            return BigDecimal("%.5f".format(stack.first)).stripTrailingZeros().toPlainString()//下5桁まで 不要な0は削除
        }
        return "ERROR"
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