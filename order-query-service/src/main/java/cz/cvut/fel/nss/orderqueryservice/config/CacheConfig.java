package cz.cvut.fel.nss.orderqueryservice.config;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
              .addMapConfig(
                  new MapConfig()
                      .setName("orders")
                      .setEvictionConfig(
                          new EvictionConfig()
                              .setEvictionPolicy(EvictionPolicy.LRU)
                              .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                              .setSize(1000)
                      )
                      .setTimeToLiveSeconds(300)
              );

        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701)
              .setPortAutoIncrement(true);
        
        JoinConfig join = network.getJoin();
        join.getMulticastConfig()
            .setEnabled(true);
        join.getTcpIpConfig()
            .setEnabled(false);
        
        return config;
    }
} 