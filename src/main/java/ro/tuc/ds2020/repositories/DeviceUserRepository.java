package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.DeviceUser;

import java.util.UUID;

public interface DeviceUserRepository extends JpaRepository<DeviceUser, UUID> {
}
