package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating SOLO artists.
 * Handles validation and creation logic specific to solo performers.
 */
@Component
public class SoloArtistFactory implements ArtistFactory {

    @Override
    public Artist createArtist(CreateArtistRequestDTO dto) {
        validateArtistData(dto);
        
        Artist artist = new Artist();
        artist.setArtistName(dto.getArtistName());
        artist.setType(ArtistType.SOLO);
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

    @Override
    public boolean supports(CreateArtistRequestDTO dto) {
        return dto.getType() == ArtistType.SOLO;
    }
}
