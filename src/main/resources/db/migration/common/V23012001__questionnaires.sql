CREATE TABLE questionnaires
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR UNIQUE
)