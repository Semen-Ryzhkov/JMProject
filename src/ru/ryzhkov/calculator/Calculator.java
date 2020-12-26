package ru.ryzhkov.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    public void calculate() {
        System.out.println("Enter arithmetic operation");
        System.out.println("Enter exit if you want to close the program");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            do {
                input = reader.readLine();
                String operator = findOperator(input);
                int index = input.indexOf(operator);
                String number1 = input.substring(0, index);
                String number2 = input.substring(index + 1);
                if (isNumber(number1) && isNumber(number2)) {
                    int num1 = Integer.parseInt(number1);
                    int num2 = Integer.parseInt(number2);
                    System.out.println(doCalculate(num1, operator, num2));
                } else if (!isNumber(number1) && !isNumber(number2)) {
                    int num1 = romanToArabic(number1);
                    int num2 = romanToArabic(number2);
                    System.out.println(arabicToRoman(doCalculate(num1, operator, num2)));
                } else {
                    throw new IllegalArgumentException("Digits must be only roman or only arabic");
                }
            } while (!input.equals("exit"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int doCalculate(int a, String b, int c) {
        if ((a <= 0) || (a > 10) || (c <= 0) || (c > 10)) {
            throw new IllegalArgumentException("Enter digits in range [1 - 10] or [I - X]");
        }
        return switch (b) {
            case "+" -> a + c;
            case "-" -> a - c;
            case "*" -> a * c;
            case "/" -> a / c;
            default -> throw new IllegalStateException("Unexpected value: " + b);
        };
    }

    private static String findOperator(String input) {
        Pattern pattern = Pattern.compile("[-\\+\\*\\/]");
        Matcher matcher = pattern.matcher(input);
        String operator = null;
        while (matcher.find()) {
            operator = matcher.group();
        }
        if (operator == null) {
            throw new IllegalArgumentException("Enter valid arithmetic operator");
        }
        return operator;
    }

    private static boolean isNumber(String str) {
        boolean isNumber = true;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                isNumber = false;
            }
        }
        return isNumber;
    }

    private static String arabicToRoman(int number) {
        if (number < 0) {
            System.out.println("Roman negative digits are not exist");
        } else if (number == 0) {
            System.out.println("Answer is zero NULL 0 ноль нуль");
        }
        List<RomanDigits> romanNumerals = RomanDigits.toList();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanDigits currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }

    private static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        Pattern p = Pattern.compile("[^XCDVIM]");
        Matcher m = p.matcher(romanNumeral);
        if (m.find()) {
            throw new IllegalArgumentException("Enter valid roman digits");
        }
        int result = 0;

        List<RomanDigits> romanNumerals = RomanDigits.toList();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanDigits symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }
        return result;
    }
}