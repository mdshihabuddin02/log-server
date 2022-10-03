/**
 * 
 */
package com.shurjomukhi.logserver.configs;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MergePolicyConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.spi.merge.PutIfAbsentMergePolicy;

/**
 * Configuration for HazleCast
 * 
 * @author Mahmudul Hasan
 * @version 2022-10-03
 */
@Configuration
public class HazelcastConfig {
	
	private static final String EVENT_STORE_NAME = "log-server-event-store";
	private static final String APPLICATION_STORE_NAME = "log-server-application-store";
	
	@Bean
	public Config hazelcast() {
		MapConfig eventStoreMap = new MapConfig(EVENT_STORE_NAME)
				.setInMemoryFormat(InMemoryFormat.OBJECT).setBackupCount(1)
				.setEvictionConfig(new EvictionConfig().setEvictionPolicy(EvictionPolicy.NONE))
				.setMergePolicyConfig(new MergePolicyConfig(PutIfAbsentMergePolicy.class.getName(), 100));

		MapConfig sentNotificationsMap = new MapConfig(APPLICATION_STORE_NAME)
				.setInMemoryFormat(InMemoryFormat.OBJECT).setBackupCount(1)
				.setEvictionConfig(new EvictionConfig().setEvictionPolicy(EvictionPolicy.LRU))
				.setMergePolicyConfig(new MergePolicyConfig(PutIfAbsentMergePolicy.class.getName(), 100));

		Config config = new Config();
		config.addMapConfig(eventStoreMap);
		config.addMapConfig(sentNotificationsMap);
		config.setProperty("hazelcast.jmx", "true");

		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		TcpIpConfig tcpIpConfig = config.getNetworkConfig().getJoin().getTcpIpConfig();
		tcpIpConfig.setEnabled(true);
		tcpIpConfig.setMembers(Collections.singletonList("127.0.0.1"));
		
		return config;
	}

}