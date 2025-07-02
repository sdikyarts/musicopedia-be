package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.Artist;

/**
 * Factory interface for creating different types of artists.
 * Each artist type (SOLO, GROUP, FRANCHISE, VARIOUS) may have
 * different creation logic, validation rules, and processing requirements.
 */
public interface ArtistFactory {
    
    /**
     * Creates an Artist entity from the DTO with type-specific logic
     * @param dto The creation request containing artist data
     * @return The created Artist entity
     */
    Artist createArtist(CreateArtistRequestDTO dto);
    
    /**
     * Validates the artist data according to type-specific rules
     * @param dto The creation request to validate
     * @throws IllegalArgumentException if validation fails
     */
    void validateArtistData(CreateArtistRequestDTO dto);
    
    /**
     * Checks if this factory can handle the given artist type
     * @param dto The creation request
     * @return true if this factory supports the artist type
     */
    boolean supports(CreateArtistRequestDTO dto);
}
