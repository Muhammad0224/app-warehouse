package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.InputProduct;

import java.util.List;

@Repository
public interface InputProductRepository extends JpaRepository<InputProduct, Long> {
    List<InputProduct> findAllByInputId(Long input_id);

    List<InputProduct> findAllByInput_FactureNumber(Integer input_factureNumber);
}
