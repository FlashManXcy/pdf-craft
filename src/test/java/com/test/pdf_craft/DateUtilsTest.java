package com.test.pdf_craft;

import com.test.pdf_craft.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilsTest {
    @Test
    void testSplitDateRange() {
        List<DateUtils.DateRange> ranges = DateUtils.splitDateRange("04-03-2021", "11-08-2021");
        
        assertThat(ranges).hasSize(6);
        assertThat(ranges.get(0).getStartDate()).isEqualTo("04-03-2021");
        assertThat(ranges.get(0).getEndDate()).isEqualTo("31-03-2021");
        assertThat(ranges.get(5).getEndDate()).isEqualTo("11-08-2021");
    }

    @Test
    void testFebruaryLeapYear() {
        List<DateUtils.DateRange> ranges = DateUtils.splitDateRange("28-02-2020", "01-03-2020");
        assertThat(ranges).hasSize(2);
        assertThat(ranges.get(0).getEndDate()).isEqualTo("29-02-2020"); // 闰年
        assertThat(ranges.get(1).getEndDate()).isEqualTo("01-03-2020");
    }
}