package com.kk.gateway.auth;

import java.util.*;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> resultList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break; // 假设以空行结束输入

            resultList.add(isValidPassword(line) ? "OK" : "NG");
        }

        // 输出统计结果
        for (String r : resultList) {
            System.out.println(r);
        }
    }

    private static boolean isValidPassword(String password) {
        // 检查长度
        if (password.length() <= 8) {
            return false;
        }

        // 检查字符类型
        int charTypes = 0;
        boolean hasUpperCase = false, hasLowerCase = false, hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            else if (Character.isLowerCase(c)) hasLowerCase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true; // 排除空格和换行符

            if (hasUpperCase && hasLowerCase && hasDigit && hasSpecial) break;
        }

        charTypes += hasUpperCase ? 1 : 0;
        charTypes += hasLowerCase ? 1 : 0;
        charTypes += hasDigit ? 1 : 0;
        charTypes += hasSpecial ? 1 : 0;

        if (charTypes < 3) {
            return false;
        }

        // 检查重复子串
        if (hasRepeatedSubstring(password)) {
            return false;
        }

        return true;
    }

    private static boolean hasRepeatedSubstring(String password) {
        // 使用滑动窗口方法检查是否有重复子串
        for (int len = 3; len <= password.length() / 2; len++) {
            Set<String> substrings = new HashSet<>();
            for (int i = 0; i <= password.length() - len; i++) {
                String substring = password.substring(i, i + len);
                if (!isUniformString(substring) && !substrings.add(substring)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isUniformString(String str) {
        if (str == null || str.isEmpty()) {
            return true; // 或者根据需求返回 false
        }

        char firstChar = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

}