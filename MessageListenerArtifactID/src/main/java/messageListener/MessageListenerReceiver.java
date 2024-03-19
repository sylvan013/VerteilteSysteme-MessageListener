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
		queue = (Queue) initialContext.lookup("/queue/ExampleQueue");
		queueConsumer = session.createConsumer(queue);
	}

	// Access Topic-Orientated Queue
	public void accessTopicQueue() throws Exception {
		topic = (Topic) initialContext.lookup("/topic/ExampleTopic");
		topicConsumer = session.createConsumer(topic);
	}

	// Get Messages and Print them Out
	public void getAndPrintMessages() throws Exception {
		TextMessage message0 = (TextMessage) queueConsumer.receive();
		TextMessage message1 = (TextMessage) topicConsumer.receive();

		queueConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				System.out.println(message);
			}
		});

		topicConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				System.out.println(message);
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
		messageListenerReceiver.getAndPrintMessages();
		messageListenerReceiver.closeConnections();
	}

}
