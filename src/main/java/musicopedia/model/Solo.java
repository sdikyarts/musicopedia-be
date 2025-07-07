package musicopedia.model;

import jakarta.persistence.*;
import lombok.Data;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupAffiliationStatus;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "solo")
public class Solo {

    @Id
    private UUID artistId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "artist_id")
    private Artist artist;

    private LocalDate birthDate;
    private LocalDate deathDate;

    @Enumerated(EnumType.STRING)
    private ArtistGender gender;

    @Enumerated(EnumType.STRING)
    private GroupAffiliationStatus groupAffiliationStatus;

    private String realName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    public Solo() {}

    public Solo(Artist artist, Member member) {
        this.artist = artist;
        this.artistId = artist.getArtistId();
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solo solo = (Solo) o;
        return java.util.Objects.equals(artistId, solo.artistId)
                && java.util.Objects.equals(realName, solo.realName)
                && java.util.Objects.equals(artist, solo.artist);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(artistId, realName, artist);
    }
}