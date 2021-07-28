package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDto implements Serializable {
    private String name;
    private Long parent_id;
}
