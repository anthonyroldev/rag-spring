CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE documents
(
    id         uuid PRIMARY KEY      DEFAULT gen_random_uuid(),
    filename   VARCHAR(255) NOT NULL,
    s3_key     VARCHAR(255) NOT NULL,
    status     VARCHAR(20)  NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'processing', 'completed', 'failed')),
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL
)