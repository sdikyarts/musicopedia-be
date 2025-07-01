package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;

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
    

    void deleteById(UUID artistId);
    
    boolean existsById(UUID artistId);
}
