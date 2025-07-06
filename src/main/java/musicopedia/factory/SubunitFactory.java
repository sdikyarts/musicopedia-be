package musicopedia.factory;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.model.Groups;
import musicopedia.model.Subunit;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class SubunitFactory {
    @Async
    public CompletableFuture<Subunit> createSubunit(SubunitRequestDTO dto, Groups mainGroup, Groups groupSubunit) {
        if (mainGroup == null) {
            throw new IllegalArgumentException("Main group is required");
        }
        Subunit subunit = new Subunit();
        subunit.setMainGroup(mainGroup);
        subunit.setSubunitName(dto.getSubunitName());
        subunit.setDescription(dto.getDescription());
        subunit.setImage(dto.getImage());
        subunit.setFormationDate(dto.getFormationDate());
        subunit.setDisbandDate(dto.getDisbandDate());
        if (dto.getSubunitGender() != null) {
            subunit.setSubunitGender(ArtistGender.valueOf(dto.getSubunitGender()));
        }
        if (dto.getActivityStatus() != null) {
            subunit.setActivityStatus(GroupActivityStatus.valueOf(dto.getActivityStatus()));
        }
        subunit.setOriginCountry(dto.getOriginCountry());
        subunit.setGroupSubunit(groupSubunit);
        return CompletableFuture.completedFuture(subunit);
    }
}
