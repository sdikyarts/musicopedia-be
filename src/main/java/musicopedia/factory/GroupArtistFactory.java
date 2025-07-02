package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating GROUP artists.
 * Handles validation and creation logic specific to musical groups.
 */
@Component
public class GroupArtistFactory implements ArtistFactory {

    @Override
    public Artist createArtist(CreateArtistRequestDTO dto) {
        validateArtistData(dto);
        
        Artist artist = new Artist();
        artist.setArtistName(dto.getArtistName());
        artist.setType(ArtistType.GROUP);
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
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        
        // Group specific validations
        if (dto.getArtistName().length() > 150) {
            throw new IllegalArgumentException("Group name cannot exceed 150 characters");
        }
        
        // Groups might have different genre requirements
        if (dto.getGenre() == null || dto.getGenre().trim().isEmpty()) {
            throw new IllegalArgumentException("Genre is required for groups");
        }
        
        // Ensure group description is provided for better context
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required for groups to explain their concept");
        }
    }

    @Override
    public boolean supports(CreateArtistRequestDTO dto) {
        return dto.getType() == ArtistType.GROUP;
    }
}
