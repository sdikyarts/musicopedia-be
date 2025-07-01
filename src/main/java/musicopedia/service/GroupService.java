package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupService {
    
    List<Groups> findAll();
    
    Optional<Groups> findById(UUID groupId);
    
    List<Groups> findByFormationDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Groups> findActiveGroups();
    
    List<Groups> findDisbandedGroups();
    
    List<Groups> findByGroupGender(ArtistGender gender);
    
    Groups save(Groups group, Artist artist);
    
    Groups update(Groups group);
    
    void deleteById(UUID groupId);
}
