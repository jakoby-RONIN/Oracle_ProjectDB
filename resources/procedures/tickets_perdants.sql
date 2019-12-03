CREATE VIEW tickets_perdants AS
SELECT tickets.* FROM tickets,d WHERE d.arrivee IS NOT NULL MINUS (SELECT * FROM tickets_gagnants);

