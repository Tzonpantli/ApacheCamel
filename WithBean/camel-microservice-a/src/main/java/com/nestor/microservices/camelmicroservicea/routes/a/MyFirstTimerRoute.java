package com.nestor.microservices.camelmicroservicea.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyFirstTimerRoute extends RouteBuilder {
	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;

	@Autowired
	private SimpleLoggingProcessingComponent loggerComponent;
	@Override
	public void configure() throws Exception {
		//queue this is one endpoint actually is timer
		//transformation 
		//Exchange body null
		from("timer:first-timer")//queue recived message
				.log("${body}")
				.transform().constant("My constant message")
				.log("${body}")
		//.transform().constant("My Constant Message")
		//.transform().constant("La hora es: " + LocalDateTime.now())
		.bean(getCurrentTimeBean)
				.log("${body}")
				.bean(loggerComponent)
				.log("${body}")
		.to("log:first-timer"); //database
		
	}
	

}
@Component
class  GetCurrentTimeBean{
	public String getCurrentTime() {
		return "El tiempo ahora es: " + LocalDateTime.now();
	}
}

@Component
class  SimpleLoggingProcessingComponent{
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
	public void process(String message) {
		logger.info("SimpleLoggingProcessingComponent {}", message);
	}
}