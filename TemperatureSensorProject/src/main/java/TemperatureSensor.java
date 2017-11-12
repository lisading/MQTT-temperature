
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TemperatureSensor {

    public static void main(String[] args) {

        System.out.println("Starting TemperatureSensor...");
        String broker = "tcp://localhost:1883";
        String clientId = "TemperatureSensor";
        MemoryPersistence persistence = new MemoryPersistence();

        try {

            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    int qos = 2;
                    String topic = "";

                    String content = "";

                    // Generate a random temperature between 0 to 100
                    Random rand = new Random();
                    int temperature = rand.nextInt(100) + 1;
                    System.out.println("Temperature: " + temperature);

                    if (temperature >= 0) {
                        if (temperature <= 45) {
                            topic = "temperature/pittsburgh/coldTemps";
                        } else if (temperature <= 80) {
                            topic = "temperature/pittsburgh/niceTemps";
                        } else {
                            topic = "temperature/pittsburgh/hotTemps";
                        }

                    }

                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    String temperatureStr = Integer.toString(temperature);
                    content = "{\"temperature\": \"" + temperatureStr + "\", \"time\": \"" + timeStamp + "\"}";
                    System.out.println("Topic: " + topic);
                    System.out.println("Publishing message: " + content);
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(qos);

                    try {
                        // publish message
                        sampleClient.publish(topic, message);
                        System.out.println("Message published");
//                        sampleClient.disconnect();
//                        System.out.println("Disconnected");
                    } catch (MqttException ex) {
                        Logger.getLogger(TemperatureSensor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }, 0, 5000); // Time interval is 5000 milliseconds

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}
