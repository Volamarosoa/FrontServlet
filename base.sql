create database servlet;
\c servlet;

create table Compte(
    id SERIAL PRIMARY KEY,
    idEntreprise int,
    numero int,
    intitule varchar(50),
    exist int default 0
);

create table Emp(
    id SERIAL PRIMARY KEY,
    nom varchar(50),
    prenom varchar(50),
    numero int
);

create table Personne(
    id SERIAL PRIMARY KEY,
    nom varchar(50)
);

insert into Compte(idEntreprise, numero, intitule) values
                    (16, 10100, 'Capital'),
                    (16, 53, 'Caisse');

insert into Emp(nom, prenom, numero) values
                ('John', 'SMITH', 1),
                ('BRID', 'Peter', 2);

insert into Personne(nom) values ('Beby'), ('Pouff');