package musicopedia.dto.response;

import lombok.Data;
import musicopedia.model.enums.ArtistType;

import java.util.UUID;

@Data
public class ArtistSummaryDTO {

    private UUID artistId;
    private String artistName;
    private ArtistType type;
    private String image;
    private String genre;
    private String originCountry;
}
