create or replace NONEDITIONABLE function creer_ticket(
  xident IN varchar,
  xpwd IN varchar,
  xnb_chevaux IN integer,
  xdesordre IN integer,
  xmontant IN numeric
) return integer

AS
id INTEGER;
ticket INTEGER;
BEGIN
  id := get_id(XIDENT, XPWD);

  IF id != 0 THEN
    SELECT MAX(id_ticket)+1 INTO ticket FROM tickets;
    INSERT INTO tickets VALUES (ticket, xnb_chevaux, xdesordre, xmontant, id);
    return id;
  ELSE
    return 0;
  END IF;
  END;
