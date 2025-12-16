package me.vt.modules.mybiz.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-19
 */
@Data
public class Cci14_100DataCriteria implements Serializable {
    private LocalDate statDate;
    private boolean pushMsg = false;
}
