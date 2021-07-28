package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Currency;
import uz.pdp.appwarehouse.domain.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameAndCategoryIdAndActiveTrue(String name, Long category_id);

    boolean existsByNameAndCategoryIdAndActiveTrueAndIdNot(String name, Long category_id, Long id);

    List<Product> findAllByActiveTrue();

    List<Product> findAllByCategoryIdAndActiveTrue(Long category_id);

    Optional<Product> findByIdAndActiveTrue(Long aLong);

    @Query(nativeQuery = true, value = "select max(code) from product")
    Integer getMaxCode();
}
