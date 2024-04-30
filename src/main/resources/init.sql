create table artist
(
    artist_id      bigserial not null,
    artist_country varchar(255),
    artist_name    varchar(255),
    primary key (artist_id)
);

create table song
(
    duration  float4,
    artist_id bigint    not null,
    id        bigserial not null,
    album     varchar(255),
    genre     varchar(255),
    title     varchar(255),
    primary key (id),
    foreign key (artist_id) references artist (artist_id)
);

INSERT INTO artist (artist_id, artist_name, artist_country)
VALUES (1, 'Linkin Park', 'USA');
INSERT INTO artist (artist_id, artist_name, artist_country)
VALUES (2, 'Sum 41', 'Canada');
INSERT INTO artist (artist_id, artist_name, artist_country)
VALUES (3, 'Thousand Foot Krutch', 'Canada');

CREATE INDEX album_index ON song (album);
CREATE INDEX genre_index ON song (genre);
