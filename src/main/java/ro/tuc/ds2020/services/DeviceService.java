package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.DeviceUser;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.DeviceUserRepository;
import ro.tuc.ds2020.repositories.SensorRepository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final DeviceUserRepository deviceUserRepository;

    private final SensorRepository sensorRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, DeviceUserRepository deviceUserRepository, SensorRepository sensorRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceUserRepository = deviceUserRepository;
        this.sensorRepository = sensorRepository;
    }

    public UUID retUser(UUID devID, UUID usrID)
    {
        Device device = deviceRepository.findById(devID).get();
        System.out.println("weeeeeeeee");
        //System.out.println(device.getDeviceUser().getId());
        DeviceUser devUser = deviceUserRepository.findById(usrID).get();

        device.setDeviceUser(devUser);
        System.out.println(devUser.getId());
        device = deviceRepository.save(device);
        System.out.println(device.getId());
        return device.getDeviceUser().getId();

    }

    public List<DeviceDTO> retDevices(UUID usrId){
        List<DeviceDTO> list = new ArrayList<>();
        List<Device> list1 = deviceRepository.findAll();

        for(Device dev: list1)
        {
            if(dev.getDeviceUser() != null)
            {
                if(dev.getDeviceUser().getId().equals(usrId))
                {
                    list.add(new DeviceDTO(dev.getId(), dev.getDescription(), dev.getAddress(), dev.getMax_hourly_energy_consumption(),
                            dev.getAverageConsumption(), dev.getDeviceUser().getId()));
                }
            }
        }

        return list;
    }

    public List<DeviceDTO> findAll() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(device -> DeviceDTO.builder()
                .id(device.getId())
                .description(device.getDescription())
                .address(device.getAddress())
                .max_hourly_energy_consumption(device.getMax_hourly_energy_consumption())
                .average_consumption(device.getAverageConsumption()).build()).collect(Collectors.toList());
                //.deviceUser(device.getDeviceUser().getId()).build()).collect(Collectors.toList());
                //.sensor(device.getSensor().convert(device.getSensor()))
                //.build()).collect(Collectors.toList());
    }

    public DeviceDTO findById(UUID uuid) {
        Optional<Device> deviceOptional = deviceRepository.findById(uuid);
        if (!deviceOptional.isPresent()) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " not found with id " + uuid);
        }

        Device device = deviceOptional.get();
        return DeviceDTO.builder()
                .id(device.getId())
                .description(device.getDescription())
                .address(device.getAddress())
                .max_hourly_energy_consumption(device.getMax_hourly_energy_consumption())
                .average_consumption(device.getAverageConsumption()).build();
    }

    public UUID insert(DeviceDTO deviceDTO) {

        return deviceRepository.save(Device.builder()
                .id(deviceDTO.getId())
                .description(deviceDTO.getDescription())
                .address(deviceDTO.getAddress())
                .max_hourly_energy_consumption(deviceDTO.getMax_hourly_energy_consumption())
                .averageConsumption(deviceDTO.getAverage_consumption())
                .build()).getId();

    }

    public UUID update(DeviceDTO deviceDTO) {

        Optional<Device> deviceOptional = deviceRepository.findById(deviceDTO.getId());
        if (!deviceOptional.isPresent()) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " not found with id " + deviceDTO.getId());
        }

        Device device = deviceOptional.get();
        device.setDescription(deviceDTO.getDescription());
        device.setAddress(deviceDTO.getAddress());
        device.setMax_hourly_energy_consumption(deviceDTO.getMax_hourly_energy_consumption());
        device.setAverageConsumption(deviceDTO.getAverage_consumption());

        deviceRepository.save(device);
        return deviceRepository.save(device).getId();
    }

    public void delete(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " not found with id " + id);
        }

        deviceRepository.delete(deviceOptional.get());
    }

    public List<SensorDTO> getDevSensor(UUID devId, String datte)
    {
        long oneDay = 24 * 60 * 60 * 1000;

        Device device = deviceRepository.findById(devId).get();
        List<Sensor> sensors = sensorRepository.findAll();
        List<SensorDTO> sensorDTOS = new ArrayList<>();

        String aux = datte.substring(1, datte.length()-1);
        System.out.println(aux);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(aux);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(parsedDate);
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        System.out.println(timestamp);
        Timestamp maxTime = new java.sql.Timestamp(timestamp.getTime());
        maxTime.setTime(timestamp.getTime() + oneDay);
        System.out.println(maxTime);

        String mat = timestamp.toString().substring(0, 10);
        System.out.println("Mat");
        System.out.println(mat);

        String aux2 = "";

        for(Sensor s: sensors)
        {
            System.out.println(1);
            if(s.getDevice() != null)
            {
                System.out.println(2);
                if(s.getDevice().getId().equals(device.getId()))
                {
                    System.out.println(3);
                    System.out.println(s.getTimestamp().toString().substring(0, 10).equals(datte));
                    aux2 = s.getTimestamp().toString().substring(0, 10);
                    System.out.println(aux2);
                    System.out.println(aux.equals(aux2));
                    if(aux2.equals(aux))
                    {
                        System.out.println(4);
                        sensorDTOS.add(new SensorDTO(s.getId(), s.getTimestamp(), s.getEnergyConsumption(), s.getDevice()));
                    }
                }
            }
        }
        return sensorDTOS;
    }
}