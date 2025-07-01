package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.ArtistRepository;
import musicopedia.service.ArtistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Artist> findById(UUID artistId) {
        return artistRepository.findById(artistId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Artist> findBySpotifyId(String spotifyId) {
        return artistRepository.findBySpotifyId(spotifyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artist> findByNameContaining(String name) {
        return artistRepository.findByArtistNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artist> findByType(ArtistType type) {
        return artistRepository.findByType(type);
    }

    @Override
    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    public void deleteById(UUID artistId) {
        artistRepository.deleteById(artistId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID artistId) {
        return artistRepository.existsById(artistId);
    }
}
