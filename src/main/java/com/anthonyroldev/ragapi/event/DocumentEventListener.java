package com.anthonyroldev.ragapi.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentEventListener {

    @EventListener
    public void onApplicationEvent(DocumentUploadedEvent event) {
        log.info("Received DocumentUploadedEvent with id: {} at {}", event.getDocumentId(), event.getTimestamp());
    }
}
