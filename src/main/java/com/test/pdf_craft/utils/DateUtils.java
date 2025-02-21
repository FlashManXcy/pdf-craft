package com.test.pdf_craft.utils;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {


    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 后端需要的日期格式
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static List<DateRange> splitDateRange(String fromDate, String toDate) {
        // 转换日期格式
        String formattedFromDate = convertDateFormat(fromDate, INPUT_FORMATTER, OUTPUT_FORMATTER);
        String formattedToDate = convertDateFormat(toDate, INPUT_FORMATTER, OUTPUT_FORMATTER);

        // 解析为 LocalDate 对象
        LocalDate startDate = LocalDate.parse(formattedFromDate, OUTPUT_FORMATTER);
        LocalDate endDate = LocalDate.parse(formattedToDate, OUTPUT_FORMATTER);

        List<DateRange> ranges = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            LocalDate monthEnd = startDate.with(TemporalAdjusters.lastDayOfMonth());
            if (monthEnd.isAfter(endDate)) {
                monthEnd = endDate;
            }
            // 转换为目标格式的字符串
            String formattedStart = startDate.format(OUTPUT_FORMATTER);
            String formattedMonthEnd = monthEnd.format(OUTPUT_FORMATTER);
            ranges.add(new DateRange(formattedStart, formattedMonthEnd));

            startDate = monthEnd.plusDays(1);
        }
        return ranges;
    }

    @Data
    public static class DateRange {
        private String startDate;
        private String endDate;

        public DateRange(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    private static String convertDateFormat(String date, DateTimeFormatter inputFormatter, DateTimeFormatter outputFormatter) {
        LocalDate localDate = LocalDate.parse(date, inputFormatter);
        return localDate.format(outputFormatter);
    }
}