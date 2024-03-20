package messageListener;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class MessageListenerSender {

	// Member-Variables
	InitialContext initialContext;
	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;

	Queue queue;
	MessageProducer queueMessageProducer;

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
		queueMessageProducer = session.createProducer(queue);
	}


	// Send Messages
	public void sendMessages() throws Exception {
		Message message = session.createTextMessage("Hello World!");
		queueMessageProducer.send(message);
		System.out.println("queue message sent");
	}

	// Close all Connections
	public void closeConnections() throws Exception {
		if (initialContext != null) {
			initialContext.close();
		}

		if (connection != null) {
			connection.close();
		}
	}

	public static void main(String[] args) throws Exception {
		MessageListenerSender messageListenerSender = new MessageListenerSender();
		messageListenerSender.connectToMessageBroker();
		messageListenerSender.accessPointToPointQueue();
		messageListenerSender.sendMessages();
		messageListenerSender.closeConnections();
	}
}