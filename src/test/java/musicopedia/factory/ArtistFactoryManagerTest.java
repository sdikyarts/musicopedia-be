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
}
