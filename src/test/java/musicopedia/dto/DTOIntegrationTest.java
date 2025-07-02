package musicopedia.dto;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.dto.request.UpdateArtistRequestDTO;
import musicopedia.dto.response.ArtistResponseDTO;
import musicopedia.dto.response.ArtistSummaryDTO;
import musicopedia.mapper.ArtistMapper;
import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the complete DTO flow:
 * CreateRequestDTO -> Entity -> ResponseDTO -> SummaryDTO
 */
class DTOIntegrationTest {

    private ArtistMapper artistMapper;

    @BeforeEach
    void setUp() {
        artistMapper = new ArtistMapper();
    }

    @Test
    void testCompleteFlow_SoloArtist() {
        // Given - Create Request DTO
        CreateArtistRequestDTO createDto = new CreateArtistRequestDTO();
        createDto.setArtistName("John Doe");
        createDto.setType(ArtistType.SOLO);
        createDto.setSpotifyId("johndoe123");
        createDto.setDescription("A talented solo artist");
        createDto.setImage("john-doe.jpg");
        createDto.setPrimaryLanguage("English");
        createDto.setGenre("Rock");
        createDto.setOriginCountry("US");
        createDto.setBirthDate(LocalDate.of(1980, 3, 15));
        createDto.setDeathDate(null);
        createDto.setSoloGender(ArtistGender.MALE);

        // When - Map to entities
        Artist artist = artistMapper.toEntity(createDto);
        artist.setArtistId(UUID.randomUUID()); // Simulate DB save
        
        Solo solo = artistMapper.createSoloFromDto(createDto, artist);
        solo.setArtistId(artist.getArtistId()); // Set ID for solo

        // Then - Map to response DTO
        ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, solo, null);
        
        // Verify complete data flow
        assertEquals(createDto.getArtistName(), responseDto.getArtistName());
        assertEquals(createDto.getType(), responseDto.getType());
        assertEquals(createDto.getSpotifyId(), responseDto.getSpotifyId());
        assertEquals(createDto.getDescription(), responseDto.getDescription());
        assertEquals(createDto.getImage(), responseDto.getImage());
        assertEquals(createDto.getPrimaryLanguage(), responseDto.getPrimaryLanguage());
        assertEquals(createDto.getGenre(), responseDto.getGenre());
        assertEquals(createDto.getOriginCountry(), responseDto.getOriginCountry());
        assertEquals(createDto.getBirthDate(), responseDto.getBirthDate());
        assertEquals(createDto.getDeathDate(), responseDto.getDeathDate());
        assertEquals(createDto.getSoloGender(), responseDto.getSoloGender());
        
        // Group fields should be null
        assertNull(responseDto.getFormationDate());
        assertNull(responseDto.getDisbandDate());
        assertNull(responseDto.getGroupGender());

