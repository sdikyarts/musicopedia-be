package musicopedia.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MemberRequestDTO {

    // Required for updates, null for creates
    private UUID memberId;
    private String fullName;
    private String description;
    private String image;
    private LocalDate birthDate;
    
    // Optional reference to solo artist if they have an official solo debut
    private UUID soloArtistId;
}
