CREATE or replace function get_id(
  XIDENT IN VARCHAR,
  XPWD IN VARCHAR
) RETURN INTEGER
AS

PRAGMA AUTONOMOUS_TRANSACTION;
l INTEGER;
CURSOR cligne(usr VARCHAR, pass VARCHAR) IS SELECT id_personne FROM personnes WHERE IDENT=usr and PASSWD=pass;

BEGIN
    OPEN cligne(XIDENT, XPWD);
    FETCH cligne INTO l;

    IF cligne%NOTFOUND THEN
      RETURN 0;
    ELSE
      RETURN l;
    END IF;
END;
/



-----------------------------------------------------------------------------------------------------

CREATE or replace function creer_ticket(
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
    INSERT INTO ticket (ticket, xnb_chevaux, xdesordre, xmontant, id);
    return id;
  ELSE
    return 0;
  END IF;
  END;
/
GRANT EXECUTE ON creer_ticket TO deniauh1;

