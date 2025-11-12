package me.vt.modules.mybiz.thirdparty.csindex;

import java.io.Serializable;
import lombok.Data;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-12
 */
@Data
public class CsBaseResp<T extends Serializable> implements Serializable {
    private String code;
    private String msg;
    private Boolean success;
    private T data;
}
