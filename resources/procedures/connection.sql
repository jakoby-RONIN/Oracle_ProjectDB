CREATE or replace function connexion(
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
      RETURN 1;
    END IF;
END;
/
GRANT EXECUTE ON connexion TO deniauh1;

