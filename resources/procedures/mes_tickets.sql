create or replace NONEDITIONABLE FUNCTION mes_tickets(
  xident VARCHAR,
  xpwd VARCHAR
)
RETURN ticket_table AS
ret ticket_table;
myid INTEGER;
e_ident_invalide EXCEPTION;
BEGIN
    myid := get_id(xident,xpwd);
    IF (myid > 0) THEN
        SELECT
        cast(multiset( SELECT * FROM
        ((select tickets_gagnants.*,'gagnant' as etat from tickets_gagnants) union
        (select tickets_perdants.*,'perdant' as etat from tickets_perdants) union
        (select distinct id_ticket,nb_chevaux,desordre,montant,id_personne,'non couru'
        from tickets natural join mise natural join courses where terminee=0)) where
        id_personne=myid) AS ticket_table)
        INTO ret FROM dual;
    ELSE
        raise e_ident_invalide;
    END IF;
return ret;
END;