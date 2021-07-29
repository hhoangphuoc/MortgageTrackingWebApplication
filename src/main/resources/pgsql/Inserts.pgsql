
SET search_path = track_and_trace_db;

INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1001, 'john@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'John', 'Otte', 'false');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1002, 'jack@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Jack', 'Otte', 'false'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1003, 'moon@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Moon', 'Otte', 'false'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1004, 'jane@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Jane', 'Otte', 'false');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1005, 'jamie@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Jamie', 'Otte', 'false'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1006, 'cruz@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Cruz', 'Otte', 'false');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1007, 'ray@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Raymond', 'Otte', 'false'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)       
    VALUES (1008, 'liz@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Liz', 'Otte', 'false'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1009, 'tt@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Taylor', 'Otte', 'true');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1010, 'tj@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Tim', 'Otte', 'true');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1011, 'dj@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Dwayne', 'Otte', 'true'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1012, 'bram@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Bram', 'Otte', 'true');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1013, 'qc@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'quecee', 'Otte', 'true');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1014, 'Mike@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Mike', 'Otte', 'true');
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1015, 'bronny@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Bronny', 'Otte', 'true'); 
INSERT INTO "user" (u_id,email, password_hash, password_salt, name, surname, is_staff)
    VALUES (1016, 'Bianka@gmail.biz', decode('password', 'escape'), decode('password', 'escape'), 'Bianka', 'Otte', 'true'); 


INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1001, 150000.0, 80256.2, 'NL12ABC1234567898');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1002, 150000.0, 80246.2, 'NL12ABC1234567898');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1003, 175000.0, 71256.2, 'NL12ABC1234567897');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1004, 200000.0, 25256.2, 'NL12ABC1234567896');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1005, 225000.0, 88256.2, 'NL12ABC1234567895');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1006, 250000.0, 98256.2, 'NL12ABC1234567894');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1007, 275000.0, 100256.2, 'NL12ABC1234567893');
INSERT INTO  customer (c_id, requested_amount, aprx_gross_income, bank_account)
VALUES (1008, 305000.0, 120256.2, 'NL12ABC1234507893');


INSERT INTO service_provider (s_id)
SELECT u.u_id FROM track_and_trace_db.user u
WHERE u.is_staff = true;



INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99988, 1001, 1009, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99987, 1002, 1010, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99986, 1003, 1011, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99985, 1004, 1012, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99984, 1005, 1013, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99983, 1006, 1014, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99982, 1007, 1015, 1);
INSERT INTO mortgage_request(m_id, c_id, s_id, index)
VALUES(99981, 1008, 1016, 1);


-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123456, 99988, 'true', 'This offer is presented by the SP and accepted by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123457, 99987, 'true', 'This offer is presented by the SP and accepted by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123458, 99986, 'true', 'This offer is presented by the SP and accepted by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123459, 99985, 'true', 'This offer is presented by the SP and accepted by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123460, 99984, 'false', 'This offer is presented by the SP and rejected by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123461, 99983, 'true', 'This offer is presented by the SP and accepted by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123462, 99982, 'false', 'This offer is presented by the SP and rejected by the customer');
-- INSERT INTO offer(offer_id, m_id, is_accepted, offer_description)
-- VALUES(123463, 99981, 'true', 'This offer is presented by the SP and accepted by the customer');





-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123456, 'Condition 1.0, Condition 1.1, Condition 1.2, Condition 1.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123457, 'Condition 2.0, Condition 2.1, Condition 2.2, Condition 2.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123458, 'Condition 3.0, Condition 3.1, Condition 3.2, Condition 3.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123459, 'Condition 4.0, Condition 4.1, Condition 4.2, Condition 4.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123460, 'Condition 5.0, Condition 5.1, Condition 5.2, Condition 5.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123461, 'Condition 6.0, Condition 6.1, Condition 6.2, Condition 6.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123462, 'Condition 7.0, Condition 7.1, Condition 7.2, Condition 7.3');
-- INSERT INTO binding_offer (binding_id, conditions_description)
-- VALUES(123463, 'Condition 8.0, Condition 8.1, Condition 8.2, Condition 8.3');


-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123456, '10 years', 0.4755);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123457, '10 years', 0.4732);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123458, '10 years', 0.4882);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123459, '10 years', 0.4665);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123460, '10 years', 0.4336);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123461, '10 years', 0.5663);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123462, '10 years', 0.2336);
-- INSERT INTO interest_offer (interest_id, duration, interest_rate)
-- VALUES(123463, '10 years', 0.4224);


-- TODO: make inserts for application
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1001, 99988, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1002, 99987, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1003, 99986, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1004, 99985, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1005, 99984, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1006, 99983, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1007, 99982, 'Mortgage');
-- INSERT INTO application(c_id, m_id, reason)
-- VALUES(1008, 99981, 'Mortgage');



INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(105, 'pdf', 'ACCEPTED', 'All good', 99988);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(106, 'html', 'REJECTED', 'Please provide .pdf', 99987);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(107, 'pdf', 'ACCEPTED', 'All good', 99986);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(108, 'pdf', 'ACCEPTED', 'All good', 99985);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(109, 'docx', 'REJECTED', 'Please provide .pdf', 99984);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(110, 'pdf', 'ACCEPTED', 'All good', 99983);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(111, 'csv', 'REJECTED', 'Please provide .pdf', 99982);
INSERT INTO document_request(doc_id, document_type, document_status, failed_explaination, mortgage_id)
VALUES(112, 'pdf', 'REJECTED', 'The document is fradulant/not original', 99981);


INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(200,'users/documents/1001', 'pdf', 105);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(201,'users/documents/1002', 'html', 106);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(202,'users/documents/1003', 'pdf', 107);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(203,'users/documents/1004', 'pdf', 108);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(204,'users/documents/1005', 'docx', 109);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(205,'users/documents/1006', 'pdf', 110);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(206,'users/documents/1007', 'csv', 111);
INSERT INTO file(file_id, file_data, file_type, doc_id)
VALUES(207,'users/documents/1008', 'pdf', 112);


INSERT INTO process_state(m_id, state, status)
VALUES(99988, 'Document checking', 'Processing');
INSERT INTO process_state(m_id, state, status)
VALUES(99987, 'Income checking', 'Processing');
INSERT INTO process_state(m_id, state, status)
VALUES(99986, 'Obligation checking', 'Rejected');
INSERT INTO process_state(m_id, state, status)
VALUES(99985, 'Interest offer checking', 'Rejected');
INSERT INTO process_state(m_id, state, status)
VALUES(99984, 'Document checking', 'Processing');
INSERT INTO process_state(m_id, state, status)
VALUES(99983, 'Application checking', 'Processing');
INSERT INTO process_state(m_id, state, status)
VALUES(99982, 'Document checking', 'Processing');
INSERT INTO process_state(m_id, state, status)
VALUES(99981, 'Document checking', 'Processing');


INSERT INTO chat_conversation(conversation_id, u_id)
VALUES(566, 1001);
INSERT INTO chat_conversation(conversation_id, u_id)
VALUES(565, 1004);
INSERT INTO chat_conversation(conversation_id, u_id)
VALUES(564, 1006);
INSERT INTO chat_conversation(conversation_id, u_id)
VALUES(563, 1007);
INSERT INTO chat_conversation(conversation_id, u_id)
VALUES(562, 1008);





