CREATE VIEW m AS SELECT mise.* FROM mise,tickets WHERE mise.id_ticket = tickets.id_ticket;
CREATE VIEW d AS SELECT dossard.* FROM dossards,mise WHERE dossards.id_course=mise.id_course;

CREATE VIEW tickets_gagnants AS
SELECT * FROM tickets WHERE
    (desordre=0 and (SELECT count(*) FROM m,d WHERE m.numero!=d.numero and m.position!=d.arrivee) = 0)
     or
    (desordre=1 and (SELECT count(*) FROM m,d WHERE m.numero!=d.numero or m.position!=d.arrivee) = 0);



--
(SELECT count(*) FROM
(SELECT * FROM mise,tickets WHERE mise.id_ticket = tickets.id_ticket AS m),
(SELECT * FROM dossards,mise WHERE dossard.id_course=mise.id_course AS d)
WHERE
m.numero!=d.numero or m.position!=d.arrivee) = 0);

-- mises d'un ticket
SELECT * FROM mise,tickets WHERE mise.id_ticket = tickets.id_ticket

-- dossard relatif a la meme course que le ticket
SELECT * FROM dossards,mise WHERE dossard.id_course=mise.id_course

