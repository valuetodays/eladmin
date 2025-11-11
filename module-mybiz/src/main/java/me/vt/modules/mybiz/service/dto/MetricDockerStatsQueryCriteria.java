package me.vt.modules.mybiz.service.dto;

import java.util.ArrayList;
import java.util.List;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;

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
