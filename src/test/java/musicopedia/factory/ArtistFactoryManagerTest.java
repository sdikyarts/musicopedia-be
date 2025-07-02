package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArtistFactoryManagerTest {

    @InjectMocks
    private ArtistFactoryManager artistFactoryManager;

    @Test
    void shouldCreateSoloArtist() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(
                new SoloArtistFactory(),
                new GroupArtistFactory(),
                new FranchiseArtistFactory(),
                new VariousArtistFactory()
        );
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("John Doe");
        dto.setType(ArtistType.SOLO);
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");

        // When
        Artist result = artistFactoryManager.createArtist(dto);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getArtistName());
        assertEquals(ArtistType.SOLO, result.getType());
        assertEquals("English", result.getPrimaryLanguage());
    }

    @Test
    void shouldCreateGroupArtist() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(
                new SoloArtistFactory(),
                new GroupArtistFactory(),
                new FranchiseArtistFactory(),
                new VariousArtistFactory()
        );
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("The Beatles");
        dto.setType(ArtistType.GROUP);
        dto.setGenre("Rock");
        dto.setDescription("Legendary British rock band");

        // When
        Artist result = artistFactoryManager.createArtist(dto);

        // Then
        assertNotNull(result);
        assertEquals("The Beatles", result.getArtistName());
        assertEquals(ArtistType.GROUP, result.getType());
        assertEquals("Rock", result.getGenre());
    }

    @Test
    void shouldValidateSoloArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new SoloArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO invalidDto = new CreateArtistRequestDTO();
        invalidDto.setType(ArtistType.SOLO);
        // Missing required fields

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(invalidDto);
        });
    }

    @Test
    void shouldThrowExceptionForUnsupportedType() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(); // Empty list
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.createArtist(dto);
        });
    }

    @Test
    void shouldThrowExceptionWhenCreateArtistWithNullType() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new SoloArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(null); // Null type

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.createArtist(dto);
        });
        assertEquals("Artist type cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValidateArtistDataWithNullType() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new SoloArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(null); // Null type

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(dto);
        });
        assertEquals("Artist type cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValidateArtistDataForUnsupportedType() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(); // Empty list
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(dto);
        });
        assertTrue(exception.getMessage().contains("No factory available for artist type"));
    }

    @Test
    void shouldCreateFranchiseArtist() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(
                new SoloArtistFactory(),
                new GroupArtistFactory(),
                new FranchiseArtistFactory(),
                new VariousArtistFactory()
        );
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("Disney");
        dto.setType(ArtistType.FRANCHISE);
        dto.setGenre("Various");
        dto.setDescription("Disney franchise artists");

        // When
        Artist result = artistFactoryManager.createArtist(dto);

        // Then
        assertNotNull(result);
        assertEquals("Disney", result.getArtistName());
        assertEquals(ArtistType.FRANCHISE, result.getType());
        assertEquals("Various", result.getGenre());
    }

    @Test
    void shouldCreateVariousArtist() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(
                new SoloArtistFactory(),
                new GroupArtistFactory(),
                new FranchiseArtistFactory(),
                new VariousArtistFactory()
        );
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("Various Artists");
        dto.setType(ArtistType.VARIOUS);
        dto.setGenre("Mixed");
        dto.setDescription("Various artists compilation");

        // When
        Artist result = artistFactoryManager.createArtist(dto);

        // Then
        assertNotNull(result);
        assertEquals("Various Artists", result.getArtistName());
        assertEquals(ArtistType.VARIOUS, result.getType());
        assertEquals("Mixed", result.getGenre());
    }

    @Test
    void shouldValidateGroupArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new GroupArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO validDto = new CreateArtistRequestDTO();
        validDto.setType(ArtistType.GROUP);
        validDto.setArtistName("Valid Group");
        validDto.setGenre("Rock");
        validDto.setDescription("A rock band formed to create amazing music together");

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            artistFactoryManager.validateArtistData(validDto);
        });
    }

    @Test
    void shouldValidateFranchiseArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new FranchiseArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO validDto = new CreateArtistRequestDTO();
        validDto.setType(ArtistType.FRANCHISE);
        validDto.setArtistName("Valid Franchise");
        validDto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        validDto.setOriginCountry("Japan");

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            artistFactoryManager.validateArtistData(validDto);
        });
    }

    @Test
    void shouldValidateVariousArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new VariousArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO validDto = new CreateArtistRequestDTO();
        validDto.setType(ArtistType.VARIOUS);
        validDto.setArtistName("Valid Various");
        validDto.setDescription("This compilation includes various artists from different genres and backgrounds");
        validDto.setGenre("Mixed");

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            artistFactoryManager.validateArtistData(validDto);
        });
    }

    @Test
    void shouldValidateComplexSoloArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new SoloArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO validDto = new CreateArtistRequestDTO();
        validDto.setType(ArtistType.SOLO);
        validDto.setArtistName("Valid Solo Artist");
        validDto.setPrimaryLanguage("English");
        validDto.setGenre("Pop");
        validDto.setSpotifyId("spotify123");
        validDto.setOriginCountry("USA");

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            artistFactoryManager.validateArtistData(validDto);
        });
    }

    @Test
    void shouldThrowExceptionForInvalidGroupArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new GroupArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO invalidDto = new CreateArtistRequestDTO();
        invalidDto.setType(ArtistType.GROUP);
        invalidDto.setArtistName("Valid Group");
        // Missing genre - should fail validation first

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(invalidDto);
        });
        assertEquals("Genre is required for groups", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForGroupWithoutDescription() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new GroupArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO invalidDto = new CreateArtistRequestDTO();
        invalidDto.setType(ArtistType.GROUP);
        invalidDto.setArtistName("Valid Group");
        invalidDto.setGenre("Rock"); // Genre is provided
        // Missing description - should fail validation

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(invalidDto);
        });
        assertEquals("Description is required for groups to explain their concept", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidFranchiseArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new FranchiseArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO invalidDto = new CreateArtistRequestDTO();
        invalidDto.setType(ArtistType.FRANCHISE);
        invalidDto.setArtistName("Valid Franchise");
        invalidDto.setDescription("Short description"); // Too short - should fail validation

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(invalidDto);
        });
        assertEquals("Franchise artists require detailed description (minimum 50 characters)", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidVariousArtistData() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new VariousArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO invalidDto = new CreateArtistRequestDTO();
        invalidDto.setType(ArtistType.VARIOUS);
        invalidDto.setArtistName("Valid Various");
        invalidDto.setDescription("This compilation includes various artists from different genres and backgrounds");
        // Missing genre - should fail validation

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(invalidDto);
        });
        assertEquals("Genre classification is required for various artist compilations", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyArtistName() {
        // Given
        List<ArtistFactory> factories = Arrays.asList(new SoloArtistFactory());
        artistFactoryManager = new ArtistFactoryManager(factories);

        CreateArtistRequestDTO invalidDto = new CreateArtistRequestDTO();
        invalidDto.setType(ArtistType.SOLO);
        invalidDto.setArtistName(""); // Empty name
        invalidDto.setPrimaryLanguage("English");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistFactoryManager.validateArtistData(invalidDto);
        });
        assertEquals("Solo artist name cannot be empty", exception.getMessage());
    }
}
