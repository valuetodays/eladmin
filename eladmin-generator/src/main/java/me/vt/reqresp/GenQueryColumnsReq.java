package me.vt.reqresp;

import java.io.Serializable;
import ll.vt.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-08-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GenQueryColumnsReq extends PageIO implements Serializable {
    private String tableName;
}
