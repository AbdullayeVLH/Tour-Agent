package az.code.touragent.services;

import az.code.touragent.configs.RabbitMQConfig;
import az.code.touragent.models.Offer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService{
    private final RabbitTemplate template;

    public RabbitMQServiceImpl(RabbitTemplate template) {
        this.template = template;
    }


    @Override
    public void sendToOfferQueue(Offer offer) {
        template.convertAndSend(RabbitMQConfig.OFFER_QUEUE, offer);
    }


}
