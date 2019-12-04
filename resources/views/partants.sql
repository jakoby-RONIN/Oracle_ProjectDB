CREATE OR REPLACE VIEW partants AS
    SELECT
        dossards.id_course, dossards.nom AS cheval, personnes.nom AS jockey, dossards.numero
    FROM
        personnes,dossards
    WHERE
        personnes.id_personne = dossards.id_personne;