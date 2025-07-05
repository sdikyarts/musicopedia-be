package musicopedia.builder;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;

import java.util.UUID;

public class ArtistBuilder {
    private String spotifyId;
    private String artistName;
    private String description;
    private String image;
    private ArtistType type;
    private String primaryLanguage;
    private String genre;
    private String originCountry;
    protected UUID artistId;

    public ArtistBuilder setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
        return this;
    }

    public ArtistBuilder setArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    public ArtistBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArtistBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public ArtistBuilder setType(ArtistType type) {
        this.type = type;
        return this;
    }

    public ArtistBuilder setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
        return this;
    }

    public ArtistBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public ArtistBuilder setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
        return this;
    }

    public ArtistBuilder setArtistId(UUID artistId) {
        this.artistId = artistId;
        return this;
    }

    public Artist build() {
        Artist artist = new Artist();
        artist.setArtistId(this.artistId);
        artist.setSpotifyId(this.spotifyId);
        artist.setArtistName(this.artistName);
        artist.setDescription(this.description);
        artist.setImage(this.image);
        artist.setType(this.type);
        artist.setPrimaryLanguage(this.primaryLanguage);
        artist.setGenre(this.genre);
        artist.setOriginCountry(this.originCountry);
        return artist;
    }
}
