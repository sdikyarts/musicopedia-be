package musicopedia.service;

import musicopedia.model.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {
    
    List<Member> findAll();
    
    Optional<Member> findById(UUID memberId);
    
    List<Member> findByNameContaining(String name);
    
    List<Member> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Member> findBySoloArtistNotNull();
    
    Member save(Member member);
    
    Member update(Member member);
    
    void deleteById(UUID memberId);
}
