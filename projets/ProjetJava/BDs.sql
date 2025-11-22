create database miniprojet;
create table utilisateurs(
    id int auto_increment primary key,
    nom varchar(50) not null,
    email varchar(50) unique not null,
    mdp varchar(10)
);
-- Table Formations
create table formations (
    id int auto_increment primary key,
    titre varchar(20) not null,
    description varchar(100),
    prix double ,
    formateur_id int not null,
    foreign key (formateur_id) references utilisateurs(id) on delete cascade 
);

-- Table Inscriptions (relation entre Étudiants et Formations)
create table inscriptions (
    id int auto_increment primary key,
    etudiant_id int not null,
    formation_id int not null,
    foreign key (etudiant_id) references utilisateurs(id) on delete cascade on update cascade,
    foreign key (formation_id) references formations(id) on delete cascade on update cascade,
    unique (etudiant_id, formation_id) -- Pour éviter les doublons
);
insert into utilisateurs values(3,'salah','salhah@gmail.com',"mdpsalah");
insert into utilisateurs values(4,'asma','asma@gmail.com',"asmamdp");
insert into utilisateurs values(5,'ammar','ammar@gmail.com',"mdpammar");
insert into utilisateurs values(6,'sirin','sirin@gmail.com',"mdpsirin");
insert into formations values(100,'java','orienté objet',2000.0,3);
insert into formations values(101,'developpment','web et mobile',3000.0,4);
select * from utilisateurs;
select * from formations;
select * from inscriptions;

