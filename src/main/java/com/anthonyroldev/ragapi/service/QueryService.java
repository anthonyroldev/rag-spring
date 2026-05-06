package com.anthonyroldev.ragapi.service;

import com.anthonyroldev.ragapi.mapper.ChunkMapper;
import com.anthonyroldev.ragapi.model.ChunkSearchResult;
import com.anthonyroldev.ragapi.model.request.AskRequest;
import com.anthonyroldev.ragapi.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class QueryService {
    private final DocumentChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;

    @Value("${app.ai.top-k}")
    private int topK;

    public List<ChunkSearchResult> retrieveRelevantChunks(AskRequest askRequest) {
        var embeddedQuestion = embeddingService.embedChunks(List.of(askRequest.question()))
                .getFirst();
        // Converts embeddedQuestion to a List<Float>
        var mappedQuestion = IntStream.range(0, embeddedQuestion.length)
                .mapToObj(i -> embeddedQuestion[i])
                .toList();

        return chunkRepository.findTopKBySimilarity(mappedQuestion, topK)
                .stream()
                .map(ChunkMapper::toChunkSearchResult)
                .toList();
    }
}
