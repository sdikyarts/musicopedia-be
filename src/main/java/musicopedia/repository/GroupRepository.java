package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Artist, UUID> {
    
    List<Artist> findByType(ArtistType type);
    
    @Query("SELECT a FROM Artist a WHERE a.type = :type AND LOWER(a.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Artist> findGroupsByGenre(@Param("type") ArtistType type, @Param("genre") String genre);
    
    @Query("SELECT a FROM Artist a WHERE a.type = :type AND a.originCountry = :countryCode")
    List<Artist> findGroupsByCountry(@Param("type") ArtistType type, @Param("countryCode") String countryCode);
    
    @Query("SELECT a FROM Artist a WHERE a.type = :type AND LOWER(a.artistName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Artist> findGroupsByNameContaining(@Param("type") ArtistType type, @Param("name") String name);
    
    @Query("SELECT COUNT(a) FROM Artist a WHERE a.type = :type AND LOWER(a.primaryLanguage) = LOWER(:language)")
    long countGroupsByLanguage(@Param("type") ArtistType type, @Param("language") String language);

    @Query("SELECT a FROM Artist a JOIN Groups g ON a.artistId = g.artistId WHERE a.type = :type AND g.formationDate = :formationDate")
    List<Artist> findGroupsByFormationDate(@Param("type") ArtistType type, @Param("formationDate") String formationDate);

    @Query("SELECT a FROM Artist a JOIN Groups g ON a.artistId = g.artistId WHERE a.type = :type AND g.disbandDate = :disbandDate")
    List<Artist> findGroupsByDisbandDate(@Param("type") ArtistType type, @Param("disbandDate") String disbandDate);
}
