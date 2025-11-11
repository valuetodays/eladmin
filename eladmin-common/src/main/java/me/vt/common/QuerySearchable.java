package me.vt.common;

import java.util.List;
import ll.vt.quarkus.commons.base.QuerySearch;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
public interface QuerySearchable {
    List<QuerySearch> toQuerySearches();
}
