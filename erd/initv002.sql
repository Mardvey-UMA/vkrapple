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
	"цена" decimal(10,2),
	"остаток_на_складе" bigint,
	"артикул" bigint,
	"производитель" varchar(255),
	"средняя_оценка" numeric,
	"описание" text,
	"категория_id_категория" bigint,
	CONSTRAINT "товар_pk" PRIMARY KEY ("товар_id")
);
-- ddl-end --
ALTER TABLE public."товар" OWNER TO postgres;
-- ddl-end --

-- object: public."заказ" | type: TABLE --
-- DROP TABLE IF EXISTS public."заказ" CASCADE;
CREATE TABLE public."заказ" (
	"заказ_id" bigint NOT NULL,
	"дата" date,
	"статус" varchar(15),
	"сумма" decimal(10,2),
	"способ_оплаты" varchar(255),
	"адрес" smallint,
	"пользователь_id_пользователь" bigint,
	CONSTRAINT "заказы_pk" PRIMARY KEY ("заказ_id")
);
-- ddl-end --
ALTER TABLE public."заказ" OWNER TO postgres;
-- ddl-end --

-- object: public."заказы_товары" | type: TABLE --
-- DROP TABLE IF EXISTS public."заказы_товары" CASCADE;
CREATE TABLE public."заказы_товары" (
	"заказы_товары_id" bigint NOT NULL,
	"количество" smallint,
	"цена" decimal(10,2),
	"заказ_id_заказ" bigint,
	"товар_id_товар" bigint,
	CONSTRAINT "заказы_товары_pk" PRIMARY KEY ("заказы_товары_id")
);
-- ddl-end --
ALTER TABLE public."заказы_товары" OWNER TO postgres;
-- ddl-end --

-- object: public."пользователь" | type: TABLE --
-- DROP TABLE IF EXISTS public."пользователь" CASCADE;
CREATE TABLE public."пользователь" (
	"пользователь_id" bigint NOT NULL,
	telegram_id bigint,
	username varchar(64),
	chat_id bigint,
	"телефон" varchar(15),
	email varchar(255),
	"адрес" varchar(255),
	created_at date,
	CONSTRAINT "пользователь_pk" PRIMARY KEY ("пользователь_id")
);
-- ddl-end --
ALTER TABLE public."пользователь" OWNER TO postgres;
-- ddl-end --

-- object: public."корзина" | type: TABLE --
-- DROP TABLE IF EXISTS public."корзина" CASCADE;
CREATE TABLE public."корзина" (
	"корзина_id" bigint NOT NULL,
	"количество" smallint,
	"дата_добавления" date,
	"товар_id_товар" bigint,
	"пользователь_id_пользователь" bigint,
	CONSTRAINT "корзина_pk" PRIMARY KEY ("корзина_id")
);
-- ddl-end --
ALTER TABLE public."корзина" OWNER TO postgres;
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

-- object: "заказ_fk" | type: CONSTRAINT --
-- ALTER TABLE public."заказы_товары" DROP CONSTRAINT IF EXISTS "заказ_fk" CASCADE;
ALTER TABLE public."заказы_товары" ADD CONSTRAINT "заказ_fk" FOREIGN KEY ("заказ_id_заказ")
REFERENCES public."заказ" ("заказ_id") MATCH FULL
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

