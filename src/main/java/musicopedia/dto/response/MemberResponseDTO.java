package musicopedia.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MemberResponseDTO {

    private UUID memberId;
    private String fullName;
    private String description;
    private String image;
    private LocalDate birthDate;
    
    // Solo artist information if they have an official solo debut
    private UUID soloArtistId;
    private String soloArtistName;
    private Boolean hasOfficialSoloDebut;
}
