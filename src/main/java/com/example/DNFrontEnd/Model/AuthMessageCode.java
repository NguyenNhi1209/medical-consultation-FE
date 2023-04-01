package com.example.DNFrontEnd.Model;

public enum AuthMessageCode implements IMessageCode {
    UNKNOWN("UNKNOWN", "Chưa có thông điệp cho trường hợp này", "Chưa có thông điệp cho trường hợp này"),
    AUTH_1_1("AUTH_1_1","Đăng nhập thành công","Đăng nhập thành công"),
    AUTH_2_1("AUTH_2_1","Đăng xuất thành công","Đăng xuất thành công"),
    AUTH_3_1("AUTH_3_1","Đăng Ký thành công","Đăng Ký thành công"),
    AUTH_1_0("AUTH_1_0","Email hoặc mật khẩu không chính xác","Đăng nhập thất bại"),
    AUTH_3_0_OTP("AUTH_3_0_OTP","YOTP không chính xác. Vui lòng thử lại!","OTP không chính xác"),
    AUTH_3_0_OTP_EXPIRED("AUTH_3_0_OTP_EXPIRED","OTP đã hết hạn. Vui lòng thử lại!","OTP hết hạn"),
    AUTH_3_1_OTP("AUTH_3_1_OTP","OTP chính xác","OTP chính xác"),
    AUTH_3_1_SEND_OTP("AUTH_3_1_SEND_OTP","Gửi OTP thành công","Gửi OTP thành công"),
    AUTH_4_1("AUTH_4_1","Đổi mật khẩu thành công. Vui lòng đăng nhập lại!","Đổi mật khẩu thành công"),
    AUTH_5_0_NOT_FOUND("AUTH_5_0_NOT_FOUND","Không tìm thấy tài khoản","Tài khoản không tìm thấy"),
    AUTH_5_0_NOT_EXIST("AUTH_5_0_NOT_EXIST","Tài khoản không tồn tại","Tài khoản đã bị xóa"),
    AUTH_5_0_EXIST("AUTH_5_0_EXIST","Tài khoản đã tồn tại","Tài khoản đã tồn tại"),
    AUTH_5_0_INACTIVE("AUTH_5_0_INACTIVE","Tài khoản của bạn chưa được xác thực. Vui lòng xác thực tài khoản!","Tài khoản đã bị vô hiệu hóa"),
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