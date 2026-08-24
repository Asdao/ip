package duke;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Parses the date and time formats accepted by Furina. */
class DateTimeParser {
    private static final DateTimeFormatter ISO_DATE = new DateTimeFormatterBuilder()
            .appendPattern("uuuu-MM-dd")
            .optionalStart().appendPattern(" HHmm").optionalEnd()
            .optionalStart().appendPattern(" HH:mm").optionalEnd()
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter DAY_MONTH_DATE = new DateTimeFormatterBuilder()
            .appendPattern("d/M/uuuu")
            .optionalStart().appendPattern(" HHmm").optionalEnd()
            .optionalStart().appendPattern(" HH:mm").optionalEnd()
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    /** Parses ISO or day/month dates, with an optional time. */
    static LocalDateTime parse(String text) {
        String trimmed = text.trim();
        return parseDateTimeOrDate(trimmed, ISO_DATE, DAY_MONTH_DATE);
    }

    private static LocalDateTime parseDateTimeOrDate(String text,
                                                     DateTimeFormatter first,
                                                     DateTimeFormatter second) {
        try {
            return LocalDateTime.parse(text, first);
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(text, second);
            } catch (DateTimeParseException ignoredTime) {
                try {
                    return LocalDate.parse(text, first).atStartOfDay();
                } catch (DateTimeParseException ignoredFirstDate) {
                    return LocalDate.parse(text, second).atStartOfDay();
                }
            }
        }
    }

    /** Formats a parsed deadline for display. */
    static String format(LocalDateTime dateTime) {
        String date = dateTime.format(DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH));
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return date;
        }
        return date + " "
                + dateTime.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH));
    }
}
