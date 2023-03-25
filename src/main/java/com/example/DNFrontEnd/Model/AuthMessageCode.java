package com.example.DNFrontEnd.Model;

public enum AuthMessageCode implements IMessageCode {
    UNKNOWN("1111.0.Unknown", "Chưa có thông điệp cho trường hợp này", "Chưa có thông điệp cho trường hợp này"),
    AUTH_1_1("auth.1.1","Login successfully","Đăng nhập thành công"),
    AUTH_2_1("auth.2.1","Logout successfully","Đăng xuất thành công"),
    AUTH_3_1("auth.3.1","Register successfully","Đăng Ký thành công"),
    AUTH_1_0("auth.1.0","Email hoặc mật khẩu không đúng","Đăng nhập thất bại"),
    AUTH_3_0_OTP("auth.3.0.OTP","Your OTP code is invalid. Please try again!","OTP không chính xác"),
    AUTH_3_1_OTP("auth.3.1.OTP","Send OTP successfully","Gửi OTP thành công"),
    AUTH_4_1("auth.4.1","Change password successfully","Đổi mật khẩu thành công"),
    AUTH_5_0_NOT_FOUND("auth.5.0.NOT.FOUND","Your account has been deleted","Tài khoản đã bị xóa"),
    AUTH_5_0_EXIST("auth.5.0.EXIST","Your account has been exist","Tài khoản đã tồn tại"),
    AUTH_5_0_INACTIVE("auth.5.0.INACTIVE","Your account is not available","Tài khoản đã bị vô hiệu hóa"),
    ;
    private String code;
    private String message;
    private String instance;

    AuthMessageCode(String code, String message, String instance) {
        this.code = code;
        this.message = message;
        this.instance = instance;
    }

//    public static AuthMessageCode from(int type) {
//        for (AuthMessageCode _type : AuthMessageCode.values()) {
//            if (_type.getType() == type) {
//                return _type;
//            }
//        }
//
//        return null;
//    }

    public static AuthMessageCode from(String code) {
        for (AuthMessageCode _type : AuthMessageCode.values()) {
            if (_type.getCode().equals(code)) {
                return _type;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}