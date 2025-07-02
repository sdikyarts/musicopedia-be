package musicopedia.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MemberSummaryDTO {

    private UUID memberId;
    private String fullName;
    private String image;
    private Boolean hasOfficialSoloDebut;
    private String soloArtistName; // Only if they have solo debut
}
