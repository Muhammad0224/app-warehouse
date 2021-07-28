package uz.pdp.appwarehouse.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.appwarehouse.domain.Attachment;
import uz.pdp.appwarehouse.domain.AttachmentContent;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.repository.AttachmentContentRepository;
import uz.pdp.appwarehouse.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/attachment")
public class AttachmentController {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @GetMapping("/info/{id}")
    public ResponseEntity<Attachment> get(@PathVariable Long id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        return optionalAttachment.map(attachment -> new ResponseEntity<>(attachment, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @SneakyThrows
    @GetMapping("/download/{id}")
    public ResponseEntity<Result> download(@PathVariable Long id, HttpServletResponse response) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (!optionalAttachment.isPresent())
            return new ResponseEntity<>(new Result("File not found", false), HttpStatus.NOT_FOUND);

        Attachment attachment = optionalAttachment.get();
        Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());

        if (!optionalAttachmentContent.isPresent())
            return new ResponseEntity<>(new Result("File not found", false), HttpStatus.NOT_FOUND);

        response.setHeader("Content-Disposition", "attachment; filename = \"" + attachment.getName() + "\"");
        response.setContentType(attachment.getContentType());

        FileCopyUtils.copy(optionalAttachmentContent.get().getBytes(), response.getOutputStream());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    public Attachment add(MultipartFile file) {
        if (file == null)
            return null;
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        Attachment attachment = attachmentRepository.save(Attachment.builder().contentType(contentType).name(originalFilename).size(size).build());

        attachmentContentRepository.save(AttachmentContent.builder().attachment(attachment).bytes(file.getBytes()).build());
        return attachment;
    }
}
