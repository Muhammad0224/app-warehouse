package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserUpdateDto implements Serializable {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private Set<Long> warehouseIds;
}
