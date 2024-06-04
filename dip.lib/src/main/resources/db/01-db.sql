DROP DATABASE IF EXISTS db_diplom;
CREATE DATABASE db_diplom WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
\c db_diplom

CREATE TABLE regions (
    id                     INTEGER PRIMARY KEY NOT NULL,
    name                   VARCHAR(128)  NOT NULL,
    comment                VARCHAR(1024)
);

CREATE TABLE clients (
    id                     SERIAL  PRIMARY KEY NOT NULL,
    region_id              INTEGER REFERENCES regions (id) NOT NULL,
    name                   VARCHAR(128)  NOT NULL,
    surname                VARCHAR(128)  NOT NULL,
    second_name            VARCHAR(128)  NOT NULL,
    birthdate              date          NOT NULL,
    count_of_bonuses       BIGINT default 0,
    comment                VARCHAR(1024)  default ''
);

CREATE TABLE product_categories (
    id                     INTEGER PRIMARY KEY NOT NULL,
    name                   VARCHAR NOT NULL UNIQUE,
	comment                VARCHAR(1024)
);

CREATE TABLE products (
    id                     INTEGER PRIMARY KEY NOT NULL,
    name                   VARCHAR(128) NOT NULL,
    category_id            INTEGER NOT NULL,
    cost                   INTEGER NOT NULL,
    count_of_product       INTEGER NOT NULL,
    delivery_datetime      timestamp without time zone NOT NULL,
    comment                VARCHAR(1024),
	FOREIGN KEY(category_id) REFERENCES product_categories (id)
);

CREATE TABLE loyality_params (
    id                     SERIAL  PRIMARY KEY NOT NULL,
    name                   VARCHAR NOT NULL,
    coefficient            DOUBLE PRECISION default 0.0 NOT NULL,
    comment                VARCHAR(1024) default ''
);

CREATE TABLE loyalities (
    id                     SERIAL  PRIMARY KEY NOT NULL,
    region_id              INTEGER REFERENCES regions (id) NOT NULL,
    product_category_id    INTEGER REFERENCES product_categories (id) NOT NULL,
    loyality_params_id     INTEGER REFERENCES loyality_params (id) NOT NULL,
	coefficient_by_region  DOUBLE PRECISION default 0.0 NOT NULL,
    comment                VARCHAR(1024) default ''
);

CREATE TABLE markets (
    id                     INTEGER PRIMARY KEY NOT NULL,
	name                   VARCHAR(128) NOT NULL,
    region_id              INTEGER REFERENCES regions (id) NOT NULL,
    market_id_on_region    INTEGER NOT NULL,
    comment                VARCHAR(1024) default '',
	
    UNIQUE (region_id, market_id_on_region)
);

CREATE TABLE terminals (
    id                     SERIAL  PRIMARY KEY NOT NULL,
	region_id              INTEGER REFERENCES regions (id) NOT NULL,
	market_id              INTEGER REFERENCES markets (id) NOT NULL,
    terminal_id_on_market  INTEGER NOT NULL,    
    comment                VARCHAR(1024) default '',
	
    UNIQUE (region_id, market_id, terminal_id_on_market)
);

CREATE TABLE order_numbers_dictionary (
    order_number           BIGSERIAL PRIMARY KEY NOT NULL,
    region_id              BIGINT  NOT NULL,
    market_id              INTEGER NOT NULL,
    terminal_id            INTEGER NOT NULL,
    terminal_basket_number BIGINT  NOT NULL,
    basket_price           INTEGER NOT NULL,
    order_time             time    without time zone NOT NULL,
    order_date             date    NOT NULL,
    comment                VARCHAR(1024) default ''
);

CREATE TABLE sales (
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    order_number         BIGINT NOT NULL,
    line_number_in_order INTEGER NOT NULL,
    region_id            INTEGER NOT NULL,
    market_id            INTEGER NOT NULL,
    terminal_id          INTEGER NOT NULL,
    product_id           INTEGER NOT NULL,
    count_of_product     INTEGER NOT NULL,
    price_of_one_piece   INTEGER NOT NULL,
    order_date           date NOT NULL,
    client_id            INTEGER NOT NULL,
    comment              VARCHAR(1024)
);

CREATE TABLE buffer_in (
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    obj_record    VARCHAR NOT NULL
);

CREATE TABLE buffer_processing (
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    obj_record    VARCHAR NOT NULL
);

CREATE TABLE buffer_quality (
    id             BIGSERIAL PRIMARY KEY NOT NULL,
    obj_record     VARCHAR NOT NULL,
    exception_text VARCHAR
);

CREATE TABLE order_numbers_dictionary__01 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__02 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__03 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__04 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__05 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__06 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__07 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__08 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__09 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__10 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__20 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__30 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__40 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__50 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__60 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__70 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__80 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary__90 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');
CREATE TABLE order_numbers_dictionary_100 (order_number BIGSERIAL PRIMARY KEY NOT NULL, region_id BIGINT  NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, terminal_basket_number BIGINT  NOT NULL, basket_price INTEGER NOT NULL, order_time time without time zone NOT NULL, order_date date NOT NULL, comment VARCHAR(1024) default '');

CREATE TABLE sales__01 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));     
CREATE TABLE sales__02 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__03 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__04 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__05 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__06 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__07 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__08 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__09 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__10 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));     
CREATE TABLE sales__20 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__30 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__40 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__50 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__60 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__70 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__80 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales__90 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
CREATE TABLE sales_100 (id BIGSERIAL PRIMARY KEY NOT NULL, order_number BIGINT NOT NULL, line_number_in_order INTEGER NOT NULL, region_id INTEGER NOT NULL, market_id INTEGER NOT NULL, terminal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, count_of_product INTEGER NOT NULL, price_of_one_piece INTEGER NOT NULL, order_date date NOT NULL, client_id INTEGER NOT NULL, comment VARCHAR(1024));
