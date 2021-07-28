package uz.pdp.appwarehouse.domain;

import lombok.*;
import uz.pdp.appwarehouse.domain.modelEntity.AbsEntity;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client extends AbsEntity {
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Builder(builderMethodName = "childBuilder")
    public Client(Long id, String name, String phoneNumber) {
        super(id, name, true);
        this.phoneNumber = phoneNumber;
    }
}
