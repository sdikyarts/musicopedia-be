package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SoloService {
    
    List<Solo> findAll();
    
    Optional<Solo> findById(UUID soloId);
    
    List<Solo> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Solo> findByGender(ArtistGender gender);
    
    List<Solo> findActiveSoloArtists();
    
    List<Solo> findDeceasedSoloArtists();
    
    Solo save(Solo solo, Artist artist);
    
    Solo update(Solo solo);
    
    void deleteById(UUID soloId);
}
