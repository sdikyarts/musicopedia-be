package musicopedia.repository;

import musicopedia.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    
    List<Member> findByMemberNameContainingIgnoreCase(String memberName);
    
    Optional<Member> findByMemberName(String memberName);
    
    List<Member> findByBirthDate(LocalDate birthDate);
    
    List<Member> findByBirthDateAfter(LocalDate date);
    
    List<Member> findByBirthDateBefore(LocalDate date);
    
    boolean existsByMemberName(String memberName);

    List<Member> findByRealNameContainingIgnoreCase(String realName);

    List<Member> findByNationality(String nationality);
}
