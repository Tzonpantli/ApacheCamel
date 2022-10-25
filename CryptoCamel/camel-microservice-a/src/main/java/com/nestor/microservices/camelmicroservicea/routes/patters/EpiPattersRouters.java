package com.nestor.microservices.camelmicroservicea.routes.patters;

import com.nestor.microservices.camelmicroservicea.routes.a.CurrencyExchange;
import org.apache.camel.Body;
import org.apache.camel.DynamicRouter;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

//@Component
public class EpiPattersRouters  extends RouteBuilder {
@Autowired
DynamicRouterBean dynamycRouterBean;

@Autowired
SplitterComponent splitter;

    @Override
    public void configure() throws Exception {
        getContext().setTracing(true);
        errorHandler(deadLetterChannel("activemq:dead-letter-queue"));
        //Pipeline
        //Content Based Routing - choice
        //Multicast
        //    from("timer:multicast?period=10000")
        //           .multicast()
        //          .to("log:endpoint 1", "log:endpoint 2");//multiplenedpoints
//    from("file:files/csv")
//           .unmarshal().csv()
//           .split(body())
//           .to("activemq:split-queue");

        //Message,Message2,Message3
//        from("file:files/csv")
//                .convertBodyTo(String.class)
        //               //.split(body(),",")
        //              .split(method(splitter))
        //             .to("activemq:split-queue");

        //Agregate things
        //Messages ==> Aggregate ==> Endpoint
        //to, 3
        from("file:files/aggregate-json")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
                .completionSize(3)
                //.completionTimeout(HIGHEST)
                .to("log:aggregate-json");

        String routingSlip = "direct:endpoint1,direct:endpoint2,direct:endpoint3";
//        from("timer:routingSlip?period=10000")
//                .transform().constant("My Menssage is Hardcoded")
//                .routingSlip(simple(routingSlip));
        //Dynamic Routing
        from("timer:routingSlip?period={{timePeriod}}")
                .transform().constant("My Menssage is Hardcoded")
                        .dynamicRouter(method(dynamycRouterBean));

        from("direct:endpoint1")
                .wireTap("log:wire-tap")
                .to("{{endpoint-for-logging}}");

        from("direct:endpoint2")
                .to("log:directendpoint2");

        from("direct:endpoint3")
                .to("log:directendpoint3");

    }


}
@Component
class SplitterComponent{
    public List<String > splitInput(String body) {
        return List.of("ABC","DEF","GHI");
    }
}
@Component
class DynamicRouterBean{
    Logger logger =  LoggerFactory.getLogger(DynamicRouterBean.class);
    int invocations;
    public String decidetheNextEndpoint(
            @ExchangeProperties Map <String,String> properties,
            @Headers Map <String,String> headers,
            @Body String body
            ){
        logger.info("{} {} {}",properties,headers,body);
        invocations ++;
        if (invocations%3==0)
            return "direct:endpoint1";
        if (invocations%3==1)
            return "direct:endpoint2,direct:endpoint3";
        return null;
    }
}