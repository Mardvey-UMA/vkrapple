-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler version: 1.2.0-beta
-- PostgreSQL version: 17.0
-- Project Site: pgmodeler.io
-- Model Author: ---

-- Database creation must be performed outside a multi lined SQL file. 
-- These commands were put in this file only as a convenience.
-- 
-- object: stationery_shop | type: DATABASE --
-- DROP DATABASE IF EXISTS stationery_shop;
CREATE DATABASE stationery_shop;
-- ddl-end --


-- object: public.category | type: TABLE --
-- DROP TABLE IF EXISTS public.category CASCADE;
CREATE TABLE public.category (
	category_id bigint NOT NULL,
	category_name varchar NOT NULL,
	CONSTRAINT category_pk PRIMARY KEY (category_id)
);
-- ddl-end --
ALTER TABLE public.category OWNER TO postgres;
-- ddl-end --

-- object: public.attribute | type: TABLE --
-- DROP TABLE IF EXISTS public.attribute CASCADE;
CREATE TABLE public.attribute (
	attribute_id bigint NOT NULL,
	attribute_name varchar(255),
	category_id_category bigint NOT NULL,
	CONSTRAINT attribute_pk PRIMARY KEY (attribute_id)
);
-- ddl-end --
ALTER TABLE public.attribute OWNER TO postgres;
-- ddl-end --

-- object: public.value | type: TABLE --
-- DROP TABLE IF EXISTS public.value CASCADE;
CREATE TABLE public.value (
	value_id bigint NOT NULL,
	value varchar(255) NOT NULL,
	product_id_product bigint NOT NULL,
	attribute_id_attribute bigint NOT NULL,
	CONSTRAINT value_pk PRIMARY KEY (value_id)
);
-- ddl-end --
ALTER TABLE public.value OWNER TO postgres;
-- ddl-end --

-- object: public.product | type: TABLE --
-- DROP TABLE IF EXISTS public.product CASCADE;
CREATE TABLE public.product (
	product_id bigint NOT NULL,
	name varchar(255),
	price numeric(10,2),
	balance_in_stock bigint NOT NULL,
	article_number bigint NOT NULL,
	rating numeric(10,2),
	description text,
	number_of_orders bigint,
	category_id_category bigint NOT NULL,
	CONSTRAINT product_pk PRIMARY KEY (product_id)
);
-- ddl-end --
ALTER TABLE public.product OWNER TO postgres;
-- ddl-end --

-- object: public."order" | type: TABLE --
-- DROP TABLE IF EXISTS public."order" CASCADE;
CREATE TABLE public."order" (
	order_id bigint NOT NULL,
	created_at date NOT NULL,
	expected_date date,
	status varchar(15),
	order_amount numeric(10,2),
	payment_method varchar(255),
	order_address varchar(255),
	_user_id__user bigint NOT NULL,
	CONSTRAINT order_pk PRIMARY KEY (order_id)
);
-- ddl-end --
ALTER TABLE public."order" OWNER TO postgres;
-- ddl-end --

-- object: public.order_product | type: TABLE --
-- DROP TABLE IF EXISTS public.order_product CASCADE;
CREATE TABLE public.order_product (
	order_product_id bigint NOT NULL,
	quantity smallint,
	amount decimal(10,2),
	order_id_order bigint NOT NULL,
	product_id_product bigint NOT NULL,
	CONSTRAINT order_product_pk PRIMARY KEY (order_product_id)
);
-- ddl-end --
ALTER TABLE public.order_product OWNER TO postgres;
-- ddl-end --

-- object: public._user | type: TABLE --
-- DROP TABLE IF EXISTS public._user CASCADE;
CREATE TABLE public._user (
	_user_id bigint NOT NULL,
	telegram_id bigint NOT NULL,
	username varchar(64),
	chat_id bigint,
	phone_number varchar(15),
	email varchar(255),
	address varchar(255),
	created_at date,
	CONSTRAINT _user_pk PRIMARY KEY (_user_id)
);
-- ddl-end --
ALTER TABLE public._user OWNER TO postgres;
-- ddl-end --

