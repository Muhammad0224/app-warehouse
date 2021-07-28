package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Client;
import uz.pdp.appwarehouse.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumberAndActiveTrue(String phoneNumber);

    boolean existsByPhoneNumberAndIdNotAndActiveTrue(String phoneNumber, Long id);

    List<User> findAllByActiveTrue();

    List<User> findAllByWarehousesIdAndActiveTrueAndWarehousesActiveTrue(Long warehouses_id);

    Optional<User> findByIdAndActiveTrue(Long aLong);

    @Query(nativeQuery = true, value = "select max(code) from users")
    Integer getMaxCode();

    @Query(nativeQuery = true, value = "select * from users t join users_warehouses uw on t.id = uw.users_id where (select count(*) from users_warehouses p where p.users_id = t.id) = 1 and uw.warehouses_id=:warehouseId")
    Set<User> getUsersWorkingOneWarehouse(Long warehouseId);
}
