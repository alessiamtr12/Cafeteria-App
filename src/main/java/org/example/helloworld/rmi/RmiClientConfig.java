package org.example.helloworld.rmi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class RmiClientConfig {
    @Bean(name = "cafeteriaRemoteClient")
    public RmiProxyFactoryBean cafeteriaRemoteClient() {
        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        proxy.setServiceUrl("rmi://localhost:1099/CafeteriaService");
        proxy.setServiceInterface(CafeteriaRemote.class);
        proxy.setLookupStubOnStartup(false);
        proxy.setRefreshStubOnConnectFailure(true);
        return proxy;
    }
}
