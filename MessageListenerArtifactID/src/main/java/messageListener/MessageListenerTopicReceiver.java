package messageListener;

import javax.naming.*;
import java.util.Properties;
import javax.jms.*;

public class MessageListenerTopicReceiver {

    // Member-Variables
    InitialContext initialContext;
    ConnectionFactory connectionFactory;
    Connection connection;
    Session session;

    Topic topic;
    MessageConsumer topicConsumer;

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

    // Access Topic-Orientated Queue
    public void accessPointToPointQueue() throws Exception {
        topic = (Topic) initialContext.lookup("dynamicTopics/ExampleTopic");
        topicConsumer = session.createConsumer(topic);
    }

    // Get Messages and Print them out
    public void getAndPrintTopicMessage() throws Exception {
        topicConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("Topic Message: " + ((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        MessageListenerTopicReceiver messageListenerTopicReceiver = new MessageListenerTopicReceiver();

        messageListenerTopicReceiver.connectToMessageBroker();
        messageListenerTopicReceiver.connection.start();
        messageListenerTopicReceiver.accessPointToPointQueue();
        messageListenerTopicReceiver.getAndPrintTopicMessage();
    }
}