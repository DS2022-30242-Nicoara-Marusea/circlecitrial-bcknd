package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.services.DeviceService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping()
    public ResponseEntity<?> getAll()
    {
        return new ResponseEntity<>(deviceService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID uuid)
    {
        return new ResponseEntity<>(deviceService.findById(uuid), HttpStatus.OK);
    }

    @GetMapping("/userdevices/{id}")
    public ResponseEntity<?> usersDevices(@PathVariable("id") UUID uuid)
    {
        return new ResponseEntity<>(deviceService.retDevices(uuid), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> insert(@RequestBody @Valid DeviceDTO deviceDTO)
    {
        return new ResponseEntity<>(deviceService.insert(deviceDTO), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid DeviceDTO deviceDTO)
    {
        return new ResponseEntity<>(deviceService.update(deviceDTO), HttpStatus.OK);
    }

    @PutMapping("/adduser/{id}")
    public ResponseEntity<?> addToUser(@PathVariable("id")UUID id, @RequestBody @Valid String usrId)
    {
        String bla = usrId.substring(1, usrId.length()-1);
        UUID userID = UUID.fromString(bla);
        return new ResponseEntity<>(deviceService.retUser(id, userID), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id)
    {
        deviceService.delete(id);
        return new ResponseEntity<>("Deleted device with id " + id, HttpStatus.OK);
    }

   @PutMapping("/chart/{id}")
    public ResponseEntity<?> mkChart(@PathVariable("id") UUID id, @RequestBody @Valid String datte)
    {
        System.out.println(datte);
        return new ResponseEntity<>(deviceService.getDevSensor(id, datte), HttpStatus.OK);
    }
}