-- object: "пользователь_fk" | type: CONSTRAINT --
-- ALTER TABLE public."заказ" DROP CONSTRAINT IF EXISTS "пользователь_fk" CASCADE;
ALTER TABLE public."заказ" ADD CONSTRAINT "пользователь_fk" FOREIGN KEY ("пользователь_id_пользователь")
REFERENCES public."пользователь" ("пользователь_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public."фото" | type: TABLE --
-- DROP TABLE IF EXISTS public."фото" CASCADE;
CREATE TABLE public."фото" (
	"фото_id" bigint NOT NULL,
	object_key uuid,
	photo_url varchar(255),
	created_at date,
	CONSTRAINT "фото_pk" PRIMARY KEY ("фото_id")
);
-- ddl-end --
ALTER TABLE public."фото" OWNER TO postgres;
-- ddl-end --

-- object: public."фото_товары" | type: TABLE --
-- DROP TABLE IF EXISTS public."фото_товары" CASCADE;
CREATE TABLE public."фото_товары" (
	"фото_товары_id" bigint NOT NULL,
	"порядковый_номер" smallint,
	"фото_id_фото" bigint,
	"товар_id_товар" bigint,
	CONSTRAINT "фото_товары_pk" PRIMARY KEY ("фото_товары_id")
);
-- ddl-end --
ALTER TABLE public."фото_товары" OWNER TO postgres;
-- ddl-end --

-- object: "фото_fk" | type: CONSTRAINT --
-- ALTER TABLE public."фото_товары" DROP CONSTRAINT IF EXISTS "фото_fk" CASCADE;
ALTER TABLE public."фото_товары" ADD CONSTRAINT "фото_fk" FOREIGN KEY ("фото_id_фото")
REFERENCES public."фото" ("фото_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "товар_fk" | type: CONSTRAINT --
-- ALTER TABLE public."фото_товары" DROP CONSTRAINT IF EXISTS "товар_fk" CASCADE;
ALTER TABLE public."фото_товары" ADD CONSTRAINT "товар_fk" FOREIGN KEY ("товар_id_товар")
REFERENCES public."товар" ("товар_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public."отзыв" | type: TABLE --
-- DROP TABLE IF EXISTS public."отзыв" CASCADE;
CREATE TABLE public."отзыв" (
	"отзыв_id" bigint NOT NULL,
	"оценка" smallint,
	"текст" text,
	"дата" date,
	"пользователь_id_пользователь" bigint,
	"товар_id_товар" bigint,
	CONSTRAINT "отзыв_pk" PRIMARY KEY ("отзыв_id")
);
-- ddl-end --
ALTER TABLE public."отзыв" OWNER TO postgres;
-- ddl-end --

-- object: "пользователь_fk" | type: CONSTRAINT --
-- ALTER TABLE public."отзыв" DROP CONSTRAINT IF EXISTS "пользователь_fk" CASCADE;
ALTER TABLE public."отзыв" ADD CONSTRAINT "пользователь_fk" FOREIGN KEY ("пользователь_id_пользователь")
REFERENCES public."пользователь" ("пользователь_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "товар_fk" | type: CONSTRAINT --
-- ALTER TABLE public."отзыв" DROP CONSTRAINT IF EXISTS "товар_fk" CASCADE;
ALTER TABLE public."отзыв" ADD CONSTRAINT "товар_fk" FOREIGN KEY ("товар_id_товар")
REFERENCES public."товар" ("товар_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public."список_желаемого" | type: TABLE --
-- DROP TABLE IF EXISTS public."список_желаемого" CASCADE;
CREATE TABLE public."список_желаемого" (
	"список_желаемого_id" bigint NOT NULL,
	"дата_добавления" date,
	"товар_id_товар" bigint,
	"пользователь_id_пользователь" bigint,
	CONSTRAINT "желания_pk" PRIMARY KEY ("список_желаемого_id")
);
-- ddl-end --
ALTER TABLE public."список_желаемого" OWNER TO postgres;
-- ddl-end --

-- object: "товар_fk" | type: CONSTRAINT --
-- ALTER TABLE public."список_желаемого" DROP CONSTRAINT IF EXISTS "товар_fk" CASCADE;
ALTER TABLE public."список_желаемого" ADD CONSTRAINT "товар_fk" FOREIGN KEY ("товар_id_товар")
REFERENCES public."товар" ("товар_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "пользователь_fk" | type: CONSTRAINT --
-- ALTER TABLE public."список_желаемого" DROP CONSTRAINT IF EXISTS "пользователь_fk" CASCADE;
ALTER TABLE public."список_желаемого" ADD CONSTRAINT "пользователь_fk" FOREIGN KEY ("пользователь_id_пользователь")
REFERENCES public."пользователь" ("пользователь_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "список_желаемого_uq" | type: CONSTRAINT --
-- ALTER TABLE public."список_желаемого" DROP CONSTRAINT IF EXISTS "список_желаемого_uq" CASCADE;
ALTER TABLE public."список_желаемого" ADD CONSTRAINT "список_желаемого_uq" UNIQUE ("пользователь_id_пользователь");
-- ddl-end --

-- object: "пользователь_fk" | type: CONSTRAINT --
-- ALTER TABLE public."корзина" DROP CONSTRAINT IF EXISTS "пользователь_fk" CASCADE;
ALTER TABLE public."корзина" ADD CONSTRAINT "пользователь_fk" FOREIGN KEY ("пользователь_id_пользователь")
REFERENCES public."пользователь" ("пользователь_id") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "корзина_uq" | type: CONSTRAINT --
-- ALTER TABLE public."корзина" DROP CONSTRAINT IF EXISTS "корзина_uq" CASCADE;
ALTER TABLE public."корзина" ADD CONSTRAINT "корзина_uq" UNIQUE ("пользователь_id_пользователь");
-- ddl-end --


