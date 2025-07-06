package musicopedia.builder;

import musicopedia.model.Groups;
import musicopedia.model.Subunit;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SubunitBuilderTest {
    @Test
    void testBuilderAndBuild() {
        Groups mainGroup = new Groups();
        Groups groupSubunit = new Groups();
        LocalDate formation = LocalDate.of(2020, 1, 1);
        LocalDate disband = LocalDate.of(2025, 1, 1);
        
        Subunit subunit = new SubunitBuilder()
                .setMainGroup(mainGroup)
                .setSubunitName("SubunitName")
                .setDescription("desc")
                .setImage("img.png")
                .setFormationDate(formation)
                .setDisbandDate(disband)
                .setSubunitGender(ArtistGender.FEMALE)
                .setActivityStatus(GroupActivityStatus.ACTIVE)
                .setOriginCountry("KR")
                .setGroupSubunit(groupSubunit)
                .build();

        assertEquals(mainGroup, subunit.getMainGroup());
        assertEquals("SubunitName", subunit.getSubunitName());
        assertEquals("desc", subunit.getDescription());
        assertEquals("img.png", subunit.getImage());
        assertEquals(formation, subunit.getFormationDate());
        assertEquals(disband, subunit.getDisbandDate());
        assertEquals(ArtistGender.FEMALE, subunit.getSubunitGender());
        assertEquals(GroupActivityStatus.ACTIVE, subunit.getActivityStatus());
        assertEquals("KR", subunit.getOriginCountry());
        assertEquals(groupSubunit, subunit.getGroupSubunit());
    }

    @Test
    void testBuilderSettersReturnThis() {
        SubunitBuilder builder = new SubunitBuilder();
        assertSame(builder, builder.setMainGroup(new Groups()));
        assertSame(builder, builder.setSubunitName("A"));
        assertSame(builder, builder.setDescription("B"));
        assertSame(builder, builder.setImage("C"));
        assertSame(builder, builder.setFormationDate(LocalDate.now()));
        assertSame(builder, builder.setDisbandDate(LocalDate.now()));
        assertSame(builder, builder.setSubunitGender(ArtistGender.MALE));
        assertSame(builder, builder.setActivityStatus(GroupActivityStatus.INACTIVE));
        assertSame(builder, builder.setOriginCountry("US"));
        assertSame(builder, builder.setGroupSubunit(new Groups()));
    }
}
