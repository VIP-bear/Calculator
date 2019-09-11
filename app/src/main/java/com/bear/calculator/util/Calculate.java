package com.bear.calculator.util;

/*
定义了用于计算的一些方法
 */

import com.bear.calculator.model.Rate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            case "^":
                double t = Math.pow(num1, num2);
                return Double.parseDouble(String.format("%.10f", t));
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

    // 长度转换计算
    public static String[] lengthConversion(int flag, double input){
        String[] results = new String[4];
        results[flag-1] = String.valueOf(input);
        if (flag == 1){
            results[1] = String.valueOf(input / 100.0);
            results[2] = String.valueOf(input / 100000.0);
            results[3] = String.format("%.10f", input / 2.54);
        }else if (flag == 2){
            results[0] = String.valueOf(input * 100);
            results[2] = String.valueOf(input / 1000.0);
            results[3] = String.format("%.10f", input * 100  / 2.54);
        }else if (flag == 3){
            results[0] = String.valueOf(input * 100000.0);
            results[1] = String.valueOf(input / 1000.0);
            results[3] = String.format("%.10f",input * 100000 / 2.54);
        }else {
            results[0] = String.valueOf(input * 2.54);
            results[1] = String.valueOf(input * 2.54 * 100);
            results[2] = String.valueOf(input * 2.54 * 100000);
        }
        return results;
    }

    // 体积转换计算
    public static String[] volumeConversion(int flag, double input){
        String[] results = new String[4];
        results[flag-1] = String.valueOf(input);
        if (flag == 1){
            results[1] = String.valueOf(input / 1000000);
            results[2] = String.valueOf(input);
            results[3] = String.valueOf(input / 1000.0);
        }else if (flag == 2){
            results[0] = String.valueOf(input * 1000000);
            results[2] = String.valueOf(input / 1000000);
            results[3] = String.valueOf(input * 1000);
        }else if (flag == 3){
            results[0] = String.valueOf(input);
            results[1] = String.valueOf(input * 1000000);
            results[3] = String.valueOf(input * 1000);
        }else {
            results[0] = String.valueOf(input);
            results[1] = String.valueOf(input * 1000000);
            results[2] = String.valueOf(input * 1000);
        }
        return results;
    }

    // 进制转换计算
    public static String[] decimalConversion(int flag, String input){
        String[] results = new String[4];
        try {
            if (flag == 1) {
                results[0] = input;
                results[1] = Integer.toOctalString(Integer.valueOf(input, 2));
                results[2] = Integer.valueOf(input, 2).toString();
                results[3] = Integer.toHexString(Integer.valueOf(input, 2));
            } else if (flag == 2) {
                results[0] = Integer.toBinaryString(Integer.valueOf(input, 8));
                results[1] = input;
                results[2] = Integer.valueOf(input, 8).toString();
                results[3] = Integer.toHexString(Integer.valueOf(input, 8));
            } else if (flag == 3) {
                results[0] = Integer.toBinaryString(Integer.parseInt(input));
                results[1] = Integer.toOctalString(Integer.parseInt(input));
                results[2] = input;
                results[3] = Integer.toHexString(Integer.parseInt(input));
            } else {
                results[0] = Integer.toBinaryString(Integer.valueOf(input, 16));
                results[1] = Integer.toOctalString(Integer.valueOf(input, 16));
                results[2] = Integer.valueOf(input, 16).toString();
                results[3] = input;
            }
        }catch (NumberFormatException e){
            return null;
        }
        return results;
    }

    // 汇率换算
    public static String[] rateConversion(Rate rate, int flag, double money){
        String[] results = new String[4];
        if (flag == 1){
            results[0] = String.valueOf(money) + rate.getTcur();
            results[1] = String.format("%.6f",money / Double.parseDouble(rate.getRate())) + rate.getScur();
        }else if (flag == 2){
            results[0] = String.valueOf(money * Double.parseDouble(rate.getRate())) + rate.getTcur();
            results[1] = String.valueOf(money) + rate.getScur();
        }
        results[2] = rate.getRate();
        results[3] = rate.getUpdate();
        return results;
    }

}
