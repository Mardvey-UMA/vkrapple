-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler version: 1.2.0-beta
-- PostgreSQL version: 17.0
-- Project Site: pgmodeler.io
-- Model Author: ---

-- Database creation must be performed outside a multi lined SQL file. 
-- These commands were put in this file only as a convenience.
-- 
-- object: new_database | type: DATABASE --
-- DROP DATABASE IF EXISTS new_database;
CREATE DATABASE new_database;
-- ddl-end --


-- object: public."категория" | type: TABLE --
-- DROP TABLE IF EXISTS public."категория" CASCADE;
CREATE TABLE public."категория" (
	"категория_id" bigint NOT NULL,
	"название" varchar,
	CONSTRAINT "категория_pk" PRIMARY KEY ("категория_id")
);
-- ddl-end --
ALTER TABLE public."категория" OWNER TO postgres;
-- ddl-end --

-- object: public."атрибут" | type: TABLE --
-- DROP TABLE IF EXISTS public."атрибут" CASCADE;
CREATE TABLE public."атрибут" (
	"атрибут_id" bigint NOT NULL,
	"название" varchar,
	"категория_id_категория" bigint,
	CONSTRAINT "атрибут_pk" PRIMARY KEY ("атрибут_id")
);
-- ddl-end --
ALTER TABLE public."атрибут" OWNER TO postgres;
-- ddl-end --

-- object: public."значение" | type: TABLE --
-- DROP TABLE IF EXISTS public."значение" CASCADE;
CREATE TABLE public."значение" (
	"значение_id" bigint NOT NULL,
	"значение" varchar,
	"товар_id_товар" bigint,
	"атрибут_id_атрибут" bigint,
	CONSTRAINT "значение_pk" PRIMARY KEY ("значение_id")
);
-- ddl-end --
ALTER TABLE public."значение" OWNER TO postgres;
-- ddl-end --

-- object: public."товар" | type: TABLE --
-- DROP TABLE IF EXISTS public."товар" CASCADE;
CREATE TABLE public."товар" (
	"товар_id" bigint NOT NULL,
	"название" varchar,
	"категория_id_категория" bigint,
	CONSTRAINT "товар_pk" PRIMARY KEY ("товар_id")
);
-- ddl-end --
ALTER TABLE public."товар" OWNER TO postgres;
-- ddl-end --

-- object: public."заказы" | type: TABLE --
-- DROP TABLE IF EXISTS public."заказы" CASCADE;
CREATE TABLE public."заказы" (
	"заказы_id" bigint NOT NULL,
	CONSTRAINT "заказы_pk" PRIMARY KEY ("заказы_id")
);
-- ddl-end --
ALTER TABLE public."заказы" OWNER TO postgres;
-- ddl-end --

-- object: public."заказы_товары" | type: TABLE --
-- DROP TABLE IF EXISTS public."заказы_товары" CASCADE;
CREATE TABLE public."заказы_товары" (
	"заказы_товары_id" bigint NOT NULL,
	"товар_id_товар" bigint,
	"заказы_id_заказы" bigint,
	CONSTRAINT "заказы_товары_pk" PRIMARY KEY ("заказы_товары_id")
);
-- ddl-end --
ALTER TABLE public."заказы_товары" OWNER TO postgres;
-- ddl-end --

-- object: public."корзина" | type: TABLE --
-- DROP TABLE IF EXISTS public."корзина" CASCADE;
CREATE TABLE public."корзина" (
	"корзина_id" bigint NOT NULL,
	"пользователь_id_пользователь" bigint,
	"товар_id_товар" bigint,
	CONSTRAINT "корзина_pk" PRIMARY KEY ("корзина_id")
);
-- ddl-end --
ALTER TABLE public."корзина" OWNER TO postgres;
-- ddl-end --

-- object: public."пользователь" | type: TABLE --
-- DROP TABLE IF EXISTS public."пользователь" CASCADE;
CREATE TABLE public."пользователь" (
	"пользователь_id" bigint NOT NULL,
	"имя" varchar,
	CONSTRAINT "пользователь_pk" PRIMARY KEY ("пользователь_id")
);
-- ddl-end --
ALTER TABLE public."пользователь" OWNER TO postgres;
-- ddl-end --

-- object: "пользователь_fk" | type: CONSTRAINT --
-- ALTER TABLE public."корзина" DROP CONSTRAINT IF EXISTS "пользователь_fk" CASCADE;
ALTER TABLE public."корзина" ADD CONSTRAINT "пользователь_fk" FOREIGN KEY ("пользователь_id_пользователь")
REFERENCES public."пользователь" ("пользователь_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "товар_fk" | type: CONSTRAINT --
-- ALTER TABLE public."корзина" DROP CONSTRAINT IF EXISTS "товар_fk" CASCADE;
ALTER TABLE public."корзина" ADD CONSTRAINT "товар_fk" FOREIGN KEY ("товар_id_товар")
REFERENCES public."товар" ("товар_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "товар_fk" | type: CONSTRAINT --
-- ALTER TABLE public."заказы_товары" DROP CONSTRAINT IF EXISTS "товар_fk" CASCADE;
ALTER TABLE public."заказы_товары" ADD CONSTRAINT "товар_fk" FOREIGN KEY ("товар_id_товар")
REFERENCES public."товар" ("товар_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "заказы_fk" | type: CONSTRAINT --
-- ALTER TABLE public."заказы_товары" DROP CONSTRAINT IF EXISTS "заказы_fk" CASCADE;
ALTER TABLE public."заказы_товары" ADD CONSTRAINT "заказы_fk" FOREIGN KEY ("заказы_id_заказы")
REFERENCES public."заказы" ("заказы_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "категория_fk" | type: CONSTRAINT --
-- ALTER TABLE public."товар" DROP CONSTRAINT IF EXISTS "категория_fk" CASCADE;
ALTER TABLE public."товар" ADD CONSTRAINT "категория_fk" FOREIGN KEY ("категория_id_категория")
REFERENCES public."категория" ("категория_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "товар_fk" | type: CONSTRAINT --
-- ALTER TABLE public."значение" DROP CONSTRAINT IF EXISTS "товар_fk" CASCADE;
ALTER TABLE public."значение" ADD CONSTRAINT "товар_fk" FOREIGN KEY ("товар_id_товар")
REFERENCES public."товар" ("товар_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "категория_fk" | type: CONSTRAINT --
-- ALTER TABLE public."атрибут" DROP CONSTRAINT IF EXISTS "категория_fk" CASCADE;
ALTER TABLE public."атрибут" ADD CONSTRAINT "категория_fk" FOREIGN KEY ("категория_id_категория")
REFERENCES public."категория" ("категория_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "атрибут_fk" | type: CONSTRAINT --
-- ALTER TABLE public."значение" DROP CONSTRAINT IF EXISTS "атрибут_fk" CASCADE;
ALTER TABLE public."значение" ADD CONSTRAINT "атрибут_fk" FOREIGN KEY ("атрибут_id_атрибут")
REFERENCES public."атрибут" ("атрибут_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --


