package me.zhengjie.modules.security.req;

import cn.valuetodays.quarkus.commons.base.Operator;
import cn.valuetodays.quarkus.commons.base.PageIO;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.QuerySearchable;
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
