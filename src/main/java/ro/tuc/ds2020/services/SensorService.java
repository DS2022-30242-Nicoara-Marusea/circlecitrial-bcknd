package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.SensorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;

    public List<SensorDTO> findAll() {
        List<Sensor> sensors = sensorRepository.findAll();
        return sensors.stream().map(sensor -> SensorDTO.builder()
                .id(sensor.getId())
                .timestamp(sensor.getTimestamp())
                .energyConsumption(sensor.getEnergyConsumption())
                .device(sensor.getDevice())
                .build()).collect(Collectors.toList());
    }

    public SensorDTO findById(UUID uuid) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(uuid);
        if (!sensorOptional.isPresent()) {
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " not found with id " + uuid);
        }

        Sensor sensor = sensorOptional.get();
        return SensorDTO.builder()
                .id(sensor.getId())
                .timestamp(sensor.getTimestamp())
                .energyConsumption(sensor.getEnergyConsumption())
                .device(sensor.getDevice())
                .build();
    }

    public UUID insert(SensorDTO sensorDTO) {
        return sensorRepository.save(Sensor.builder()
                .id(sensorDTO.getId())
                .timestamp(sensorDTO.getTimestamp())
                .energyConsumption(sensorDTO.getEnergyConsumption())
                .device(sensorDTO.getDevice()).build()).getId();

    }

    public UUID update(SensorDTO sensorDTO) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(sensorDTO.getId());
        if (!sensorOptional.isPresent()) {
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " not found with id " + sensorDTO.getId());
        }

        Sensor sensor = sensorOptional.get();
        sensor.setTimestamp(sensorDTO.getTimestamp());
        sensor.setEnergyConsumption(sensorDTO.getEnergyConsumption());
        sensor.setDevice(sensorDTO.getDevice());

        sensorRepository.save(sensor);
        return sensorRepository.save(sensor).getId();
    }

    public void delete(UUID id) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);
        if (!sensorOptional.isPresent()) {
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " not found with id " + id);
        }

        sensorRepository.delete(sensorOptional.get());
    }
}
