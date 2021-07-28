package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appwarehouse.domain.Attachment;
import uz.pdp.appwarehouse.domain.AttachmentContent;

import java.util.Optional;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long> {
    Optional<AttachmentContent> findByAttachmentId(Long aLong);
}
