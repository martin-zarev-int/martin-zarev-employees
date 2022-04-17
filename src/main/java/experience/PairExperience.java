package experience;

import lombok.Data;

@Data
public class PairExperience {

    private final long firstEmployeeId;
    private final long secondEmployeeId;
    private final long projectId;
    private final long overlappingMonths;
}
