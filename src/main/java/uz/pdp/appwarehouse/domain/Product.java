package uz.pdp.appwarehouse.domain;

import lombok.*;
import uz.pdp.appwarehouse.domain.modelEntity.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category_id"}))
public class Product extends AbsEntity {
    @ManyToOne(optional = false)
    private Category category;

    @OneToOne
    private Attachment attachment;

    @Column(unique = true)
    private Integer code;

    @ManyToOne
    private Measurement measurement;

    @Builder(builderMethodName = "childBuilder")
    public Product(Long id, String name, Category category, Attachment attachment, Integer code, Measurement measurement){
        super(id, name, true);
        this.category = category;
        this.attachment = attachment;
        this.code = code;
        this.measurement = measurement;
    }
}
