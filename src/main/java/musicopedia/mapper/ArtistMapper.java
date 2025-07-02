package musicopedia.mapper;

import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.dto.request.UpdateArtistRequestDTO;
import musicopedia.dto.response.ArtistResponseDTO;
import musicopedia.dto.response.ArtistSummaryDTO;
import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistType;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public Artist toEntity(CreateArtistRequestDTO dto) {
        Artist artist = new Artist();
        artist.setArtistName(dto.getArtistName());
        artist.setType(dto.getType());
        artist.setSpotifyId(dto.getSpotifyId());
        artist.setDescription(dto.getDescription());
        artist.setImage(dto.getImage());
        artist.setPrimaryLanguage(dto.getPrimaryLanguage());
        artist.setGenre(dto.getGenre());
        artist.setOriginCountry(dto.getOriginCountry());
        return artist;
    }

    public void updateEntityFromDto(Artist artist, UpdateArtistRequestDTO dto) {
        if (dto.getArtistName() != null) {
            artist.setArtistName(dto.getArtistName());
        }
        if (dto.getType() != null) {
            artist.setType(dto.getType());
        }
        if (dto.getSpotifyId() != null) {
            artist.setSpotifyId(dto.getSpotifyId());
        }
        if (dto.getDescription() != null) {
            artist.setDescription(dto.getDescription());
        }
        if (dto.getImage() != null) {
            artist.setImage(dto.getImage());
        }
        if (dto.getPrimaryLanguage() != null) {
            artist.setPrimaryLanguage(dto.getPrimaryLanguage());
        }
        if (dto.getGenre() != null) {
            artist.setGenre(dto.getGenre());
        }
        if (dto.getOriginCountry() != null) {
            artist.setOriginCountry(dto.getOriginCountry());
        }
    }

    public ArtistResponseDTO toResponseDto(Artist artist, Solo solo, Groups group) {
        ArtistResponseDTO dto = new ArtistResponseDTO();
        dto.setArtistId(artist.getArtistId());
        dto.setSpotifyId(artist.getSpotifyId());
        dto.setArtistName(artist.getArtistName());
        dto.setDescription(artist.getDescription());
        dto.setImage(artist.getImage());
        dto.setType(artist.getType());
        dto.setPrimaryLanguage(artist.getPrimaryLanguage());
        dto.setGenre(artist.getGenre());
        dto.setOriginCountry(artist.getOriginCountry());

        // Add solo-specific data if available
        if (solo != null) {
            dto.setBirthDate(solo.getBirthDate());
            dto.setDeathDate(solo.getDeathDate());
            dto.setSoloGender(solo.getGender());
        }

        // Add group-specific data if available
        if (group != null) {
            dto.setFormationDate(group.getFormationDate());
            dto.setDisbandDate(group.getDisbandDate());
            dto.setGroupGender(group.getGroupGender());
        }

        return dto;
    }

    public ArtistSummaryDTO toSummaryDto(Artist artist) {
        ArtistSummaryDTO dto = new ArtistSummaryDTO();
        dto.setArtistId(artist.getArtistId());
        dto.setArtistName(artist.getArtistName());
        dto.setType(artist.getType());
        dto.setImage(artist.getImage());
        dto.setGenre(artist.getGenre());
        dto.setOriginCountry(artist.getOriginCountry());
        return dto;
    }

    public Solo createSoloFromDto(CreateArtistRequestDTO dto, Artist artist) {
        if (dto.getType() != ArtistType.SOLO) {
            return null;
        }
        
        Solo solo = new Solo();
        solo.setArtist(artist);
        solo.setBirthDate(dto.getBirthDate());
        solo.setDeathDate(dto.getDeathDate());
        solo.setGender(dto.getSoloGender());
        return solo;
    }

    public Groups createGroupFromDto(CreateArtistRequestDTO dto, Artist artist) {
        if (dto.getType() != ArtistType.GROUP) {
            return null;
        }
        
        Groups group = new Groups();
        group.setArtist(artist);
        group.setFormationDate(dto.getFormationDate());
        group.setDisbandDate(dto.getDisbandDate());
        group.setGroupGender(dto.getGroupGender());
        return group;
    }
}
