package musicopedia.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.GroupActivityStatus;
import musicopedia.model.enums.GroupAffiliationStatus;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class BaseArtistDTO {

    private String artistName;
    private ArtistType type;
    private String spotifyId;
    private String description;
    private String image;
    private String primaryLanguage;
    private String genre;
    private String originCountry;

    // Solo-specific fields
    private String realName;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private ArtistGender soloGender;
    private GroupAffiliationStatus groupAffiliationStatus;

    // Group-specific fields
    private LocalDate formationDate;
    private LocalDate disbandDate;
    private ArtistGender groupGender;
    private GroupActivityStatus activityStatus;
}
