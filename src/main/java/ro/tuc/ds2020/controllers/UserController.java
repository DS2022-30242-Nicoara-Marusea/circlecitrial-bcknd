package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.services.UserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/devicesusers")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAll()
    {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID uuid)
    {
        return new ResponseEntity<>(userService.findById(uuid), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> insert(@RequestBody @Valid UserDTO userDTO)
    {
        return new ResponseEntity<>(userService.insert(userDTO), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid UserDTO userDTO)
    {
        return new ResponseEntity<>(userService.update(userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id)
    {
        userService.delete(id);
        return new ResponseEntity<>("Deleted user with id " + id, HttpStatus.OK);
    }
}
