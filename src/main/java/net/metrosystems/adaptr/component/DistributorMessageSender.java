package net.metrosystems.adaptr.component;

import net.metrosystems.adaptr.data.AdaptrMessage;
import net.metrosystems.adaptr.service.RoutingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DistributorMessageSender {

	final
	JmsTemplate jmsTemplate;

	final
	RoutingInformation routingInformation;

	@Autowired
	public DistributorMessageSender(JmsTemplate jmsTemplate, RoutingInformation routingInformation) {
		this.jmsTemplate = jmsTemplate;
		this.routingInformation = routingInformation;
	}

	public void sendMessage(final AdaptrMessage adaptrMessage) {
		for (String queueName : routingInformation.getRoutingInfo()) {
			jmsTemplate.send(queueName, session -> session.createObjectMessage(adaptrMessage));
		}
//		jmsTemplate.send(session -> session.createObjectMessage(adaptrMessage));
	}
}
