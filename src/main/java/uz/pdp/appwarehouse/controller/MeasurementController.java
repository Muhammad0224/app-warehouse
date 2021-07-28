package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Measurement;
import uz.pdp.appwarehouse.domain.Warehouse;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.MeasurementDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.repository.MeasurementRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/measurement")
public class MeasurementController {

    @Autowired
    private MeasurementRepository measurementRepository;

    @GetMapping
    public ResponseEntity<List<Measurement>> get() {
        return new ResponseEntity<>(measurementRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Measurement> get(@PathVariable Long id) {
        Optional<Measurement> optionalMeasurement = measurementRepository.findByIdAndActiveTrue(id);
        return optionalMeasurement.map(measurement -> new ResponseEntity<>(measurement, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody MeasurementDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (measurementRepository.existsByNameAndActiveTrue(dto.getName()))
            return new ResponseEntity<>(new Result("This measurement already exist", false), HttpStatus.CONFLICT);

        measurementRepository.save(Measurement.childBuilder().name(dto.getName()).build());
        return new ResponseEntity<>(new Result("Measurement created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody MeasurementDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (measurementRepository.existsByNameAndIdNotAndActiveTrue(dto.getName(), id))
            return new ResponseEntity<>(new Result("This measurement already exist", false), HttpStatus.CONFLICT);

        Optional<Measurement> optionalMeasurement = measurementRepository.findByIdAndActiveTrue(id);

        if (optionalMeasurement.isPresent()) {
            Measurement measurement = optionalMeasurement.get();
            measurement.setName(dto.getName());
            measurementRepository.save(measurement);
            return new ResponseEntity<>(new Result("Measurement edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    //todo spravochnik tablelarda delete bo'lmaydi

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> delete(@PathVariable Long id){
//        Optional<Measurement> optionalMeasurement = measurementRepository.findByIdAndActiveTrue(id);
//        if (optionalMeasurement.isPresent()){
//            Measurement measurement = optionalMeasurement.get();
//            measurement.setActive(false);
//            measurementRepository.save(measurement);
//            return new ResponseEntity<>(true,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
//    }

}
