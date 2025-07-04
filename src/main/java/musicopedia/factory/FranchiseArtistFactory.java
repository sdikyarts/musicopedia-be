package musicopedia.factory;

import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating FRANCHISE artists.
 * Handles validation and creation logic specific to franchise artists
 * (like virtual idols, fictional characters, or brand-based artists).
 */
@Component
public class FranchiseArtistFactory extends AbstractArtistFactory {

    @Override
    protected ArtistType getArtistType() {
        return ArtistType.FRANCHISE;
    }

    @Override
    public void validateArtistData(ArtistRequestDTO dto) {
        if (dto.getArtistName() == null || dto.getArtistName().trim().isEmpty()) {
            throw new IllegalArgumentException("Franchise artist name cannot be empty");
        }
        
        // Franchise specific validations
        if (dto.getArtistName().length() > 200) {
            throw new IllegalArgumentException("Franchise artist name cannot exceed 200 characters");
        }
        
        // Franchise artists must have detailed descriptions explaining the concept
        if (dto.getDescription() == null || dto.getDescription().length() < 50) {
            throw new IllegalArgumentException("Franchise artists require detailed description (minimum 50 characters)");
        }
        
        // Origin country is crucial for franchise artists to understand the market
        if (dto.getOriginCountry() == null || dto.getOriginCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Origin country is required for franchise artists");
        }
    }
}
