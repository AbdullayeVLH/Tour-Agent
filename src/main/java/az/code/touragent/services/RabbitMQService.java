package az.code.touragent.services;

import az.code.touragent.models.Offer;

public interface RabbitMQService {
    void sendToQueue(Offer offer);
}
