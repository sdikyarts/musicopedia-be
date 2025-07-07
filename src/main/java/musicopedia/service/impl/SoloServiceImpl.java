package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.repository.SoloRepository;
import musicopedia.service.SoloService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class SoloServiceImpl implements SoloService {

    private final SoloRepository soloRepository;

    public SoloServiceImpl(SoloRepository soloRepository) {
        this.soloRepository = soloRepository;
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Solo>> findAll() {
        List<Solo> solos = soloRepository.findAll();
        return CompletableFuture.completedFuture(solos);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Optional<Solo>> findById(UUID soloId) {
        Optional<Solo> solo = soloRepository.findById(soloId);
        return CompletableFuture.completedFuture(solo);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Solo>> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Solo> allSolos = soloRepository.findAll();
        List<Solo> filtered = allSolos.stream()
                .filter(solo -> {
                    LocalDate birthDate = solo.getBirthDate();
                    return birthDate != null && 
                           !birthDate.isBefore(startDate) && 
                           !birthDate.isAfter(endDate);
                })
                .toList();
        return CompletableFuture.completedFuture(filtered);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Solo>> findByGender(ArtistGender gender) {
        List<Solo> allSolos = soloRepository.findAll();
        List<Solo> filtered = allSolos.stream()
                .filter(solo -> solo.getGender() == gender)
                .toList();
        return CompletableFuture.completedFuture(filtered);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Solo>> findActiveSoloArtists() {
        List<Solo> allSolos = soloRepository.findAll();
        List<Solo> filtered = allSolos.stream()
                .filter(solo -> solo.getDeathDate() == null)
                .toList();
        return CompletableFuture.completedFuture(filtered);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Solo>> findDeceasedSoloArtists() {
        List<Solo> allSolos = soloRepository.findAll();
        List<Solo> filtered = allSolos.stream()
                .filter(solo -> solo.getDeathDate() != null)
                .toList();
        return CompletableFuture.completedFuture(filtered);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Solo> save(Solo solo, Artist artist) {
        // Save the artist entity elsewhere if needed (not in soloRepository)
        solo.setArtist(artist);
        Solo savedSolo = soloRepository.save(solo);
        return CompletableFuture.completedFuture(savedSolo);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Solo> update(Solo solo) {
        if (soloRepository.existsById(solo.getArtistId())) {
            if (solo.getArtist() == null) {
                // Do not save, but return the input solo (not null)
                return CompletableFuture.completedFuture(solo);
            }
            Solo updatedSolo = soloRepository.save(solo);
            return CompletableFuture.completedFuture(updatedSolo);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> deleteById(UUID soloId) {
        soloRepository.deleteById(soloId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Boolean> existsById(UUID soloId) {
        boolean exists = soloRepository.existsById(soloId);
        return CompletableFuture.completedFuture(exists);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Solo>> findByRealNameContaining(String realName) {
        // Use artistName as the real name field
        List<Solo> solos = soloRepository.findAll().stream()
                .filter(solo -> solo.getArtist() != null && solo.getArtist().getArtistName() != null && solo.getArtist().getArtistName().contains(realName))
                .toList();
        return CompletableFuture.completedFuture(solos);
    }
}
