package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.DeviceUser;
import ro.tuc.ds2020.repositories.DeviceUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private DeviceUserRepository deviceUserRepository;

    public List<UserDTO> findAll() {
        List<DeviceUser> users = deviceUserRepository.findAll();
        return users.stream().map(user -> UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .admin(user.isAdmin())
                .build()).collect(Collectors.toList());
    }

    public UserDTO findById(UUID uuid) {
        Optional<DeviceUser> userOptional = deviceUserRepository.findById(uuid);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException(DeviceUser.class.getSimpleName() + " not found with id " + uuid);
        }

        DeviceUser user = userOptional.get();
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .admin(user.isAdmin())
                .build();
    }

    public UUID insert(UserDTO userDTO) {
        return deviceUserRepository.save(DeviceUser.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .address(userDTO.getAddress())
                .admin(userDTO.isAdmin())
                .build()).getId();
    }

    public UUID update(UserDTO userDTO) {
        Optional<DeviceUser> userOptional = deviceUserRepository.findById(userDTO.getId());
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException(DeviceUser.class.getSimpleName() + " not found with id " + userDTO.getId());
        }

        DeviceUser user = userOptional.get();
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        user.setAdmin(userDTO.isAdmin());

        deviceUserRepository.save(user);
        return deviceUserRepository.save(user).getId();
    }

    public void delete(UUID id) {
        Optional<DeviceUser> userOptional = deviceUserRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException(DeviceUser.class.getSimpleName() + " not found with id " + id);
        }

        deviceUserRepository.delete(userOptional.get());
    }
}
