package org.byby.init;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.CommunicationSpi;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Collections;

public class IgniteInit {

    public static Ignite start() {
        // Preparing IgniteConfiguration using Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("HelloIgnite");

        // The node will be started as a client node.
        //cfg.setClientMode(true);

        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);

        var discoverySpi = createDiscoverySpi();
        var communicationSpi = createCommunicationSpi();

        cfg.setDiscoverySpi(discoverySpi);
        cfg.setCommunicationSpi(communicationSpi);

        // Starting the node
        return Ignition.start(cfg);
    }

    private static DiscoverySpi createDiscoverySpi() {
        // Explicitly configure TCP discovery SPI to provide list of initial nodes
        // from the first cluster.
        TcpDiscoverySpi firstDiscoverySpi = new TcpDiscoverySpi();

        // Initial local port to listen to.
        firstDiscoverySpi.setLocalPort(48500);

        // Changing local port range. This is an optional action.
        firstDiscoverySpi.setLocalPortRange(20);

        var firstIpFinder = createIpFinder();
        // Overriding IP finder.
        firstDiscoverySpi.setIpFinder(firstIpFinder);

        return firstDiscoverySpi;
    }

    private static CommunicationSpi createCommunicationSpi() {
        // Explicitly configure TCP communication SPI by changing local port number for
        // the nodes from the first cluster.
        TcpCommunicationSpi firstCommSpi = new TcpCommunicationSpi();

        firstCommSpi.setLocalPort(48100);

        return firstCommSpi;
    }

    // Setting up an IP Finder to ensure the client can locate the servers.
    private static TcpDiscoveryIpFinder createIpFinder() {
        TcpDiscoveryVmIpFinder firstIpFinder = new TcpDiscoveryVmIpFinder();

        // Addresses and port range of the nodes from the first cluster.
        // 127.0.0.1 can be replaced with actual IP addresses or host names.
        // The port range is optional.
        firstIpFinder.setAddresses(Collections.singletonList("127.0.0.1:48500..48520"));

        return firstIpFinder;
    }
}
