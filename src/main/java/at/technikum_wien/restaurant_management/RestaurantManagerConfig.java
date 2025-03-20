package at.technikum_wien.restaurant_management;

import at.technikum_wien.restaurant_management.model.notifications.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RestaurantManagerConfig {

    @Bean
    public Notifier notifier(){
        return new Notifier();
    }

    @Bean
    public OutOfStockNotificationFactory outOfStockNotificationFactory() {
        return new OutOfStockNotificationFactory();
    }

    @Bean
    public AddedStockNotificationFactory addedStockNotificationFactory() {
        return new AddedStockNotificationFactory();
    }

}
