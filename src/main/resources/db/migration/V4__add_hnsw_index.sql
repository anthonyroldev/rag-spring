CREATE INDEX ON document_chunk USING hnsw (embedding vector_cosine_ops)
    WITH (m = 16, ef_construction = 64);