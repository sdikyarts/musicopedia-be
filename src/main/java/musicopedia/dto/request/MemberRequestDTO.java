package musicopedia.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import musicopedia.dto.common.BaseMemberDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberRequestDTO extends BaseMemberDTO {
    // All common fields are inherited from BaseMemberDTO
    // No additional fields needed for this DTO
}
