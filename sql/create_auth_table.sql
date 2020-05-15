-- auth.account definition

-- Drop table

-- DROP TABLE auth.account;

create TABLE auth.account (
	id serial NOT NULL,
	username varchar(20) NOT NULL,
	pass varchar(50) NOT NULL,
	email varchar(355) NOT NULL,
	create_ts timestamp NOT NULL,
	last_login timestamp NULL,
	CONSTRAINT account_email_key UNIQUE (email),
	CONSTRAINT account_pkey PRIMARY KEY (id),
	CONSTRAINT account_username_key UNIQUE (username)
);

