package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Measurement;
import uz.pdp.appwarehouse.domain.User;
import uz.pdp.appwarehouse.domain.Warehouse;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.MeasurementDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.model.WarehouseDto;
import uz.pdp.appwarehouse.repository.MeasurementRepository;
import uz.pdp.appwarehouse.repository.UserRepository;
import uz.pdp.appwarehouse.repository.WarehouseRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Warehouse>> get() {
        return new ResponseEntity<>(warehouseRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> get(@PathVariable Long id) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(id);
        return optionalWarehouse.map(warehouse -> new ResponseEntity<>(warehouse, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody WarehouseDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (warehouseRepository.existsByNameAndActiveTrue(dto.getName()))
            return new ResponseEntity<>(new Result("This warehouse already exist", false), HttpStatus.CONFLICT);

        warehouseRepository.save(Warehouse.childBuilder().name(dto.getName()).build());
        return new ResponseEntity<>(new Result("Warehouse created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody WarehouseDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (warehouseRepository.existsByNameAndIdNotAndActiveTrue(dto.getName(), id))
            return new ResponseEntity<>(new Result("This warehouse already exist", false), HttpStatus.CONFLICT);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(id);

        if (optionalWarehouse.isPresent()) {
            Warehouse warehouse = optionalWarehouse.get();
            warehouse.setName(dto.getName());
            warehouseRepository.save(warehouse);
            return new ResponseEntity<>(new Result("warehouse edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    // todo relation tablelarni statusniyam false qilish kerak
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(id);
        if (optionalWarehouse.isPresent()) {
            Warehouse warehouse = optionalWarehouse.get();
            Set<User> workers = userRepository.getUsersWorkingOneWarehouse(id);
            workers.forEach(user -> {
                user.setActive(false);
                userRepository.save(user);
            });

            warehouse.setActive(false);
            warehouseRepository.save(warehouse);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
