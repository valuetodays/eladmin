package me.zhengjie.exception;

import lombok.Getter;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 * 统一异常处理
 */
@Getter
public class BadRequestException extends RuntimeException{

    private Integer status = 400;

    public BadRequestException(String msg){
        super(msg);
    }

    public BadRequestException(int status, String msg) {
        super(msg);
        this.status = status;
    }
}
