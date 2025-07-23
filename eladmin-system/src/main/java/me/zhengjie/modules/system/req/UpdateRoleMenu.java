package me.zhengjie.modules.system.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-23
 */
@Data
public class UpdateRoleMenu implements Serializable {
    @NotNull
    private Long id;
    @NotEmpty
    private List<LongIdBase> menus;
}
