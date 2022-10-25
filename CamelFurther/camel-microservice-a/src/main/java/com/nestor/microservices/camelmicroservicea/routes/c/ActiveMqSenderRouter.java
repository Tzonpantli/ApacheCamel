package com.nestor.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws  Exception{
        //timer
 //       from("timer:active-mq-timer?period=10000")
   //             .transform().constant("My meessage for active MQ")
     //           .log("${body}")
       //         .to("activemq:my.queue");
        //queue
//        from("file:files/json")
//                //.transform().constant("My meessage for active MQ")
 //               .log("${body}")
 //               .to("activemq:my-activemq-queue");
        from("file:files/xml")
                .log("${body}")
                .to("activemq:my-activemq-xml-queue");
    }

}
