package uz.pdp.appwarehouse.domain;

import lombok.*;
import uz.pdp.appwarehouse.domain.modelEntity.AbsEntity;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "parent_id"}))
public class Category extends AbsEntity {
    @ManyToOne
    private Category parent;

    @Builder(builderMethodName = "childBuilder")
    public Category(Long id, String name, Category parent){
        super(id, name, true);
        this.parent = parent;
    }

}
