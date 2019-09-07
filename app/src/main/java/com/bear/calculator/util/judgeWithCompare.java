package com.bear.calculator.util;

/*
定义了用于判断和比较的一些方法
 */

import android.widget.Button;

public class judgeWithCompare {

    // 操作符优先级比较
    public static int priority(String op){
        switch (op){
            case "+":
            case "-":
                return 1;
            case "x":
            case "÷":
                return 2;
            case "%":
                return 3;
            case "(":
            case ")":
                return 4;
            default:
                return 0;
        }
    }

    // 判断字符串是否是操作符
    public static boolean isOperator(String op){
        if (op.equals("+") || op.equals("-") || op.equals("x") || op.equals("÷")
                || op.equals("%") || op.equals("(") || op.equals(")")){
            return true;
        }
        return false;
    }

    // 判断%是否是合法输入
    public static String judgePercent(String inputStr){
        int len = inputStr.length();
        if (!inputStr.equals("") && !inputStr.contains("=")) {
            if ((!(inputStr.charAt(len - 1) == ' ') && !(inputStr.charAt(len - 1) == '-'))
                    || (len > 2 && (inputStr.charAt(len - 1) == ' ') && (inputStr.charAt(len - 2) == ')'))) {
                inputStr = inputStr + " " + "%";
                return inputStr;
            }
        }
        return inputStr;
    }

    // 判断式子是否合法
    public static int judgeFormula(String inputStr){
        int left = 0;
        int right = 0;
        for (int i = 0; i < inputStr.length(); i++){
            if (inputStr.charAt(i) == '('){
                left++;
            }else if(inputStr.charAt(i) == ')'){
                right++;
            }
        }
        if (left == right){
            return 0;
        }else if (left > right){
            return 1;
        }else {
            return -1;
        }
    }

    // 判断括号是否是合法输入
    public static String judgeParenthesis(String op, String inputStr){
        boolean flag = true;        // 判断最后一个字符是否为操作符
        flag = inputStr.endsWith("+ ") || inputStr.endsWith("- ") || inputStr.endsWith("x ") || inputStr.endsWith("÷ ");
        if (op.equals("(")){    // 左括号
            if (inputStr.equals("")){
                return op + " ";
            }else if (inputStr.equals("-")){
                return inputStr + " " + op + " ";
            }else if(flag){
                return inputStr + op + " ";
            }
        }else {     // 右括号
            if (!inputStr.equals("") && !flag && (judgeFormula(inputStr) == 1)){
                return inputStr + " " + op;
            }
        }
        return inputStr;
    }


}
