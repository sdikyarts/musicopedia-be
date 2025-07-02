package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.ArtistRepository;
import musicopedia.service.ArtistService;
import musicopedia.factory.ArtistFactoryManager;
import musicopedia.dto.request.CreateArtistRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistFactoryManager artistFactoryManager;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistFactoryManager artistFactoryManager) {
        this.artistRepository = artistRepository;
        this.artistFactoryManager = artistFactoryManager;
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
    public Artist createArtist(CreateArtistRequestDTO dto) {
        // Use factory pattern for type-specific validation and creation
        artistFactoryManager.validateArtistData(dto);
        Artist artist = artistFactoryManager.createArtist(dto);
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
