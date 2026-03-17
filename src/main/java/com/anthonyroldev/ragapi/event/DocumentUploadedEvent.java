package com.anthonyroldev.ragapi.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentUploadedEvent extends ApplicationEvent {
    private String documentId;
    private LocalDateTime time;

    public DocumentUploadedEvent(Object source, String documentId, LocalDateTime time) {
        super(source);
        this.documentId = documentId;
        this.time = time;
    }
}

