package com.lambdaschool.dogsinitial;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DogsinitialApplication
{
    public static final String EXCHANGE_NAME = "LambdaServer";
    public static final String QUEUE_NAME_DOGS_BREED = "DogsBreedQueue";
    public static final String QUEUE_NAME_DOGS = "DogsQueue";

    public static void main(String[] args)
    {
        SpringApplication.run(DogsinitialApplication.class, args);
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueueHigh() {
        return new Queue(QUEUE_NAME_DOGS);
    }

    @Bean
    public Binding declareBindingHigh() {
        return BindingBuilder.bind(appQueueHigh()).to(appExchange()).with(QUEUE_NAME_DOGS);
    }

    @Bean
    public Queue appQueueLow() {
        return new Queue(QUEUE_NAME_DOGS_BREED);
    }

    @Bean
    public Binding declareBindingLow() {
        return BindingBuilder.bind(appQueueLow()).to(appExchange()).with(QUEUE_NAME_DOGS_BREED);
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

}



