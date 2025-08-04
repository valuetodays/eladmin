package me.zhengjie.reqresp;

import cn.valuetodays.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-08-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GenQueryTablesReq extends PageIO implements Serializable {
    private String name;
}
