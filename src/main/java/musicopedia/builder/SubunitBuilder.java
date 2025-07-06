package musicopedia.builder;

import musicopedia.model.Groups;
import musicopedia.model.Subunit;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import java.time.LocalDate;

public class SubunitBuilder {
    private Groups mainGroup;
    private String subunitName;
    private String description;
    private String image;
    private LocalDate formationDate;
    private LocalDate disbandDate;
    private ArtistGender subunitGender;
    private GroupActivityStatus activityStatus;
    private String originCountry;
    private Groups groupSubunit;

    public SubunitBuilder setMainGroup(Groups mainGroup) {
        this.mainGroup = mainGroup;
        return this;
    }
    public SubunitBuilder setSubunitName(String subunitName) {
        this.subunitName = subunitName;
        return this;
    }
    public SubunitBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    public SubunitBuilder setImage(String image) {
        this.image = image;
        return this;
    }
    public SubunitBuilder setFormationDate(LocalDate formationDate) {
        this.formationDate = formationDate;
        return this;
    }
    public SubunitBuilder setDisbandDate(LocalDate disbandDate) {
        this.disbandDate = disbandDate;
        return this;
    }
    public SubunitBuilder setSubunitGender(ArtistGender subunitGender) {
        this.subunitGender = subunitGender;
        return this;
    }
    public SubunitBuilder setActivityStatus(GroupActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
        return this;
    }
    public SubunitBuilder setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
        return this;
    }
    public SubunitBuilder setGroupSubunit(Groups groupSubunit) {
        this.groupSubunit = groupSubunit;
        return this;
    }
    public Subunit build() {
        Subunit subunit = new Subunit();
        subunit.setMainGroup(mainGroup);
        subunit.setSubunitName(subunitName);
        subunit.setDescription(description);
        subunit.setImage(image);
        subunit.setFormationDate(formationDate);
        subunit.setDisbandDate(disbandDate);
        subunit.setSubunitGender(subunitGender);
        subunit.setActivityStatus(activityStatus);
        subunit.setOriginCountry(originCountry);
        subunit.setGroupSubunit(groupSubunit);
        return subunit;
    }
}
