package me.vt.modules.system.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

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
