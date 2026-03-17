CREATE TABLE document_chunk
(
    id          uuid PRIMARY KEY         DEFAULT gen_random_uuid(),
    document_id uuid        NOT NULL,
    chunk_index integer     NOT NULL CHECK (chunk_index >= 0),
    content     text        NOT NULL CHECK ( length(content) > 0 ),
    embedding   vector(768) NULL         DEFAULT NULL,
    created_at  timestamp with time zone DEFAULT now(),
    updated_at  timestamp with time zone DEFAULT now(),
    FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE
);