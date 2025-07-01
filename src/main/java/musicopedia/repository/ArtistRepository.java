package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    List<Artist> findByArtistNameContainingIgnoreCase(String name);
    List<Artist> findByType(ArtistType type);
    List<Artist> findByGenreContainingIgnoreCase(String genre);
    List<Artist> findByOriginCountry(String countryCode);
    List<Artist> findByPrimaryLanguageIgnoreCase(String language);
    long countByType(ArtistType type);
    boolean existsBySpotifyId(String spotifyId);
    Optional<Artist> findBySpotifyId(String spotifyId);
}