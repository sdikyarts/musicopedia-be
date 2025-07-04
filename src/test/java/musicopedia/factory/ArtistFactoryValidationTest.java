package musicopedia.factory;

import musicopedia.dto.request.ArtistRequestDTO;
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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
        ArtistRequestDTO dto = new ArtistRequestDTO();
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

    @Test
    void variousArtistNameCannotExceed300Characters() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("A".repeat(301)); // 301 characters - exceeds limit
        dto.setGenre("Mixed");
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Various artist compilation name cannot exceed 300 characters", exception.getMessage());
    }

    @Test
    void variousArtistNameCannotBeEmpty() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName(""); // Empty name
        dto.setGenre("Mixed");
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Various artist compilation name cannot be empty", exception.getMessage());
    }

    @Test
    void variousArtistNameCannotBeNull() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName(null); // Null name
        dto.setGenre("Mixed");
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Various artist compilation name cannot be empty", exception.getMessage());
    }

    @Test
    void variousArtistNameCannotBeWhitespaceOnly() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("   "); // Whitespace only
        dto.setGenre("Mixed");
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Various artist compilation name cannot be empty", exception.getMessage());
    }

    @Test
    void variousArtistDescriptionCannotBeTooShort() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setGenre("Mixed");
        dto.setDescription("Short"); // Less than 30 characters

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Various artist compilations require description (minimum 30 characters) to explain the collection", exception.getMessage());
    }

    @Test
    void variousArtistDescriptionCannotBeNull() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setGenre("Mixed");
        dto.setDescription(null); // Null description

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Various artist compilations require description (minimum 30 characters) to explain the collection", exception.getMessage());
    }

    @Test
    void variousArtistGenreCannotBeEmpty() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setGenre(""); // Empty genre
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Genre classification is required for various artist compilations", exception.getMessage());
    }

    @Test
    void variousArtistGenreCannotBeNull() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setGenre(null); // Null genre
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Genre classification is required for various artist compilations", exception.getMessage());
    }

    @Test
    void variousArtistGenreCannotBeWhitespaceOnly() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setGenre("   "); // Whitespace only
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factoryManager.validateArtistData(dto);
        });
        
        assertEquals("Genre classification is required for various artist compilations", exception.getMessage());
    }

    @Test
    void validVariousArtistWithMaxLengthName() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("A".repeat(300)); // Exactly 300 characters - should be valid
        dto.setGenre("Mixed");
        dto.setDescription("A compilation of various artists from different genres and backgrounds");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    @Test
    void validVariousArtistWithMinimumDescription() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Various Artists Compilation");
        dto.setGenre("Mixed");
        dto.setDescription("A".repeat(30)); // Exactly 30 characters - minimum requirement

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    @Test
    void validVariousArtistPassesValidation() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setType(ArtistType.VARIOUS);
        dto.setArtistName("Now That's What I Call Music!");
        dto.setGenre("Pop/Rock/Dance");
        dto.setDescription("A compilation featuring the biggest hits from various popular artists across different genres");
        dto.setPrimaryLanguage("English");
        dto.setOriginCountry("UK");

        // Should not throw any exception
        assertDoesNotThrow(() -> {
            factoryManager.validateArtistData(dto);
            factoryManager.createArtist(dto);
        });
    }

    // Test to cover the false branch in supports() method for all factories
    @Test
    void variousArtistFactoryDoesNotSupportOtherTypes() {
        VariousArtistFactory factory = new VariousArtistFactory();
        
        ArtistRequestDTO soloDto = new ArtistRequestDTO();
        soloDto.setType(ArtistType.SOLO);
        assertFalse(factory.supports(soloDto));
        
        ArtistRequestDTO groupDto = new ArtistRequestDTO();
        groupDto.setType(ArtistType.GROUP);
        assertFalse(factory.supports(groupDto));
        
        ArtistRequestDTO franchiseDto = new ArtistRequestDTO();
        franchiseDto.setType(ArtistType.FRANCHISE);
        assertFalse(factory.supports(franchiseDto));
    }

    @Test
    void soloArtistFactoryDoesNotSupportOtherTypes() {
        SoloArtistFactory factory = new SoloArtistFactory();
        
        ArtistRequestDTO groupDto = new ArtistRequestDTO();
        groupDto.setType(ArtistType.GROUP);
        assertFalse(factory.supports(groupDto));
        
        ArtistRequestDTO variousDto = new ArtistRequestDTO();
        variousDto.setType(ArtistType.VARIOUS);
        assertFalse(factory.supports(variousDto));
        
        ArtistRequestDTO franchiseDto = new ArtistRequestDTO();
        franchiseDto.setType(ArtistType.FRANCHISE);
        assertFalse(factory.supports(franchiseDto));
    }

    @Test
    void groupArtistFactoryDoesNotSupportOtherTypes() {
        GroupArtistFactory factory = new GroupArtistFactory();
        
        ArtistRequestDTO soloDto = new ArtistRequestDTO();
        soloDto.setType(ArtistType.SOLO);
        assertFalse(factory.supports(soloDto));
        
        ArtistRequestDTO variousDto = new ArtistRequestDTO();
        variousDto.setType(ArtistType.VARIOUS);
        assertFalse(factory.supports(variousDto));
        
        ArtistRequestDTO franchiseDto = new ArtistRequestDTO();
        franchiseDto.setType(ArtistType.FRANCHISE);
        assertFalse(factory.supports(franchiseDto));
    }

    @Test
    void franchiseArtistFactoryDoesNotSupportOtherTypes() {
        FranchiseArtistFactory factory = new FranchiseArtistFactory();
        
        ArtistRequestDTO soloDto = new ArtistRequestDTO();
        soloDto.setType(ArtistType.SOLO);
        assertFalse(factory.supports(soloDto));
        
        ArtistRequestDTO groupDto = new ArtistRequestDTO();
        groupDto.setType(ArtistType.GROUP);
        assertFalse(factory.supports(groupDto));
        
        ArtistRequestDTO variousDto = new ArtistRequestDTO();
        variousDto.setType(ArtistType.VARIOUS);
        assertFalse(factory.supports(variousDto));
    }
}
