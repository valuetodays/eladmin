package me.vt.modules.mybiz.service.dto;

import cn.valuetodays.quarkus.commons.base.PageIO;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.QuerySearchable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MetricDockerStatsQueryCriteria extends PageIO implements QuerySearchable {


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();

        return querySearches;
    }
}
