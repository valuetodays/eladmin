package me.vt;

import cn.vt.exception.CommonException;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.SingleLineFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-06-17
 */
public class MySingleLineFormat extends SingleLineFormat {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        String newUrl = StringUtils.substringBefore(url, "?") + "?...";
//        if (sql.contains(",122,122,122,")) {
//            throw new CommonException("show error");
//        }
        return now + "|" + elapsed + "|" + category + "|connection " + connectionId + "|url " + newUrl + "|" + "" + "|" + P6Util.singleLine(sql);
    }
}
