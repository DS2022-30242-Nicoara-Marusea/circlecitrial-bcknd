package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.services.SensorService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/sensors")
public class SensorController {
    @Autowired
    private SensorService sensorService;

    @GetMapping()
    public ResponseEntity<?> getAll()
    {
        return new ResponseEntity<>(sensorService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID uuid)
    {
        return new ResponseEntity<>(sensorService.findById(uuid), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> insert(@RequestBody @Valid SensorDTO sensorDTO)
    {
        return new ResponseEntity<>(sensorService.insert(sensorDTO), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid SensorDTO sensorDTO)
    {
        return new ResponseEntity<>(sensorService.update(sensorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id)
    {
        sensorService.delete(id);
        return new ResponseEntity<>("Deleted device with id " + id, HttpStatus.OK);
    }
}
