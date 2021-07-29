

CREATE TABLE "user" (
  u_id serial4 PRIMARY KEY,
  email text UNIQUE,
  password_hash bytea,
  password_salt bytea,
  name text,
  surname text,
  is_staff BOOLEAN
);

CREATE TABLE login_tokens (
  token text PRIMARY KEY,
  u_id int4,
  expire timestamp,
  FOREIGN KEY (u_id) REFERENCES "user" (u_id)
);

CREATE TABLE customer(
  c_id integer PRIMARY KEY,
  requested_amount float(53),
  aprx_gross_income float(53),
  bank_account text,
  FOREIGN KEY (c_id) REFERENCES "user"(u_id)
);

CREATE TABLE service_provider(
  s_id integer PRIMARY KEY,
  FOREIGN KEY (s_id) REFERENCES "user"(u_id)
);

CREATE TABLE mortgage_request(
  m_id serial4 PRIMARY KEY,
  c_id integer NOT NULL,
  s_id integer NOT NULL,
  index integer,
  FOREIGN KEY (c_id) REFERENCES customer,
  FOREIGN KEY (s_id) REFERENCES service_provider
);

CREATE TABLE mortgage_check(
  m_id integer PRIMARY KEY,
  failedExplannation text,
  isIncomeAccepted boolean,
  isCollateralAccepted boolean,
  isFiniancialObligationsAccepted boolean,
  FOREIGN KEY (m_id) REFERENCES mortgage_request(m_id) ON DELETE CASCADE
);

CREATE TABLE process_state(
  m_id integer PRIMARY KEY,
  state text,
  status text,
  FOREIGN KEY (m_id) REFERENCES mortgage_request(m_id) ON DELETE CASCADE
);

CREATE TABLE state_timestamp(
  m_id integer,
  state text,
  started_time timestamp,
  end_time timestamp,
  PRIMARY KEY (m_id, state)
);

CREATE TABLE application(
  c_id integer NOT NULL,
  m_id integer NOT NULL,
  grossIncome float,
  willRetired boolean,
  havePartner boolean,
  partnerGrossIncome float,
  willPartnerRetire boolean,
  haveStudentLoan boolean, 
  studentDebt float,
  haveSpousalMaintenance boolean,
  monthlyAlimony float,
  haveOtherDebt boolean,
  otherDebtInfo text,
  otherDebtAmount float,
  haveCredits boolean,
  creditInfo text,
  creditAmount  float,
  bankAmount  float,
  collateral1 text,
  price1  float,
  collateral2 text,
  price2  float,
  collateral3 text,
  price3  float,
  requestedMortgageAmount float,
  date date,
  duration text,
  reason text,
  PRIMARY KEY (m_id, c_id),
  FOREIGN KEY (m_id) REFERENCES mortgage_request ON DELETE CASCADE,
  FOREIGN KEY (c_id) REFERENCES customer ON DELETE CASCADE
);

CREATE TABLE interest_offer(
  m_id integer PRIMARY KEY,
  duration text,
  interest_rate float(53),
  FOREIGN KEY (m_id) REFERENCES mortgage_request(m_id) ON DELETE CASCADE
);


CREATE TABLE binding_offer(
  m_id integer PRIMARY KEY,
  conditions_description text,
  FOREIGN KEY (m_id) REFERENCES mortgage_request(m_id) ON DELETE CASCADE
);
  
CREATE TABLE document_request(
  doc_id serial PRIMARY KEY,
  mortgage_id integer NOT NULL,
  document_type text,
  document_status text,
  description text,
  failed_explanation text,
  FOREIGN KEY (mortgage_id) REFERENCES mortgage_request(m_id) ON DELETE CASCADE
);

CREATE TABLE file (
  file_id serial PRIMARY KEY,
  doc_id integer,
  file_name text,
  file_type text,
  file_data bytea,
  FOREIGN KEY (doc_id) REFERENCES document_request(doc_id) ON DELETE CASCADE
);

CREATE TABLE chat_message(
  chat_id serial PRIMARY KEY,
  body text,
  time timestamp,
  sender integer,
  receiver integer,
  FOREIGN KEY (sender) REFERENCES "user",
  FOREIGN KEY (receiver) REFERENCES "user"
);
  
CREATE TABLE mortgage_process_definition(
  mpd_id integer PRIMARY KEY,
  updated_at timestamp,
  CHECK(mpd_id = 1)
);
  
CREATE TABLE process_step_definition(
  pd_id integer PRIMARY KEY,
  mpd_id integer,
  completed_time interval,
  client_description text,
  service_provider_description text,
  index integer,
  FOREIGN KEY (mpd_id) REFERENCES mortgage_process_definition
);
  
CREATE TABLE required_document(
  rd_id integer PRIMARY KEY,
  pd_id integer,
  document_title text,
  client_description text,
  service_provider_description text,
  file_type text,
  size_limit integer,
  FOREIGN KEY (pd_id) REFERENCES process_step_definition
);

CREATE TABLE completed_time (
  state text PRIMARY KEY,
  complete_time interval
);
