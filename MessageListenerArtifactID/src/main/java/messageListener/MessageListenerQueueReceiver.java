package messageListener;

import javax.naming.*;
import java.util.Properties;
import javax.jms.*;

public class MessageListenerQueueReceiver {

    // Member-Variables
    InitialContext initialContext;
    ConnectionFactory connectionFactory;
    Connection connection;
    Session session;

    Queue queue;
    MessageConsumer queueConsumer;

    // Connect to Message-Broker
    public void connectToMessageBroker() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

        initialContext = new InitialContext(properties);

        connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

        connection = connectionFactory.createConnection();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    // Access Point-To-Point Queue
    public void accessPointToPointQueue() throws Exception {
        queue = (Queue) initialContext.lookup("dynamicQueues/ExampleQueue");
        queueConsumer = session.createConsumer(queue);
    }

    // Get Messages and Print them Out
    public void getAndPrintQueueMessage() throws Exception {
        queueConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("Queue Message: " + ((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        MessageListenerQueueReceiver messageListenerReceiver = new MessageListenerQueueReceiver();

        messageListenerReceiver.connectToMessageBroker();
        messageListenerReceiver.accessPointToPointQueue();
        messageListenerReceiver.connection.start();
        messageListenerReceiver.getAndPrintQueueMessage();
    }
}
