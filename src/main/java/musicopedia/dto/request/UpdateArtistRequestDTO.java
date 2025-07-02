package musicopedia.dto.request;

import lombok.Data;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;

import java.time.LocalDate;

@Data
public class UpdateArtistRequestDTO {

    private String artistName;
    private ArtistType type;
    private String spotifyId;

    private String description;
    private String image;
    private String primaryLanguage;
    private String genre;
    private String originCountry;

    // Solo-specific fields
    private LocalDate birthDate;
    private LocalDate deathDate;
    private ArtistGender soloGender;

    // Group-specific fields
    private LocalDate formationDate;
    private LocalDate disbandDate;
    private ArtistGender groupGender;
}
