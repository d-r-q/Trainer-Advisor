CREATE TABLE completing
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    completing_date  date,
    numeric_result   SERIAL,
    text_result      VARCHAR,
    questionnaire_id BIGINT NOT NULL REFERENCES questionnaires (id) ON DELETE CASCADE,
    client_id        BIGINT NOT NULL REFERENCES clients (id) ON DELETE CASCADE,
    therapist_id     BIGINT NOT NULL REFERENCES therapists (id) ON DELETE CASCADE
)