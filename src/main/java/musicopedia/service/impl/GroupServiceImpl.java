package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.GroupRepository;
import musicopedia.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private GroupService self;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    @Lazy
    public void setSelf(GroupService self) {
        this.self = self;
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Groups>> findAll() {
        List<Artist> artists = groupRepository.findByType(ArtistType.GROUP);
        List<Groups> groups = artists.stream()
                .map(this::convertToGroup)
                .toList();
        return CompletableFuture.completedFuture(groups);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Optional<Groups>> findById(UUID groupId) {
        Optional<Groups> group = groupRepository.findById(groupId)
                .filter(artist -> artist.getType() == ArtistType.GROUP)
                .map(this::convertToGroup);
        return CompletableFuture.completedFuture(group);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Groups>> findByFormationDateBetween(LocalDate startDate, LocalDate endDate) {
        return self.findAll()
                .thenApply(allGroups -> allGroups.stream()
                        .filter(group -> {
                            LocalDate formationDate = group.getFormationDate();
                            return formationDate != null && 
                                   !formationDate.isBefore(startDate) && 
                                   !formationDate.isAfter(endDate);
                        })
                        .toList());
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Groups>> findActiveGroups() {
        return self.findAll()
                .thenApply(allGroups -> allGroups.stream()
                        .filter(group -> group.getDisbandDate() == null)
                        .toList());
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Groups>> findDisbandedGroups() {
        return self.findAll()
                .thenApply(allGroups -> allGroups.stream()
                        .filter(group -> group.getDisbandDate() != null)
                        .toList());
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Groups>> findByGroupGender(ArtistGender gender) {
        return self.findAll()
                .thenApply(allGroups -> allGroups.stream()
                        .filter(group -> group.getGroupGender() == gender)
                        .toList());
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Groups> save(Groups group, Artist artist) {
        artist.setType(ArtistType.GROUP);
        Artist savedArtist = groupRepository.save(artist);
        group.setArtistId(savedArtist.getArtistId());
        group.setArtist(savedArtist);
        return CompletableFuture.completedFuture(group);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Groups> update(Groups group) {
        if (groupRepository.existsById(group.getArtistId())) {
            Artist artist = group.getArtist();
            if (artist != null) {
                groupRepository.save(artist);
            }
            return CompletableFuture.completedFuture(group);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> deleteById(UUID groupId) {
        groupRepository.deleteById(groupId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Boolean> existsById(UUID groupId) {
        boolean exists = groupRepository.existsById(groupId);
        return CompletableFuture.completedFuture(exists);
    }

    // Changed to public for testing purposes
    public Groups convertToGroup(Artist artist) {
        Groups group = new Groups();
        group.setArtistId(artist.getArtistId());
        group.setArtist(artist);
        return group;
    }
}
