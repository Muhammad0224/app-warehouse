package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.*;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.*;
import uz.pdp.appwarehouse.repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/output")
public class OutputController {
    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping
    public ResponseEntity<List<Output>> get() {
        return new ResponseEntity<>(outputRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/byWarehouse/{warehouseId}")
    public ResponseEntity<List<Output>> getByWarehouse(@PathVariable Long warehouseId) {
        return new ResponseEntity<>(outputRepository.findAllByWarehouseId(warehouseId), HttpStatus.OK);
    }

    @GetMapping("/byClient/{clientId}")
    public ResponseEntity<List<Output>> getBySupplier(@PathVariable Long clientId) {
        return new ResponseEntity<>(outputRepository.findAllByClientId(clientId), HttpStatus.OK);
    }

    @GetMapping("/byCurrency/{currencyId}")
    public ResponseEntity<List<Output>> getByCurrency(@PathVariable Long currencyId) {
        return new ResponseEntity<>(outputRepository.findAllByCurrencyId(currencyId), HttpStatus.OK);
    }

    @GetMapping("/byFactureNumber/{factureNumber}")
    public ResponseEntity<Output> getByFacture(@PathVariable Integer factureNumber) {
        Optional<Output> optionalOutput = outputRepository.findByFactureNumber(factureNumber);
        return optionalOutput.map(output -> new ResponseEntity<>(output, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Output> get(@PathVariable Long id) {
        Optional<Output> optionalOutput = outputRepository.findById(id);
        return optionalOutput.map(output -> new ResponseEntity<>(output, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody OutputDto dto) {
        if (Utils.isEmpty(dto.getFactureNumber()))
            return new ResponseEntity<>(new Result("Facture number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getWarehouseId()))
            return new ResponseEntity<>(new Result("Warehouse shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getCurrencyId()))
            return new ResponseEntity<>(new Result("Currency shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getClientId()))
            return new ResponseEntity<>(new Result("Client shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(dto.getWarehouseId());
        if (!optionalWarehouse.isPresent())
            return new ResponseEntity<>(new Result("Warehouse not found", false), HttpStatus.NOT_FOUND);

        Optional<Currency> optionalCurrency = currencyRepository.findByIdAndActiveTrue(dto.getCurrencyId());
        if (!optionalCurrency.isPresent())
            return new ResponseEntity<>(new Result("Currency not found", false), HttpStatus.NOT_FOUND);

        Optional<Client> optionalClient = clientRepository.findByIdAndActiveTrue(dto.getClientId());
        if (!optionalClient.isPresent())
            return new ResponseEntity<>(new Result("Client not found", false), HttpStatus.NOT_FOUND);

        outputRepository.save(Output.builder()
                .warehouse(optionalWarehouse.get())
                .currency(optionalCurrency.get())
                .factureNumber(dto.getFactureNumber())
                .client(optionalClient.get())
                .code(getCode())
                .soldDate(Timestamp.valueOf(LocalDateTime.now()))
                .build());

        return new ResponseEntity<>(new Result("Output created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody OutputUpdateDto dto) {
        if (Utils.isEmpty(dto.getFactureNumber()))
            return new ResponseEntity<>(new Result("Facture number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getWarehouseId()))
            return new ResponseEntity<>(new Result("Warehouse shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getCurrencyId()))
            return new ResponseEntity<>(new Result("Currency shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getClientId()))
            return new ResponseEntity<>(new Result("Client shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(dto.getWarehouseId());
        if (!optionalWarehouse.isPresent())
            return new ResponseEntity<>(new Result("Warehouse not found", false), HttpStatus.NOT_FOUND);

        Optional<Currency> optionalCurrency = currencyRepository.findByIdAndActiveTrue(dto.getCurrencyId());
        if (!optionalCurrency.isPresent())
            return new ResponseEntity<>(new Result("Currency not found", false), HttpStatus.NOT_FOUND);

        Optional<Client> optionalClient = clientRepository.findByIdAndActiveTrue(dto.getClientId());
        if (!optionalClient.isPresent())
            return new ResponseEntity<>(new Result("Client not found", false), HttpStatus.NOT_FOUND);

        Optional<Output> optionalOutput = outputRepository.findById(id);

        if (optionalOutput.isPresent()) {
            Output output = optionalOutput.get();
            output.setCurrency(optionalCurrency.get());
            output.setFactureNumber(dto.getFactureNumber());
            output.setClient(optionalClient.get());
            output.setWarehouse(optionalWarehouse.get());
            outputRepository.save(output);
            return new ResponseEntity<>(new Result("Input edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
//        Optional<Input> optionalInput = inputRepository.findById(id);
//        if (optionalInput.isPresent()) {
//            Input input = optionalInput.get();
//            input.setActive(false);
//            inputRepository.save(input);
//            return new ResponseEntity<>(true, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
//    }

    private Integer getCode() {
        Integer maxCode = outputRepository.getMaxCode();
        if (Utils.isEmpty(maxCode))
            return 1;
        return maxCode + 1;
    }

}
