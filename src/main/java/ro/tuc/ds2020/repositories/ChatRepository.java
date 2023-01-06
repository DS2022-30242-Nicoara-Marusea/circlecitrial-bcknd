package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Chat;
import ro.tuc.ds2020.entities.Device;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
