package ma.aza.sgtm.logistics.utils;

import ma.aza.sgtm.logistics.records.DateTimeRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    public static List<LocalDate> getDaysBetween(LocalDate from, LocalDate to) {

        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to dates must not be null");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from date cannot be after to date");
        }

        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = from;

        while (!current.isAfter(to)) {
            dates.add(current);
            current = current.plusDays(1);
        }

        return dates;
    }

    public static List<DateTimeRange> getDayTimeRanges(
            LocalDate from,
            LocalDate to,
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to must not be null");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from cannot be after to");
        }

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime must not be null");
        }

        List<LocalDate> days = getDaysBetween(from, to);
        List<DateTimeRange> ranges = new ArrayList<>();

        for (int i = 0; i < days.size(); i++) {
            LocalDate day = days.get(i);

            LocalDateTime start;
            LocalDateTime end;

            boolean isFirst = i == 0;
            boolean isLast = i == days.size() - 1;

            if (isFirst) {
                // First day: start at provided start time
                start = LocalDateTime.of(day, startTime);
                // End at end of day
                end = LocalDateTime.of(day, LocalTime.of(23, 59, 59));
            } else if (isLast) {
                // Last day: start at midnight
                start = LocalDateTime.of(day, LocalTime.MIDNIGHT);
                // End at provided end time
                end = LocalDateTime.of(day, endTime);
            } else {
                // Middle days: full-day coverage
                start = LocalDateTime.of(day, LocalTime.MIDNIGHT);
                end = LocalDateTime.of(day, LocalTime.of(23, 59, 59));
            }

            ranges.add(new DateTimeRange(start, end));
        }

        return ranges;
    }


}
