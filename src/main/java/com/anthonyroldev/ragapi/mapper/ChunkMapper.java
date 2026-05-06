package com.anthonyroldev.ragapi.mapper;

import com.anthonyroldev.ragapi.model.ChunkSearchResult;
import com.anthonyroldev.ragapi.repository.projection.ChunkMatch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChunkMapper {
    public ChunkSearchResult toChunkSearchResult(ChunkMatch chunkMatch) {
        return new ChunkSearchResult(chunkMatch.getContent(),
                toCosineSimilarity(chunkMatch.getSimilarityScore()));
    }

    private Float toCosineSimilarity(Float similarityScore) {
        return 1 - similarityScore;
    }
}
