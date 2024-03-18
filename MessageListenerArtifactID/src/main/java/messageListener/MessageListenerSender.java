package messageListener;

import javax.naming.*;
import javax.jms.*;

public class MessageListenerSender {
	
	// Member-Variables
	InitialContext initialContext;
	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;
	
	Queue queue;
	MessageProducer queueMessageProducer;
	
	Topic topic;
	MessageProducer topicMessageProducer;
	
	// Connect to Message-Broker
	public void connectToMessageBroker() throws Exception {
		initialContext = new InitialContext();

        connectionFactory = (ConnectionFactory)
                initialContext.lookup("/ConnectionFactory");

        connection = connectionFactory.createConnection();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	// Access Point-To-Point Queue
	public void accessPointToPointQueue() throws Exception {
		queue = (Queue) initialContext.lookup("/queue/ExampleQueue");
        queueMessageProducer = session.createProducer(queue);
	}
	
	// Access Topic-Orientated Queue
	public void accessTopicQueue() throws Exception {
		topic = (Topic) initialContext.lookup("/topic/ExampleTopic");
        topicMessageProducer = session.createProducer(topic);
	}
	
	// Send Messages
	public void sendMessages() throws Exception {
		Message message0 = session.createTextMessage("Hello World!");
        queueMessageProducer.send(message0);
        
        Message message1 = session.createTextMessage("Goodbye World!");
        topicMessageProducer.send(message1);
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
		messageListenerSender.accessTopicQueue();
		messageListenerSender.sendMessages();
		messageListenerSender.closeConnections();
	}
}
