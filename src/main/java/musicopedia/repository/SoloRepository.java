package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SoloRepository extends JpaRepository<Artist, UUID> {
    
    List<Artist> findByType(ArtistType type);
    
    @Query("SELECT a FROM Artist a WHERE a.type = :type AND LOWER(a.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Artist> findSoloArtistsByGenre(@Param("type") ArtistType type, @Param("genre") String genre);
    
    @Query("SELECT a FROM Artist a WHERE a.type = :type AND a.originCountry = :countryCode")
    List<Artist> findSoloArtistsByCountry(@Param("type") ArtistType type, @Param("countryCode") String countryCode);
    
    @Query("SELECT a FROM Artist a WHERE a.type = :type AND LOWER(a.artistName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Artist> findSoloArtistsByNameContaining(@Param("type") ArtistType type, @Param("name") String name);
    
    @Query("SELECT COUNT(a) FROM Artist a WHERE a.type = :type AND LOWER(a.primaryLanguage) = LOWER(:language)")
    long countSoloArtistsByLanguage(@Param("type") ArtistType type, @Param("language") String language);

    @Query("SELECT a FROM Artist a JOIN Solo s ON a.artistId = s.artistId WHERE s.birthDate = :birthDate")
    List<Artist> findByBirthDate(@Param("birthDate") LocalDate birthDate);

    @Query("SELECT a FROM Artist a JOIN Solo s ON a.artistId = s.artistId WHERE s.deathDate = :deathDate")
    List<Artist> findByDeathDate(@Param("deathDate") LocalDate deathDate);

    @Query("SELECT a FROM Artist a JOIN Solo s ON a.artistId = s.artistId WHERE s.deathDate IS NULL OR s.deathDate > CURRENT_DATE")
    List<Artist> findAliveArtists();

    @Query("SELECT a FROM Artist a JOIN Solo s ON a.artistId = s.artistId WHERE s.deathDate IS NOT NULL AND s.deathDate <= CURRENT_DATE")
    List<Artist> findDeceasedArtists();
}
