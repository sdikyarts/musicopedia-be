package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    
    List<Member> findByFullNameContainingIgnoreCase(String name);
    
    Optional<Member> findByFullName(String fullName);
    
    List<Member> findByBirthDate(LocalDate birthDate);
    
    List<Member> findByBirthDateAfter(LocalDate date);
    
    List<Member> findByBirthDateBefore(LocalDate date);
    
    List<Member> findBySoloArtist(Artist soloArtist);
    
    @Query("SELECT m FROM Member m WHERE m.soloArtist.artistId = :soloArtistId")
    List<Member> findBySoloArtistId(@Param("soloArtistId") UUID soloArtistId);
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.soloArtist.artistId = :soloArtistId")
    long countBySoloArtistId(@Param("soloArtistId") UUID soloArtistId);
    
    boolean existsByFullName(String fullName);
    
    List<Member> findBySoloArtistIsNull();
}
