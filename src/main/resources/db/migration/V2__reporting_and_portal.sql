CREATE TABLE report_definition (
  id uuid PRIMARY KEY, organization_id uuid NOT NULL REFERENCES organization, owner_id uuid NOT NULL REFERENCES app_user,
  name varchar(100) NOT NULL, options jsonb NOT NULL, created_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX report_org ON report_definition(organization_id);
CREATE TABLE rfq (
  id uuid PRIMARY KEY, organization_id uuid NOT NULL REFERENCES organization, public_code varchar(50) NOT NULL UNIQUE,
  title varchar(160) NOT NULL, currency varchar(3) NOT NULL, status varchar(20) NOT NULL, closes_on date NOT NULL
);
CREATE TABLE supplier_quote (
  id uuid PRIMARY KEY, rfq_id uuid NOT NULL REFERENCES rfq, organization_id uuid NOT NULL REFERENCES organization,
  supplier_name varchar(120) NOT NULL, contact_email varchar(200) NOT NULL, total numeric(16,2) NOT NULL CHECK(total>0),
  preferences jsonb NOT NULL, state varchar(20) NOT NULL, created_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX quote_pending ON supplier_quote(created_at) WHERE state='PENDING';
CREATE TABLE presentation_profile (organization_id uuid PRIMARY KEY REFERENCES organization, settings jsonb NOT NULL);
CREATE TABLE quote_document (quote_id uuid PRIMARY KEY REFERENCES supplier_quote, caption text NOT NULL, created_at timestamptz NOT NULL DEFAULT now());
