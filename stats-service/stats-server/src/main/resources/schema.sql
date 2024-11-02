DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats
(
    id    int      NOT NULL GENERATED ALWAYS AS IDENTITY,
    app  varchar(200) NOT NULL,
    uri varchar(200) NOT NULL,
    ip varchar(50) NOT NULL,
    created_time timestamp without time zone NOT NULL,
    CONSTRAINT pk_stats PRIMARY KEY (id)
    );