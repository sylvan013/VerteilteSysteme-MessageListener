package messageListener;

import javax.naming.*;

import java.util.Properties;

import javax.jms.*;

public class MessageListenerReceiver {

	// Member-Variables
	InitialContext initialContext;
	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;

	Queue queue;
	MessageConsumer queueConsumer;

	Topic topic;
	MessageConsumer topicConsumer;

	// Connect to Message-Broker
	public void connectToMessageBroker() throws Exception {
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
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

	// Access Topic-Orientated Queue
	public void accessTopicQueue() throws Exception {
		topic = (Topic) initialContext.lookup("dynamicTopics/ExampleTopic");
		topicConsumer = session.createConsumer(topic);
	}

	// Get Messages and Print them Out
	public void getAndPrintQueueMessage() throws Exception {
		queueConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				System.out.println(message);
				try {
					System.out.println("Queue Message: " + ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void getAndPrintTopicMessage() throws Exception {
		System.out.println("AAAAAAAAA");
		topicConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				System.out.println(message);
				try {
					System.out.println("BBBB");
					System.out.println("Topic Message: " + message.toString());
					System.out.println("AAAA");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Close all Connections
	public void closeConnections() throws Exception {
		initialContext.close();
		connection.close();
	}

	public static void main(String[] args) throws Exception {
		MessageListenerReceiver messageListenerReceiver = new MessageListenerReceiver();

		messageListenerReceiver.connectToMessageBroker();
		messageListenerReceiver.accessPointToPointQueue();
		messageListenerReceiver.accessTopicQueue();
		messageListenerReceiver.connection.start();
		messageListenerReceiver.getAndPrintQueueMessage();
		messageListenerReceiver.getAndPrintTopicMessage();
		messageListenerReceiver.closeConnections();
		
	}
}
