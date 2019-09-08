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
            case "sin":
                return Math.sin(num1/180.0*Math.PI);
            case "cos":
                return Math.cos(num1);
            case "tan":
                return Math.tan(num1);
            case "√":
                return Math.sqrt(num1);
            case "³√":
                return Math.pow(num1, 1.0/3);
            case "ln":
                return Math.log(num1);
            case "log":
                return Math.log10(num1);
            default:
                return 0;
        }
    }

}
