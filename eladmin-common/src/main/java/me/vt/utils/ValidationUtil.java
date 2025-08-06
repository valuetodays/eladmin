package me.vt.utils;

import me.vt.exception.BadRequestException;

import java.util.Objects;

/**
 * 验证工具
 *
 * @author Zheng Jie
 * @since 2018-11-23
 */
public class ValidationUtil {

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if (Objects.isNull(obj)) {
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

  /**
   * 验证是否为邮箱
   */
//  public static boolean isEmail(String email) {
//    return Validator.isEmail(email);
//  }
}
