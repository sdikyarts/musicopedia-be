package musicopedia.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class BaseMemberDTO {
    
    private UUID memberId;
    private String memberName;
    private String realName;
    private String description;
    private String image;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private UUID soloArtistId;
    private String nationality;
}
