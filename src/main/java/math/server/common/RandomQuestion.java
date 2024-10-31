package math.server.common;

import math.server.model.Question;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

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
                double result = evaluateExpression(expression);
                boolean validExpression = validateInteger(result);

                if (validExpression) {
                    return new Question(Arrays.toString(numbers), String.valueOf(result), expression);
                }
            } catch (Exception e) {
                throw new RuntimeException("Cannot get random question");
            }
        }
    }

    public static boolean validateInteger(double number) {
        return number == Math.floor(number);
    }

    public static double evaluateExpression(String expression) {
        // Loại bỏ khoảng trắng
        expression = expression.replaceAll("\\s+", "");

        if (expression.contains(".")) {
            throw new IllegalArgumentException("Phép tính không hợp lệ");
        }

        // Kiểm tra tính hợp lệ của chuỗi
        if (!expression.matches("[0-9+*/-]*")) {
            throw new IllegalArgumentException("Phép tính không hợp lệ: " + expression);
        }

        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                // Đọc số nguyên
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--; // Giảm chỉ số để tránh tăng thêm
                int number = Integer.parseInt(sb.toString());
                numbers.push(number);
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                // Kiểm tra trước khi thêm toán tử
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(ch);
            }
        }

        // Thực hiện các phép toán còn lại
        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return 0;
    }

    private static int applyOperation(char operator, int b, int a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new IllegalArgumentException("Không thể chia cho 0");
                }
                return a / b;
        }

        throw new RuntimeException("Không thể tính phép tính này");
    }


    public static void main(String[] args) {
       for (int i = 0; i< 6; ++ i) {
           Question question = getRandomQuestion();

           try {
               double result = evaluateExpression(question.getExpression());
               System.out.println("Ket qua: " + question.getExpression() + " : " + result);
           } catch (Exception e) {
               System.out.println(e);
           }
       }
    }
}
