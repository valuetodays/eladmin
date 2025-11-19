package me.vt.db;

import java.io.Serializable;
import lombok.Data;

/**
 * .
 *
 * @author lei.liu
 * @since 2023-05-30
 */
@Data
public class AffectedRowsResp implements Serializable {
    private int successUpdates;

    public static AffectedRowsResp empty() {
        return of(0);
    }

    public static AffectedRowsResp of(int successUpdates) {
        AffectedRowsResp resp = new AffectedRowsResp();
        resp.setSuccessUpdates(successUpdates);
        return resp;
    }

}
