package musicopedia.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import musicopedia.dto.common.BaseArtistDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArtistRequestDTO extends BaseArtistDTO {
    // All common fields are inherited from BaseArtistDTO
    // Add request-specific fields here if needed in the future
}
