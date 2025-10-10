package me.vt.modules.security.req;

import ll.vt.quarkus.commons.base.Operator;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.QuerySearchable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OnlineUserQueryReq extends PageIO implements QuerySearchable {
    private String username;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(username)) {
            querySearches.add(QuerySearch.of("username", username, Operator.LIKE));
        }
        return querySearches;
    }

}
