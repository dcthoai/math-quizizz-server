package math.server.common;

import java.util.Random;

public class RandomMathExpression {

    public static void main(String[] args) {
        for(int i = 0; i< 10; ++i ) {
            int targetNumber = generateRandomNumber(2, 100);
            String[] expressions = generateRandomExpressions(targetNumber);

            System.out.println("Target number: " + targetNumber);
            for (String expression : expressions) {
                System.out.println(expression);
            }

            System.out.println("---------");
        }
    }

    // Hàm sinh số ngẫu nhiên trong khoảng [min, max], tránh 0 và 1
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        int number;
        do {
            number = random.nextInt(max - min + 1) + min;
        } while (number == 0 || number == 1);  // Loại bỏ 0 và 1
        return number;
    }

    // Hàm sinh 4 phép tính với 1 phép tính đúng
    public static String[] generateRandomExpressions(int targetNumber) {
        Random random = new Random();
        String[] expressions = new String[4];
        int correctIndex = random.nextInt(4);  // Chọn vị trí cho phép tính đúng

        for (int i = 0; i < 4; i++) {
            if (i == correctIndex) {
                // Phép tính đúng với targetNumber
                expressions[i] = generateCorrectExpression(targetNumber);
            } else {
                // Phép tính sai
                expressions[i] = generateIncorrectExpression();
            }
        }

        return expressions;
    }

    // Hàm sinh phép tính đúng, bao gồm cả *, /
    public static String generateCorrectExpression(int targetNumber) {
        Random random = new Random();
        int a = generateRandomNumber(2, 50);
        int b = generateRandomNumber(2, 10); // Để phép chia dễ cho kết quả nguyên
        int c;

        // Chọn phép toán ngẫu nhiên bao gồm cả *, /
        String[] operators = {"+", "-", "*", "/"};
        String operator1 = operators[random.nextInt(4)];
        String operator2 = operators[random.nextInt(4)];

        // Tạo phép tính với targetNumber
        if (operator1.equals("/") && b != 0 && a % b != 0) {
            a = a - (a % b); // Điều chỉnh a để a / b cho kết quả nguyên
        }
        if (operator2.equals("/") && targetNumber % b != 0) {
            c = targetNumber / b;
        } else {
            c = generateRandomNumber(2, 10);
        }

        return a + " " + operator1 + " " + b + " " + operator2 + " " + c + " TRUE";
    }

    // Hàm sinh phép tính sai với 2 phép toán và 3 số
    public static String generateIncorrectExpression() {
        Random random = new Random();
        int a = generateRandomNumber(2, 50);
        int b = generateRandomNumber(2, 50);
        int c = generateRandomNumber(2, 50);

        // Chọn 2 phép toán ngẫu nhiên từ 4 phép cộng, trừ, nhân, chia
        String[] operators = {"+", "-", "*", "/"};
        String operator1, operator2;

        // Đảm bảo không có 2 dấu giống nhau
        do {
            operator1 = operators[random.nextInt(4)];
            operator2 = operators[random.nextInt(4)];
        } while (operator1.equals(operator2));

        // Đảm bảo phép chia chỉ được thực hiện khi kết quả là số nguyên
        if (operator1.equals("/") && b != 0 && a % b != 0) {
            a = a - (a % b); // Điều chỉnh a để a / b cho kết quả nguyên
        }
        if (operator2.equals("/") && c != 0 && b % c != 0) {
            b = b - (b % c); // Điều chỉnh b để b / c cho kết quả nguyên
        }

        return a + " " + operator1 + " " + b + " " + operator2 + " " + c;
    }
}
