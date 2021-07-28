package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Client;
import uz.pdp.appwarehouse.domain.User;
import uz.pdp.appwarehouse.domain.Warehouse;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.ClientDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.model.UserDto;
import uz.pdp.appwarehouse.model.UserUpdateDto;
import uz.pdp.appwarehouse.repository.ClientRepository;
import uz.pdp.appwarehouse.repository.UserRepository;
import uz.pdp.appwarehouse.repository.WarehouseRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @GetMapping
    public ResponseEntity<List<User>> get() {
        return new ResponseEntity<>(userRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/byWarehouse/{warehouseId}")
    public ResponseEntity<List<User>> getByWarehouse(@PathVariable Long warehouseId) {
        return new ResponseEntity<>(userRepository.findAllByWarehousesIdAndActiveTrueAndWarehousesActiveTrue(warehouseId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findByIdAndActiveTrue(id);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody UserDto dto) {
        if (Utils.isEmpty(dto.getFirstName()))
            return new ResponseEntity<>(new Result("First name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("Phone number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getWarehouseIds()))
            return new ResponseEntity<>(new Result("Warehouse shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        if (userRepository.existsByPhoneNumberAndActiveTrue(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("This user already exist", false), HttpStatus.CONFLICT);

        Set<Warehouse> warehouses = new HashSet<>();
        for (Long warehouseId : dto.getWarehouseIds()) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(warehouseId);
            if (!optionalWarehouse.isPresent())
                return new ResponseEntity<>(new Result("Warehouse not found", false), HttpStatus.NOT_FOUND);
            warehouses.add(optionalWarehouse.get());
        }

        userRepository.save(User.builder()
                .firstName(dto.getFirstName())
                .phoneNumber(dto.getPhoneNumber())
                .lastName(dto.getLastName())
                .code(getCode())
                .warehouses(warehouses)
                .password(dto.getPassword()).build());
        return new ResponseEntity<>(new Result("Client created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        if (Utils.isEmpty(dto.getFirstName()))
            return new ResponseEntity<>(new Result("First name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("Phone number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getWarehouseIds()))
            return new ResponseEntity<>(new Result("Warehouse shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        if (userRepository.existsByPhoneNumberAndIdNotAndActiveTrue(dto.getPhoneNumber(),id))
            return new ResponseEntity<>(new Result("This user already exist", false), HttpStatus.CONFLICT);

        Set<Warehouse> warehouses = new HashSet<>();
        for (Long warehouseId : dto.getWarehouseIds()) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(warehouseId);
            if (!optionalWarehouse.isPresent())
                return new ResponseEntity<>(new Result("Warehouse not found", false), HttpStatus.NOT_FOUND);
            warehouses.add(optionalWarehouse.get());
        }

        Optional<User> optionalUser = userRepository.findByIdAndActiveTrue(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setPassword(dto.getPassword());
            user.setWarehouses(warehouses);
            userRepository.save(user);
            return new ResponseEntity<>(new Result("User edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findByIdAndActiveTrue(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            userRepository.save(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    private Integer getCode(){
        Integer maxCode = userRepository.getMaxCode();
        if (Utils.isEmpty(maxCode))
            return 1;
        return maxCode+1;
    }
}
