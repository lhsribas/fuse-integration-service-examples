package br.com.ribas.fis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQConfigurableService {


    @Value("${activemq.url}")
    public String amqURL;


    public void sysout(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ amqURL);
    }

}
