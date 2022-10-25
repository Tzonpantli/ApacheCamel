package com.nestor.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.crypto.CryptoDataFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws  Exception{

        //timer
        from("timer:active-mq-timer?period=10000")
                .transform().constant("My meessage for active MQ")
                .log("${body}")
                .marshal(createEncryptor())
                .to("activemq:my-activemq-queue");
        //queue
//        from("file:files/json")
//                //.transform().constant("My meessage for active MQ")
 //               .log("${body}")
 //               .to("activemq:my-activemq-queue");
//        from("file:files/xml")
//                .log("${body}")
//                .to("activemq:my-activemq-xml-queue");
    }
    private CryptoDataFormat createEncryptor() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        ClassLoader classLoader = getClass().getClassLoader();
        keyStore.load(classLoader.getResourceAsStream("myDesKey.jceks"), "someKeystorePassword".toCharArray());
        Key sharedKey = keyStore.getKey("myDesKey", "someKeyPassword".toCharArray());

        CryptoDataFormat sharedKeyCrypto = new CryptoDataFormat("DES", sharedKey);
        return sharedKeyCrypto;
    }

}
