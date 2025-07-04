package musicopedia.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import musicopedia.dto.common.BaseArtistDTO;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArtistResponseDTO extends BaseArtistDTO {

    private UUID artistId;
    
    // All other common fields are inherited from BaseArtistDTO
}
