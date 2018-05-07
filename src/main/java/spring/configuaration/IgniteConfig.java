package spring.configuaration;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.Ignition;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by debasish paul on 07-05-2018.
 */
@Configuration
public class IgniteConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteConfig.class);

    @Bean(name = "ignite")
    public Ignite getIgniteCache() {
        org.apache.ignite.configuration.IgniteConfiguration igniteConfiguration = new org.apache.ignite.configuration.IgniteConfiguration();

//        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
//        DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
//        dataRegionConfiguration.setName("Default_Region");
//        dataRegionConfiguration.setMaxSize(4l * 1024 * 1024);
//        storageCfg.setDataRegionConfigurations(dataRegionConfiguration);
//        igniteConfiguration.setDataStorageConfiguration(storageCfg);

//        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
//        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
//        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47502"));
//        tcpDiscoverySpi.setIpFinder(ipFinder);
//        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);


        IgniteLogger log = new Slf4jLogger();
        igniteConfiguration.setGridLogger(log);

        igniteConfiguration.setWorkDirectory("D:/test_project/ignite-workDir");


        Ignite ignite = Ignition.start(igniteConfiguration);
        LOGGER.info(">>>>>>>>>>>>>>>>Ignite Cache Started successfully");

        return ignite;
    }
}
