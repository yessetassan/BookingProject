package com.yesset.booking.config.cron;

import com.yesset.booking.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@AllArgsConstructor
@Slf4j
public class ItemConfig {

    private ItemService itemService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "https://booking-project-6t5v.onrender.com/open-api/categories";

    /*
    Чтобы товар был видным при поиске проверить на доступность
     */
    @Scheduled(cron = "0 1 0 * * ?")
    void checkAvailability() {
        log.info("Item available check...");
        itemService.checkAvailability();
    }


    @Scheduled(fixedRate = 600_000) // каждые 10 минут (в миллисекундах)
    public void keepRenderAwake() {
        try {
            String response = restTemplate.getForObject(URL, String.class);
            System.out.println("Ping successful: " + response);
        } catch (Exception e) {
            System.err.println("Ping failed: " + e.getMessage());
        }
    }

}
