package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Category;
import uz.pdp.appwarehouse.domain.Currency;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndParentIdAndActiveTrue(String name, Long parent_id);

    boolean existsByNameAndParentIdAndIdNotAndActiveTrue(String name, Long parent_id, Long id);

    List<Category> findAllByParentIdAndActiveTrue(Long parent_id);

    Optional<Category> findByIdAndActiveTrue(Long aLong);
}
