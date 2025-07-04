package musicopedia.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import musicopedia.dto.common.BaseMemberDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberResponseDTO extends BaseMemberDTO {
    
    // Response-specific fields only
    private String soloArtistName;
    private Boolean hasOfficialSoloDebut;
}
