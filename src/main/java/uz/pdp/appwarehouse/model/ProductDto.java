package uz.pdp.appwarehouse.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.appwarehouse.domain.Category;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto implements Serializable {
    private String name;
    private Long categoryId;
    private MultipartFile file;
    private Long measurementId;
}
