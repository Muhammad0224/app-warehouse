package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Client;
import uz.pdp.appwarehouse.domain.Supplier;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.ClientDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.model.SupplierDto;
import uz.pdp.appwarehouse.repository.ClientRepository;
import uz.pdp.appwarehouse.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<List<Client>> get() {
        return new ResponseEntity<>(clientRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> get(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findByIdAndActiveTrue(id);
        return optionalClient.map(client -> new ResponseEntity<>(client, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody ClientDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("Phone number shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        if (clientRepository.existsByPhoneNumberAndActiveTrue(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("This client already exist", false), HttpStatus.CONFLICT);

        clientRepository.save(Client.childBuilder().name(dto.getName()).phoneNumber(dto.getPhoneNumber()).build());
        return new ResponseEntity<>(new Result("Client created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody ClientDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("Phone number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (clientRepository.existsByPhoneNumberAndIdNotAndActiveTrue(dto.getPhoneNumber(), id))
            return new ResponseEntity<>(new Result("This client already exist", false), HttpStatus.CONFLICT);

        Optional<Client> optionalClient = clientRepository.findByIdAndActiveTrue(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setName(dto.getName());
            client.setPhoneNumber(dto.getPhoneNumber());
            clientRepository.save(client);
            return new ResponseEntity<>(new Result("Client edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        Optional<Client> optionalClient = clientRepository.findByIdAndActiveTrue(id);
        if (optionalClient.isPresent()){
            Client client = optionalClient.get();
            client.setActive(false);
            clientRepository.save(client);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
    }

}
