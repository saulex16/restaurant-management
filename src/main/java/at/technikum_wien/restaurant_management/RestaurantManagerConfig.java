package at.technikum_wien.restaurant_management;

import at.technikum_wien.restaurant_management.model.notifications.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RestaurantManagerConfig {

    @Bean
    public Notifier notifier(List<Observer<?>> observers){
        Notifier notifier = new Notifier();
        for(Observer<?> observer:observers){
            for(NotificationType type: observer.getNotificationTypes()){
                notifier.subscribe(type, observer);
            }
        }

        return notifier;
    }

    @Bean
    public LowStockNotificationFactory lowStockNotificationFactory() {
        return new LowStockNotificationFactory();
    }

    @Bean
    public AddedStockNotificationFactory addedStockNotificationFactory() {
        return new AddedStockNotificationFactory();
    }
}
