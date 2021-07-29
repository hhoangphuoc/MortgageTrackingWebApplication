SET search_path = track_and_trace_db;

INSERT INTO service_provider (s_id)
SELECT u.u_id FROM track_and_trace_db.user u
WHERE u.is_staff = true;


