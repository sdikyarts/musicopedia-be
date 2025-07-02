package musicopedia.factory;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates how different artist types have different validation rules
 * through the Factory Method pattern implementation.
 */
class ArtistFactoryValidationTest {

    private ArtistFactoryManager factoryManager;

    @BeforeEach
    void setUp() {
        factoryManager = new ArtistFactoryManager(Arrays.asList(
                new SoloArtistFactory(),
                new GroupArtistFactory(),
                new FranchiseArtistFactory(),
                new VariousArtistFactory()
        ));
    }

    @Test
    void soloArtistRequiresPrimaryLanguage() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("Valid Name");
        // Missing primaryLanguage

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertTrue(exception.getMessage().contains("Primary language is required for solo artists"));
    }

    @Test
    void groupArtistRequiresDescription() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group Name");
        dto.setGenre("Rock");
        // Missing description

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertTrue(exception.getMessage().contains("Description is required for groups"));
    }

    @Test
    void franchiseArtistRequiresDetailedDescription() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Virtual Idol");
        dto.setDescription("Too short"); // Less than 50 characters
        dto.setOriginCountry("JP");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertTrue(exception.getMessage().contains("minimum 50 characters"));
    }

    @Test
    void variousArtistRequiresGenre() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setDescription("A compilation of various artists and songs");
        // Missing genre

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertTrue(exception.getMessage().contains("Genre classification is required"));
    }

    @Test
    void validFranchiseArtistPassesValidation() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Hatsune Miku");
        dto.setDescription("A virtual singer developed by Crypton Future Media using Yamaha's Vocaloid voice synthesis technology. Known for her distinctive turquoise twin-tails and digital concerts.");
        dto.setOriginCountry("JP");
        dto.setGenre("Electronic");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }
}
