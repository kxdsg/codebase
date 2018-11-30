package com.argus.encypt;

/**
 * Created by xingding on 18/11/9.
 */
public class PasswordUtil {
    /**
     * 获取带有盐值的密码
     *
     * @param password 密码
     * @param salt     盐值
     * @return 带有盐值的密码
     */
    private static String getPasswordWithSalt(String password, String salt) {
        // 盐值开头
        String strAddStr = "@#";
        // 盐值结尾
        String strAddEnd = "*!";
        StringBuilder sb = new StringBuilder();
        sb.append(password) // 追加密码
                .append(strAddStr) // 追加盐值开头
                .append(salt) // 追加动态盐值
                .append(strAddEnd); // 追加盐值固定结尾
        return sb.toString();
    }

    /**
     * 加密密码
     *
     * @param password 密码
     * @return
     */
    public static String encryptPassword(String password, String account) {
        String passwordWithSalt = getPasswordWithSalt(password, account);
        return MD5.encode(passwordWithSalt).toLowerCase();
    }

}
