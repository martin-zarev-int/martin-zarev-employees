package employees.csv;

import lombok.Getter;

@Getter
public enum FieldPositions {
    EMPLOYEE_ID(0),
    PROJECT_ID(1),
    DATE_FROM(2),
    DATE_TO(3);

    private final int position;

    FieldPositions(int position) {
        this.position = position;
    }
}
