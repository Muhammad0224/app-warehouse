package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Currency;
import uz.pdp.appwarehouse.domain.Measurement;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.CurrencyDto;
import uz.pdp.appwarehouse.model.MeasurementDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.repository.CurrencyRepository;
import uz.pdp.appwarehouse.repository.MeasurementRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping
    public ResponseEntity<List<Currency>> get() {
        return new ResponseEntity<>(currencyRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> get(@PathVariable Long id) {
        Optional<Currency> optionalCurrency = currencyRepository.findByIdAndActiveTrue(id);
        return optionalCurrency.map(currency -> new ResponseEntity<>(currency, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody CurrencyDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (currencyRepository.existsByNameAndActiveTrue(dto.getName()))
            return new ResponseEntity<>(new Result("This currency already exist", false), HttpStatus.CONFLICT);

        currencyRepository.save(Currency.childBuilder().name(dto.getName()).build());
        return new ResponseEntity<>(new Result("Currency created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody CurrencyDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (currencyRepository.existsByNameAndIdNotAndActiveTrue(dto.getName(), id))
            return new ResponseEntity<>(new Result("This currency already exist", false), HttpStatus.CONFLICT);

        Optional<Currency> currencyOptional = currencyRepository.findByIdAndActiveTrue(id);

        if (currencyOptional.isPresent()) {
            Currency currency = currencyOptional.get();
            currency.setName(dto.getName());
            currencyRepository.save(currency);
            return new ResponseEntity<>(new Result("Currency edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    //todo spravochnik tablelarda delete bo'lmaydi

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> delete(@PathVariable Long id){
//        Optional<Currency> optionalCurrency = currencyRepository.findByIdAndActiveTrue(id);
//        if (optionalCurrency.isPresent()){
//            Currency currency = optionalCurrency.get();
//            currency.setActive(false);
//            currencyRepository.save(currency);
//            return new ResponseEntity<>(true,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
//    }

}
