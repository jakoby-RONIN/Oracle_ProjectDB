import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection cnx;
        Statement stat;

        // Se connecter au serveur : il peut y avoir plusieurs connexions indépendantes
        // dans une même application
        // Connexion AUTOCOMMIT : appelle COMMIT après chaque executeQuery()
        cnx = DriverManager.getConnection("jdbc:oracle:thin:@mvx1.esiee.fr:1521:xe", "deniauh", "Eet6HFkm6nr");
        cnx.setAutoCommit(true);

        // Créer un Statement pour cette connexion. Il peut y en avoir plusieurs par connexion.
        stat = cnx.createStatement();


        // Demo 1 : créer la table passwords.
        // Provoquera une erreur dès la 2ème exécution du programme.
        System.out.println("Création de la table des mots de passe.");

        try {
            stat.executeQuery("CREATE TABLE passwords(login VARCHAR(10) PRIMARY KEY, pass VARCHAR(30) NOT NULL, echecs INTEGER NOT NULL, validite DATE)");
            stat.executeQuery("INSERT INTO passwords VALUES('durand', 'azerty', 0, to_date('31/12/2015', 'DD/MM/YYYY'))");
            stat.executeQuery("INSERT INTO passwords VALUES('dupont', 'toto1234', 3, NULL)");
        } catch (SQLException ex) {
        }  // ignorer les erreurs

        try {
            // Demo 2 : afficher la table passwords
            ResultSet rset = stat.executeQuery("SELECT * FROM passwords");
            String lastLogin = "(vide)", lastPass = "(vide)";

            System.out.println("Table des mots de passe:");
            while (rset.next()) {
                lastLogin = rset.getString(1);
                lastPass = rset.getString("PASS");
                System.out.println(lastLogin + " " + lastPass + " " + rset.getInt("ECHECS") + " " + rset.getDate(4));
            }

            // Demo 3 : procédures stockées
            // Inverser le mot de passe du dernier login lu via un appel chpass()
            String inverse = new StringBuffer(lastPass).reverse().toString();
            //String query = "CALL chpass('" + lastLogin + "', '" + lastPass + "', '" + inverse + "')";
            String query = "CALL chpass('dupont', 'tata1', 'tata2')";
            System.out.println("Je lance: " + query);
            stat.executeQuery(query);
        }

        // Traitement des erreurs
        catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erreur Oracle " + ex.getErrorCode() + " : " + ex.getMessage());
            System.exit(1);
        }

        stat.close();
    }
}
