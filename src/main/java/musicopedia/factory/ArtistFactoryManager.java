package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.Artist;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Main factory manager that coordinates all artist factories.
 * Uses the Factory Method pattern to delegate artist creation
 * to the appropriate type-specific factory.
 */
@Component
public class ArtistFactoryManager {

    private final List<ArtistFactory> artistFactories;

    public ArtistFactoryManager(List<ArtistFactory> artistFactories) {
        this.artistFactories = artistFactories;
    }

    /**
     * Creates an artist using the appropriate factory based on the artist type.
     * 
     * @param dto The creation request containing artist data
     * @return The created Artist entity
     * @throws IllegalArgumentException if no factory supports the artist type
     */
    public Artist createArtist(CreateArtistRequestDTO dto) {
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Artist type cannot be null");
        }

        ArtistFactory factory = findFactory(dto);
        return factory.createArtist(dto);
    }

    /**
     * Validates artist data using the appropriate factory.
     * 
     * @param dto The creation request to validate
     * @throws IllegalArgumentException if validation fails or no factory supports the type
     */
    public void validateArtistData(CreateArtistRequestDTO dto) {
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Artist type cannot be null");
        }

        ArtistFactory factory = findFactory(dto);
        factory.validateArtistData(dto);
    }

    /**
     * Finds the appropriate factory for the given artist type.
     * 
     * @param dto The creation request
     * @return The factory that supports the artist type
     * @throws IllegalArgumentException if no factory supports the artist type
     */
    private ArtistFactory findFactory(CreateArtistRequestDTO dto) {
        return artistFactories.stream()
                .filter(factory -> factory.supports(dto))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No factory available for artist type: " + dto.getType()));
    }
}
