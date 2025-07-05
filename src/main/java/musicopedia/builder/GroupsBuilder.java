package musicopedia.builder;

import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import musicopedia.model.Artist;
import java.time.LocalDate;
import java.util.UUID;

public class GroupsBuilder extends ArtistBuilder {
    private LocalDate formationDate;
    private LocalDate disbandDate;
    private ArtistGender groupGender;
    private GroupActivityStatus activityStatus;
    private Artist artist;

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
    @Override
    public GroupsBuilder setArtistId(UUID artistId) {
        super.setArtistId(artistId);
        return this;
    }

    public GroupsBuilder setArtist(Artist artist) {
        this.artist = artist;
        return this;
    }

    public Groups buildGroups() {
        Groups groups = new Groups();
        groups.setArtistId(super.artistId);
        groups.setArtist(this.artist != null ? this.artist : super.build());
        groups.setFormationDate(this.formationDate);
        groups.setDisbandDate(this.disbandDate);
        groups.setGroupGender(this.groupGender);
        groups.setActivityStatus(this.activityStatus);
        return groups;
    }
}
