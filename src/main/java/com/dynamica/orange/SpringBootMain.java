package com.dynamica.orange;
import com.dynamica.orange.Receiver.OrangeMessageListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBootMain extends SpringBootServletInitializer {
    public final static String SFG_MESSAGE_QUEUE = "sfg-message-queue";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootMain.class, args);
    }
    // Override the configure method from the SpringBootServletInitializer class
   /* @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootMain.class);
    }*/
/*
    @Bean
    Queue queue() {
        return new Queue(SFG_MESSAGE_QUEUE, false);
    }
    @Bean
    TopicExchange exchange(){
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(SFG_MESSAGE_QUEUE);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(OrangeMessageListener receiver){
        return new MessageListenerAdapter(receiver,"receiveMessage");
    }
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter adapter){
        SimpleMessageListenerContainer container=new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(SFG_MESSAGE_QUEUE);
        container.setMessageListener(adapter);
        return container;
    }*/

}
