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
    void soloArtistNameCannotExceed100Characters() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("A".repeat(101)); // 101 characters - exceeds limit
        dto.setPrimaryLanguage("English");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Solo artist name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    void soloArtistNameCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName(""); // Empty name
        dto.setPrimaryLanguage("English");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Solo artist name cannot be empty", exception.getMessage());
    }

    @Test
    void soloArtistNameCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName(null); // Null name
        dto.setPrimaryLanguage("English");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Solo artist name cannot be empty", exception.getMessage());
    }

    @Test
    void soloArtistNameCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("   "); // Whitespace only
        dto.setPrimaryLanguage("English");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Solo artist name cannot be empty", exception.getMessage());
    }

    @Test
    void soloArtistPrimaryLanguageCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("Valid Artist");
        dto.setPrimaryLanguage(""); // Empty primary language

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Primary language is required for solo artists", exception.getMessage());
    }

    @Test
    void soloArtistPrimaryLanguageCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("Valid Artist");
        dto.setPrimaryLanguage("   "); // Whitespace only

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Primary language is required for solo artists", exception.getMessage());
    }

    @Test
    void validSoloArtistWithMaxLengthName() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("A".repeat(100)); // Exactly 100 characters - should be valid
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    @Test
    void validSoloArtistPassesValidation() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.SOLO);
        dto.setArtistName("Taylor Swift");
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");
        dto.setOriginCountry("USA");
        dto.setDescription("American singer-songwriter");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
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

    @Test
    void groupArtistNameCannotExceed150Characters() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("A".repeat(151)); // 151 characters - exceeds limit
        dto.setGenre("Rock");
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Group name cannot exceed 150 characters", exception.getMessage());
    }

    @Test
    void groupArtistNameCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName(""); // Empty name
        dto.setGenre("Rock");
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Group name cannot be empty", exception.getMessage());
    }

    @Test
    void groupArtistNameCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName(null); // Null name
        dto.setGenre("Rock");
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Group name cannot be empty", exception.getMessage());
    }

    @Test
    void groupArtistNameCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("   "); // Whitespace only
        dto.setGenre("Rock");
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Group name cannot be empty", exception.getMessage());
    }

    @Test
    void groupArtistGenreCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group");
        dto.setGenre(""); // Empty genre
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Genre is required for groups", exception.getMessage());
    }

    @Test
    void groupArtistGenreCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group");
        dto.setGenre(null); // Null genre
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Genre is required for groups", exception.getMessage());
    }

    @Test
    void groupArtistGenreCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group");
        dto.setGenre("   "); // Whitespace only
        dto.setDescription("Valid description");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Genre is required for groups", exception.getMessage());
    }

    @Test
    void groupArtistDescriptionCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group");
        dto.setGenre("Rock");
        dto.setDescription(""); // Empty description

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Description is required for groups to explain their concept", exception.getMessage());
    }

    @Test
    void groupArtistDescriptionCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group");
        dto.setGenre("Rock");
        dto.setDescription(null); // Null description

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Description is required for groups to explain their concept", exception.getMessage());
    }

    @Test
    void groupArtistDescriptionCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("Valid Group");
        dto.setGenre("Rock");
        dto.setDescription("   "); // Whitespace only

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Description is required for groups to explain their concept", exception.getMessage());
    }

    @Test
    void validGroupArtistWithMaxLengthName() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("A".repeat(150)); // Exactly 150 characters - should be valid
        dto.setGenre("Rock");
        dto.setDescription("A rock band formed to create amazing music together");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    @Test
    void validGroupArtistPassesValidation() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.GROUP);
        dto.setArtistName("The Beatles");
        dto.setGenre("Rock");
        dto.setDescription("Legendary British rock band from Liverpool");
        dto.setPrimaryLanguage("English");
        dto.setOriginCountry("UK");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    @Test
    void franchiseArtistNameCannotExceed200Characters() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("A".repeat(201)); // 201 characters - exceeds limit
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry("Japan");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Franchise artist name cannot exceed 200 characters", exception.getMessage());
    }

    @Test
    void franchiseArtistNameCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName(""); // Empty name
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry("Japan");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Franchise artist name cannot be empty", exception.getMessage());
    }

    @Test
    void franchiseArtistNameCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName(null); // Null name
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry("Japan");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Franchise artist name cannot be empty", exception.getMessage());
    }

    @Test
    void franchiseArtistNameCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("   "); // Whitespace only
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry("Japan");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Franchise artist name cannot be empty", exception.getMessage());
    }

    @Test
    void franchiseArtistDescriptionCannotBeTooShort() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Valid Franchise");
        dto.setDescription("Short description"); // Less than 50 characters
        dto.setOriginCountry("Japan");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Franchise artists require detailed description (minimum 50 characters)", exception.getMessage());
    }

    @Test
    void franchiseArtistDescriptionCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Valid Franchise");
        dto.setDescription(null); // Null description
        dto.setOriginCountry("Japan");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Franchise artists require detailed description (minimum 50 characters)", exception.getMessage());
    }

    @Test
    void franchiseArtistOriginCountryCannotBeEmpty() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Valid Franchise");
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry(""); // Empty origin country

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Origin country is required for franchise artists", exception.getMessage());
    }

    @Test
    void franchiseArtistOriginCountryCannotBeNull() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Valid Franchise");
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry(null); // Null origin country

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Origin country is required for franchise artists", exception.getMessage());
    }

    @Test
    void franchiseArtistOriginCountryCannotBeWhitespaceOnly() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Valid Franchise");
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry("   "); // Whitespace only

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Origin country is required for franchise artists", exception.getMessage());
    }

    @Test
    void validFranchiseArtistWithMaxLengthName() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("A".repeat(200)); // Exactly 200 characters - should be valid
        dto.setDescription("This is a detailed description of the franchise that contains more than fifty characters to meet validation requirements");
        dto.setOriginCountry("Japan");
        dto.setGenre("Virtual");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    @Test
    void validFranchiseArtistWithMinimumDescription() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setType(ArtistType.FRANCHISE);
        dto.setArtistName("Virtual Singer");
        dto.setDescription("A".repeat(50)); // Exactly 50 characters - minimum requirement
        dto.setOriginCountry("Japan");
        dto.setGenre("Virtual");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }
}
