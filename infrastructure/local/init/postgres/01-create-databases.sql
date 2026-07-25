CREATE DATABASE wasla_control;
CREATE DATABASE wasla_tenant;
CREATE DATABASE wasla_keycloak;

\connect wasla_tenant
CREATE EXTENSION IF NOT EXISTS postgis;

\connect wasla_control
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";