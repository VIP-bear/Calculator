package com.bear.calculator.util;

/*
定义了用于计算的一些方法
 */

public class Calculate {

    // 两个数之间的运算
    public static double twoDigitOperation(double num1, double num2, String op){
        switch (op){
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                return num1 * num2;
            case "÷":
                return num1 / num2;
            default:
                return 0;
        }
    }

    // 一个数的运算
    public static double oneDigitOperation(double num1, String op){
        switch (op){
            case "%":
                return num1 / 100.0;
            default:
                return 0;
        }
    }

}
