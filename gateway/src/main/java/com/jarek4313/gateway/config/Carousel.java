package com.jarek4313.gateway.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class Carousel {
    private final EurekaClient eurekaClient;
    List<InstanceInfo> instanceInfoList = new ArrayList<>();
    int currentIndex = 0;

    public Carousel(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        try {
            initAuthCarousel();
        } catch (NullPointerException e) {
            log.error("Cant find active instances of Auth Service");
        }
    }

    public String getUriAuth() {
        StringBuilder stringBuilder = new StringBuilder();
        InstanceInfo instanceInfo = instanceInfoList.get(currentIndex);
        stringBuilder.append(instanceInfo.getIPAddr()).append(":").append(instanceInfo.getPort());

        if (instanceInfoList.size() - 1 == currentIndex) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        return stringBuilder.toString();
    }

    private void events() {
        eurekaClient.registerEventListener(eurekaEvent -> {
            log.info("--START initAuthCarousel-registerEvent");
            initAuthCarousel();
            log.info("--STOP initAuthCarousel-registerEvent");
        });
        eurekaClient.unregisterEventListener(eurekaEvent -> {
            try {
                log.info("--START initAuthCarousel-unregisterEvent");
                initAuthCarousel();
            } catch (NullPointerException e) {
                log.error("Cant find active instances of Auth Service");
            }
            log.info("--STOP initAuthCarousel-unregisterEvent");
        });
    }

    private void initAuthCarousel() throws NullPointerException {
        log.info("--START initAuthCarousel");
        instanceInfoList = eurekaClient.getApplication("AUTH-SERVICE").getInstances();
        log.info("--STOP initAuthCarousel");
    }
}
