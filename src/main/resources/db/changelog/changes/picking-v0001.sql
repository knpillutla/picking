CREATE TABLE picks
(
    ID serial  primary key,
    ORDER_LINE_ID integer,
    BATCH_NBR character varying(50), 
    BUS_NAME character varying(50),
	LOCN_NBR integer not null default 0,
	COMPANY  character varying(15), -- is gift
	DIVISION  character varying(10), -- is gift
	BUS_UNIT  character varying(3) NOT NULL,
    LOCN_BRCD character varying(25),
    ITEM_BRCD character varying(25),
    QTY integer NOT NULL DEFAULT 0,
    PICKED_QTY integer DEFAULT 0,
    FROM_CONTAINER_NBR  character varying(25),
    TO_CONTAINER_NBR  character varying(25),
    STATUS character varying(25),
    ORDER_ID   INTEGER NOT NULL,
    ORDER_NBR  character varying(25),
	ORDER_LINE_NBR  INTEGER NOT NULL,
	PACKAGE_NBR  character varying(25),
	SINGLES character varying(1) NULL,
	TRANSACTION_NAME character varying(50),
	SOURCE character varying(50),
	HOST_NAME  character varying(50),
    CREATED_DTTM  timestamp default NOW(),
    UPDATED_DTTM  timestamp default NOW(),
    USER_ID character varying(25),
    VERSION INTEGER
    
);
