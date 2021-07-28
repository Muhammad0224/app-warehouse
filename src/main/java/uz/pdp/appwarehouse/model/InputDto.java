package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class InputDto implements Serializable {
    private Long warehouseId;
    private Long supplierId;
    private Long currencyId;
    private Integer factureNumber;
}
