package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class WarehouseDto implements Serializable {
    private String name;
}
