create table videojuego {
    id int primary key auto_increment,
    nombre varchar(200) not null,
    descripcion varchar(3000),
    imagen_url varchar(500)
};

insert into videojuego (nombre, descripcion, imagen_url)  values ('Borderland 1','juego muy bueno','https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Borderlands_3_cover_art.jpg/220px-Borderlands_3_cover_art.jpg');
insert into videojuego (nombre, descripcion, imagen_url)  values ('Borderland 1.1','juego muuy bueno','https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Borderlands_3_cover_art.jpg/220px-Borderlands_3_cover_art.jpg');
insert into videojuego (nombre, descripcion, imagen_url)  values ('Borderland 2','juego muuuy bueno','https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Borderlands_3_cover_art.jpg/220px-Borderlands_3_cover_art.jpg');
insert into videojuego (nombre, descripcion, imagen_url)  values ('Borderland 3','juego muuuuy bueno','https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Borderlands_3_cover_art.jpg/220px-Borderlands_3_cover_art.jpg');

