package musicopedia.builder;

import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import java.time.LocalDate;

public class GroupsBuilder extends ArtistBuilder {
    private LocalDate formationDate;
    private LocalDate disbandDate;
    private ArtistGender groupGender;
    private GroupActivityStatus activityStatus;

    public GroupsBuilder setFormationDate(LocalDate formationDate) {
        this.formationDate = formationDate;
        return this;
    }

    public GroupsBuilder setDisbandDate(LocalDate disbandDate) {
        this.disbandDate = disbandDate;
        return this;
    }

    public GroupsBuilder setGroupGender(ArtistGender groupGender) {
        this.groupGender = groupGender;
        return this;
    }

    public GroupsBuilder setActivityStatus(GroupActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
        return this;
    }

    @Override
    public GroupsBuilder setSpotifyId(String spotifyId) {
        super.setSpotifyId(spotifyId);
        return this;
    }
    @Override
    public GroupsBuilder setArtistName(String artistName) {
        super.setArtistName(artistName);
        return this;
    }
    @Override
    public GroupsBuilder setDescription(String description) {
        super.setDescription(description);
        return this;
    }
    @Override
    public GroupsBuilder setImage(String image) {
        super.setImage(image);
        return this;
    }
    @Override
    public GroupsBuilder setType(musicopedia.model.enums.ArtistType type) {
        super.setType(type);
        return this;
    }
    @Override
    public GroupsBuilder setPrimaryLanguage(String primaryLanguage) {
        super.setPrimaryLanguage(primaryLanguage);
        return this;
    }
    @Override
    public GroupsBuilder setGenre(String genre) {
        super.setGenre(genre);
        return this;
    }
    @Override
    public GroupsBuilder setOriginCountry(String originCountry) {
        super.setOriginCountry(originCountry);
        return this;
    }

    public Groups buildGroups() {
        Groups groups = new Groups();
        groups.setArtist(super.build());
        groups.setFormationDate(this.formationDate);
        groups.setDisbandDate(this.disbandDate);
        groups.setGroupGender(this.groupGender);
        groups.setActivityStatus(this.activityStatus);
        return groups;
    }
}
