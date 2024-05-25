CREATE TABLE therapeutic_data_descriptors
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owner_ref   BIGINT      NOT NULL REFERENCES therapists,

    created_at  TIMESTAMPTZ NOT NULL,
    modified_at TIMESTAMPTZ,
    version     BIGINT      NOT NULL
);

CREATE TABLE therapeutic_data_blocks
(
    id                             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    therapeutic_data_descriptor_id BIGINT  NOT NULL REFERENCES therapeutic_data_descriptors,
    label                          VARCHAR NOT NULL,
    block_index INT NOT NULL,
    UNIQUE (therapeutic_data_descriptor_id, label),
    UNIQUE (therapeutic_data_descriptor_id, block_index)
);

CREATE TABLE therapeutic_data_fields
(
    id                        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    therapeutic_data_block_id BIGINT  NOT NULL REFERENCES therapeutic_data_blocks,
    label                     VARCHAR NOT NULL,
    type                      VARCHAR NOT NULL,
    required                  BOOLEAN NOT NULL,
    field_index               INT     NOT NULL
);