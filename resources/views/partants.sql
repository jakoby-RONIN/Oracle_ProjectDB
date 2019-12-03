CREATE VIEW partants AS
SELECT dossards.id_course, dossards.nom AS cheval, personnes.nom AS jockey, dossard.numero FROM personnes,jockeys,cheveau,courses
WHERE dossards.id_course = courses.id_course AND personnes.id_personne = dossards.id_personne;