-- auth.account definition

-- Drop table

-- DROP TABLE auth.account;

CREATE TABLE auth.account (
	id serial NOT NULL,
	user_name varchar(20) NOT NULL,
	"password" varchar(130) NOT NULL,
	email varchar(355) NOT NULL,
	creation_ts timestamp NOT NULL,
	last_login timestamp NULL,
	salt varchar(34) NOT NULL,
	CONSTRAINT account_email_key UNIQUE (email),
	CONSTRAINT account_pkey PRIMARY KEY (id),
	CONSTRAINT account_username_key UNIQUE (user_name)
);
