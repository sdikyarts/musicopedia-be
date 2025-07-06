package musicopedia.service;

import musicopedia.model.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberService {
    
    CompletableFuture<List<Member>> findAll();
    
    CompletableFuture<Optional<Member>> findById(UUID memberId);
    
    CompletableFuture<List<Member>> findByNameContaining(String name);
    
    CompletableFuture<List<Member>> findByRealNameContaining(String realName);
    
    CompletableFuture<List<Member>> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    CompletableFuture<List<Member>> findBySoloArtistNotNull();
    
    CompletableFuture<Member> save(Member member);
    
    CompletableFuture<Member> update(Member member);
    
    CompletableFuture<Void> deleteById(UUID memberId);
    
    CompletableFuture<Boolean> existsById(UUID memberId);

    CompletableFuture<List<Member>> findByNationality(String nationality);
}
