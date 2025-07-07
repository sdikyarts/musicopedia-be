package musicopedia.repository;

import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SoloRepository extends JpaRepository<Solo, UUID> {
    
    List<Solo> findByArtist_Type(ArtistType type);
    
    @Query("SELECT s FROM Solo s WHERE s.artist.type = :type AND LOWER(s.artist.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Solo> findSoloArtistsByGenre(@Param("type") ArtistType type, @Param("genre") String genre);
    
    @Query("SELECT s FROM Solo s WHERE s.artist.type = :type AND s.artist.originCountry = :countryCode")
    List<Solo> findSoloArtistsByCountry(@Param("type") ArtistType type, @Param("countryCode") String countryCode);
    
    @Query("SELECT s FROM Solo s WHERE s.artist.type = :type AND LOWER(s.artist.artistName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Solo> findSoloArtistsByNameContaining(@Param("type") ArtistType type, @Param("name") String name);
    
    @Query("SELECT COUNT(s) FROM Solo s WHERE s.artist.type = :type AND LOWER(s.artist.primaryLanguage) = LOWER(:language)")
    long countSoloArtistsByLanguage(@Param("type") ArtistType type, @Param("language") String language);

    @Query("SELECT s FROM Solo s WHERE s.birthDate = :birthDate")
    List<Solo> findByBirthDate(@Param("birthDate") LocalDate birthDate);

    @Query("SELECT s FROM Solo s WHERE s.deathDate = :deathDate")
    List<Solo> findByDeathDate(@Param("deathDate") LocalDate deathDate);

    @Query("SELECT s FROM Solo s WHERE s.deathDate IS NULL OR s.deathDate > CURRENT_DATE")
    List<Solo> findAliveArtists();

    @Query("SELECT s FROM Solo s WHERE s.deathDate IS NOT NULL AND s.deathDate <= CURRENT_DATE")
    List<Solo> findDeceasedArtists();

    @Query("SELECT s FROM Solo s WHERE LOWER(s.realName) LIKE LOWER(CONCAT('%', :realName, '%'))")
    List<Solo> findBySoloRealNameContaining(@Param("realName") String realName);

    @Query("SELECT s FROM Solo s WHERE s.debutDate = :debutDate")
    List<Solo> findByDebutDate(@Param("debutDate") LocalDate debutDate);

    @Query("SELECT s FROM Solo s WHERE s.debutDate BETWEEN :startDate AND :endDate")
    List<Solo> findByDebutDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
