package uz.pdp.appwarehouse.domain;

import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.appwarehouse.domain.modelEntity.AbsEntity;

import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Measurement extends AbsEntity {
    @Builder(builderMethodName = "childBuilder")
    public Measurement(Long id, String name) {
        super(id, name, true);
    }
}
