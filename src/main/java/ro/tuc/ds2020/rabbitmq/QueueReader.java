package ro.tuc.ds2020.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.SensorService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
public class QueueReader {
    private final SensorService sensorService;
    private final DeviceService deviceService;

    SensorDTO sensorDTO = new SensorDTO();
    DeviceDTO deviceDTO = new DeviceDTO();

    public QueueReader(SensorService sensorService, DeviceService deviceService) {
        this.sensorService = sensorService;
        this.deviceService = deviceService;
    }

    @PostConstruct
    public void receiveData() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        String uri = "amqps://pxycwtcm:xMByUVCGay2FpwL0wvI5TkmC26FclX9b@goose.rmq2.cloudamqp.com/pxycwtcm";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(30000);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queue = "SensorMeasurements";
        boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
        boolean exclusive = false;  //exclusive - if queue only will be used by one connection
        boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

        channel.queueDeclare(queue, durable, exclusive, autoDelete, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String[] arr = new String[10000];
            String data = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received '" + data + "'");
            arr = data.split(",");

            char c = 'd';
            for(String s: arr){
                switch (c) {
                    case 'd':
                        System.out.println(s.substring(14, s.length()-1));
                        String aux = s.substring(14, 24) + " " +s.substring(25, 33);
                        LocalDateTime dateTime = LocalDateTime.parse(aux, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        long t = dateTime.toEpochSecond(ZoneOffset.UTC);
                        Timestamp timestamp = new Timestamp(t);
                        sensorDTO.setTimestamp(timestamp);
                        break;
                    case 'e':
                        System.out.println(s.substring(14, s.length()));
                        sensorDTO.setEnergyConsumption(Double.parseDouble(s.substring(15, s.length())));
                        break;
                    case 'f':
                        System.out.println(s.substring(17, s.length()-3));
                        deviceDTO = deviceService.findById(UUID.fromString(s.substring(16, s.length()-2)));
                        Device device = new Device();
                        device.setId(deviceDTO.getId());
                        sensorDTO.setDevice(device);
                        break;
                    default:
                        System.out.println("Nu uita de default!");
                        break;
                }
                c = (char) (c+1);
            }
            System.out.println("Sensor: " + sensorDTO.getTimestamp() + ", " +
                    sensorDTO.getEnergyConsumption() + ", " + sensorDTO.getDevice().getId());
            sensorService.insert(sensorDTO);
        };

        channel.basicConsume("SensorMeasurements", true, deliverCallback, consumerTag -> {
        });
    }
}
