package net.metrosystems.adaptr.component;

import net.metrosystems.adaptr.data.AdaptrMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class QueueDistributor implements SessionAwareMessageListener {

	final
	MessageConverter messageConverter;

	final
	DistributorMessageSender sender;

	@Autowired
	public QueueDistributor(MessageConverter messageConverter, @Lazy DistributorMessageSender sender) {
		this.messageConverter = messageConverter;
		this.sender = sender;
	}

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		try {
//			session.run();
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
			AdaptrMessage response = (AdaptrMessage) messageConverter.fromMessage(message);
			sender.sendMessage(response);
			System.out.println("Application : order response received : {" + response + "}");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
//			session.commit();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			session.rollback();
		}
	}
}