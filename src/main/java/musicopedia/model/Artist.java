package musicopedia.model;

import jakarta.persistence.*;
import lombok.Data;
import musicopedia.model.enums.ArtistType;
import java.util.UUID;

@Data
@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID artistId;

    @Column(unique = true, length = 22)
    private String spotifyId;

    @Column(nullable = false)
    private String artistName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtistType type;

    private String primaryLanguage;
    private String genre;

    @Column(length = 2)
    private String originCountry;
}
