package employees.csv;

import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public enum SupportedDateTypes {
    ISO_DATE("iso date", DateTimeFormatter.ISO_DATE),
    BASIC_ISO_DATE("basic iso date", DateTimeFormatter.BASIC_ISO_DATE),
    ISO_LOCAL_DATE("iso local date", DateTimeFormatter.ISO_LOCAL_DATE),
    ISO_OFFSET_DATE("iso offset date", DateTimeFormatter.ISO_OFFSET_DATE),
    ISO_ORDINAL_DATE("iso ordinal date", DateTimeFormatter.ISO_ORDINAL_DATE),
    ISO_WEEK_DATE("iso week date", DateTimeFormatter.ISO_WEEK_DATE);

    private final String userFriendlyName;
    private final DateTimeFormatter dateTimeFormatter;

    SupportedDateTypes(String userFriendlyName, DateTimeFormatter dateTimeFormatter) {
        this.userFriendlyName = userFriendlyName;
        this.dateTimeFormatter = dateTimeFormatter;
    }
}
