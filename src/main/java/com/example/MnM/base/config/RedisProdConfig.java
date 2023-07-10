package com.example.MnM.base.config;

import com.example.MnM.base.redis.RedisInfo;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DnsResolvers;
import io.lettuce.core.resource.MappingSocketAddressResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Profile({"prod"})
@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisProdConfig {

    @Value("${spring.data.redis.database}")
    private int database;

    private final RedisInfo redisInfo;


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisInfo.getNodes());
        redisClusterConfiguration.setPassword(redisInfo.getPassword());
        redisClusterConfiguration.setMaxRedirects(redisInfo.getMaxRedirects());

        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enableAllAdaptiveRefreshTriggers()
                .enablePeriodicRefresh(Duration.ofHours(1L))
                .build();
        ClientOptions clientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();

        for (String node : redisInfo.getNodes()) {
            log.info("nodes : {}", node);
        }

        MappingSocketAddressResolver resolver = MappingSocketAddressResolver.create(DnsResolvers.UNRESOLVED,
                hostAndPort -> {
                    log.info("redisInfo.getConnectIp : {}", redisInfo.getConnectIp());
                    HostAndPort andPort = HostAndPort.of(redisInfo.getConnectIp(), hostAndPort.getPort());
                    log.info("andPort -> {}:{}",andPort.getHostText(),andPort.getPort());
                    return andPort;
                }
        );

        ClientResources clientResources = ClientResources.builder()
                .socketAddressResolver(resolver)
                .build();


        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .clientOptions(clientOptions)
                .clientResources(clientResources)
                .build();


        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
        connectionFactory.setDatabase(database);

        return connectionFactory;
    }
}
