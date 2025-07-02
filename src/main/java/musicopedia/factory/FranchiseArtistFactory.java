package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating FRANCHISE artists.
 * Handles validation and creation logic specific to franchise artists
 * (like virtual idols, fictional characters, or brand-based artists).
 */
@Component
public class FranchiseArtistFactory implements ArtistFactory {

    @Override
    public Artist createArtist(CreateArtistRequestDTO dto) {
        // Note: Validation is done separately via validateArtistData()
        // This method focuses only on object creation
        
        Artist artist = new Artist();
        artist.setArtistName(dto.getArtistName());
        artist.setType(ArtistType.FRANCHISE);
        artist.setSpotifyId(dto.getSpotifyId());
        artist.setDescription(dto.getDescription());
        artist.setImage(dto.getImage());
        artist.setPrimaryLanguage(dto.getPrimaryLanguage());
        artist.setGenre(dto.getGenre());
        artist.setOriginCountry(dto.getOriginCountry());
        
        return artist;
    }

    @Override
    public void validateArtistData(CreateArtistRequestDTO dto) {
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

    @Override
    public boolean supports(CreateArtistRequestDTO dto) {
        return dto.getType() == ArtistType.FRANCHISE;
    }
}
