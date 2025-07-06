package musicopedia.dto.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public abstract class BaseSubunitDTO {
    private UUID mainGroupId;
    private String subunitName;
    private String description;
    private String image;
    private LocalDate formationDate;
    private LocalDate disbandDate;
    private String subunitGender;
    private String activityStatus;
    private String originCountry;
    private UUID groupSubunitId;
}
