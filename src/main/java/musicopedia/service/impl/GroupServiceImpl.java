package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.GroupRepository;
import musicopedia.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Transactional(readOnly = true)
    public List<Groups> findAll() {
        List<Artist> artists = groupRepository.findByType(ArtistType.GROUP);
        return artists.stream()
                .map(this::convertToGroup)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Groups> findById(UUID groupId) {
        return groupRepository.findById(groupId)
                .filter(artist -> artist.getType() == ArtistType.GROUP)
                .map(this::convertToGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groups> findByFormationDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Groups> allGroups = self.findAll();
        return allGroups.stream()
                .filter(group -> {
                    LocalDate formationDate = group.getFormationDate();
                    return formationDate != null && 
                           !formationDate.isBefore(startDate) && 
                           !formationDate.isAfter(endDate);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groups> findActiveGroups() {
        List<Groups> allGroups = self.findAll();
        return allGroups.stream()
                .filter(group -> group.getDisbandDate() == null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groups> findDisbandedGroups() {
        List<Groups> allGroups = self.findAll();
        return allGroups.stream()
                .filter(group -> group.getDisbandDate() != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groups> findByGroupGender(ArtistGender gender) {
        List<Groups> allGroups = self.findAll();
        return allGroups.stream()
                .filter(group -> group.getGroupGender() == gender)
                .toList();
    }

    @Override
    public Groups save(Groups group, Artist artist) {
        artist.setType(ArtistType.GROUP);
        Artist savedArtist = groupRepository.save(artist);
        group.setArtistId(savedArtist.getArtistId());
        group.setArtist(savedArtist);
        return group;
    }

    @Override
    public Groups update(Groups group) {
        if (groupRepository.existsById(group.getArtistId())) {
            Artist artist = group.getArtist();
            if (artist != null) {
                groupRepository.save(artist);
            }
            return group;
        }
        return null;
    }

    @Override
    public void deleteById(UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    // Changed to public for testing purposes
    public Groups convertToGroup(Artist artist) {
        Groups group = new Groups();
        group.setArtistId(artist.getArtistId());
        group.setArtist(artist);
        return group;
    }
}
