import java.sql.*;
import java.util.Scanner;

public class DemoPMU {

    public static Connection cnx;
    public static Scanner sc;
    public static String login, pwd;

    private static void clearScreen(String titre) {
        System.out.print("\033[H\033[2J");
        System.out.println("EIG-2011 == Démo Mini-projet == " + titre + "\n\n");
    }

    private static void pause() {
        System.out.print("\nPressez ENTREE pour retourner au menu");
        sc.nextLine();
    }

    private static void inscription() throws SQLException {
        Statement st = cnx.createStatement();

        System.out.print("\nVotre nom: ");
        String nom = sc.nextLine();
        System.out.print("Votre prénom: ");
        String prenom = sc.nextLine();
        System.out.print("Identifiant souhaité: ");
        String login = sc.nextLine();
        System.out.print("Mot de passe: ");
        String passwd = sc.nextLine();


        String query = "CALL deniauh1.inscrire('" + nom + "','" + prenom + "','" + login + "','" + passwd + "')";

        try {
            st.execute(query);
        }catch (SQLIntegrityConstraintViolationException e){
            System.err.println("\nDésolé, identifiant déjà utilisé!");
            pause();
            return;
        }
        System.out.println("\n\nVous êtes inscrit\n");



        pause();
    }

    private static boolean connexion() throws SQLException {
        boolean ok = false;
        Statement st = cnx.createStatement();

        System.out.print("Votre identifiant: ");
        login = sc.nextLine();

        System.out.print("Votre mot de passe: ");
        pwd = sc.nextLine();

        ResultSet rset = st.executeQuery("SELECT deniauh1.connexion('" + login + "','" + pwd + "') FROM dual");
        if (rset.next())
            ok = (rset.getInt(1) == 1);
        if (!ok) {
            System.out.println("La connexion a échoué.");
            pause();
        }


        return ok;
    }

    private static void historique() throws SQLException {
        Statement st = cnx.createStatement();
        ResultSet rset = st.executeQuery("SELECT id_ticket,nb_chevaux,desordre,montant,etat FROM TABLE(deniauh1.mes_tickets('" + login + "','" + pwd + "'))");

        System.out.println("\n\nVos tickets:\n");
        System.out.println("num : #chevaux : desordre : montant : etat");
        System.out.println("------------------------------------------");

        while (rset.next())
            System.out.println(rset.getInt("ID_TICKET") + " : " + rset.getInt("NB_CHEVAUX") + " : " + rset.getInt("DESORDRE") + " : " + rset.getDouble("MONTANT") + " : " + rset.getString("ETAT"));

        pause();
    }

    private static void parier() throws SQLException {
        Statement st = cnx.createStatement();

        // liste des courses non courues
        System.out.println("Liste des courses non courues:");
        System.out.println("------------------------------\n\n");

        // La vue non_courues est publique
        //
        ResultSet res = st.executeQuery("SELECT * FROM deniauh1.non_courues");
        while (res.next())
            System.out.println(res.getInt("ID_COURSE") + " " + res.getDate("QUAND") + " " + res.getString("LOCALITE") + " " + res.getString("LIBELLE"));

        System.out.print("\n\nNuméro de course (ou 0 pour abandonner): ");
        int course = Integer.parseInt(sc.nextLine());
        if (course > 0) {
            System.out.println("\n\ndossard : cheval : jockey");
            System.out.println("-----------------------\n\n");

            res = st.executeQuery("SELECT * FROM deniauh1.partants WHERE id_course=" + course);
            while (res.next())
                System.out.println(+res.getInt("NUMERO") + " : " + res.getString("CHEVAL") + " : " + res.getString("JOCKEY"));

            System.out.print("\nVoulez-vous parier (o/n)? ");

            if (sc.nextLine().equals("o")) {
                int desordre, nbchevaux, id_ticket;
                double montant;

                System.out.print("\nNombre de chevaux: ");
                nbchevaux = Integer.parseInt(sc.nextLine());
                System.out.print("Pari dans l'ordre (o/n)? ");
                if (sc.nextLine().equals("o")) desordre = 0;
                else desordre = 1;

                System.out.print("Montant: ");
                montant = Double.parseDouble(sc.nextLine());

                // Créer le ticket en base : creer_ticket() doit renvoyer un numero de
                // ticket > 0 si la création a réussie, 0 dans le cas contraire.
                res = st.executeQuery("SELECT deniauh1.creer_ticket('" + login + "','" + pwd + "'," + nbchevaux + "," + desordre + "," + montant + ") FROM dual");

                // Ajouter les dossards à ce ticket, au fur et à mesure
                // qu'ils sont lus à partir de l'entrée standard
                if (res.next() && (id_ticket = res.getInt(1)) > 0) {
                    System.out.print("Liste de vos dossards, séparés par un espace: ");
                    for (int i = 1; i <= nbchevaux; i++) {
                        int d = sc.nextInt();
                        // ajout du dossard d en position i
                        String query = "CALL deniauh1.ajout_mise_ticket('" + login + "','" + pwd + "'," + id_ticket + "," + course + "," + d + "," + i + ")";
                        System.out.println(query);
                        st.execute(query);

                    }
                    sc.nextLine();
                    System.out.println("\nC'est enregistré!");
                    pause();
                } else
                    System.out.println("La création du ticket a échoué.");
            }
        }
    }


    public static void main(String[] args) throws SQLException {
        int choix = -1;
        boolean connecte = false;

        // Connexion vers Oracle
        cnx = DriverManager.getConnection("jdbc:oracle:thin:@mvx1.esiee.fr:1521:xe", "deniauh2", "Eet6HFkm6nr");
        cnx.setAutoCommit(true);

        // menu principal
        sc = new Scanner(System.in);
        while (choix != 0) {
            // Menu contextuel : dépend si l'utilisateur est connecté ou non
            if (connecte) {
                clearScreen("Bienvenue " + login + " !");
                System.out.println("1) Voir vos tickets (gagnants/perdants/non courus)");
                System.out.println("2) Voir le détail d'une course + parier");
            } else {
                clearScreen("Ecran d'accueil");
                System.out.println("1) vous inscrire");
                System.out.println("2) vous connecter");
            }

            System.out.println("0) quitter");
            System.out.print("\n\nVotre choix: ");

            choix = Integer.parseInt(sc.nextLine());
            if (connecte)
                switch (choix) {
                    case 1:
                        historique();
                        break;
                    case 2:
                        parier();
                        break;
                }
            else
                switch (choix) {
                    case 1:
                        inscription();
                        break;
                    case 2:
                        connecte = connexion();
                        break;
                }
        }
    }
}
