import java.util.Scanner;

public class Restaurant {

    // Codes ANSI pour les couleurs
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {

        Menu menu = new Menu();
        Commande commande = new Commande();
        Stock stock = new Stock();
        Scanner scanner = new Scanner(System.in);

        afficherLogo();

        while (true) {
            afficherMenu();

            System.out.print(ANSI_YELLOW + "➤ Choisissez une option : " + ANSI_RESET);
            int choix;
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "⛝ Entrée invalide. Veuillez entrer un nombre." + ANSI_RESET);
                continue;
            }

            switch (choix) {
                case 1 -> {
                    System.out.println(ANSI_GREEN + "\n⚜ Menu disponible :" + ANSI_RESET);
                    menu.getPlats().forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("🍽 Entrez le nom du plat : ");
                    String nomPlat = scanner.nextLine();
                    Plat plat = menu.getPlatParNom(nomPlat);
                    if (plat != null && stock.verifierStock(plat.getId())) {
                        System.out.print("Quantité : ");
                        int quantite = scanner.nextInt();
                        scanner.nextLine();
                        commande.ajouterPlat(plat, quantite);
                        stock.mettreAJourStock(plat.getId());
                        commande.enregistrerCommande();
                        commande.enregistrerDansHistorique(plat.getId(), plat.getNom(), plat.getPrix(), quantite);
                        System.out.println(ANSI_GREEN + "√ Commande enregistrée avec succès !" + ANSI_RESET);
                    } else {
                        System.out.println(ANSI_RED + "✘ Stock insuffisant ou plat introuvable." + ANSI_RESET);
                    }
                }
                case 3 -> commande.afficherCommande();
                case 4 -> stock.afficherEtModifierStock(scanner);
                case 5 -> commande.afficherHistorique();
                case 6 -> {
                    System.out.println(ANSI_CYAN + "\n☻ Merci d’avoir utilisé notre système de gestion de restaurant !");
                    System.out.println("À bientôt et bon appétit !" + ANSI_RESET);
                    pause(1000);
                    clearConsole();
                    return;
                }
                default -> System.out.println(ANSI_RED + "✘ Option invalide. Veuillez réessayer." + ANSI_RESET);
            }

            System.out.println(); // Ligne vide après chaque action
        }
    }

    public static void afficherLogo() {
        System.out.println(ANSI_BLUE +
                "╔══════════════════════════════════╗\n" +
                "║    ↠     LA Bella TAVOLA         ║\n" +
                "╚══════════════════════════════════╝\n" + ANSI_RESET);
    }

    public static void afficherMenu() {
        System.out.println(ANSI_CYAN +
                "╔══════════════════════════════════╗\n" +
                "║         MENU PRINCIPAL           ║\n" +
                "╠══════════════════════════════════╣\n" +
                "║ 1. Afficher le menu              ║\n" +
                "║ 2. Passer une commande           ║\n" +
                "║ 3. Afficher la commande          ║\n" +
                "║ 4. Afficher / Modifier le stock  ║\n" +
                "║ 5. Historique des commandes      ║\n" +
                "║ 6. Quitter                       ║\n" +
                "╚══════════════════════════════════╝" + ANSI_RESET);
    }

    public static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
