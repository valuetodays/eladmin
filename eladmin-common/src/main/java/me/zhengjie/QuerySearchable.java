package me.zhengjie;

import cn.valuetodays.quarkus.commons.base.QuerySearch;

import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
public interface QuerySearchable {
    List<QuerySearch> toQuerySearches();
}
