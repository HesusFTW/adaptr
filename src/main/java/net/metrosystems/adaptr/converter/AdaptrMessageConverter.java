package net.metrosystems.adaptr.converter;

import net.metrosystems.adaptr.data.AdaptrMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.ObjectUtils;

import javax.jms.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AdaptrMessageConverter implements MessageConverter {

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		if (object instanceof Message) {
			return (Message) object;
		} else if (object instanceof AdaptrMessage) {
			return createMessageForByteArray((AdaptrMessage) object, session);
		} else {
			throw new MessageConversionException("Cannot convert object of type [" +
					ObjectUtils.nullSafeClassName(object) + "] to JMS message. Supported message " +
					"payloads are: String, byte array, Map<String,?>, Serializable object.");
		}
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		if (message instanceof TextMessage) {
			return extractAdaptrMessageFromTextMessage((TextMessage) message);
		} else if (message instanceof BytesMessage) {
			return extractAdaptrMessageFromByteMessage((BytesMessage) message);
		} else if (message instanceof ObjectMessage) {
			return extractSerializableFromMessage((ObjectMessage) message);
		} else {
			throw new MessageConversionException("Cannot convert object of type [" +
					ObjectUtils.nullSafeClassName(message) + "] to JMS message. Supported message " +
					"payloads are: String, byte array, Map<String,?>, Serializable object.");
		}
	}

	protected BytesMessage createMessageForByteArray(AdaptrMessage adaptrMessage, Session session) throws JMSException {
		BytesMessage message = session.createBytesMessage();
		message.writeBytes(adaptrMessage.getPayload());
		for (Map.Entry<String, String> entry : adaptrMessage.getHeaders().entrySet()) {
			message.setStringProperty(entry.getKey(), entry.getValue());
		}
		return message;
	}

	protected AdaptrMessage extractAdaptrMessageFromTextMessage(TextMessage message) throws JMSException {
		AdaptrMessage adaptrMessage = new AdaptrMessage();
		adaptrMessage.setPayload(message.getText().getBytes());
		HashMap<String, String> headers = new HashMap<>();
		while (message.getPropertyNames().hasMoreElements()) {
			String header = (String) message.getPropertyNames().nextElement();
			headers.put(header, message.getStringProperty(header));
		}
		adaptrMessage.setHeaders(headers);
		return adaptrMessage;
	}

	protected AdaptrMessage extractAdaptrMessageFromByteMessage(BytesMessage message) throws JMSException {
		AdaptrMessage adaptrMessage = new AdaptrMessage();
		byte[] bytes = new byte[(int) message.getBodyLength()];
		message.readBytes(bytes);
		adaptrMessage.setPayload(bytes);
		HashMap<String, String> headers = new HashMap<>();
		while (message.getPropertyNames().hasMoreElements()) {
			String header = (String) message.getPropertyNames().nextElement();
			headers.put(header, message.getStringProperty(header));
		}
		adaptrMessage.setHeaders(headers);
		return adaptrMessage;
	}

	protected Serializable extractSerializableFromMessage(ObjectMessage message) throws JMSException {
		return message.getObject();
	}

}
