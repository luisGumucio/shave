package com.manaco.org.entries;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Value("${other.rabbitmq.queue}")
    private String queueProducto1;

    @Value("${other.rabbitmq.exchange}")
    private String exchangeProducto1;

    @Value("${other.rabbitmq.routingkey}")
    private String routingKeyProducto1;

    @Value("${other1.rabbitmq.queue}")
    private String queueProducto2;

    @Value("${other1.rabbitmq.exchange}")
    private String exchangeProducto2;

    @Value("${other1.rabbitmq.routingkey}")
    private String routingKeyProducto2;


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
    Queue queueProducto1() {
        return new Queue(queueProducto1, false);
    }

    @Bean
    Queue queueProducto2() {
        return new Queue(queueProducto2, false);
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
    TopicExchange exchangeProducto1() {
        return new TopicExchange(exchangeProducto1);
    }

    @Bean
    TopicExchange exchangeProducto2() {
        return new TopicExchange(exchangeProducto2);
    }

    @Bean
    Binding bindingProducto() {
        return BindingBuilder.bind(queueProducto()).to(exchangeProducto()).with(routingKeyProducto);
    }

    @Bean
    Binding bindingProducto1() {
        return BindingBuilder.bind(queueProducto1()).to(exchangeProducto1()).with(routingKeyProducto1);
    }

    @Bean
    Binding bindingProducto2() {
        return BindingBuilder.bind(queueProducto1()).to(exchangeProducto1()).with(routingKeyProducto2);
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

