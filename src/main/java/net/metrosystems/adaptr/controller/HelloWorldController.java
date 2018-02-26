package net.metrosystems.adaptr.controller;

import net.metrosystems.adaptr.data.AdaptrMessage;
import net.metrosystems.adaptr.data.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/hello")
public class HelloWorldController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private final String[] HEADERS = {"Service", "Operation", "TargetApplication", "SourceApplication", "Country", "Salesline", "Store", "ServiceVersion"};

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${queue-upload}")
	private String destinationName;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Greeting sayHello(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
		jmsTemplate.convertAndSend(destinationName, generate(String.format(template, name)));
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	/*@JmsListener(destination = "${queue-upload}")
	public void receive(String message) {
		counter.incrementAndGet();
		System.out.println(message + counter);
	}*/

	private AdaptrMessage generate(String payload) {
		AdaptrMessage adaptrMessage = new AdaptrMessage();
		adaptrMessage.setPayload(payload.getBytes());
		HashMap<String, String> headers = new HashMap<>();
		for (String header : HEADERS) {
			headers.put(header, header + new Random().nextInt(HEADERS.length));
		}
		adaptrMessage.setHeaders(headers);
		return adaptrMessage;
	}

}
