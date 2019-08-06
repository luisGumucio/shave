package com.manaco.org.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${repuestos.rabbitmq.queue}")
    private String queueName;

    @Value("${repuestos.rabbitmq.exchange}")
    private String exchange;

    @Value("${repuestos.rabbitmq.routingkey}")
    private String routingKey;


    @Value("${prima.rabbitmq.queue}")
    private String queuePrima;

    @Value("${prima.rabbitmq.exchange}")
    private String exchangePrima;

    @Value("${prima.rabbitmq.routingkey}")
    private String routingKeyPrima;


    @Value("${producto.rabbitmq.queue}")
    private String queueProducto;

    @Value("${producto.rabbitmq.exchange}")
    private String exchangeProducto;

    @Value("${producto.rabbitmq.routingkey}")
    private String routingKeyProducto;


    @Bean
    Queue queueRepuestos() {
        return new Queue(queueName, false);
    }

    @Bean
    Queue queuePrima() {
        return new Queue(queuePrima, false);
    }

    @Bean
    Queue queueProducto() {
        return new Queue(queueProducto, false);
    }


    @Bean
    TopicExchange exchangeRepuestos() {
        return new TopicExchange(exchange);
    }

    @Bean
    TopicExchange exchangePrima() {
        return new TopicExchange(exchangePrima);
    }

    @Bean
    TopicExchange exchangeProducto() {
        return new TopicExchange(exchangeProducto);
    }

    @Bean
    Binding bindingProducto() {
        return BindingBuilder.bind(queueProducto()).to(exchangeProducto()).with(routingKeyProducto);
    }

    @Bean
    Binding bindingRepuestos() {
        return BindingBuilder.bind(queueRepuestos()).to(exchangeRepuestos()).with(routingKey);
    }

    @Bean
    Binding bindingPrima() {
        return BindingBuilder.bind(queuePrima()).to(exchangePrima()).with(routingKeyPrima);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter()
    {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        return new Jackson2JsonMessageConverter(mapper);
    }
}

