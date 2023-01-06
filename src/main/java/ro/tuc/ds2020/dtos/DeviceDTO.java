package ro.tuc.ds2020.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDTO {
    private UUID id;

    private String description;

    private String address;

    private double max_hourly_energy_consumption;

    private double average_consumption;

    private UUID deviceUser;
}
