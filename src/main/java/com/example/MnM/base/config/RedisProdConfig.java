package com.example.MnM.base.config;

import com.example.MnM.base.redis.RedisInfo;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Profile({"prod"})
@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisProdConfig {

    @Value("${spring.data.redis.cluster.nodes}")
    private List<String> nodes;

    @Value("${spring.data.redis.database}")
    private int database;

    @Value("${spring.data.redis.cluster.password}")
    private String password;

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

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(nodes);
        redisClusterConfiguration.setPassword(password);

        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enableAllAdaptiveRefreshTriggers()
                .enablePeriodicRefresh(Duration.ofHours(1L))
                .build();
        ClientOptions clientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();

        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .clientOptions(clientOptions)
                .build();

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
        connectionFactory.setDatabase(database);

        return connectionFactory;
    }
}
