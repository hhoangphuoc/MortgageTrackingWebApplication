SET search_path = track_and_trace_db;

DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS service_provider CASCADE;
DROP TABLE IF EXISTS mortgage_request CASCADE;
DROP TABLE IF EXISTS process_state CASCADE;
DROP TABLE IF EXISTS applicaiton CASCADE;
DROP TABLE IF EXISTS application CASCADE;
DROP TABLE IF EXISTS offer CASCADE;
DROP TABLE IF EXISTS interest_offer CASCADE;
DROP TABLE IF EXISTS binding_offer CASCADE;
DROP TABLE IF EXISTS document_request CASCADE;
DROP TABLE IF EXISTS file CASCADE;
DROP TABLE IF EXISTS chat_conversation CASCADE;
DROP TABLE IF EXISTS chat_message CASCADE;
DROP TABLE IF EXISTS mortgage_process_definition CASCADE;
DROP TABLE IF EXISTS process_step_definition CASCADE;
DROP TABLE IF EXISTS required_document CASCADE;

