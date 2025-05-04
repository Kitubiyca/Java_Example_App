package ru.shop_example.gateway.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.shop_example.gateway.service.ServiceHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class RefreshRoutesListener implements ApplicationListener<RefreshRoutesEvent> {

    private final ReactiveDiscoveryClient reactiveDiscoveryClient;
    private final ServiceHandler serviceHandler;

    @Override
    public void onApplicationEvent(RefreshRoutesEvent event) {
        log.info("Refreshing routes");
        serviceHandler.handleServices(reactiveDiscoveryClient.getServices());
    }
}
