package com.walemao.megastore.service.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class JmsPushTest {

	@Autowired
	private JmsTemplate jmsTemplate;
	
//	@Autowired
//	@Qualifier("queueDestination")
//	private Destination responseDestinatin;
	
	public void pushMessage(Destination destination, final String message){
		System.out.println("---------------生产者发了一个消息：" + message);  
        jmsTemplate.send(destination, new MessageCreator() {  
            public Message createMessage(Session session) throws JMSException {  
                TextMessage textMsg = session.createTextMessage(message);  
                //textMsg.setJMSReplyTo(responseDestinatin);
                return textMsg;
            }  
        }); 
	}
}