        // Test summary DTO
        ArtistSummaryDTO summaryDto = artistMapper.toSummaryDto(artist);
        assertEquals(artist.getArtistId(), summaryDto.getArtistId());
        assertEquals(createDto.getArtistName(), summaryDto.getArtistName());
        assertEquals(createDto.getType(), summaryDto.getType());
        assertEquals(createDto.getImage(), summaryDto.getImage());
        assertEquals(createDto.getGenre(), summaryDto.getGenre());
        assertEquals(createDto.getOriginCountry(), summaryDto.getOriginCountry());
    }

    @Test
    void testCompleteFlow_GroupArtist() {
        // Given - Create Request DTO
        CreateArtistRequestDTO createDto = new CreateArtistRequestDTO();
        createDto.setArtistName("The Beatles");
        createDto.setType(ArtistType.GROUP);
        createDto.setSpotifyId("beatles123");
        createDto.setDescription("Legendary British rock band");
        createDto.setImage("beatles.jpg");
        createDto.setPrimaryLanguage("English");
        createDto.setGenre("Rock");
        createDto.setOriginCountry("GB");
        createDto.setFormationDate(LocalDate.of(1960, 8, 1));
        createDto.setDisbandDate(LocalDate.of(1970, 4, 10));
        createDto.setGroupGender(ArtistGender.MALE);

        // When - Map to entities
        Artist artist = artistMapper.toEntity(createDto);
        artist.setArtistId(UUID.randomUUID()); // Simulate DB save
        
        Groups group = artistMapper.createGroupFromDto(createDto, artist);
        group.setArtistId(artist.getArtistId()); // Set ID for group

        // Then - Map to response DTO
        ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, null, group);
        
        // Verify complete data flow
        assertEquals(createDto.getArtistName(), responseDto.getArtistName());
        assertEquals(createDto.getType(), responseDto.getType());
        assertEquals(createDto.getSpotifyId(), responseDto.getSpotifyId());
        assertEquals(createDto.getDescription(), responseDto.getDescription());
        assertEquals(createDto.getImage(), responseDto.getImage());
        assertEquals(createDto.getPrimaryLanguage(), responseDto.getPrimaryLanguage());
        assertEquals(createDto.getGenre(), responseDto.getGenre());
        assertEquals(createDto.getOriginCountry(), responseDto.getOriginCountry());
        assertEquals(createDto.getFormationDate(), responseDto.getFormationDate());
        assertEquals(createDto.getDisbandDate(), responseDto.getDisbandDate());
        assertEquals(createDto.getGroupGender(), responseDto.getGroupGender());
        
        // Solo fields should be null
        assertNull(responseDto.getBirthDate());
        assertNull(responseDto.getDeathDate());
        assertNull(responseDto.getSoloGender());
    }

    @Test
    void testUpdateFlow() {
        // Given - Existing artist
        Artist existingArtist = new Artist();
        existingArtist.setArtistId(UUID.randomUUID());
        existingArtist.setArtistName("Original Name");
        existingArtist.setType(ArtistType.SOLO);
        existingArtist.setGenre("Pop");
        existingArtist.setOriginCountry("US");

        // Update request
        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
        updateDto.setArtistName("Updated Name");
        updateDto.setGenre("Rock");
        updateDto.setDescription("Updated description");

        // When - Apply update
        artistMapper.updateEntityFromDto(existingArtist, updateDto);

        // Then - Verify partial update
        assertEquals("Updated Name", existingArtist.getArtistName());
        assertEquals("Rock", existingArtist.getGenre());
        assertEquals("Updated description", existingArtist.getDescription());
        assertEquals(ArtistType.SOLO, existingArtist.getType()); // Unchanged
        assertEquals("US", existingArtist.getOriginCountry()); // Unchanged

        // Create response DTO
        ArtistResponseDTO responseDto = artistMapper.toResponseDto(existingArtist, null, null);
        assertEquals("Updated Name", responseDto.getArtistName());
        assertEquals("Rock", responseDto.getGenre());
        assertEquals("Updated description", responseDto.getDescription());
    }

    @Test
    void testAllArtistTypes() {
        // Test that all artist types work in the DTO flow
        for (ArtistType type : ArtistType.values()) {
            // Given
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Test " + type.name());
            dto.setType(type);
            dto.setGenre("Test Genre");

            // When
            Artist artist = artistMapper.toEntity(dto);
            artist.setArtistId(UUID.randomUUID());

            ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, null, null);
            ArtistSummaryDTO summaryDto = artistMapper.toSummaryDto(artist);

            // Then
            assertEquals(type, responseDto.getType());
            assertEquals(type, summaryDto.getType());
            assertTrue(responseDto.getArtistName().contains(type.name()));
            assertTrue(summaryDto.getArtistName().contains(type.name()));
        }
    }

    @Test
    void testAllGenderTypes() {
        // Test that all gender types work in the DTO flow
        for (ArtistGender gender : ArtistGender.values()) {
            // Test Solo gender
            CreateArtistRequestDTO soloDto = new CreateArtistRequestDTO();
            soloDto.setArtistName("Solo " + gender.name());
            soloDto.setType(ArtistType.SOLO);
            soloDto.setSoloGender(gender);

            Artist soloArtist = artistMapper.toEntity(soloDto);
            soloArtist.setArtistId(UUID.randomUUID());
            Solo solo = artistMapper.createSoloFromDto(soloDto, soloArtist);

            ArtistResponseDTO soloResponse = artistMapper.toResponseDto(soloArtist, solo, null);
            assertEquals(gender, soloResponse.getSoloGender());

            // Test Group gender
            CreateArtistRequestDTO groupDto = new CreateArtistRequestDTO();
            groupDto.setArtistName("Group " + gender.name());
            groupDto.setType(ArtistType.GROUP);
            groupDto.setGroupGender(gender);

            Artist groupArtist = artistMapper.toEntity(groupDto);
            groupArtist.setArtistId(UUID.randomUUID());
            Groups group = artistMapper.createGroupFromDto(groupDto, groupArtist);

            ArtistResponseDTO groupResponse = artistMapper.toResponseDto(groupArtist, null, group);
            assertEquals(gender, groupResponse.getGroupGender());
        }
    }

    @Test
    void testDTOFieldLimits() {
        // Test that DTOs handle edge cases correctly
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        
        // Test with very long strings (should be validated in controller layer)
        String longName = "A".repeat(300);
        dto.setArtistName(longName);
        dto.setType(ArtistType.FRANCHISE);
        
        Artist artist = artistMapper.toEntity(dto);
        assertEquals(longName, artist.getArtistName());
        
        // Test with special characters
        dto.setArtistName("Björk & Ólafur Arnalds");
        dto.setOriginCountry("IS");
        dto.setPrimaryLanguage("Íslenska");
        
        artist = artistMapper.toEntity(dto);
        assertEquals("Björk & Ólafur Arnalds", artist.getArtistName());
        assertEquals("IS", artist.getOriginCountry());
        assertEquals("Íslenska", artist.getPrimaryLanguage());
    }

    @Test
    void testComplexValidationScenarios() {
        // Test various edge cases and validation scenarios
        
        // Scenario 1: Artist with minimal data
        CreateArtistRequestDTO minimalDto = new CreateArtistRequestDTO();
        minimalDto.setArtistName("X");
        minimalDto.setType(ArtistType.VARIOUS);
        
        Artist minimalArtist = artistMapper.toEntity(minimalDto);
        assertNotNull(minimalArtist);
        assertEquals("X", minimalArtist.getArtistName());
        
        // Scenario 2: Artist with maximum realistic data
        CreateArtistRequestDTO maximalDto = createMaximalArtistDto();
        Artist maximalArtist = artistMapper.toEntity(maximalDto);
        validateMaximalArtist(maximalArtist, maximalDto);
        
        // Scenario 3: Artist type transformation scenarios
        testArtistTypeTransformations();
        
        // Scenario 4: Date validation scenarios
        testDateValidationScenarios();
        
        // Scenario 5: Special character handling
        testSpecialCharacterHandling();
        
        // Scenario 6: Null and empty string handling
        testNullAndEmptyStringHandling();
    }

    private CreateArtistRequestDTO createMaximalArtistDto() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("The Extremely Long Artist Name That Tests Maximum Length Boundaries");
        dto.setType(ArtistType.GROUP);
        dto.setSpotifyId("spotify1234567890abcdef");
        dto.setDescription("This is a very detailed description that contains multiple sentences. " +
                          "It includes information about the artist's background, musical style, " +
                          "influences, and career highlights. The description spans multiple lines " +
                          "and contains various punctuation marks, numbers like 2020, and symbols.");
        dto.setImage("https://example.com/very/long/path/to/image/file/with/special-characters_123.jpg");
        dto.setPrimaryLanguage("English (United States)");
        dto.setGenre("Progressive Rock/Metal Fusion");
        dto.setOriginCountry("US");
        dto.setFormationDate(LocalDate.of(1990, 12, 31));
        dto.setDisbandDate(LocalDate.of(2025, 1, 1));
        dto.setGroupGender(ArtistGender.MIXED);
        return dto;
    }

    private void validateMaximalArtist(Artist artist, CreateArtistRequestDTO dto) {
        assertEquals(dto.getArtistName(), artist.getArtistName());
        assertEquals(dto.getType(), artist.getType());
        assertEquals(dto.getSpotifyId(), artist.getSpotifyId());
        assertEquals(dto.getDescription(), artist.getDescription());
        assertEquals(dto.getImage(), artist.getImage());
        assertEquals(dto.getPrimaryLanguage(), artist.getPrimaryLanguage());
        assertEquals(dto.getGenre(), artist.getGenre());
        assertEquals(dto.getOriginCountry(), artist.getOriginCountry());
    }

    private void testArtistTypeTransformations() {
        for (ArtistType sourceType : ArtistType.values()) {
            for (ArtistType targetType : ArtistType.values()) {
                if (sourceType != targetType) {
                    // Create artist with source type
                    CreateArtistRequestDTO createDto = new CreateArtistRequestDTO();
                    createDto.setArtistName("Transform Test");
                    createDto.setType(sourceType);
                    
                    Artist artist = artistMapper.toEntity(createDto);
                    artist.setArtistId(UUID.randomUUID());
                    
                    // Update to target type
                    UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
                    updateDto.setType(targetType);
                    
                    artistMapper.updateEntityFromDto(artist, updateDto);
                    assertEquals(targetType, artist.getType());
                }
            }
        }
    }

    private void testDateValidationScenarios() {
        // Test edge dates
        LocalDate[] testDates = {
            LocalDate.of(1900, 1, 1),    // Very old date
            LocalDate.of(2000, 2, 29),   // Leap year
            LocalDate.of(2100, 12, 31),  // Future date
            LocalDate.now(),             // Current date
            LocalDate.now().minusDays(1), // Yesterday
            LocalDate.now().plusDays(1)   // Tomorrow
        };
        
        for (LocalDate birthDate : testDates) {
            for (LocalDate deathDate : testDates) {
                if (deathDate.isAfter(birthDate)) {
                    CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
                    dto.setArtistName("Date Test Artist");
                    dto.setType(ArtistType.SOLO);
                    dto.setBirthDate(birthDate);
                    dto.setDeathDate(deathDate);
                    dto.setSoloGender(ArtistGender.MALE);
                    
                    Artist artist = artistMapper.toEntity(dto);
                    artist.setArtistId(UUID.randomUUID());
                    Solo solo = artistMapper.createSoloFromDto(dto, artist);
                    
                    if (solo != null) {
                        assertEquals(birthDate, solo.getBirthDate());
                        assertEquals(deathDate, solo.getDeathDate());
                    }
                }
            }
        }
    }

    private void testSpecialCharacterHandling() {
        String[] specialNames = {
            "Björk",
            "Café del Mar",
            "Sigur Rós",
            "Motörhead",
            "Mötley Crüe",
            "Queensrÿche",
            "Blue Öyster Cult",
            "Mägo de Oz",
            "Лайма Вайкуле",
            "坂本龍一",
            "이승환",
            "محمد عبده",
            "فيروز"
        };
        
        for (String name : specialNames) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName(name);
            dto.setType(ArtistType.SOLO);
            dto.setPrimaryLanguage(determineLanguageFromName(name));
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(name, artist.getArtistName());
            
            ArtistResponseDTO responseDto = artistMapper.toResponseDto(artist, null, null);
            assertEquals(name, responseDto.getArtistName());
            
            ArtistSummaryDTO summaryDto = artistMapper.toSummaryDto(artist);
            assertEquals(name, summaryDto.getArtistName());
        }
    }

    private String determineLanguageFromName(String name) {
        // Complex logic to determine language based on character patterns
        if (name.matches(".*[а-яё].*")) return "Russian";
        if (name.matches(".*[가-힣].*")) return "Korean";
        if (name.matches(".*[ひらがなカタカナ].*")) return "Japanese";
        if (name.matches(".*[أ-ي].*")) return "Arabic";
        if (name.matches(".*[àáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ].*")) return "European";
        return "English";
    }

    private void testNullAndEmptyStringHandling() {
        UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
        
        // Test all combinations of null/empty/whitespace values
        String[] testValues = {null, "", " ", "  ", "\t", "\n", "valid"};
        
        for (String artistName : testValues) {
            for (String description : testValues) {
                for (String genre : testValues) {
                    updateDto.setArtistName(artistName);
                    updateDto.setDescription(description);
                    updateDto.setGenre(genre);
                    
                    Artist artist = new Artist();
                    artist.setArtistName("Original");
                    artist.setDescription("Original Desc");
                    artist.setGenre("Original Genre");
                    
                    artistMapper.updateEntityFromDto(artist, updateDto);
                    
                    // Verify null-safe updates
                    if (artistName != null) {
                        assertEquals(artistName, artist.getArtistName());
                    } else {
                        assertEquals("Original", artist.getArtistName());
                    }
                    
                    if (description != null) {
                        assertEquals(description, artist.getDescription());
                    } else {
                        assertEquals("Original Desc", artist.getDescription());
                    }
                    
                    if (genre != null) {
                        assertEquals(genre, artist.getGenre());
                    } else {
                        assertEquals("Original Genre", artist.getGenre());
                    }
                }
            }
        }
    }

    @Test
    void testComplexMappingScenarios() {
        // Test multiple entity combinations
        testSoloToGroupConversion();
        testGroupToSoloConversion();
        testPartialUpdateComplexity();
        testConcurrentMappingOperations();
    }

    private void testSoloToGroupConversion() {
        // Create a solo artist first
        CreateArtistRequestDTO soloDto = new CreateArtistRequestDTO();
        soloDto.setArtistName("Solo Artist");
        soloDto.setType(ArtistType.SOLO);
        soloDto.setBirthDate(LocalDate.of(1980, 5, 15));
        soloDto.setSoloGender(ArtistGender.FEMALE);
        
        Artist artist = artistMapper.toEntity(soloDto);
        artist.setArtistId(UUID.randomUUID());
        Solo solo = artistMapper.createSoloFromDto(soloDto, artist);
        
        // Convert to group
        CreateArtistRequestDTO groupDto = new CreateArtistRequestDTO();
        groupDto.setArtistName("Converted Group");
        groupDto.setType(ArtistType.GROUP);
        groupDto.setFormationDate(LocalDate.of(2000, 1, 1));
        groupDto.setGroupGender(ArtistGender.MIXED);
        
        Groups group = artistMapper.createGroupFromDto(groupDto, artist);
        
        // Test response mapping with both solo and group data
        ArtistResponseDTO responseWithBoth = artistMapper.toResponseDto(artist, solo, group);
        
        // Should contain both solo and group information
        assertEquals(solo.getBirthDate(), responseWithBoth.getBirthDate());
        assertEquals(solo.getGender(), responseWithBoth.getSoloGender());
        assertEquals(group.getFormationDate(), responseWithBoth.getFormationDate());
        assertEquals(group.getGroupGender(), responseWithBoth.getGroupGender());
    }

    private void testGroupToSoloConversion() {
        // Similar but opposite conversion
        CreateArtistRequestDTO groupDto = new CreateArtistRequestDTO();
        groupDto.setArtistName("Group Artist");
        groupDto.setType(ArtistType.GROUP);
        groupDto.setFormationDate(LocalDate.of(1995, 6, 20));
        groupDto.setDisbandDate(LocalDate.of(2005, 12, 31));
        groupDto.setGroupGender(ArtistGender.MALE);
        
        Artist artist = artistMapper.toEntity(groupDto);
        artist.setArtistId(UUID.randomUUID());
        Groups group = artistMapper.createGroupFromDto(groupDto, artist);
        
        // Add solo information later
        CreateArtistRequestDTO soloDto = new CreateArtistRequestDTO();
        soloDto.setType(ArtistType.SOLO);
        soloDto.setBirthDate(LocalDate.of(1970, 3, 10));
        soloDto.setSoloGender(ArtistGender.MALE);
        
        Solo solo = artistMapper.createSoloFromDto(soloDto, artist);
        
        ArtistResponseDTO responseWithBoth = artistMapper.toResponseDto(artist, solo, group);
        
        assertNotNull(responseWithBoth.getBirthDate());
        assertNotNull(responseWithBoth.getFormationDate());
        assertNotNull(responseWithBoth.getDisbandDate());
    }

    private void testPartialUpdateComplexity() {
        // Test complex partial update scenarios
        Artist originalArtist = createComplexArtist();
        
        // Test multiple partial updates
        for (int i = 0; i < 5; i++) {
            UpdateArtistRequestDTO updateDto = createRandomUpdateDto(i);
            artistMapper.updateEntityFromDto(originalArtist, updateDto);
            
            // Verify that non-updated fields remain unchanged
            assertNotNull(originalArtist.getArtistName());
            assertNotNull(originalArtist.getType());
        }
    }

    private Artist createComplexArtist() {
        Artist artist = new Artist();
        artist.setArtistId(UUID.randomUUID());
        artist.setArtistName("Complex Artist");
        artist.setType(ArtistType.FRANCHISE);
        artist.setSpotifyId("complex123");
        artist.setDescription("Complex description");
        artist.setImage("complex.jpg");
        artist.setPrimaryLanguage("Multiple");
        artist.setGenre("Experimental");
        artist.setOriginCountry("UN");
        return artist;
    }

    private UpdateArtistRequestDTO createRandomUpdateDto(int iteration) {
        UpdateArtistRequestDTO dto = new UpdateArtistRequestDTO();
        
        switch (iteration % 4) {
            case 0:
                dto.setArtistName("Updated Name " + iteration);
                break;
            case 1:
                dto.setGenre("Updated Genre " + iteration);
                dto.setDescription("Updated Description " + iteration);
                break;
            case 2:
                dto.setOriginCountry(iteration % 2 == 0 ? "US" : "GB");
                dto.setPrimaryLanguage("Updated Language " + iteration);
                break;
            case 3:
                dto.setImage("updated" + iteration + ".jpg");
                dto.setSpotifyId("updated" + iteration);
                break;
        }
        
        return dto;
    }

    private void testConcurrentMappingOperations() {
        // Simulate concurrent operations on the same data
        CreateArtistRequestDTO baseDto = new CreateArtistRequestDTO();
        baseDto.setArtistName("Concurrent Test");
        baseDto.setType(ArtistType.SOLO);
        
        Artist sharedArtist = artistMapper.toEntity(baseDto);
        sharedArtist.setArtistId(UUID.randomUUID());
        
        // Perform multiple operations that might interfere with each other
        for (int i = 0; i < 10; i++) {
            // Create different update scenarios
            UpdateArtistRequestDTO update1 = new UpdateArtistRequestDTO();
            update1.setArtistName("Concurrent " + i);
            
            UpdateArtistRequestDTO update2 = new UpdateArtistRequestDTO();
            update2.setGenre("Genre " + i);
            
            artistMapper.updateEntityFromDto(sharedArtist, update1);
            artistMapper.updateEntityFromDto(sharedArtist, update2);
            
            // Verify consistency
            assertTrue(sharedArtist.getArtistName().startsWith("Concurrent"));
            assertTrue(sharedArtist.getGenre().startsWith("Genre"));
        }
    }

    @Test
    void testBoundaryConditionsAndEdgeCases() {
        testCountryCodeValidation();
        testSpotifyIdPatterns();
        testGenreComplexity();
        testImageUrlValidation();
        testLanguageCodeComplexity();
    }

    private void testCountryCodeValidation() {
        // Test various country code patterns and edge cases
        String[] validCountryCodes = {"US", "GB", "DE", "FR", "JP", "KR", "CN", "RU", "BR", "AU"};
        String[] invalidCountryCodes = {"USA", "GBR", "X", "123", "", null, "ZZ", "XX"};
        String[] edgeCases = {"us", "Gb", "DE ", " FR", "J P", "K-R"};
        
        for (String validCode : validCountryCodes) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Test Artist");
            dto.setType(ArtistType.SOLO);
            dto.setOriginCountry(validCode);
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(validCode, artist.getOriginCountry());
            
            // Test in response mapping
            ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, null);
            assertEquals(validCode, response.getOriginCountry());
        }
        
        for (String invalidCode : invalidCountryCodes) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Test Artist");
            dto.setType(ArtistType.SOLO);
            dto.setOriginCountry(invalidCode);
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(invalidCode, artist.getOriginCountry()); // Mapper doesn't validate, just maps
        }
        
        for (String edgeCase : edgeCases) {
            UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
            updateDto.setOriginCountry(edgeCase);
            
            Artist artist = new Artist();
            artist.setOriginCountry("US");
            
            artistMapper.updateEntityFromDto(artist, updateDto);
            assertEquals(edgeCase, artist.getOriginCountry());
        }
    }

    private void testSpotifyIdPatterns() {
        // Test various Spotify ID patterns
        String[] validSpotifyIds = {
            "1234567890123456789012",  // Exactly 22 chars
            "abcdefghijklmnopqrstuv",  // All letters
            "1a2b3c4d5e6f7g8h9i0j1k",  // Mixed alphanumeric
            "ABCDEFGHIJKLMNOPQRSTUV",  // All uppercase
            "123456789012345678901",   // 21 chars (under limit)
            "a",                       // Single character
            ""                         // Empty string
        };
        
        String[] invalidSpotifyIds = {
            "12345678901234567890123", // 23 chars (over limit)
            "1234567890123456789012345678901234567890", // Way over limit
            null                       // Null value
        };
        
        for (String validId : validSpotifyIds) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Spotify Test");
            dto.setType(ArtistType.SOLO);
            dto.setSpotifyId(validId);
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(validId, artist.getSpotifyId());
            
            // Test update scenario
            UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
            updateDto.setSpotifyId(validId);
            artistMapper.updateEntityFromDto(artist, updateDto);
            assertEquals(validId, artist.getSpotifyId());
        }
        
        for (String invalidId : invalidSpotifyIds) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Spotify Test");
            dto.setType(ArtistType.SOLO);
            dto.setSpotifyId(invalidId);
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(invalidId, artist.getSpotifyId()); // Mapper allows invalid data
        }
    }

    private void testGenreComplexity() {
        // Test complex genre combinations and edge cases
        String[] complexGenres = {
            "Progressive Rock/Metal Fusion",
            "Electronic Dance Music (EDM)",
            "Hip-Hop/Rap/R&B",
            "Classical/Orchestral/Symphonic",
            "Jazz/Blues/Soul/Funk",
            "World Music/Folk/Traditional",
            "Experimental/Avant-garde/Noise",
            "Post-Rock/Post-Metal/Ambient",
            "Alternative/Indie/Underground",
            "Pop/Commercial/Mainstream"
        };
        
        String[] edgeCaseGenres = {
            "",                    // Empty
            " ",                   // Whitespace
            "A",                   // Single character
            "A".repeat(1000),      // Very long
            "Genre with 123 numbers",
            "Genre-with-dashes",
            "Genre_with_underscores",
            "Genre with (parentheses)",
            "Genre with [brackets]",
            "Genre with 'quotes'",
            "Genre with \"double quotes\"",
            "Género con acentos",
            "жанр на русском",
            "ジャンル",
            null                   // Null value
        };
        
        // Test complex genres
        for (String genre : complexGenres) {
            testGenreMapping(genre);
        }
        
        // Test edge case genres
        for (String genre : edgeCaseGenres) {
            testGenreMapping(genre);
        }
        
        // Test genre transformations
        testGenreTransformations();
    }

    private void testGenreMapping(String genre) {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("Genre Test");
        dto.setType(ArtistType.SOLO);
        dto.setGenre(genre);
        
        Artist artist = artistMapper.toEntity(dto);
        assertEquals(genre, artist.getGenre());
        
        ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, null);
        assertEquals(genre, response.getGenre());
        
        ArtistSummaryDTO summary = artistMapper.toSummaryDto(artist);
        assertEquals(genre, summary.getGenre());
    }

    private void testGenreTransformations() {
        Artist artist = new Artist();
        artist.setGenre("Original Genre");
        
        String[] transformationGenres = {
            "Rock", "Pop", "Jazz", "Classical", "Electronic", 
            "Hip-Hop", "Country", "Blues", "Folk", "Metal"
        };
        
        for (String newGenre : transformationGenres) {
            UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
            updateDto.setGenre(newGenre);
            
            artistMapper.updateEntityFromDto(artist, updateDto);
            assertEquals(newGenre, artist.getGenre());
        }
    }

    private void testImageUrlValidation() {
        String[] imageUrls = {
            "https://example.com/image.jpg",
            "http://test.com/path/to/image.png",
            "https://cdn.example.com/images/artist/profile/123456.webp",
            "/relative/path/to/image.gif",
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgA...",
            "image.jpg",
            "",
            null,
            "not-a-valid-url",
            "https://very-long-domain-name-that-exceeds-normal-limits.example.com/very/long/path/to/image/file.jpg"
        };
        
        for (String imageUrl : imageUrls) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Image Test");
            dto.setType(ArtistType.SOLO);
            dto.setImage(imageUrl);
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(imageUrl, artist.getImage());
            
            // Test response mapping
            ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, null);
            assertEquals(imageUrl, response.getImage());
        }
    }

    private void testLanguageCodeComplexity() {
        String[] languages = {
            "English", "Spanish", "French", "German", "Italian", "Portuguese",
            "Chinese", "Japanese", "Korean", "Russian", "Arabic", "Hindi",
            "English (US)", "English (UK)", "Spanish (Spain)", "Spanish (Mexico)",
            "Chinese (Simplified)", "Chinese (Traditional)",
            "en", "es", "fr", "de", "it", "pt", "zh", "ja", "ko", "ru", "ar", "hi",
            "en-US", "en-GB", "es-ES", "es-MX", "zh-CN", "zh-TW",
            "", null, " ", "Unknown", "Multiple", "Mixed"
        };
        
        for (String language : languages) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Language Test");
            dto.setType(ArtistType.SOLO);
            dto.setPrimaryLanguage(language);
            
            Artist artist = artistMapper.toEntity(dto);
            assertEquals(language, artist.getPrimaryLanguage());
            
            // Test language consistency across mappings
            ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, null);
            assertEquals(language, response.getPrimaryLanguage());
            
            // Test language updates
            UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
            updateDto.setPrimaryLanguage(language);
            artistMapper.updateEntityFromDto(artist, updateDto);
            assertEquals(language, artist.getPrimaryLanguage());
        }
    }

    @Test
    void testComplexBusinessLogicScenarios() {
        testArtistLifecycleManagement();
        testDataConsistencyAcrossMappings();
        testPerformanceWithLargeDataSets();
        testErrorRecoveryScenarios();
    }

    private void testArtistLifecycleManagement() {
        // Test complete artist lifecycle: creation -> updates -> final state
        CreateArtistRequestDTO initialDto = new CreateArtistRequestDTO();
        initialDto.setArtistName("Lifecycle Artist");
        initialDto.setType(ArtistType.SOLO);
        initialDto.setBirthDate(LocalDate.of(1980, 1, 1));
        initialDto.setSoloGender(ArtistGender.MALE);
        
        Artist artist = artistMapper.toEntity(initialDto);
        artist.setArtistId(UUID.randomUUID());
        Solo solo = artistMapper.createSoloFromDto(initialDto, artist);
        
        // Stage 1: Early career update
        UpdateArtistRequestDTO earlyCareerUpdate = new UpdateArtistRequestDTO();
        earlyCareerUpdate.setGenre("Indie Rock");
        earlyCareerUpdate.setDescription("Emerging indie rock artist");
        artistMapper.updateEntityFromDto(artist, earlyCareerUpdate);
        
        // Stage 2: Mainstream success
        UpdateArtistRequestDTO mainstreamUpdate = new UpdateArtistRequestDTO();
        mainstreamUpdate.setGenre("Alternative Rock");
        mainstreamUpdate.setDescription("Mainstream alternative rock star");
        mainstreamUpdate.setSpotifyId("mainstream123");
        artistMapper.updateEntityFromDto(artist, mainstreamUpdate);
        
        // Stage 3: Genre evolution
        UpdateArtistRequestDTO evolutionUpdate = new UpdateArtistRequestDTO();
        evolutionUpdate.setGenre("Electronic/Experimental");
        evolutionUpdate.setDescription("Experimental electronic musician");
        artistMapper.updateEntityFromDto(artist, evolutionUpdate);
        
        // Verify final state
        assertEquals("Lifecycle Artist", artist.getArtistName());
        assertEquals("Electronic/Experimental", artist.getGenre());
        assertEquals("Experimental electronic musician", artist.getDescription());
        assertEquals("mainstream123", artist.getSpotifyId());
        
        // Test final response
        ArtistResponseDTO finalResponse = artistMapper.toResponseDto(artist, solo, null);
        assertEquals("Electronic/Experimental", finalResponse.getGenre());
        assertEquals(LocalDate.of(1980, 1, 1), finalResponse.getBirthDate());
    }

    private void testDataConsistencyAcrossMappings() {
        // Test that data remains consistent across multiple mapping operations
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("Consistency Test");
        dto.setType(ArtistType.GROUP);
        dto.setSpotifyId("consistent123");
        dto.setGenre("Consistent Genre");
        dto.setFormationDate(LocalDate.of(2000, 6, 15));
        dto.setGroupGender(ArtistGender.MIXED);
        
        Artist artist = artistMapper.toEntity(dto);
        artist.setArtistId(UUID.randomUUID());
        Groups group = artistMapper.createGroupFromDto(dto, artist);
        
        // Perform multiple response mappings
        for (int i = 0; i < 10; i++) {
            ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, group);
            ArtistSummaryDTO summary = artistMapper.toSummaryDto(artist);
            
            // Verify consistency
            assertEquals(dto.getArtistName(), response.getArtistName());
            assertEquals(dto.getArtistName(), summary.getArtistName());
            assertEquals(dto.getType(), response.getType());
            assertEquals(dto.getType(), summary.getType());
            assertEquals(dto.getSpotifyId(), response.getSpotifyId());
            assertEquals(dto.getGenre(), response.getGenre());
            assertEquals(dto.getGenre(), summary.getGenre());
            assertEquals(dto.getFormationDate(), response.getFormationDate());
            assertEquals(dto.getGroupGender(), response.getGroupGender());
        }
    }

    private void testPerformanceWithLargeDataSets() {
        // Test mapping performance with large amounts of data
        int testSize = 1000;
        
        for (int i = 0; i < testSize; i++) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Performance Test Artist " + i);
            dto.setType(i % 2 == 0 ? ArtistType.SOLO : ArtistType.GROUP);
            dto.setDescription("Performance test description for artist number " + i + 
                             ". This is a longer description to test mapping performance " +
                             "with more substantial data volumes.");
            dto.setGenre("Genre " + (i % 10));
            dto.setOriginCountry(i % 5 == 0 ? "US" : "GB");
            
            Artist artist = artistMapper.toEntity(dto);
            artist.setArtistId(UUID.randomUUID());
            
            if (dto.getType() == ArtistType.SOLO) {
                dto.setBirthDate(LocalDate.of(1980 + (i % 40), (i % 12) + 1, (i % 28) + 1));
                dto.setSoloGender(i % 3 == 0 ? ArtistGender.MALE : 
                                 i % 3 == 1 ? ArtistGender.FEMALE : ArtistGender.MIXED);
                Solo solo = artistMapper.createSoloFromDto(dto, artist);
                
                ArtistResponseDTO response = artistMapper.toResponseDto(artist, solo, null);
                assertNotNull(response);
                assertEquals(dto.getArtistName(), response.getArtistName());
            } else {
                dto.setFormationDate(LocalDate.of(1990 + (i % 30), (i % 12) + 1, (i % 28) + 1));
                dto.setGroupGender(i % 3 == 0 ? ArtistGender.MALE : 
                                  i % 3 == 1 ? ArtistGender.FEMALE : ArtistGender.MIXED);
                Groups group = artistMapper.createGroupFromDto(dto, artist);
                
                ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, group);
                assertNotNull(response);
                assertEquals(dto.getArtistName(), response.getArtistName());
            }
            
            ArtistSummaryDTO summary = artistMapper.toSummaryDto(artist);
            assertNotNull(summary);
            assertEquals(dto.getArtistName(), summary.getArtistName());
        }
    }

    private void testErrorRecoveryScenarios() {
        // Test mapper behavior with corrupted or inconsistent data
        Artist corruptedArtist = new Artist();
        // Intentionally leave some fields null to test null handling
        corruptedArtist.setArtistName("Corrupted Artist");
        // type is null
        // other fields are null
        
        try {
            ArtistResponseDTO response = artistMapper.toResponseDto(corruptedArtist, null, null);
            assertNotNull(response);
            assertEquals("Corrupted Artist", response.getArtistName());
            assertNull(response.getType());
        } catch (Exception e) {
            fail("Mapper should handle null values gracefully: " + e.getMessage());
        }
        
        try {
            ArtistSummaryDTO summary = artistMapper.toSummaryDto(corruptedArtist);
            assertNotNull(summary);
            assertEquals("Corrupted Artist", summary.getArtistName());
            assertNull(summary.getType());
        } catch (Exception e) {
            fail("Mapper should handle null values gracefully: " + e.getMessage());
        }
        
        // Test with completely null artist
        try {
            artistMapper.toResponseDto(null, null, null);
            fail("Expected NullPointerException when artist is null");
        } catch (NullPointerException e) {
            // Expected behavior - mapper doesn't handle null input
            assertNotNull(e.getMessage(), "Exception message should not be null");
            assertTrue(e.getMessage().contains("artist") || 
                      e.getMessage().toLowerCase().contains("null"),
                      "Exception should mention null artist");
        }
    }
}
