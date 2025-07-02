package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.dto.request.CreateArtistRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistService {
    

    List<Artist> findAll();
    

    Optional<Artist> findById(UUID artistId);
    

    Optional<Artist> findBySpotifyId(String spotifyId);
    

    List<Artist> findByNameContaining(String name);
    

    List<Artist> findByType(ArtistType type);
    

    Artist save(Artist artist);
    
    /**
     * Creates and saves an artist using the factory pattern with type-specific validation
     * @param dto The creation request containing artist data
     * @return The created and saved Artist entity
     */
    Artist createArtist(CreateArtistRequestDTO dto);
    

    void deleteById(UUID artistId);
    
    boolean existsById(UUID artistId);
}
