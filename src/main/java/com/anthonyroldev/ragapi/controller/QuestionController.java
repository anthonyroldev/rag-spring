package com.anthonyroldev.ragapi.controller;

import com.anthonyroldev.ragapi.model.ChunkSearchResult;
import com.anthonyroldev.ragapi.model.request.AskRequest;
import com.anthonyroldev.ragapi.service.QueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ask")
public class QuestionController {
    private final QueryService queryService;

    @PostMapping
    public ResponseEntity<List<ChunkSearchResult>> handleQuestion(@Valid @RequestBody AskRequest askRequest){
        return ResponseEntity.ok(queryService.retrieveRelevantChunks(askRequest));
    }
}
