ajout_mise_ticket(xident varchar, xpwd varchar, xid_ticket integer,xid_course integer, xnumero integer, xposition integer)

CREATE OR REPLACE PROCEDURE ajout_mise_ticket(
  xident IN varchar,
  xpwd IN varchar,
  xid_ticket IN integer,
  xid_course IN integer,
  xnumero IN integer,
  xposition IN integer
) AS
id INTEGER
wrong_user_or_password EXCEPTION
BEGIN
  id := get_id(XIDENT, XPWD);

  IF id != 0 THEN
    INSERT INTO dossards VALUES(xid_course, xnumero, xposition, )
  ELSE
    RAISE wrong_user_or_password;

END;
