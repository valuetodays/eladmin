package me.vt.modules.mybiz.service.ta4j.pojo;

import java.io.Serializable;

import lombok.Data;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-24
 */
@Data
public class KdjContextResp implements Serializable {
    private BarSeries baseBarSeries;
    private Indicator<Num> k;
    private Indicator<Num> d;
    private Indicator<Num> j;
    private int rsvPeriod;
}
