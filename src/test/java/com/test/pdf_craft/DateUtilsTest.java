package com.test.pdf_craft;

import com.test.pdf_craft.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilsTest {

    @Test
    public void testSplitDateRange() {
        List<DateUtils.DateRange> ranges = DateUtils.splitDateRange("2021-04-03", "2021-11-08");
        assertEquals(8, ranges.size());
        assertEquals("03-04-2021", ranges.get(0).getStartDate());
        assertEquals("30-04-2021", ranges.get(0).getEndDate());
        assertEquals("30-09-2021", ranges.get(5).getEndDate());
    }

    @Test
    public void testFebruaryLeapYear() {
        List<DateUtils.DateRange> ranges = DateUtils.splitDateRange("2020-02-28", "2020-03-01");
        assertEquals(2, ranges.size());
        assertEquals("29-02-2020", ranges.get(0).getEndDate());
        assertEquals("01-03-2020", ranges.get(1).getEndDate());
    }
}