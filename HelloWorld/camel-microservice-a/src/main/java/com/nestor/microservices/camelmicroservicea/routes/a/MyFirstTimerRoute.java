package com.nestor.microservices.camelmicroservicea.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyFirstTimerRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//queue this is one endpoint actually is timer
		//transformation 
		//Exchange body null
		from("timer:first-timer")//queue 
		//.transform().constant("My Constant Message")
		.transform().constant("La hora es: " + LocalDateTime.now())
		.to("log:first-timer"); //database
		
	}
	

}
