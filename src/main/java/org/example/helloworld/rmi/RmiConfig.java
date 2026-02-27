package org.example.helloworld.rmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
public class RmiConfig {

    @Bean
    public RmiServiceExporter cafeteriaRmiExporter(CafeteriaRemoteImpl cafeteriaRemote) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("CafeteriaService");
        exporter.setServiceInterface(CafeteriaRemote.class);
        exporter.setService(cafeteriaRemote);
        exporter.setRegistryPort(1099); // RMI registry port
        return exporter;
    }
}
