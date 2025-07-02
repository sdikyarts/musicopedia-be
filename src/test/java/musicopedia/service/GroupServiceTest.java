package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.GroupRepository;
import musicopedia.service.config.ServiceTestConfig;
import musicopedia.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(ServiceTestConfig.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    private GroupService groupService;

    private UUID testId;
    private Artist testArtist;
    private Groups testGroup;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        groupService = new GroupServiceImpl(groupRepository);

        testId = UUID.randomUUID();
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("BTS");
        testArtist.setType(ArtistType.GROUP);
        testArtist.setOriginCountry("KR");
        testArtist.setPrimaryLanguage("Korean");

        testGroup = new Groups();
        testGroup.setArtistId(testId);
        testGroup.setArtist(testArtist);
        testGroup.setFormationDate(LocalDate.of(2013, 6, 13));
        testGroup.setGroupGender(ArtistGender.MALE);
    }

    @Test
    void testFindAll() {
        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("Group 1");
        artist1.setType(ArtistType.GROUP);

        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("Group 2");
        artist2.setType(ArtistType.GROUP);

        List<Artist> artists = Arrays.asList(artist1, artist2);
        
        when(groupRepository.findByType(ArtistType.GROUP)).thenReturn(artists);

        List<Groups> result = groupService.findAll();

        assertEquals(2, result.size());
        assertEquals("Group 1", result.get(0).getArtist().getArtistName());
        assertEquals("Group 2", result.get(1).getArtist().getArtistName());
        verify(groupRepository, times(1)).findByType(ArtistType.GROUP);
    }

    @Test
    void testFindById() {
        when(groupRepository.findById(testId)).thenReturn(Optional.of(testArtist));

        Optional<Groups> result = groupService.findById(testId);

        assertTrue(result.isPresent());
        assertEquals("BTS", result.get().getArtist().getArtistName());
        verify(groupRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByIdNotFound() {
        when(groupRepository.findById(testId)).thenReturn(Optional.empty());

        Optional<Groups> result = groupService.findById(testId);

        assertFalse(result.isPresent());
        verify(groupRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByIdWrongType() {
        Artist soloArtist = new Artist();
        soloArtist.setArtistId(testId);
        soloArtist.setArtistName("Solo Artist");
        soloArtist.setType(ArtistType.SOLO);
        
        when(groupRepository.findById(testId)).thenReturn(Optional.of(soloArtist));

        Optional<Groups> result = groupService.findById(testId);

        assertFalse(result.isPresent());
        verify(groupRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByFormationDateBetween() {
        // Create test data
        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("Group 1");
        artist1.setType(ArtistType.GROUP);
        
        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("Group 2");
        artist2.setType(ArtistType.GROUP);
        
        Artist artist3 = new Artist();
        artist3.setArtistId(UUID.randomUUID());
        artist3.setArtistName("Group 3");
        artist3.setType(ArtistType.GROUP);

        Artist artistWithNullDate = new Artist();
        artistWithNullDate.setArtistId(UUID.randomUUID());
        artistWithNullDate.setArtistName("Group Null Date");
        artistWithNullDate.setType(ArtistType.GROUP);

        when(groupRepository.findByType(ArtistType.GROUP))
            .thenReturn(Arrays.asList(artist1, artist2, artist3, artistWithNullDate));
            
        // Create a spy of GroupServiceImpl to override convertToGroup
        GroupServiceImpl groupServiceSpy = spy((GroupServiceImpl)groupService);
        groupServiceSpy.setSelf(groupServiceSpy);  // Set self reference for proxy calls
        
        Groups group1 = new Groups();
        group1.setArtistId(artist1.getArtistId());
        group1.setArtist(artist1);
        group1.setFormationDate(LocalDate.of(2012, 1, 1));
        
        Groups group2 = new Groups();
        group2.setArtistId(artist2.getArtistId());
        group2.setArtist(artist2);
        group2.setFormationDate(LocalDate.of(2015, 1, 1));
        
        Groups group3 = new Groups();
        group3.setArtistId(artist3.getArtistId());
        group3.setArtist(artist3);
        group3.setFormationDate(LocalDate.of(2018, 1, 1));

        Groups groupWithNullDate = new Groups();
        groupWithNullDate.setArtistId(artistWithNullDate.getArtistId());
        groupWithNullDate.setArtist(artistWithNullDate);
        groupWithNullDate.setFormationDate(null);
        
        // Mock convertToGroup for each artist
        doReturn(group1).when(groupServiceSpy).convertToGroup(artist1);
        doReturn(group2).when(groupServiceSpy).convertToGroup(artist2);
        doReturn(group3).when(groupServiceSpy).convertToGroup(artist3);
        doReturn(groupWithNullDate).when(groupServiceSpy).convertToGroup(artistWithNullDate);

        List<Groups> result = groupServiceSpy.findByFormationDateBetween(
            LocalDate.of(2014, 1, 1), 
            LocalDate.of(2016, 12, 31)
        );

        assertEquals(1, result.size());
        assertEquals("Group 2", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindActiveGroups() {
        // Create test data
        Artist activeGroup = new Artist();
        activeGroup.setArtistId(UUID.randomUUID());
        activeGroup.setArtistName("Active");
        activeGroup.setType(ArtistType.GROUP);
        
        Artist disbandedGroup = new Artist();
        disbandedGroup.setArtistId(UUID.randomUUID());
        disbandedGroup.setArtistName("Disbanded");
        disbandedGroup.setType(ArtistType.GROUP);

        when(groupRepository.findByType(ArtistType.GROUP))
            .thenReturn(Arrays.asList(activeGroup, disbandedGroup));
            
        // Create a spy of GroupServiceImpl to override convertToGroup
        GroupServiceImpl groupServiceSpy = spy((GroupServiceImpl)groupService);
        groupServiceSpy.setSelf(groupServiceSpy);  // Set self reference for proxy calls
        
        Groups active = new Groups();
        active.setArtistId(activeGroup.getArtistId());
        active.setArtist(activeGroup);
        active.setFormationDate(LocalDate.of(2013, 1, 1));
        active.setDisbandDate(null);
        
        Groups disbanded = new Groups();
        disbanded.setArtistId(disbandedGroup.getArtistId());
        disbanded.setArtist(disbandedGroup);
        disbanded.setFormationDate(LocalDate.of(2010, 1, 1));
        disbanded.setDisbandDate(LocalDate.of(2020, 1, 1));
        
        // Mock convertToGroup for each artist
        doReturn(active).when(groupServiceSpy).convertToGroup(activeGroup);
        doReturn(disbanded).when(groupServiceSpy).convertToGroup(disbandedGroup);

        List<Groups> result = groupServiceSpy.findActiveGroups();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindDisbandedGroups() {
        // Create test data
        Artist activeGroup = new Artist();
        activeGroup.setArtistId(UUID.randomUUID());
        activeGroup.setArtistName("Active");
        activeGroup.setType(ArtistType.GROUP);
        
        Artist disbandedGroup = new Artist();
        disbandedGroup.setArtistId(UUID.randomUUID());
        disbandedGroup.setArtistName("Disbanded");
        disbandedGroup.setType(ArtistType.GROUP);

        when(groupRepository.findByType(ArtistType.GROUP))
            .thenReturn(Arrays.asList(activeGroup, disbandedGroup));
            
        // Create a spy of GroupServiceImpl to override convertToGroup
        GroupServiceImpl groupServiceSpy = spy((GroupServiceImpl)groupService);
        groupServiceSpy.setSelf(groupServiceSpy);  // Set self reference for proxy calls
        
        Groups active = new Groups();
        active.setArtistId(activeGroup.getArtistId());
        active.setArtist(activeGroup);
        active.setFormationDate(LocalDate.of(2013, 1, 1));
        active.setDisbandDate(null);
        
        Groups disbanded = new Groups();
        disbanded.setArtistId(disbandedGroup.getArtistId());
        disbanded.setArtist(disbandedGroup);
        disbanded.setFormationDate(LocalDate.of(2010, 1, 1));
        disbanded.setDisbandDate(LocalDate.of(2020, 1, 1));
        
        // Mock convertToGroup for each artist
        doReturn(active).when(groupServiceSpy).convertToGroup(activeGroup);
        doReturn(disbanded).when(groupServiceSpy).convertToGroup(disbandedGroup);

        List<Groups> result = groupServiceSpy.findDisbandedGroups();

        assertEquals(1, result.size());
        assertEquals("Disbanded", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindByGroupGender() {
        // Create test data
        Artist maleGroupArtist = new Artist();
        maleGroupArtist.setArtistId(UUID.randomUUID());
        maleGroupArtist.setArtistName("Male Group");
        maleGroupArtist.setType(ArtistType.GROUP);
        
        Artist femaleGroupArtist = new Artist();
        femaleGroupArtist.setArtistId(UUID.randomUUID());
        femaleGroupArtist.setArtistName("Female Group");
        femaleGroupArtist.setType(ArtistType.GROUP);
        
        when(groupRepository.findByType(ArtistType.GROUP))
            .thenReturn(Arrays.asList(maleGroupArtist, femaleGroupArtist));
            
        // Create a spy of GroupServiceImpl to override convertToGroup
        GroupServiceImpl groupServiceSpy = spy((GroupServiceImpl)groupService);
        groupServiceSpy.setSelf(groupServiceSpy);  // Set self reference for proxy calls
        
        Groups maleGroup = new Groups();
        maleGroup.setArtistId(maleGroupArtist.getArtistId());
        maleGroup.setArtist(maleGroupArtist);
        maleGroup.setFormationDate(LocalDate.of(2013, 1, 1));
        maleGroup.setGroupGender(ArtistGender.MALE);
        
        Groups femaleGroup = new Groups();
        femaleGroup.setArtistId(femaleGroupArtist.getArtistId());
        femaleGroup.setArtist(femaleGroupArtist);
        femaleGroup.setFormationDate(LocalDate.of(2014, 1, 1));
        femaleGroup.setGroupGender(ArtistGender.FEMALE);
        
        // Mock convertToGroup for each artist
        doReturn(maleGroup).when(groupServiceSpy).convertToGroup(maleGroupArtist);
        doReturn(femaleGroup).when(groupServiceSpy).convertToGroup(femaleGroupArtist);

        List<Groups> result = groupServiceSpy.findByGroupGender(ArtistGender.MALE);

        assertEquals(1, result.size());
        assertEquals("Male Group", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testSave() {
        when(groupRepository.save(any(Artist.class))).thenReturn(testArtist);

        Groups savedGroup = groupService.save(testGroup, testArtist);

        assertEquals(testId, savedGroup.getArtistId());
        assertEquals("BTS", savedGroup.getArtist().getArtistName());
        assertEquals(ArtistType.GROUP, testArtist.getType());
        verify(groupRepository, times(1)).save(testArtist);
    }

    @Test
    void testUpdate() {
        when(groupRepository.existsById(testId)).thenReturn(true);
        when(groupRepository.save(testArtist)).thenReturn(testArtist);

        Groups updatedGroup = groupService.update(testGroup);

        assertNotNull(updatedGroup);
        verify(groupRepository, times(1)).existsById(testId);
        verify(groupRepository, times(1)).save(testArtist);
    }

    @Test
    void testUpdateNotFound() {
        when(groupRepository.existsById(testId)).thenReturn(false);

        Groups updatedGroup = groupService.update(testGroup);

        assertNull(updatedGroup);
        verify(groupRepository, times(1)).existsById(testId);
        verify(groupRepository, never()).save(any());
    }

    @Test
    void testUpdateWithNullArtist() {
        Groups groupWithNullArtist = new Groups();
        groupWithNullArtist.setArtistId(testId);
        groupWithNullArtist.setArtist(null);

        when(groupRepository.existsById(testId)).thenReturn(true);

        Groups updatedGroup = groupService.update(groupWithNullArtist);

        assertNotNull(updatedGroup);
        verify(groupRepository, times(1)).existsById(testId);
        verify(groupRepository, never()).save(any());
    }

    @Test
    void testDeleteById() {
        doNothing().when(groupRepository).deleteById(testId);

        groupService.deleteById(testId);

        verify(groupRepository, times(1)).deleteById(testId);
    }


}
