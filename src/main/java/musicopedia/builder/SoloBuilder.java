package musicopedia.builder;

import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupAffiliationStatus;
import java.time.LocalDate;
import java.util.UUID;
import musicopedia.model.Artist;

public class SoloBuilder extends ArtistBuilder {
    private LocalDate birthDate;
    private LocalDate deathDate;
    private ArtistGender gender;
    private GroupAffiliationStatus groupAffiliationStatus;
    private Artist artist;

    public SoloBuilder setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public SoloBuilder setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
        return this;
    }

    public SoloBuilder setGender(ArtistGender gender) {
        this.gender = gender;
        return this;
    }

    public SoloBuilder setGroupAffiliationStatus(GroupAffiliationStatus status) {
        this.groupAffiliationStatus = status;
        return this;
    }

    @Override
    public SoloBuilder setSpotifyId(String spotifyId) {
        super.setSpotifyId(spotifyId);
        return this;
    }
    @Override
    public SoloBuilder setArtistName(String artistName) {
        super.setArtistName(artistName);
        return this;
    }
    @Override
    public SoloBuilder setDescription(String description) {
        super.setDescription(description);
        return this;
    }
    @Override
    public SoloBuilder setImage(String image) {
        super.setImage(image);
        return this;
    }
    @Override
    public SoloBuilder setType(musicopedia.model.enums.ArtistType type) {
        super.setType(type);
        return this;
    }
    @Override
    public SoloBuilder setPrimaryLanguage(String primaryLanguage) {
        super.setPrimaryLanguage(primaryLanguage);
        return this;
    }
    @Override
    public SoloBuilder setGenre(String genre) {
        super.setGenre(genre);
        return this;
    }
    @Override
    public SoloBuilder setOriginCountry(String originCountry) {
        super.setOriginCountry(originCountry);
        return this;
    }
    @Override
    public SoloBuilder setArtistId(UUID artistId) {
        super.setArtistId(artistId);
        return this;
    }

    public SoloBuilder setArtist(Artist artist) {
        this.artist = artist;
        return this;
    }

    public Solo buildSolo() {
        Solo solo = new Solo();
        solo.setArtistId(super.artistId);
        solo.setArtist(this.artist != null ? this.artist : super.build());
        solo.setBirthDate(this.birthDate);
        solo.setDeathDate(this.deathDate);
        solo.setGender(this.gender);
        solo.setGroupAffiliationStatus(this.groupAffiliationStatus);
        return solo;
    }
}
