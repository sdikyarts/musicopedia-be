package musicopedia.mapper;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.dto.request.UpdateArtistRequestDTO;
import musicopedia.dto.response.ArtistResponseDTO;
import musicopedia.dto.response.ArtistSummaryDTO;
import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
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
    void testToEntity_FromCreateArtistRequestDTO() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("Test Artist");
        dto.setType(ArtistType.SOLO);
        dto.setSpotifyId("test123");
        dto.setDescription("Test description");
        dto.setImage("test-image.jpg");
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");
        dto.setOriginCountry("US");

        Artist expectedArtist = new Artist();
        expectedArtist.setArtistName("Test Artist");
        expectedArtist.setType(ArtistType.SOLO);
        expectedArtist.setSpotifyId("test123");
        expectedArtist.setDescription("Test description");
        expectedArtist.setImage("test-image.jpg");
        expectedArtist.setPrimaryLanguage("English");
        expectedArtist.setGenre("Pop");
        expectedArtist.setOriginCountry("US");

        when(artistFactoryManager.createArtist(any(CreateArtistRequestDTO.class))).thenReturn(expectedArtist);

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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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
        assertNull(responseDto.getFormationDate());
        assertNull(responseDto.getDisbandDate());
        assertNull(responseDto.getGroupGender());
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
        assertNull(responseDto.getBirthDate());
        assertNull(responseDto.getDeathDate());
        assertNull(responseDto.getSoloGender());
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
        ArtistSummaryDTO summaryDto = artistMapper.toSummaryDto(artist);

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
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setBirthDate(LocalDate.of(1990, 5, 15));
        dto.setDeathDate(LocalDate.of(2020, 10, 20));
        dto.setSoloGender(ArtistGender.MALE);

        Artist artist = createTestArtist();

        // When
        Solo solo = artistMapper.createSoloFromDto(dto, artist);

        // Then
        assertNotNull(solo);
        assertEquals(artist, solo.getArtist());
        assertEquals(LocalDate.of(1990, 5, 15), solo.getBirthDate());
        assertEquals(LocalDate.of(2020, 10, 20), solo.getDeathDate());
        assertEquals(ArtistGender.MALE, solo.getGender());
    }

    @Test
    void testCreateSoloFromDto_NonSoloType() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
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
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setFormationDate(LocalDate.of(2000, 1, 1));
        dto.setDisbandDate(LocalDate.of(2010, 12, 31));
        dto.setGroupGender(ArtistGender.MIXED);

        Artist artist = createTestArtist();

        // When
        Groups group = artistMapper.createGroupFromDto(dto, artist);

        // Then
        assertNotNull(group);
        assertEquals(artist, group.getArtist());
        assertEquals(LocalDate.of(2000, 1, 1), group.getFormationDate());
        assertEquals(LocalDate.of(2010, 12, 31), group.getDisbandDate());
        assertEquals(ArtistGender.MIXED, group.getGroupGender());
    }

    @Test
    void testCreateGroupFromDto_NonGroupType() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
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
        Artist artist = new Artist();
        artist.setArtistId(UUID.randomUUID());
        artist.setArtistName("Test Artist");
        artist.setType(ArtistType.SOLO);
        artist.setSpotifyId("test123");
        artist.setDescription("Test description");
        artist.setImage("test-image.jpg");
        artist.setPrimaryLanguage("English");
        artist.setGenre("Pop");
        artist.setOriginCountry("US");
        return artist;
    }

    private Solo createTestSolo(Artist artist) {
        Solo solo = new Solo();
        solo.setArtist(artist);
        solo.setBirthDate(LocalDate.of(1990, 5, 15));
        solo.setDeathDate(LocalDate.of(2020, 10, 20));
        solo.setGender(ArtistGender.MALE);
        return solo;
    }

    private Groups createTestGroup(Artist artist) {
        Groups group = new Groups();
        group.setArtist(artist);
        group.setFormationDate(LocalDate.of(2000, 1, 1));
        group.setDisbandDate(LocalDate.of(2010, 12, 31));
        group.setGroupGender(ArtistGender.MIXED);
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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
        UpdateArtistRequestDTO updateDto1 = new UpdateArtistRequestDTO();
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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

        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
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
}
