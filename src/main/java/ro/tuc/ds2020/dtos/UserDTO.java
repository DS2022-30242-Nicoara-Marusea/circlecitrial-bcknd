package ro.tuc.ds2020.dtos;

import lombok.*;
import ro.tuc.ds2020.entities.Device;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private UUID id;

    private String name;

    private String address;

    private boolean admin;

}
