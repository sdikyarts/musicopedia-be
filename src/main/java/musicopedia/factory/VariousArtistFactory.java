package musicopedia.factory;

import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating VARIOUS artists.
 * Handles validation and creation logic for compilation albums,
 * various artists compilations, or other miscellaneous artist types.
 */
@Component
public class VariousArtistFactory extends AbstractArtistFactory {

    @Override
    protected ArtistType getArtistType() {
        return ArtistType.VARIOUS;
    }

    @Override
    public void validateArtistData(ArtistRequestDTO dto) {
        if (dto.getArtistName() == null || dto.getArtistName().trim().isEmpty()) {
            throw new IllegalArgumentException("Various artist compilation name cannot be empty");
        }
        
        // Various artists specific validations
        if (dto.getArtistName().length() > 300) {
            throw new IllegalArgumentException("Various artist compilation name cannot exceed 300 characters");
        }
        
        // Various artists should have comprehensive descriptions
        if (dto.getDescription() == null || dto.getDescription().length() < 30) {
            throw new IllegalArgumentException("Various artist compilations require description (minimum 30 characters) to explain the collection");
        }
        
        // Genre might be mixed for various artists, but should be specified
        if (dto.getGenre() == null || dto.getGenre().trim().isEmpty()) {
            throw new IllegalArgumentException("Genre classification is required for various artist compilations");
        }
    }
}
