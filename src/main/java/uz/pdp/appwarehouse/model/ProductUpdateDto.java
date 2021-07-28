package uz.pdp.appwarehouse.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductUpdateDto implements Serializable {
    private String name;
    private Long categoryId;
    private MultipartFile file;
    private Long measurementId;
}
