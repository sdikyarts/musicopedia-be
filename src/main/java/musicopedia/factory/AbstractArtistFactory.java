package musicopedia.factory;

import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;

/**
 * Abstract base class for artist factories.
 * Provides common artist creation logic while allowing subclasses
 * to implement specific validation and type determination.
 */
public abstract class AbstractArtistFactory implements ArtistFactory {
    
    @Override
    public Artist createArtist(ArtistRequestDTO dto) {
        // Note: Validation is done separately via validateArtistData()
        // This method focuses only on object creation
        
        Artist artist = new Artist();
        artist.setArtistName(dto.getArtistName());
        artist.setType(getArtistType());
        artist.setSpotifyId(dto.getSpotifyId());
        artist.setDescription(dto.getDescription());
        artist.setImage(dto.getImage());
        artist.setPrimaryLanguage(dto.getPrimaryLanguage());
        artist.setGenre(dto.getGenre());
        artist.setOriginCountry(dto.getOriginCountry());
        
        return artist;
    }
    
    /**
     * Returns the artist type that this factory creates.
     * Must be implemented by concrete factory classes.
     * @return The artist type for this factory
     */
    protected abstract ArtistType getArtistType();
    
    @Override
    public boolean supports(ArtistRequestDTO dto) {
        return dto.getType() == getArtistType();
    }
}