-- object: public.cart | type: TABLE --
-- DROP TABLE IF EXISTS public.cart CASCADE;
CREATE TABLE public.cart (
	cart_id bigint NOT NULL,
	quantity smallint NOT NULL,
	add_date date NOT NULL,
	product_id_product bigint,
	_user_id__user bigint NOT NULL,
	CONSTRAINT cart_pk PRIMARY KEY (cart_id)
);
-- ddl-end --
ALTER TABLE public.cart OWNER TO postgres;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.cart DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.cart ADD CONSTRAINT product_fk FOREIGN KEY (product_id_product)
REFERENCES public.product (product_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.order_product DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.order_product ADD CONSTRAINT product_fk FOREIGN KEY (product_id_product)
REFERENCES public.product (product_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: order_fk | type: CONSTRAINT --
-- ALTER TABLE public.order_product DROP CONSTRAINT IF EXISTS order_fk CASCADE;
ALTER TABLE public.order_product ADD CONSTRAINT order_fk FOREIGN KEY (order_id_order)
REFERENCES public."order" (order_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: category_fk | type: CONSTRAINT --
-- ALTER TABLE public.product DROP CONSTRAINT IF EXISTS category_fk CASCADE;
ALTER TABLE public.product ADD CONSTRAINT category_fk FOREIGN KEY (category_id_category)
REFERENCES public.category (category_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.value DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.value ADD CONSTRAINT product_fk FOREIGN KEY (product_id_product)
REFERENCES public.product (product_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: category_fk | type: CONSTRAINT --
-- ALTER TABLE public.attribute DROP CONSTRAINT IF EXISTS category_fk CASCADE;
ALTER TABLE public.attribute ADD CONSTRAINT category_fk FOREIGN KEY (category_id_category)
REFERENCES public.category (category_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: attribute_fk | type: CONSTRAINT --
-- ALTER TABLE public.value DROP CONSTRAINT IF EXISTS attribute_fk CASCADE;
ALTER TABLE public.value ADD CONSTRAINT attribute_fk FOREIGN KEY (attribute_id_attribute)
REFERENCES public.attribute (attribute_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: _user_fk | type: CONSTRAINT --
-- ALTER TABLE public."order" DROP CONSTRAINT IF EXISTS _user_fk CASCADE;
ALTER TABLE public."order" ADD CONSTRAINT _user_fk FOREIGN KEY (_user_id__user)
REFERENCES public._user (_user_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.photo | type: TABLE --
-- DROP TABLE IF EXISTS public.photo CASCADE;
CREATE TABLE public.photo (
	photo_id bigint NOT NULL,
	object_key uuid,
	photo_url varchar(255),
	created_at date,
	CONSTRAINT photo_pk PRIMARY KEY (photo_id)
);
-- ddl-end --
ALTER TABLE public.photo OWNER TO postgres;
-- ddl-end --

-- object: public.photo_product | type: TABLE --
-- DROP TABLE IF EXISTS public.photo_product CASCADE;
CREATE TABLE public.photo_product (
	photo_product_id bigint NOT NULL,
	index_number smallint NOT NULL,
	photo_id_photo bigint NOT NULL,
	product_id_product bigint NOT NULL,
	CONSTRAINT photo_product_pk PRIMARY KEY (photo_product_id)
);
-- ddl-end --
ALTER TABLE public.photo_product OWNER TO postgres;
-- ddl-end --

-- object: photo_fk | type: CONSTRAINT --
-- ALTER TABLE public.photo_product DROP CONSTRAINT IF EXISTS photo_fk CASCADE;
ALTER TABLE public.photo_product ADD CONSTRAINT photo_fk FOREIGN KEY (photo_id_photo)
REFERENCES public.photo (photo_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.photo_product DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.photo_product ADD CONSTRAINT product_fk FOREIGN KEY (product_id_product)
REFERENCES public.product (product_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE RESTRICT;
-- ddl-end --

-- object: public.review | type: TABLE --
-- DROP TABLE IF EXISTS public.review CASCADE;
CREATE TABLE public.review (
	review_id bigint NOT NULL,
	rating smallint,
	text text,
	created_at date NOT NULL,
	_user_id__user bigint,
	product_id_product bigint,
	CONSTRAINT review_pk PRIMARY KEY (review_id)
);
-- ddl-end --
ALTER TABLE public.review OWNER TO postgres;
-- ddl-end --

-- object: _user_fk | type: CONSTRAINT --
-- ALTER TABLE public.review DROP CONSTRAINT IF EXISTS _user_fk CASCADE;
ALTER TABLE public.review ADD CONSTRAINT _user_fk FOREIGN KEY (_user_id__user)
REFERENCES public._user (_user_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.review DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.review ADD CONSTRAINT product_fk FOREIGN KEY (product_id_product)
REFERENCES public.product (product_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.wish_list | type: TABLE --
-- DROP TABLE IF EXISTS public.wish_list CASCADE;
CREATE TABLE public.wish_list (
	wish_list_id bigint NOT NULL,
	add_date date NOT NULL,
	product_id_product bigint,
	_user_id__user bigint NOT NULL,
	CONSTRAINT wish_list_pk PRIMARY KEY (wish_list_id)
);
-- ddl-end --
ALTER TABLE public.wish_list OWNER TO postgres;
-- ddl-end --

-- object: product_fk | type: CONSTRAINT --
-- ALTER TABLE public.wish_list DROP CONSTRAINT IF EXISTS product_fk CASCADE;
ALTER TABLE public.wish_list ADD CONSTRAINT product_fk FOREIGN KEY (product_id_product)
REFERENCES public.product (product_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: _user_fk | type: CONSTRAINT --
-- ALTER TABLE public.wish_list DROP CONSTRAINT IF EXISTS _user_fk CASCADE;
ALTER TABLE public.wish_list ADD CONSTRAINT _user_fk FOREIGN KEY (_user_id__user)
REFERENCES public._user (_user_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: wish_list_uq | type: CONSTRAINT --
-- ALTER TABLE public.wish_list DROP CONSTRAINT IF EXISTS wish_list_uq CASCADE;
ALTER TABLE public.wish_list ADD CONSTRAINT wish_list_uq UNIQUE (_user_id__user);
-- ddl-end --

-- object: _user_fk | type: CONSTRAINT --
-- ALTER TABLE public.cart DROP CONSTRAINT IF EXISTS _user_fk CASCADE;
ALTER TABLE public.cart ADD CONSTRAINT _user_fk FOREIGN KEY (_user_id__user)
REFERENCES public._user (_user_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: cart_uq | type: CONSTRAINT --
-- ALTER TABLE public.cart DROP CONSTRAINT IF EXISTS cart_uq CASCADE;
ALTER TABLE public.cart ADD CONSTRAINT cart_uq UNIQUE (_user_id__user);
-- ddl-end --

-- object: public.review_photo | type: TABLE --
-- DROP TABLE IF EXISTS public.review_photo CASCADE;
CREATE TABLE public.review_photo (
	review_photo_id bigint NOT NULL,
	index_number smallint NOT NULL,
	review_id_review bigint,
	photo_id_photo bigint,
	CONSTRAINT review_photo_pk PRIMARY KEY (review_photo_id)
);
-- ddl-end --
ALTER TABLE public.review_photo OWNER TO postgres;
-- ddl-end --

-- object: review_fk | type: CONSTRAINT --
-- ALTER TABLE public.review_photo DROP CONSTRAINT IF EXISTS review_fk CASCADE;
ALTER TABLE public.review_photo ADD CONSTRAINT review_fk FOREIGN KEY (review_id_review)
REFERENCES public.review (review_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: photo_fk | type: CONSTRAINT --
-- ALTER TABLE public.review_photo DROP CONSTRAINT IF EXISTS photo_fk CASCADE;
ALTER TABLE public.review_photo ADD CONSTRAINT photo_fk FOREIGN KEY (photo_id_photo)
REFERENCES public.photo (photo_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.role | type: TABLE --
-- DROP TABLE IF EXISTS public.role CASCADE;
CREATE TABLE public.role (
	role_id smallint NOT NULL,
	role_name varchar(10),
	description text,
	CONSTRAINT role_pk PRIMARY KEY (role_id)
);
-- ddl-end --
ALTER TABLE public.role OWNER TO postgres;
-- ddl-end --

-- object: public.role_user | type: TABLE --
-- DROP TABLE IF EXISTS public.role_user CASCADE;
CREATE TABLE public.role_user (
	role_user_id bigint NOT NULL,
	role_id_role smallint NOT NULL,
	_user_id__user bigint NOT NULL,
	CONSTRAINT role_user_pk PRIMARY KEY (role_user_id)
);
-- ddl-end --
ALTER TABLE public.role_user OWNER TO postgres;
-- ddl-end --

-- object: role_fk | type: CONSTRAINT --
-- ALTER TABLE public.role_user DROP CONSTRAINT IF EXISTS role_fk CASCADE;
ALTER TABLE public.role_user ADD CONSTRAINT role_fk FOREIGN KEY (role_id_role)
REFERENCES public.role (role_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: _user_fk | type: CONSTRAINT --
-- ALTER TABLE public.role_user DROP CONSTRAINT IF EXISTS _user_fk CASCADE;
ALTER TABLE public.role_user ADD CONSTRAINT _user_fk FOREIGN KEY (_user_id__user)
REFERENCES public._user (_user_id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --


