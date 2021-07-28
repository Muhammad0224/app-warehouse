package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OutputProductDto implements Serializable {
    private Long productId;
    private Long outputId;
    private Double amount;
    private Double price;
}
