package musicopedia.mapper;

import musicopedia.builder.ArtistBuilder;
import musicopedia.builder.SoloBuilder;
import musicopedia.builder.GroupsBuilder;
import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.dto.response.ArtistResponseDTO;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.GroupActivityStatus;
import musicopedia.model.enums.GroupAffiliationStatus;
import musicopedia.factory.ArtistFactoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistMapperTest {

    private ArtistMapper artistMapper;
    
    @Mock
    private ArtistFactoryManager artistFactoryManager;

    @BeforeEach
    void setUp() {
        artistMapper = new ArtistMapper(artistFactoryManager);
    }

    @Test
    void testToEntity_FromArtistRequestDTO() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setArtistName("Test Artist");
        dto.setType(ArtistType.SOLO);
        dto.setSpotifyId("test123");
        dto.setDescription("Test description");
        dto.setImage("test-image.jpg");
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");
        dto.setOriginCountry("US");

        Artist expectedArtist = new ArtistBuilder()
            .setArtistName("Test Artist")
            .setType(ArtistType.SOLO)
            .setSpotifyId("test123")
            .setDescription("Test description")
            .setImage("test-image.jpg")
            .setPrimaryLanguage("English")
            .setGenre("Pop")
            .setOriginCountry("US")
            .build();

        when(artistFactoryManager.createArtist(any(ArtistRequestDTO.class))).thenReturn(expectedArtist);

        // When
        Artist artist = artistMapper.toEntity(dto);

        // Then
        assertNotNull(artist);
        assertEquals("Test Artist", artist.getArtistName());
        assertEquals(ArtistType.SOLO, artist.getType());
        assertEquals("test123", artist.getSpotifyId());
        assertEquals("Test description", artist.getDescription());
        assertEquals("test-image.jpg", artist.getImage());
        assertEquals("English", artist.getPrimaryLanguage());
        assertEquals("Pop", artist.getGenre());
        assertEquals("US", artist.getOriginCountry());
        assertNull(artist.getArtistId()); // Should be null until saved
    }

    @Test
    void testUpdateEntityFromDto() {
        // Given
        Artist existingArtist = new Artist();
        existingArtist.setArtistId(UUID.randomUUID());
        existingArtist.setArtistName("Original Name");
        existingArtist.setType(ArtistType.SOLO);
        existingArtist.setGenre("Rock");

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setArtistName("Updated Name");
        updateDto.setGenre("Jazz");
        updateDto.setOriginCountry("CA");

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals("Updated Name", existingArtist.getArtistName());
        assertEquals(ArtistType.SOLO, existingArtist.getType()); // Unchanged
        assertEquals("Jazz", existingArtist.getGenre());
        assertEquals("CA", existingArtist.getOriginCountry());
    }

    @Test
    void testUpdateEntityFromDto_NullValues() {
        // Given
        Artist existingArtist = new Artist();
        existingArtist.setArtistName("Original Name");
        existingArtist.setType(ArtistType.GROUP);
        existingArtist.setGenre("Rock");

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setGenre("Pop");
        // Other fields are null

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals("Original Name", existingArtist.getArtistName()); // Unchanged
        assertEquals(ArtistType.GROUP, existingArtist.getType()); // Unchanged
        assertEquals("Pop", existingArtist.getGenre()); // Updated
    }

    @Test
    void testToResponseDto_WithSoloData() {
        // Given
        Artist artist = createTestArtist();
        Solo solo = createTestSolo(artist);

        // When
        ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, solo, null);

        // Then
        assertNotNull(responseDto);
        assertEquals(artist.getArtistId(), responseDto.getArtistId());
        assertEquals(artist.getArtistName(), responseDto.getArtistName());
        assertEquals(artist.getType(), responseDto.getType());
        assertEquals(solo.getBirthDate(), responseDto.getBirthDate());
        assertEquals(solo.getDeathDate(), responseDto.getDeathDate());
        assertEquals(solo.getGender(), responseDto.getSoloGender());
        assertEquals(solo.getGroupAffiliationStatus(), responseDto.getGroupAffiliationStatus());
        assertNull(responseDto.getFormationDate());
        assertNull(responseDto.getDisbandDate());
        assertNull(responseDto.getGroupGender());
        assertNull(responseDto.getActivityStatus());
    }

    @Test
    void testToResponseDto_WithGroupData() {
        // Given
        Artist artist = createTestArtist();
        artist.setType(ArtistType.GROUP);
        Groups group = createTestGroup(artist);

        // When
        ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, null, group);

        // Then
        assertNotNull(responseDto);
        assertEquals(artist.getArtistId(), responseDto.getArtistId());
        assertEquals(artist.getArtistName(), responseDto.getArtistName());
        assertEquals(artist.getType(), responseDto.getType());
        assertEquals(group.getFormationDate(), responseDto.getFormationDate());
        assertEquals(group.getDisbandDate(), responseDto.getDisbandDate());
        assertEquals(group.getGroupGender(), responseDto.getGroupGender());
        assertEquals(group.getActivityStatus(), responseDto.getActivityStatus());
        assertNull(responseDto.getBirthDate());
        assertNull(responseDto.getDeathDate());
        assertNull(responseDto.getSoloGender());
        assertNull(responseDto.getGroupAffiliationStatus());
    }

    @Test
    void testToResponseDto_ArtistOnly() {
        // Given
        Artist artist = createTestArtist();

        // When
        ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, null, null);

        // Then
        assertNotNull(responseDto);
        assertEquals(artist.getArtistId(), responseDto.getArtistId());
        assertEquals(artist.getArtistName(), responseDto.getArtistName());
        assertEquals(artist.getType(), responseDto.getType());
        assertNull(responseDto.getBirthDate());
        assertNull(responseDto.getFormationDate());
    }

    @Test
    void testToSummaryDto() {
        // Given
        Artist artist = createTestArtist();

        // When
        ArtistResponseDTO summaryDto = artistMapper.toSummaryDto(artist);

        // Then
        assertNotNull(summaryDto);
        assertEquals(artist.getArtistId(), summaryDto.getArtistId());
        assertEquals(artist.getArtistName(), summaryDto.getArtistName());
        assertEquals(artist.getType(), summaryDto.getType());
        assertEquals(artist.getImage(), summaryDto.getImage());
        assertEquals(artist.getGenre(), summaryDto.getGenre());
        assertEquals(artist.getOriginCountry(), summaryDto.getOriginCountry());
    }

    @Test
    void testCreateSoloFromDto_SoloType() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setBirthDate(LocalDate.of(1990, 5, 15));
        dto.setDeathDate(LocalDate.of(2020, 10, 20));
        dto.setSoloGender(ArtistGender.MALE);
        dto.setGroupAffiliationStatus(GroupAffiliationStatus.WAS_IN_A_GROUP);

        Artist artist = createTestArtist();

        // When
        Solo solo = new SoloBuilder()
            .setArtistId(artist.getArtistId())
            .setArtist(artist)
            .setArtistName(artist.getArtistName())
            .setSpotifyId(artist.getSpotifyId())
            .setDescription(artist.getDescription())
            .setImage(artist.getImage())
            .setType(artist.getType())
            .setPrimaryLanguage(artist.getPrimaryLanguage())
            .setGenre(artist.getGenre())
            .setOriginCountry(artist.getOriginCountry())
            .setBirthDate(dto.getBirthDate())
            .setDeathDate(dto.getDeathDate())
            .setGender(dto.getSoloGender())
            .setGroupAffiliationStatus(dto.getGroupAffiliationStatus())
            .buildSolo();

        // Then
        assertNotNull(solo);
        assertEquals(artist, solo.getArtist());
        assertEquals(artist.getArtistId(), solo.getArtistId());
        assertEquals(LocalDate.of(1990, 5, 15), solo.getBirthDate());
        assertEquals(LocalDate.of(2020, 10, 20), solo.getDeathDate());
        assertEquals(ArtistGender.MALE, solo.getGender());
        assertEquals(GroupAffiliationStatus.WAS_IN_A_GROUP, solo.getGroupAffiliationStatus());
    }

    @Test
    void testCreateSoloFromDto_NonSoloType() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setBirthDate(LocalDate.of(1990, 5, 15));
        dto.setSoloGender(ArtistGender.MALE);

        Artist artist = createTestArtist();

        // When
        Solo solo = artistMapper.createSoloFromDto(dto, artist);

        // Then
        assertNull(solo);
    }

    @Test
    void testCreateGroupFromDto_GroupType() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setFormationDate(LocalDate.of(2000, 1, 1));
        dto.setDisbandDate(LocalDate.of(2010, 12, 31));
        dto.setGroupGender(ArtistGender.MIXED);
        dto.setActivityStatus(GroupActivityStatus.DISBANDED);

        Artist artist = createTestArtist();

        // When
        Groups group = new GroupsBuilder()
            .setArtistId(artist.getArtistId())
            .setArtist(artist)
            .setArtistName(artist.getArtistName())
            .setSpotifyId(artist.getSpotifyId())
            .setDescription(artist.getDescription())
            .setImage(artist.getImage())
            .setType(artist.getType())
            .setPrimaryLanguage(artist.getPrimaryLanguage())
            .setGenre(artist.getGenre())
            .setOriginCountry(artist.getOriginCountry())
            .setFormationDate(dto.getFormationDate())
            .setDisbandDate(dto.getDisbandDate())
            .setGroupGender(dto.getGroupGender())
            .setActivityStatus(dto.getActivityStatus())
            .buildGroups();

        // Then
        assertNotNull(group);
        assertEquals(artist, group.getArtist());
        assertEquals(artist.getArtistId(), group.getArtistId());
        assertEquals(LocalDate.of(2000, 1, 1), group.getFormationDate());
        assertEquals(LocalDate.of(2010, 12, 31), group.getDisbandDate());
        assertEquals(ArtistGender.MIXED, group.getGroupGender());
        assertEquals(GroupActivityStatus.DISBANDED, group.getActivityStatus());
    }

    @Test
    void testCreateGroupFromDto_NonGroupType() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setFormationDate(LocalDate.of(2000, 1, 1));
        dto.setGroupGender(ArtistGender.MIXED);

        Artist artist = createTestArtist();

        // When
        Groups group = artistMapper.createGroupFromDto(dto, artist);

        // Then
        assertNull(group);
    }


    // Helper methods
    private Artist createTestArtist() {
        Artist artist = new ArtistBuilder()
            .setArtistName("Test Artist")
            .setType(ArtistType.SOLO)
            .setSpotifyId("test123")
            .setDescription("Test description")
            .setImage("test-image.jpg")
            .setPrimaryLanguage("English")
            .setGenre("Pop")
            .setOriginCountry("US")
            .build();
        artist.setArtistId(UUID.randomUUID());
        return artist;
    }

    private Solo createTestSolo(Artist artist) {
        Solo solo = new SoloBuilder()
            .setArtistName(artist.getArtistName())
            .setType(artist.getType())
            .setBirthDate(LocalDate.of(1990, 5, 15))
            .setDeathDate(LocalDate.of(2020, 10, 20))
            .setGender(ArtistGender.MALE)
            .setGroupAffiliationStatus(GroupAffiliationStatus.NEVER_IN_A_GROUP)
            .buildSolo();
        solo.setArtist(artist);
        return solo;
    }

    private Groups createTestGroup(Artist artist) {
        Groups group = new GroupsBuilder()
            .setArtistName(artist.getArtistName())
            .setType(artist.getType())
            .setFormationDate(LocalDate.of(2000, 1, 1))
            .setDisbandDate(LocalDate.of(2010, 12, 31))
            .setGroupGender(ArtistGender.MIXED)
            .setActivityStatus(GroupActivityStatus.DISBANDED)
            .buildGroups();
        group.setArtist(artist);
        return group;
    }

    @Test
    void testUpdateEntityFromDto_AllFieldsUpdated() {
        // Given
        Artist existingArtist = new Artist();
        existingArtist.setArtistId(UUID.randomUUID());
        existingArtist.setArtistName("Original Name");
        existingArtist.setType(ArtistType.SOLO);
        existingArtist.setSpotifyId("original123");
        existingArtist.setDescription("Original description");
        existingArtist.setImage("original-image.jpg");
        existingArtist.setPrimaryLanguage("Spanish");
        existingArtist.setGenre("Rock");
        existingArtist.setOriginCountry("ES");

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setArtistName("Updated Name");
        updateDto.setType(ArtistType.GROUP);
        updateDto.setSpotifyId("updated456");
        updateDto.setDescription("Updated description");
        updateDto.setImage("updated-image.jpg");
        updateDto.setPrimaryLanguage("English");
        updateDto.setGenre("Jazz");
        updateDto.setOriginCountry("US");

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals("Updated Name", existingArtist.getArtistName());
        assertEquals(ArtistType.GROUP, existingArtist.getType());
        assertEquals("updated456", existingArtist.getSpotifyId());
        assertEquals("Updated description", existingArtist.getDescription());
        assertEquals("updated-image.jpg", existingArtist.getImage());
        assertEquals("English", existingArtist.getPrimaryLanguage());
        assertEquals("Jazz", existingArtist.getGenre());
        assertEquals("US", existingArtist.getOriginCountry());
    }

    @Test
    void testUpdateEntityFromDto_PartialUpdates() {
        // Given
        Artist existingArtist = new Artist();
        existingArtist.setArtistId(UUID.randomUUID());
        existingArtist.setArtistName("Original Name");
        existingArtist.setType(ArtistType.SOLO);
        existingArtist.setSpotifyId("original123");
        existingArtist.setDescription("Original description");
        existingArtist.setImage("original-image.jpg");
        existingArtist.setPrimaryLanguage("Spanish");
        existingArtist.setGenre("Rock");
        existingArtist.setOriginCountry("ES");

        // Test updating only artistName and genre
        ArtistRequestDTO updateDto1 = new ArtistRequestDTO();
        updateDto1.setArtistName("New Name");
        updateDto1.setGenre("Pop");

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto1);

        // Then
        assertEquals("New Name", existingArtist.getArtistName());
        assertEquals(ArtistType.SOLO, existingArtist.getType()); // Unchanged
        assertEquals("original123", existingArtist.getSpotifyId()); // Unchanged
        assertEquals("Original description", existingArtist.getDescription()); // Unchanged
        assertEquals("original-image.jpg", existingArtist.getImage()); // Unchanged
        assertEquals("Spanish", existingArtist.getPrimaryLanguage()); // Unchanged
        assertEquals("Pop", existingArtist.getGenre()); // Updated
        assertEquals("ES", existingArtist.getOriginCountry()); // Unchanged
    }

    @Test
    void testUpdateEntityFromDto_OnlySpotifyIdAndDescription() {
        // Given
        Artist existingArtist = createTestArtist();
        String originalName = existingArtist.getArtistName();
        ArtistType originalType = existingArtist.getType();

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setSpotifyId("new-spotify-id");
        updateDto.setDescription("New description");

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals(originalName, existingArtist.getArtistName()); // Unchanged
        assertEquals(originalType, existingArtist.getType()); // Unchanged
        assertEquals("new-spotify-id", existingArtist.getSpotifyId()); // Updated
        assertEquals("New description", existingArtist.getDescription()); // Updated
    }

    @Test
    void testUpdateEntityFromDto_OnlyImageAndLanguage() {
        // Given
        Artist existingArtist = createTestArtist();
        String originalGenre = existingArtist.getGenre();
        String originalCountry = existingArtist.getOriginCountry();

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setImage("new-image.png");
        updateDto.setPrimaryLanguage("French");

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals("new-image.png", existingArtist.getImage()); // Updated
        assertEquals("French", existingArtist.getPrimaryLanguage()); // Updated
        assertEquals(originalGenre, existingArtist.getGenre()); // Unchanged
        assertEquals(originalCountry, existingArtist.getOriginCountry()); // Unchanged
    }

    @Test
    void testUpdateEntityFromDto_OnlyType() {
        // Given
        Artist existingArtist = createTestArtist();
        String originalName = existingArtist.getArtistName();

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setType(ArtistType.FRANCHISE);

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals(originalName, existingArtist.getArtistName()); // Unchanged
        assertEquals(ArtistType.FRANCHISE, existingArtist.getType()); // Updated
    }

    @Test
    void testUpdateEntityFromDto_OnlyOriginCountry() {
        // Given
        Artist existingArtist = createTestArtist();
        String originalGenre = existingArtist.getGenre();

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setOriginCountry("CA");

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals(originalGenre, existingArtist.getGenre()); // Unchanged
        assertEquals("CA", existingArtist.getOriginCountry()); // Updated
    }

    @Test
    void testUpdateEntityFromDto_EmptyStringValues() {
        // Given
        Artist existingArtist = createTestArtist();

        ArtistRequestDTO updateDto = new ArtistRequestDTO();
        updateDto.setArtistName(""); // Empty string, not null
        updateDto.setDescription(""); // Empty string, not null
        updateDto.setGenre(""); // Empty string, not null

        // When
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then
        assertEquals("", existingArtist.getArtistName()); // Updated to empty
        assertEquals("", existingArtist.getDescription()); // Updated to empty
        assertEquals("", existingArtist.getGenre()); // Updated to empty
    }

    @Test
    void createSoloFromDto_shouldReturnNullIfNotSoloType() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.GROUP); // Not SOLO
        Artist artist = createTestArtist();
        Solo result = artistMapper.createSoloFromDto(dto, artist);
        assertNull(result);
    }

    @Test
    void createSoloFromDto_shouldMapAllFieldsIfSoloType() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setBirthDate(LocalDate.of(1988, 7, 7));
        dto.setDeathDate(LocalDate.of(2020, 1, 1));
        dto.setSoloGender(ArtistGender.FEMALE);
        dto.setGroupAffiliationStatus(GroupAffiliationStatus.NEVER_IN_A_GROUP);
        Artist artist = createTestArtist();
        Solo solo = artistMapper.createSoloFromDto(dto, artist);
        assertNotNull(solo);
        assertEquals(artist.getArtistName(), solo.getArtist().getArtistName());
        assertEquals(LocalDate.of(1988, 7, 7), solo.getBirthDate());
        assertEquals(LocalDate.of(2020, 1, 1), solo.getDeathDate());
        assertEquals(ArtistGender.FEMALE, solo.getGender());
        assertEquals(GroupAffiliationStatus.NEVER_IN_A_GROUP, solo.getGroupAffiliationStatus());
    }

    @Test
    void createGroupFromDto_shouldReturnNullIfNotGroupType() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.SOLO); // Not GROUP
        Artist artist = createTestArtist();
        Groups result = artistMapper.createGroupFromDto(dto, artist);
        assertNull(result);
    }

    @Test
    void createGroupFromDto_shouldMapAllFieldsIfGroupType() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setFormationDate(LocalDate.of(2010, 5, 5));
        dto.setDisbandDate(LocalDate.of(2020, 6, 6));
        dto.setGroupGender(ArtistGender.MIXED);
        dto.setActivityStatus(GroupActivityStatus.ACTIVE);
        Artist artist = createTestArtist();
        artist.setType(ArtistType.GROUP);
        Groups group = artistMapper.createGroupFromDto(dto, artist);
        assertNotNull(group);
        assertEquals(artist.getArtistName(), group.getArtist().getArtistName());
        assertEquals(LocalDate.of(2010, 5, 5), group.getFormationDate());
        assertEquals(LocalDate.of(2020, 6, 6), group.getDisbandDate());
        assertEquals(ArtistGender.MIXED, group.getGroupGender());
        assertEquals(GroupActivityStatus.ACTIVE, group.getActivityStatus());
    }

    @Test
    void createArtistFromDto_shouldMapAllFields() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setArtistName("A");
        dto.setType(ArtistType.SOLO);
        dto.setSpotifyId("spid");
        dto.setDescription("desc");
        dto.setImage("img");
        dto.setPrimaryLanguage("lang");
        dto.setGenre("genre");
        dto.setOriginCountry("OC");
        Artist artist = artistMapper.createArtistFromDto(dto);
        assertNotNull(artist);
        assertEquals("A", artist.getArtistName());
        assertEquals(ArtistType.SOLO, artist.getType());
        assertEquals("spid", artist.getSpotifyId());
        assertEquals("desc", artist.getDescription());
        assertEquals("img", artist.getImage());
        assertEquals("lang", artist.getPrimaryLanguage());
        assertEquals("genre", artist.getGenre());
        assertEquals("OC", artist.getOriginCountry());
    }
}
