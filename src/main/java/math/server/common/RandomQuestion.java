package math.server.common;

import math.server.model.Question;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomQuestion {

    public static Question getRandomQuestion() {
        Random rand = new Random();
        char[] operations = {'*', '/', '+', '-'};
        int[] numbers = new int[5];

        while (true) {
            for (int i = 0; i < 5; i++) {
                int randomNum = rand.nextInt(8) + 2;
                numbers[i] = randomNum;
            }

            int num1 = numbers[0];
            int num2 = numbers[1];
            int num3 = numbers[2];

            char operation1 = operations[rand.nextInt(4)];
            char operation2 = operations[rand.nextInt(4)];

            try {
                String expression = "" + num1 + operation1 + num2 + operation2 + num3;
                int result = evaluateExpression(expression);

                return new Question(Arrays.toString(numbers), String.valueOf(result), expression);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int evaluateExpression(String expression) {
        expression = expression.strip();
        expression = expression.replaceAll("\\s+", ""); // Replace white space

        if (!expression.matches("[0-9+*/-]*")) {
            throw new IllegalArgumentException("Invalid expression");
        }

        char[] expressionChar = expression.toCharArray();

        try {
            if (precedence(expressionChar[3]) && !precedence(expressionChar[1])) {
                int firstNum = expressionChar[0] - '0';
                int secondNum = applyOperation(expressionChar[3], expressionChar[2] - '0', expressionChar[4] - '0');

                return applyOperation(expressionChar[1], firstNum, secondNum);
            } else {
                int firstNum = applyOperation(expressionChar[1], expressionChar[0] - '0', expressionChar[2] - '0');
                int secondNum = expressionChar[4] - '0';

                return applyOperation(expressionChar[3], firstNum, secondNum);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return false;
            case '*':
            case '/':
                return true;
        }

        throw new RuntimeException("Invalid operator");
    }

    private static int applyOperation(char operator, int a, int b) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0 || (a % b != 0)) {
                    throw new IllegalArgumentException("Invalid expression");
                }

                return a / b;
        }

        throw new RuntimeException("Not found operator");
    }

    public static boolean isValidAnswer(String original, String answer) {
        Map<Character, Integer> originalCount = getCountMap(original);
        Map<Character, Integer> answerCount = getCountMap(answer);

        for (Map.Entry<Character, Integer> entry : answerCount.entrySet()) {
            char number = entry.getKey();
            int count = entry.getValue();

            if (!originalCount.containsKey(number) || count > originalCount.get(number)) {
                return false;
            }
        }

        return true;
    }

    private static Map<Character, Integer> getCountMap(String input) {
        Map<Character, Integer> countMap = new HashMap<>();

        for (char character : input.toCharArray()) {
            if (Character.isDigit(character)) {
                countMap.put(character, countMap.getOrDefault(character, 0) + 1);
            }
        }

        return countMap;
    }
}
