package math.server.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomMathExpression {

    public static void main(String[] args) {
        // Bước 1: Tạo mảng chứa 5 số ngẫu nhiên từ 2 - 10
        for (int k = 0; k< 10; ++k) {
            Random rand = new Random();
            ArrayList<Integer> numbers = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                int randomNum = rand.nextInt(9) + 2; // Số ngẫu nhiên từ 2 đến 10
                numbers.add(randomNum);
            }

            System.out.println("5 số ngẫu nhiên: " + numbers);

            // Bước 2: Lấy ngẫu nhiên 3 số từ 5 số
            Collections.shuffle(numbers); // Trộn mảng
            int num1 = numbers.get(0);
            int num2 = numbers.get(1);
            int num3 = numbers.get(2);

            System.out.println("3 số được chọn: " + num1 + ", " + num2 + ", " + num3);

            // Bước 3: Chọn ngẫu nhiên 2 loại phép toán từ +, -, *, /
            char[] operations = {'*', '/', '+', '-'};
            boolean validOperation = false;
            int result = 0;
            String expression = "";

            while (!validOperation) {
                char operation1 = operations[rand.nextInt(4)];
                char operation2 = operations[rand.nextInt(4)];

                // Bước 4: Tạo phép tính
                boolean isValidDivision = true;
                int intermediateResult = 0;

                // Thực hiện phép tính đầu tiên
                switch (operation1) {
                    case '+':
                        intermediateResult = num1 + num2;
                        break;
                    case '-':
                        intermediateResult = num1 - num2;
                        break;
                    case '*':
                        intermediateResult = num1 * num2;
                        break;
                    case '/':
                        if (num2 != 0 && num1 % num2 == 0) {
                            intermediateResult = num1 / num2;
                        } else {
                            isValidDivision = false;
                        }
                        break;
                }

                // Nếu phép chia không hợp lệ thì chọn lại
                if (!isValidDivision) {
                    continue;
                }

                // Thực hiện phép tính thứ hai với intermediateResult và num3
                switch (operation2) {
                    case '+':
                        result = intermediateResult + num3;
                        validOperation = true;
                        expression = "(" + num1 + " " + operation1 + " " + num2 + ") " + operation2 + " " + num3;
                        break;
                    case '-':
                        result = intermediateResult - num3;
                        validOperation = true;
                        expression = "(" + num1 + " " + operation1 + " " + num2 + ") " + operation2 + " " + num3;
                        break;
                    case '*':
                        result = intermediateResult * num3;
                        validOperation = true;
                        expression = "(" + num1 + " " + operation1 + " " + num2 + ") " + operation2 + " " + num3;
                        break;
                    case '/':
                        if (num3 != 0 && intermediateResult % num3 == 0) {
                            result = intermediateResult / num3;
                            validOperation = true;
                            expression = "(" + num1 + " " + operation1 + " " + num2 + ") " + operation2 + " " + num3;
                        }
                        break;
                }
            }

            // In ra phép tính và kết quả
            System.out.println("Phép tính: " + expression + " = " + result);
        }
    }
}
