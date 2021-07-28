package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Input;
import uz.pdp.appwarehouse.domain.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InputRepository extends JpaRepository<Input, Long> {

    List<Input> findAllByWarehouseId(Long category_id);

    List<Input> findAllBySupplierId(Long supplier_id);

    List<Input> findAllByCurrencyId(Long currency_id);

    Optional<Input> findByFactureNumber(Integer factureNumber);

    @Query(nativeQuery = true, value = "select max(code) from inputs")
    Integer getMaxCode();
}
