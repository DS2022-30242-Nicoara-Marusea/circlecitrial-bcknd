package ro.tuc.ds2020.dtos;

import lombok.*;
import ro.tuc.ds2020.entities.Device;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SensorDTO {
    private UUID id;

    private Timestamp timestamp;

    private double energyConsumption;

    private Device device;
}
