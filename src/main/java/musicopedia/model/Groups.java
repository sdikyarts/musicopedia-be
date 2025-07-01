package musicopedia.model;

import jakarta.persistence.*;
import lombok.Data;
import musicopedia.model.enums.ArtistGender;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "groups")
public class Groups {

    @Id
    private UUID artistId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "artist_id")
    private Artist artist;

    private LocalDate formationDate;
    private LocalDate disbandDate;

    @Enumerated(EnumType.STRING)
    private ArtistGender groupGender;
}