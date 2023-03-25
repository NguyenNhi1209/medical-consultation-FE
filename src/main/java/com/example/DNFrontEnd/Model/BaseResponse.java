package com.example.DNFrontEnd.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String messageCode;
    private String message;
    private Object data;
    public BaseResponse(T data){
        this.data = data;
    }
    public BaseResponse(int code, String message, String messageCode){
        this.code = code;
        this.message = message;
        this.messageCode = messageCode;
    }
    public BaseResponse(String messageCode){
        this.messageCode = messageCode;
    }

}
