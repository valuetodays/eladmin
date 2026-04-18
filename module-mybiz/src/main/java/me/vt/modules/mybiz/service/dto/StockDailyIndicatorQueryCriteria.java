package me.vt.modules.mybiz.service.dto;

import com.vt.quarkus.commons.base.PageIO;
import com.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;

import java.util.ArrayList;
import java.util.List;

/**
* @author valuetodays
* @since 2025-11-18 20:01
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class StockDailyIndicatorQueryCriteria extends PageIO implements QuerySearchable {


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();

        return querySearches;
    }
}
