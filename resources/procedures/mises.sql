
CREATE OR REPLACE PROCEDURE ajout_mise_ticket(
  xident IN varchar,
  xpwd IN varchar,
  xid_ticket IN integer,
  xid_course IN integer,
  xnumero IN integer,
  xposition IN integer
) AS
PRAGMA AUTONOMOUS_TRANSACTION;
id INTEGER;
wrong_user_or_password EXCEPTION;

BEGIN
  id := get_id(XIDENT, XPWD);

  IF (id != 0) THEN
    INSERT INTO mise VALUES(xid_course, xnumero, xid_ticket, xposition);
    COMMIT;
  ELSE
    RAISE wrong_user_or_password;
  END IF;
END;
