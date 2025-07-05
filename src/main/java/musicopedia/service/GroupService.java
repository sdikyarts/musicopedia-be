package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GroupService {
    
    CompletableFuture<List<Groups>> findAll();
    
    CompletableFuture<Optional<Groups>> findById(UUID groupId);
    
    CompletableFuture<List<Groups>> findByFormationDateBetween(LocalDate startDate, LocalDate endDate);
    
    CompletableFuture<List<Groups>> findActiveGroups();
    
    CompletableFuture<List<Groups>> findDisbandedGroups();
    
    CompletableFuture<List<Groups>> findByGroupGender(ArtistGender gender);
    
    CompletableFuture<Groups> save(Groups group, Artist artist);
    
    CompletableFuture<Groups> update(Groups group);
    
    CompletableFuture<Void> deleteById(UUID groupId);
    
    CompletableFuture<Boolean> existsById(UUID groupId);
}
