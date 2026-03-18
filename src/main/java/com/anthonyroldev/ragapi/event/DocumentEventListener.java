package com.anthonyroldev.ragapi.event;

import com.anthonyroldev.ragapi.service.ChunkingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class DocumentEventListener {
    private final ChunkingService chunkingService;

    @EventListener
    public void onApplicationEvent(DocumentUploadedEvent event) {
        log.info("Received DocumentUploadedEvent with id: {} at {}", event.getDocumentId(), event.getTimestamp());
        chunkingService.chunkDocument(UUID.fromString(event.getDocumentId()));
    }
}
