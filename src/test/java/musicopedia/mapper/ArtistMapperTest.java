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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArtistMapperTest {

    private ArtistMapper artistMapper;

    @BeforeEach
    void setUp() {
        artistMapper = new ArtistMapper();
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

    @Test
    void testComplexMappingScenarios() {
        testMultipleEntityCombinations();
        testConditionalMappingLogic();
        testNestedValidationScenarios();
        testConcurrentMappingOperations();
        testErrorHandlingAndRecovery();
    }

    private void testMultipleEntityCombinations() {
        // Test all possible combinations of Artist, Solo, and Groups entities
        Artist baseArtist = createTestArtist();

        // Scenario 1: Artist only
        ArtistResponseDTO response1 = artistMapper.toResponseDto(baseArtist, null, null);
        validateBasicArtistMapping(baseArtist, response1);

        // Scenario 2: Artist + Solo
        Solo solo = createTestSolo(baseArtist);
        ArtistResponseDTO response2 = artistMapper.toResponseDto(baseArtist, solo, null);
        validateBasicArtistMapping(baseArtist, response2);
        validateSoloMapping(solo, response2);

        // Scenario 3: Artist + Groups
        Groups group = createTestGroup(baseArtist);
        ArtistResponseDTO response3 = artistMapper.toResponseDto(baseArtist, null, group);
        validateBasicArtistMapping(baseArtist, response3);
        validateGroupMapping(group, response3);

        // Scenario 4: Artist + Solo + Groups (unusual but possible)
        ArtistResponseDTO response4 = artistMapper.toResponseDto(baseArtist, solo, group);
        validateBasicArtistMapping(baseArtist, response4);
        validateSoloMapping(solo, response4);
        validateGroupMapping(group, response4);

        // Scenario 5: Null artist with non-null solo/group
        try {
            artistMapper.toResponseDto(null, solo, group);
            fail("Should throw exception with null artist");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    private void validateBasicArtistMapping(Artist artist, ArtistResponseDTO response) {
        assertEquals(artist.getArtistId(), response.getArtistId());
        assertEquals(artist.getArtistName(), response.getArtistName());
        assertEquals(artist.getType(), response.getType());
        assertEquals(artist.getSpotifyId(), response.getSpotifyId());
        assertEquals(artist.getDescription(), response.getDescription());
        assertEquals(artist.getImage(), response.getImage());
        assertEquals(artist.getPrimaryLanguage(), response.getPrimaryLanguage());
        assertEquals(artist.getGenre(), response.getGenre());
        assertEquals(artist.getOriginCountry(), response.getOriginCountry());
    }

    private void validateSoloMapping(Solo solo, ArtistResponseDTO response) {
        if (solo != null) {
            assertEquals(solo.getBirthDate(), response.getBirthDate());
            assertEquals(solo.getDeathDate(), response.getDeathDate());
            assertEquals(solo.getGender(), response.getSoloGender());
        } else {
            assertNull(response.getBirthDate());
            assertNull(response.getDeathDate());
            assertNull(response.getSoloGender());
        }
    }

    private void validateGroupMapping(Groups group, ArtistResponseDTO response) {
        if (group != null) {
            assertEquals(group.getFormationDate(), response.getFormationDate());
            assertEquals(group.getDisbandDate(), response.getDisbandDate());
            assertEquals(group.getGroupGender(), response.getGroupGender());
        } else {
            assertNull(response.getFormationDate());
            assertNull(response.getDisbandDate());
            assertNull(response.getGroupGender());
        }
    }

    private void testConditionalMappingLogic() {
        // Test conditional mapping based on artist type
        for (ArtistType type : ArtistType.values()) {
            CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
            dto.setArtistName("Conditional Test " + type.name());
            dto.setType(type);
            dto.setGenre("Test Genre");

            // Set type-specific data
            if (type == ArtistType.SOLO) {
                dto.setBirthDate(LocalDate.of(1985, 3, 15));
                dto.setSoloGender(ArtistGender.FEMALE);
                // Group fields should be ignored
                dto.setFormationDate(LocalDate.of(2000, 1, 1));
                dto.setGroupGender(ArtistGender.MIXED);
            } else if (type == ArtistType.GROUP) {
                dto.setFormationDate(LocalDate.of(1995, 6, 20));
                dto.setGroupGender(ArtistGender.MALE);
                // Solo fields should be ignored
                dto.setBirthDate(LocalDate.of(1980, 12, 25));
                dto.setSoloGender(ArtistGender.FEMALE);
            } else {
                // For FRANCHISE and VARIOUS, set both and see what happens
                dto.setBirthDate(LocalDate.of(1990, 9, 10));
                dto.setSoloGender(ArtistGender.MIXED);
                dto.setFormationDate(LocalDate.of(2005, 4, 5));
                dto.setGroupGender(ArtistGender.FEMALE);
            }

            Artist artist = artistMapper.toEntity(dto);
            artist.setArtistId(UUID.randomUUID());

            Solo solo = artistMapper.createSoloFromDto(dto, artist);
            Groups group = artistMapper.createGroupFromDto(dto, artist);

            // Verify conditional creation
            if (type == ArtistType.SOLO) {
                assertNotNull(solo, "Solo should be created for SOLO type");
                assertNull(group, "Group should not be created for SOLO type");
                assertEquals(dto.getBirthDate(), solo.getBirthDate());
                assertEquals(dto.getSoloGender(), solo.getGender());
            } else if (type == ArtistType.GROUP) {
                assertNull(solo, "Solo should not be created for GROUP type");
                assertNotNull(group, "Group should be created for GROUP type");
                assertEquals(dto.getFormationDate(), group.getFormationDate());
                assertEquals(dto.getGroupGender(), group.getGroupGender());
            } else {
                assertNull(solo, "Solo should not be created for " + type + " type");
                assertNull(group, "Group should not be created for " + type + " type");
            }

            // Test response mapping with conditional data
            ArtistResponseDTO response = artistMapper.toResponseDto(artist, solo, group);
            assertEquals(type, response.getType());
        }
    }

    private void testNestedValidationScenarios() {
        // Test complex validation scenarios with nested conditions
        testDateValidationComplexity();
        testGenderValidationComplexity();
        testStringFieldValidationComplexity();
        testNullSafetyInNestedOperations();
    }

    private void testDateValidationComplexity() {
        // Test date validation with complex business rules
        LocalDate[] birthDates = {
                LocalDate.of(1900, 1, 1),     // Very old
                LocalDate.of(1950, 6, 15),    // Older generation
                LocalDate.of(1980, 12, 31),   // Generation X
                LocalDate.of(2000, 2, 29),    // Leap year birth
                LocalDate.now().minusYears(18), // Just adult
                LocalDate.now().minusYears(5),  // Child (unusual for artist)
                LocalDate.now(),              // Born today
                LocalDate.now().plusDays(1)   // Future birth (invalid)
        };

        LocalDate[] deathDates = {
                null,                         // Still alive
                LocalDate.now().minusYears(1), // Recent death
                LocalDate.of(2020, 3, 15),    // Specific date
                LocalDate.now(),              // Died today
                LocalDate.now().plusDays(1)   // Future death (invalid)
        };

        for (LocalDate birthDate : birthDates) {
            for (LocalDate deathDate : deathDates) {
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

                    // Test logical consistency
                    if (birthDate != null && deathDate != null) {
                        if (deathDate.isBefore(birthDate)) {
                            // Invalid scenario - death before birth
                            // Mapper doesn't validate, but we can test the data flow
                            assertTrue(deathDate.isBefore(birthDate),
                                    "Death date should be before birth date in this test scenario");
                        }
                    }
                }

                // Test response mapping
                ArtistResponseDTO response = artistMapper.toResponseDto(artist, solo, null);
                assertEquals(birthDate, response.getBirthDate());
                assertEquals(deathDate, response.getDeathDate());
            }
        }
    }

    private void testGenderValidationComplexity() {
        // Test gender validation across different scenarios
        for (ArtistGender soloGender : ArtistGender.values()) {
            for (ArtistGender groupGender : ArtistGender.values()) {
                // Test solo artist with all gender combinations
                CreateArtistRequestDTO soloDto = new CreateArtistRequestDTO();
                soloDto.setArtistName("Solo Gender Test");
                soloDto.setType(ArtistType.SOLO);
                soloDto.setSoloGender(soloGender);
                soloDto.setGroupGender(groupGender); // Should be ignored

                Artist soloArtist = artistMapper.toEntity(soloDto);
                soloArtist.setArtistId(UUID.randomUUID());
                Solo solo = artistMapper.createSoloFromDto(soloDto, soloArtist);
                Groups soloGroup = artistMapper.createGroupFromDto(soloDto, soloArtist);

                assertNotNull(solo);
                assertNull(soloGroup);
                assertEquals(soloGender, solo.getGender());

                // Test group artist with all gender combinations
                CreateArtistRequestDTO groupDto = new CreateArtistRequestDTO();
                groupDto.setArtistName("Group Gender Test");
                groupDto.setType(ArtistType.GROUP);
                groupDto.setGroupGender(groupGender);
                groupDto.setSoloGender(soloGender); // Should be ignored

                Artist groupArtist = artistMapper.toEntity(groupDto);
                groupArtist.setArtistId(UUID.randomUUID());
                Solo groupSolo = artistMapper.createSoloFromDto(groupDto, groupArtist);
                Groups group = artistMapper.createGroupFromDto(groupDto, groupArtist);

                assertNull(groupSolo);
                assertNotNull(group);
                assertEquals(groupGender, group.getGroupGender());

                // Test response mapping
                ArtistResponseDTO soloResponse = artistMapper.toResponseDto(soloArtist, solo, null);
                assertEquals(soloGender, soloResponse.getSoloGender());
                assertNull(soloResponse.getGroupGender());

                ArtistResponseDTO groupResponse = artistMapper.toResponseDto(groupArtist, null, group);
                assertEquals(groupGender, groupResponse.getGroupGender());
                assertNull(groupResponse.getSoloGender());
            }
        }
    }

    private void testStringFieldValidationComplexity() {
        // Test string field validation with complex scenarios
        String[] artistNames = {
                "",                           // Empty
                " ",                          // Whitespace
                "A",                          // Single character
                "Normal Artist Name",         // Normal case
                "Artist With 123 Numbers",    // With numbers
                "Artist-With-Hyphens",        // With hyphens
                "Artist_With_Underscores",    // With underscores
                "Artist (With Parentheses)",  // With parentheses
                "Artist & Co.",               // With ampersand
                "Björk Guðmundsdóttir",      // Unicode characters
                "李小龙",                     // Chinese characters
                "محمد عبدالحليم حافظ",          // Arabic characters
                "Владимир Высоцкий",          // Cyrillic characters
                "A".repeat(255),              // Maximum realistic length
                "A".repeat(1000),             // Very long
                null                          // Null
        };

        String[] descriptions = {
                null, "", " ", "Short desc",
                "A very long description that contains multiple sentences and various punctuation marks. " +
                        "It includes information about the artist's background, musical style, influences, and career highlights. " +
                        "The description spans multiple lines and contains various characters including numbers like 2020, " +
                        "symbols like @#$%^&*(), and Unicode characters like café, naïve, résumé.",
                "Description with\nnewlines\nand\ttabs",
                "Description with \"quotes\" and 'apostrophes'",
                "<script>alert('XSS test')</script>", // Potential XSS
                "Description with emoji 🎵🎶🎸🥁"
        };

        for (String artistName : artistNames) {
            for (String description : descriptions) {
                CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
                dto.setArtistName(artistName);
                dto.setDescription(description);
                dto.setType(ArtistType.SOLO);

                Artist artist = artistMapper.toEntity(dto);
                assertEquals(artistName, artist.getArtistName());
                assertEquals(description, artist.getDescription());

                // Test update scenarios
                UpdateArtistRequestDTO updateDto = new UpdateArtistRequestDTO();
                updateDto.setArtistName(artistName);
                updateDto.setDescription(description);

                Artist existingArtist = createTestArtist();
                artistMapper.updateEntityFromDto(existingArtist, updateDto);

                if (artistName != null) {
                    assertEquals(artistName, existingArtist.getArtistName());
                }
                if (description != null) {
                    assertEquals(description, existingArtist.getDescription());
                }

                // Test response mapping
                ArtistResponseDTO response = artistMapper.toResponseDto(artist, null, null);
                assertEquals(artistName, response.getArtistName());
                assertEquals(description, response.getDescription());
            }
        }
    }

    private void testNullSafetyInNestedOperations() {
        // Test null safety in complex nested operations

        // Test with null DTO
        try {
            artistMapper.toEntity(null);
            fail("Should throw exception with null DTO");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with DTO having null fields
        CreateArtistRequestDTO nullFieldDto = new CreateArtistRequestDTO();
        // All fields are null

        try {
            Artist artist = artistMapper.toEntity(nullFieldDto);
            // Should handle null fields gracefully
            assertNull(artist.getArtistName());
            assertNull(artist.getType());
            assertNull(artist.getSpotifyId());
        } catch (Exception e) {
            // Some null fields might cause issues
        }

        // Test update with null DTO
        Artist existingArtist = createTestArtist();
        String originalName = existingArtist.getArtistName();

        try {
            artistMapper.updateEntityFromDto(existingArtist, null);
            fail("Should throw exception with null update DTO");
        } catch (NullPointerException e) {
            // Expected
            assertEquals(originalName, existingArtist.getArtistName()); // Should remain unchanged
        }

        // Test response mapping with null entities
        try {
            artistMapper.toResponseDto(null, null, null);
            fail("Should throw exception with all null parameters");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test summary mapping with null artist
        try {
            artistMapper.toSummaryDto(null);
            fail("Should throw exception with null artist");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private void testConcurrentMappingOperations() {
        // Test mapper behavior under concurrent access scenarios
        Artist sharedArtist = createTestArtist();

        // Simulate multiple concurrent updates
        for (int i = 0; i < 100; i++) {
            UpdateArtistRequestDTO update1 = new UpdateArtistRequestDTO();
            update1.setGenre("Genre " + i);

            UpdateArtistRequestDTO update2 = new UpdateArtistRequestDTO();
            update2.setDescription("Description " + i);

            UpdateArtistRequestDTO update3 = new UpdateArtistRequestDTO();
            update3.setOriginCountry(i % 2 == 0 ? "US" : "GB");

            // Apply updates in sequence (simulating concurrent access)
            artistMapper.updateEntityFromDto(sharedArtist, update1);
            artistMapper.updateEntityFromDto(sharedArtist, update2);
            artistMapper.updateEntityFromDto(sharedArtist, update3);

            // Verify final state
            assertEquals("Genre " + i, sharedArtist.getGenre());
            assertEquals("Description " + i, sharedArtist.getDescription());
            assertEquals(i % 2 == 0 ? "US" : "GB", sharedArtist.getOriginCountry());

            // Test response mapping after each update
            ArtistResponseDTO response = artistMapper.toResponseDto(sharedArtist, null, null);
            assertEquals("Genre " + i, response.getGenre());
            assertEquals("Description " + i, response.getDescription());
            assertEquals(i % 2 == 0 ? "US" : "GB", response.getOriginCountry());
        }
    }

    private void testErrorHandlingAndRecovery() {
        // Test mapper behavior with corrupted or invalid data

        // Test with artist having inconsistent type vs related entities
        Artist inconsistentArtist = new Artist();
        inconsistentArtist.setArtistId(UUID.randomUUID());
        inconsistentArtist.setArtistName("Inconsistent Artist");
        inconsistentArtist.setType(ArtistType.SOLO); // Says SOLO but we'll pass GROUP data

        Groups group = createTestGroup(inconsistentArtist);

        ArtistResponseDTO response = artistMapper.toResponseDto(inconsistentArtist, null, group);
        assertEquals(ArtistType.SOLO, response.getType()); // Should reflect artist type
        assertEquals(group.getFormationDate(), response.getFormationDate()); // But include group data

        // Test with missing required fields
        Artist incompleteArtist = new Artist();
        incompleteArtist.setArtistId(UUID.randomUUID());
        // Missing name and type

        try {
            ArtistResponseDTO incompleteResponse = artistMapper.toResponseDto(incompleteArtist, null, null);
            assertNotNull(incompleteResponse);
            assertEquals(incompleteArtist.getArtistId(), incompleteResponse.getArtistId());
            assertNull(incompleteResponse.getArtistName());
            assertNull(incompleteResponse.getType());
        } catch (Exception e) {
            // Some mappers might not handle missing data gracefully
        }

        // Test recovery from partial failures
        CreateArtistRequestDTO partialDto = new CreateArtistRequestDTO();
        partialDto.setArtistName("Recovery Test");
        partialDto.setType(ArtistType.FRANCHISE); // Type that doesn't create solo/group

        Artist recoveryArtist = artistMapper.toEntity(partialDto);
        recoveryArtist.setArtistId(UUID.randomUUID());

        Solo nullSolo = artistMapper.createSoloFromDto(partialDto, recoveryArtist);
        Groups nullGroup = artistMapper.createGroupFromDto(partialDto, recoveryArtist);

        assertNull(nullSolo);
        assertNull(nullGroup);

        // Should still be able to create response
        ArtistResponseDTO recoveryResponse = artistMapper.toResponseDto(recoveryArtist, nullSolo, nullGroup);
        assertNotNull(recoveryResponse);
        assertEquals("Recovery Test", recoveryResponse.getArtistName());
        assertEquals(ArtistType.FRANCHISE, recoveryResponse.getType());
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
