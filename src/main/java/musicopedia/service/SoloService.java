package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SoloService {
    
    CompletableFuture<List<Solo>> findAll();
    
    CompletableFuture<Optional<Solo>> findById(UUID soloId);
    
    CompletableFuture<List<Solo>> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    CompletableFuture<List<Solo>> findByGender(ArtistGender gender);
    
    CompletableFuture<List<Solo>> findActiveSoloArtists();
    
    CompletableFuture<List<Solo>> findDeceasedSoloArtists();
    
    CompletableFuture<Solo> save(Solo solo, Artist artist);
    
    CompletableFuture<Solo> update(Solo solo);
    
    CompletableFuture<Void> deleteById(UUID soloId);
    
    CompletableFuture<Boolean> existsById(UUID soloId);
}
