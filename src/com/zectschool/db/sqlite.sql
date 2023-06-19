-- Active: 1671817056927@@127.0.0.1@3306@zectschool
-- ecoleconfigs ok
-- annee scolaire ok
-- eleves ok
-- professeurss ok
-- comptes ok
-- matieres ok
-- classes ok
-- inscriptions
-- comptables ok
-- notes ok
-- evaluations ok
-- cours ok
-- emplois de temps ok
-- absences ok
-- administrateur ok
-- paiemements ok

drop DATABASE if exists zectschool;
CREATE DATABASE zectschool;
use zectschool;

-- Table: ecoleconfigs
CREATE TABLE ecoleconfigs (
    nomAbrevie VARCHAR(100) NOT NULL,
    nomComplet VARCHAR(100) NOT NULL,
    adresse VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    photo VARCHAR(200) NOT NULL
);

-- Table: comptes
CREATE TABLE comptes (
  compteId INTEGER PRIMARY KEY AUTO_INCREMENT,
  password VARCHAR(100) NOT NULL,
  nom VARCHAR(50) NOT NULL,
  prenom VARCHAR(50) NOT NULL,
  adresse VARCHAR(50),
  sexe enum('F', 'M'),
  role enum('ADMIN', 'ELEVE', 'PROFESSEUR', 'COMPTABLE') NOT NULL,
  photo Varchar(256)
);

-- Table: anneescolaires
CREATE TABLE anneescolaires (
    anneescolaireId Integer PRIMARY KEY AUTO_INCREMENT,
    annee VARCHAR(10) NOT NULL,
    active BOOLEAN NOT NULL,
    inscriptionPrix  DECIMAL(10, 2) default 0.00,
    reinscriptionPrix  DECIMAL(10, 2) DEFAULT 0.00
);

-- Table: classes
CREATE TABLE classes (
    classeId Integer PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) UNIQUE,
    montant DECIMAL(10, 2) NOT NULL,
    nombreEleves Integer NOT NULL,
    nombreProfs Integer NOT NULL,
    nombreMatieres Integer NOT NULL
);


CREATE TABLE administrateurs (
    matriculeAdmin VARCHAR(256),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(50),
    compteId Integer NOT NULL,
    Foreign Key (compteId) REFERENCES comptes(compteId)
);

CREATE TABLE comptable (
    matriculeComptable VARCHAR(256),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(50),
    compteId Integer NOT NUll,
    Foreign Key (compteId) REFERENCES comptes(compteId)

);


CREATE TABLE professeurs (
    matriculeProf VARCHAR(256) Primary key,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(50),
    hoairesTotal Integer default 0,
    hoairesNonPayes Integer default 0,
    tauxHoaire  DECIMAL(10, 2),
    compteId Integer not NULL,
    Foreign Key (compteId) REFERENCES comptes(compteId)
);

CREATE TABLE eleves (
  pvEleve VARCHAR(256) PRIMARY KEY,
  phoneParent VARCHAR(20) NOT NULL,
  createdAat TIMESTAMP,
  updatedAat TIMESTAMP,
  totalAPayer DECIMAL(10, 2) DEFAULT 0.00,
  totalPaye DECIMAL(10, 2) DEFAULT 0.00,
  isSolde BOOLEAN DEFAULT false,
  anneescolaireId INTEGER not null,
  classeId integer NOT NULL,
  compteId integer NOT NULL,
  FOREIGN KEY (anneescolaireId) REFERENCES anneescolaires(anneescolaireId) ON DELETE CASCADE,
  FOREIGN KEY (classeId) REFERENCES classes(classeId) ON DELETE CASCADE,
  FOREIGN KEY (compteId) REFERENCES comptes(compteId) ON DELETE CASCADE
);

CREATE TABLE matieres (
    matiereId Integer PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL UNIQUE,
    coefficient Integer NOT NULL,
    moyenne DECIMAL(2,2)
);
CREATE TABLE matiere_classe (
  id Integer PRIMARY KEY AUTO_INCREMENT,
  classeId Integer NOT NULL,
  matiereId Integer NOT NULL,
  Foreign Key (classeId) REFERENCES classes(classeId),
  Foreign Key (matiereId) REFERENCES matieres(matiereId)
);


-- Table: professeurs_matieres
CREATE TABLE professeurs_matieres (
  id Integer PRIMARY KEY AUTO_INCREMENT,
  matriculeProf VARCHAR(256) NOT NULL,
  matiereId Integer NOT NULL,
  Foreign Key (matriculeProf) REFERENCES professeurs(matriculeProf),
  Foreign Key (matiereId) REFERENCES matieres(matiereId)
);

-- Table: professeurs_classes
CREATE TABLE professeurs_classes (
  id Integer PRIMARY KEY AUTO_INCREMENT,
  matriculeProf VARCHAR(256) NOT NULL,
  classeId Integer NOT NULL,
  Foreign Key (matriculeProf) REFERENCES professeurs(matriculeProf),
  Foreign Key (classeId) REFERENCES classes(classeId)
);

-- Table: comptable

-- Table: timetable
CREATE TABLE timetables (
  timetablesId Integer PRIMARY KEY AUTO_INCREMENT,
  isEvaluation BOOLEAN DEFAULT false,
  classeId Integer NOT NULL,
  Foreign Key (classeId) REFERENCES classes(classeId)
);

