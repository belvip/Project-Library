--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    author_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    author_email character varying(100) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.author OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_author_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.author_author_id_seq OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_author_id_seq OWNED BY public.author.author_id;


--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    book_id integer NOT NULL,
    title character varying(200) NOT NULL,
    number_of_copies integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: book_author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_author (
    book_id integer NOT NULL,
    author_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.book_author OWNER TO postgres;

--
-- Name: book_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_book_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.book_book_id_seq OWNER TO postgres;

--
-- Name: book_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_book_id_seq OWNED BY public.book.book_id;


--
-- Name: book_loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_loan (
    book_id integer NOT NULL,
    loan_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.book_loan OWNER TO postgres;

--
-- Name: books_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books_category (
    book_id integer NOT NULL,
    category_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.books_category OWNER TO postgres;

--
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    category_id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.category OWNER TO postgres;

--
-- Name: category_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_category_id_seq OWNER TO postgres;

--
-- Name: category_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_category_id_seq OWNED BY public.category.category_id;


--
-- Name: loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan (
    loan_id integer NOT NULL,
    member_id integer NOT NULL,
    loandate timestamp with time zone NOT NULL,
    duedate timestamp with time zone NOT NULL,
    returndate timestamp with time zone,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    penalty integer DEFAULT 0
);


ALTER TABLE public.loan OWNER TO postgres;

--
-- Name: loan_loan_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.loan_loan_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.loan_loan_id_seq OWNER TO postgres;

--
-- Name: loan_loan_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.loan_loan_id_seq OWNED BY public.loan.loan_id;


--
-- Name: loan_penalties; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan_penalties (
    penalty_id integer NOT NULL,
    loan_id integer NOT NULL,
    penalty_amount integer NOT NULL,
    penalty_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.loan_penalties OWNER TO postgres;

--
-- Name: loan_penalties_penalty_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.loan_penalties_penalty_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.loan_penalties_penalty_id_seq OWNER TO postgres;

--
-- Name: loan_penalties_penalty_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.loan_penalties_penalty_id_seq OWNED BY public.loan_penalties.penalty_id;


--
-- Name: member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.member (
    member_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    adhesion_date timestamp with time zone NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.member OWNER TO postgres;

--
-- Name: member_member_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.member_member_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.member_member_id_seq OWNER TO postgres;

--
-- Name: member_member_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.member_member_id_seq OWNED BY public.member.member_id;


--
-- Name: author author_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN author_id SET DEFAULT nextval('public.author_author_id_seq'::regclass);


--
-- Name: book book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN book_id SET DEFAULT nextval('public.book_book_id_seq'::regclass);


--
-- Name: category category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category ALTER COLUMN category_id SET DEFAULT nextval('public.category_category_id_seq'::regclass);


--
-- Name: loan loan_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan ALTER COLUMN loan_id SET DEFAULT nextval('public.loan_loan_id_seq'::regclass);


--
-- Name: loan_penalties penalty_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan_penalties ALTER COLUMN penalty_id SET DEFAULT nextval('public.loan_penalties_penalty_id_seq'::regclass);


--
-- Name: member member_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member ALTER COLUMN member_id SET DEFAULT nextval('public.member_member_id_seq'::regclass);


--
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author (author_id, first_name, last_name, author_email, created_at) FROM stdin;
1	Pierre	Mbida	pierre@gmail.com	2025-01-12 20:46:12.725399
2	Simon	Abanda	simon@gmail.com	2025-01-12 20:49:01.152191
3	Jean-Marc	Mbiya	jean@gmail.com	2025-01-12 20:56:52.393921
4	Delphine	Foyet	delphine@mail.com	2025-01-12 21:30:50.047737
5	Béatrice	Tchamda	beatrice@gmail.com	2025-01-12 21:35:45.379032
6	Amélie	Ndjeuga	amelie@gmail.com	2025-01-12 21:36:51.693113
7	Cynthia	Njoh	cynthia@gmail.com	2025-01-12 21:37:38.086565
9	Simon	Abanda	abanda@gmail.com	2025-01-12 21:51:57.463685
10	Victor	Ewondo	victor@gmail.com	2025-01-12 21:56:23.446498
11	Joseph	Nguele	joseph@gmail.com	2025-01-12 22:51:16.087755
12	soko	lolo	soko@gmail.com	2025-01-12 23:49:25.714446
13	Pouadjeu	Belvinard	belvinard97@gmail.com	2025-01-13 06:12:22.743505
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (book_id, title, number_of_copies, created_at) FROM stdin;
6	Rester Motivé	34	2025-01-12 23:04:59.377839
7	La Persévérance	3	2025-01-12 23:07:03.288029
9	Le Temps est Précieux 	5	2025-01-13 00:58:22.587185
2	Spring Boot	55	2025-01-12 22:56:38.318364
1	Le roi de Bamoun	21	2025-01-12 22:52:08.423743
8	Chants de la savane	59	2025-01-12 23:53:15.464368
4	Les Empires d'Afrique	9	2025-01-12 22:59:10.158272
3	Développement Front-End avec React	9	2025-01-12 22:57:43.049782
\.


--
-- Data for Name: book_author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_author (book_id, author_id, created_at) FROM stdin;
1	11	2025-01-12 22:52:08.427616
3	2	2025-01-12 22:57:43.057807
4	5	2025-01-12 22:59:10.16589
6	9	2025-01-12 23:04:59.386029
8	2	2025-01-12 23:53:15.466511
2	10	2025-01-13 00:07:48.130893
7	9	2025-01-13 00:48:51.518242
9	6	2025-01-13 00:58:22.597264
\.


--
-- Data for Name: book_loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_loan (book_id, loan_id, created_at) FROM stdin;
4	1	2025-01-13 02:49:09.966512+01
9	3	2025-01-13 03:07:14.961993+01
2	4	2025-01-13 03:08:45.366881+01
1	5	2025-01-13 03:13:42.846381+01
8	8	2025-01-13 03:23:32.40546+01
3	9	2025-01-13 03:26:59.274768+01
\.


--
-- Data for Name: books_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.books_category (book_id, category_id, created_at) FROM stdin;
1	8	2025-01-12 22:52:08.432973
3	3	2025-01-12 22:57:43.058928
4	7	2025-01-12 22:59:10.167055
6	4	2025-01-12 23:04:59.387077
8	1	2025-01-12 23:53:15.474444
2	6	2025-01-13 00:07:48.130893
7	4	2025-01-13 00:48:51.518242
9	4	2025-01-13 00:58:22.598451
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (category_id, category_name, created_at) FROM stdin;
1	Philosophie	2025-01-12 12:50:31.378646
2	Science-Fiction	2025-01-12 18:27:47.276793
3	Informatique & Programmation	2025-01-12 18:37:59.575598
4	Développement Personnel	2025-01-12 19:35:42.288249
6	Biographies	2025-01-12 19:37:14.396147
7	Histoire	2025-01-12 19:39:59.681948
8	Traditions et culture	2025-01-12 21:52:22.839201
9	Roman	2025-01-12 23:49:50.751165
\.


--
-- Data for Name: loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loan (loan_id, member_id, loandate, duedate, returndate, created_at, penalty) FROM stdin;
3	4	2025-01-13 03:07:14.960912+01	2025-03-03 03:07:14.960912+01	\N	2025-01-13 03:07:14.961993+01	0
4	6	2025-01-13 03:08:45.366358+01	2025-03-03 03:08:45.366358+01	\N	2025-01-13 03:08:45.366881+01	0
5	5	2025-01-13 03:13:42.844987+01	2025-03-03 03:13:42.844987+01	2025-01-13 03:14:05.020389+01	2025-01-13 03:13:42.846381+01	0
8	5	2025-01-13 03:23:32.403954+01	2025-03-03 03:23:32.403954+01	\N	2025-01-13 03:23:32.40546+01	0
1	5	2025-01-13 02:49:09.958882+01	2025-03-03 02:49:09.958882+01	2025-01-13 03:25:42.37616+01	2025-01-13 02:49:09.966512+01	0
9	1	2025-01-13 03:26:59.27353+01	2025-03-03 03:26:59.27353+01	\N	2025-01-13 03:26:59.274768+01	0
\.


--
-- Data for Name: loan_penalties; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loan_penalties (penalty_id, loan_id, penalty_amount, penalty_date) FROM stdin;
\.


--
-- Data for Name: member; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.member (member_id, first_name, last_name, email, adhesion_date, created_at) FROM stdin;
1	Emmanuel	Ndjomo	emmanuel.ndjomo@gmail.com	2025-01-13 01:30:48.879+01	2025-01-13 01:30:48.882683+01
2	Clarisse	Fongang	clarisse.fongang@yahoo.com	2025-01-13 01:33:11.403+01	2025-01-13 01:33:11.405323+01
3	Serge	Mbarga	serge.mbarga@outlook.com	2025-01-13 01:33:49.228+01	2025-01-13 01:33:49.229129+01
4	Aline	Kamga	aline.kamga@gmail.com	2025-01-13 01:34:30.838+01	2025-01-13 01:34:30.840783+01
5	Fabrice	Tchoumba	fabrice.tchoumba@yahoo.fr	2025-01-13 01:35:12.211+01	2025-01-13 01:35:12.213672+01
6	Alice	Mefire	lice.mefire@gmail.com	2025-01-13 01:35:55.095+01	2025-01-13 01:35:55.099643+01
\.


--
-- Name: author_author_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.author_author_id_seq', 13, true);


--
-- Name: book_book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_book_id_seq', 9, true);


--
-- Name: category_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_category_id_seq', 9, true);


--
-- Name: loan_loan_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.loan_loan_id_seq', 9, true);


--
-- Name: loan_penalties_penalty_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.loan_penalties_penalty_id_seq', 1, false);


--
-- Name: member_member_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.member_member_id_seq', 8, true);


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_id);


--
-- Name: book_author book_author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT book_author_pkey PRIMARY KEY (book_id, author_id);


--
-- Name: book_loan book_loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_loan
    ADD CONSTRAINT book_loan_pkey PRIMARY KEY (book_id, loan_id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (book_id);


--
-- Name: books_category books_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_category
    ADD CONSTRAINT books_category_pkey PRIMARY KEY (book_id, category_id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (category_id);


--
-- Name: loan_penalties loan_penalties_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan_penalties
    ADD CONSTRAINT loan_penalties_pkey PRIMARY KEY (penalty_id);


--
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (loan_id);


--
-- Name: member member_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member
    ADD CONSTRAINT member_pkey PRIMARY KEY (member_id);


--
-- Name: author unique_author_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT unique_author_email UNIQUE (author_email);


--
-- Name: category unique_category_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT unique_category_name UNIQUE (category_name);


--
-- Name: member unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member
    ADD CONSTRAINT unique_email UNIQUE (email);


--
-- Name: book_author book_author_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT book_author_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.author(author_id) ON DELETE CASCADE;


--
-- Name: book_author book_author_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT book_author_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id) ON DELETE CASCADE;


--
-- Name: book_loan book_loan_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_loan
    ADD CONSTRAINT book_loan_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id) ON DELETE CASCADE;


--
-- Name: book_loan book_loan_loan_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_loan
    ADD CONSTRAINT book_loan_loan_id_fkey FOREIGN KEY (loan_id) REFERENCES public.loan(loan_id) ON DELETE CASCADE;


--
-- Name: books_category books_category_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_category
    ADD CONSTRAINT books_category_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id) ON DELETE CASCADE;


--
-- Name: books_category books_category_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_category
    ADD CONSTRAINT books_category_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.category(category_id) ON DELETE CASCADE;


--
-- Name: loan loan_member_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_member_id_fkey FOREIGN KEY (member_id) REFERENCES public.member(member_id) ON DELETE CASCADE;


--
-- Name: loan_penalties loan_penalties_loan_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan_penalties
    ADD CONSTRAINT loan_penalties_loan_id_fkey FOREIGN KEY (loan_id) REFERENCES public.loan(loan_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

