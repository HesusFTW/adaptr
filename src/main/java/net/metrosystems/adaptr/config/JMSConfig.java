package net.metrosystems.adaptr.config;

import net.metrosystems.adaptr.component.QueueDistributor;
import net.metrosystems.adaptr.converter.AdaptrMessageConverter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;

import java.util.Arrays;

@Configuration
@EnableJms
public class JMSConfig {
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${queue-upload}")
	private String destinationName;

	@Autowired
	QueueDistributor queueDistributor;

	@Bean
	@Primary
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setTrustedPackages(Arrays.asList("net.metrosystems.adaptr.data", "java.util"));
		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setTargetConnectionFactory(activeMQConnectionFactory());
		connectionFactory.setSessionCacheSize(10);
		return connectionFactory;
	}

	@Bean
	public DefaultMessageListenerContainer defaultMessageListenerContainer() {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(activeMQConnectionFactory());
		container.setDestinationName(destinationName);
		container.setMessageListener(queueDistributor);
		return container;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
//		template.setDefaultDestinationName(destinationName);
		return new JmsTemplate(cachingConnectionFactory());
	}

	@Bean
	MessageConverter converter(){
		return new AdaptrMessageConverter();
	}

}
