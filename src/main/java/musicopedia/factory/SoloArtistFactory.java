package musicopedia.factory;

import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating SOLO artists.
 * Handles validation and creation logic specific to solo performers.
 */
@Component
public class SoloArtistFactory extends AbstractArtistFactory {

    @Override
    protected ArtistType getArtistType() {
        return ArtistType.SOLO;
    }

    @Override
    public void validateArtistData(ArtistRequestDTO dto) {
        if (dto.getArtistName() == null || dto.getArtistName().trim().isEmpty()) {
            throw new IllegalArgumentException("Solo artist name cannot be empty");
        }
        
        // Solo artist specific validations
        if (dto.getArtistName().length() > 100) {
            throw new IllegalArgumentException("Solo artist name cannot exceed 100 characters");
        }
        
        // For solo artists, we might want to ensure certain fields are present
        if (dto.getPrimaryLanguage() == null || dto.getPrimaryLanguage().trim().isEmpty()) {
            throw new IllegalArgumentException("Primary language is required for solo artists");
        }
    }
}
