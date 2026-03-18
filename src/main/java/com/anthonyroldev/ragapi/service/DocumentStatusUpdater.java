package com.anthonyroldev.ragapi.service;

import com.anthonyroldev.ragapi.entities.DocumentEntity;
import com.anthonyroldev.ragapi.entities.enums.StatusEnum;
import com.anthonyroldev.ragapi.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DocumentStatusUpdater {
    private final DocumentRepository documentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setStatus(DocumentEntity document, StatusEnum status) {
        document.setStatus(status);
        documentRepository.save(document);
    }
}