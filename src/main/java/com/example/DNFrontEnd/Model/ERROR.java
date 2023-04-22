package com.example.DNFrontEnd.Model;

public enum ERROR {
    SYSTEM_ERROR(-99 , "System error , please try again !"),
    SUCCESS(1, "SUCCESS"),
    INVALID_PARAM (2 , "param invalid"),

    BAD_REQUEST(400, "Bad request"),
    INVALID_REQUEST (3 , "request is invalid"),
    IP_BLOCK(3 ,"this ip block" ),

    // code 1xx --> gianh cho user
    INVALID_EMAIL(100 , "email is invalid"),
    USER_NOT_FOUND(101 , "user not found"),
    NOT_FOUND_RESOURCE(404, "not found resource")
    ;


    private int code;
    private String message;

    ERROR(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
