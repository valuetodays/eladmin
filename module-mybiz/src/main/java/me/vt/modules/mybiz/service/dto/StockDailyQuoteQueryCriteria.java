package me.vt.modules.mybiz.service.dto;

import com.vt.quarkus.commons.base.PageIO;
import com.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
* @author valuetodays
* @since 2025-11-17 19:01
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class StockDailyQuoteQueryCriteria extends PageIO implements QuerySearchable {

    /** 精确 */
    @Schema(description = "编号")
    private String code;

    /** 精确 */
    @Schema(description = "统计日期")
    private LocalDate statDate;


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();

        return querySearches;
    }
}
