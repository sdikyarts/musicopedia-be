package musicopedia.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import musicopedia.dto.common.BaseSubunitDTO;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubunitResponseDTO extends BaseSubunitDTO {
    private UUID subunitId;
    private String mainGroupName;
    private String groupSubunitName;
}
