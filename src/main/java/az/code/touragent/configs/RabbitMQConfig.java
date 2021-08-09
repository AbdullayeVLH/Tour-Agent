package az.code.touragent.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("!dev")
public class RabbitMQConfig {

    public static final String QUEUE = "session_queue";
    public static final String STOP_QUEUE = "stop_queue";
    public static final String OFFER_QUEUE = "offer_queue";
    public static final String ACCEPTED_QUEUE = "accepted_queue";
    public static final String EXCHANGE = "session_exchange";
    public static final String ROUTING_KEY = "session_routing_key";


    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public Queue stopQueue(){ return new Queue(STOP_QUEUE);}

    @Bean
    public Queue offerQueue(){ return new Queue(OFFER_QUEUE);}

    @Bean
    public Queue acceptedQueue(){ return new Queue(ACCEPTED_QUEUE); }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws URISyntaxException {
        final URI rabbitMqUrl = new URI(System.getenv("CLOUDAMQP_URL"));
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMqUrl);
        return factory;
    }

    @Bean
    public RabbitTemplate template() throws URISyntaxException {
        RabbitTemplate temp = new RabbitTemplate(connectionFactory());
        temp.setMessageConverter(converter());
        return temp;
    }
}