CREATE TABLE timetable (
  timetableId Integer PRIMARY KEY AUTO_INCREMENT,
  jourSemaine enum('LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI', 'DIMANCHE') NOT NULL,
  heureDebut TIME NOT NULL,
  heureFin TIME NOT NULL,
  matiereId Integer NOT NULL,
  timetablesId Integer NOT NULL,
  Foreign Key (matiereId) REFERENCES matieres(matiereId),
  Foreign Key (timetablesId) REFERENCES timetables(timetablesId)
);

-- Table: timetables

-- Table: cours
CREATE TABLE cours (
    coursId Integer PRIMARY KEY AUTO_INCREMENT,
    dureeCours Integer NOT NULL,
    annule BOOLEAN DEFAULT false,
    jour TIMESTAMP,
    matriculeProf VARCHAR(256) NOT NULL,
    matiereId Integer NOT NULL,
    Foreign Key (matriculeProf) REFERENCES professeurs(matriculeProf),
    Foreign Key (matiereId) REFERENCES matieres(matiereId)
);

-- Table: absences
CREATE TABLE absences (
  absenceId Integer PRIMARY KEY AUTO_INCREMENT,
  pvEleve VARCHAR(256) ,
  justifie BOOLEAN DEFAULT false,
  coursId Integer NOT NULL,
  Foreign Key (coursId) REFERENCES cours(coursId)
);

-- Table: evaluations
CREATE TABLE evaluations (
  evaluationId Integer PRIMARY KEY AUTO_INCREMENT,
  matriculeProf VARCHAR(256) NOT NULL,
  matiereId Integer NOT NULL,
  timetablesId Integer NOT NULL,
  Foreign Key (matriculeProf) REFERENCES professeurs(matriculeProf),
  Foreign Key (matiereId) REFERENCES matieres(matiereId),
  Foreign Key (timetablesId) REFERENCES timetables(timetablesId)
);

-- Table: Note
CREATE TABLE note (
    noteId Integer PRIMARY KEY AUTO_INCREMENT,
    valeur DECIMAL(5, 2) NOT NULL,
    pvEleve VARCHAR(256) ,
    evaluationId Integer NOT NULL,
    Foreign Key (pvEleve) REFERENCES eleves(pvEleve),
    Foreign Key (evaluationId) REFERENCES evaluations(evaluationId)
);

-- Table: administrateurs

create table paiement_professeurs (
    paiementId Integer PRIMARY KEY AUTO_INCREMENT,
    montant DECIMAL(10, 2) NOT NULL,
    datePaiement TIMESTAMP NOT NULL,
    matriculeProf VARCHAR(256) NOT NULL,
    Foreign Key (matriculeProf) REFERENCES professeurs(matriculeProf)
);

create table paiement_eleves (
    paiementId Integer PRIMARY KEY AUTO_INCREMENT,
    montant DECIMAL(10, 2) NOT NULL,
    datePaiement TIMESTAMP NOT NULL,
    pvEleve VARCHAR(256) NOT NULL,
    Foreign Key (pvEleve) REFERENCES eleves(pvEleve)
);

-- trigger pour mettre a jour le nombres des eleves dans la table classe
DROP TRIGGER IF EXISTS update_nombre_eleves;
DELIMITER //

CREATE TRIGGER update_nombre_eleves
AFTER INSERT ON eleves
FOR EACH ROW
BEGIN
    UPDATE classes
    SET nombreEleves = nombreEleves + 1
    WHERE classeId = NEW.classeId;
END//

DELIMITER ;

DROP TRIGGER IF EXISTS delete_nombre_eleves;
DELIMITER //

CREATE TRIGGER delete_nombre_eleves
AFTER DELETE ON eleves
FOR EACH ROW
BEGIN
    UPDATE classes
    SET nombreEleves = nombreEleves - 1
    WHERE classeId = OLD.classeId;
END//

DELIMITER ;

-- trigger pour mettre a jour le nombres des profs dans la table classe

DROP TRIGGER IF EXISTS update_nombre_profs;
DELIMITER //

CREATE TRIGGER update_nombre_profs
AFTER INSERT ON professeurs
FOR EACH ROW
BEGIN
    UPDATE classes
    SET nombreProfs = nombreProfs + 1
    WHERE classeId IN (
        SELECT classeId
        FROM professeurs_classes
        WHERE matriculeProf = NEW.matriculeProf
    );
END//

DELIMITER ;

DROP TRIGGER IF EXISTS delete_nombre_profs;
DELIMITER //

CREATE TRIGGER delete_nombre_profs
AFTER DELETE ON professeurs
FOR EACH ROW
BEGIN
    UPDATE classes
    SET nombreProfs = nombreProfs - 1
    WHERE classeId IN (
        SELECT classeId
        FROM professeurs_classes
        WHERE matriculeProf = OLD.matriculeProf
    );
END//

DELIMITER ;

-- trigger pour mettre a jour le nombres des matieres dans la table classe

DROP TRIGGER IF EXISTS update_nombre_matieres;
DELIMITER //

CREATE TRIGGER update_nombre_matieres
AFTER INSERT ON matiere_classe
FOR EACH ROW
BEGIN
    UPDATE classes
    SET nombreMatieres = nombreMatieres + 1
    WHERE classeId = NEW.classeId;
END//

DELIMITER ;

DROP TRIGGER IF EXISTS delete_nombre_matieres;
DELIMITER //

CREATE TRIGGER delete_nombre_matieres
AFTER DELETE ON matiere_classe
FOR EACH ROW
BEGIN
    UPDATE classes
    SET nombreMatieres = nombreMatieres - 1
    WHERE classeId = OLD.classeId;
END//
DELIMITER ;

