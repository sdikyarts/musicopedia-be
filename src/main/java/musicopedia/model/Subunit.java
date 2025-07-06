package musicopedia.model;

import jakarta.persistence.*;
import lombok.Data;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "subunit")
public class Subunit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID subunitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_group_id", nullable = false)
    private Groups mainGroup;

    @Column(nullable = false)
    private String subunitName;

    @Column(length = 1000)
    private String description;

    private String image;

    private LocalDate formationDate;
    private LocalDate disbandDate;

    @Enumerated(EnumType.STRING)
    private ArtistGender subunitGender;

    @Enumerated(EnumType.STRING)
    private GroupActivityStatus activityStatus;

    private String originCountry;

    // Nullable: only if subunit has debuted officially
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_subunit_id")
    private Groups groupSubunit;
}
