package musicopedia.dto.response;

import lombok.Data;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ArtistResponseDTO {

    private UUID artistId;
    private String spotifyId;
    private String artistName;
    private String description;
    private String image;
    private ArtistType type;
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
