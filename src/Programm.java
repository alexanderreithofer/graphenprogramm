import java.util.ArrayList;

public class Programm {

    String datei;

    public Programm() {
        datei = "/Users/alexanderreithofer/IdeaProjects/Graphenprogramm/src/matrix.csv";
    }

    public void start() {
        CSVReader csvReader = new CSVReader(datei);
        Matrix m = new Matrix(csvReader.lesen());
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        try {

            if(m.exzentritaet(0) == -1)
                System.out.println("Graph nicht zusammenhängend!");
            else {
                System.out.print("Exzentrizitäten: ");
                for(int i = 0; i<m.getRows(); i++) {
                    String ex = m.exzentritaet(i) != -1 ? String.valueOf(m.exzentritaet(i)) : "∞";
                    System.out.print(alpha.charAt(i) + ": " + ex + "; ");
                }

                System.out.print("\nRadius/Durchmesser/Zentrum: ");
                System.out.print("rad(G)=" + (m.radius() != -1 ? m.radius() : "∞") + ", ");
                System.out.print("dm(G)=" + (m.durchmesser() != -1 ? m.durchmesser() : "∞") + ", ");
                System.out.println("(G)=" + m.zentrumString());
            }

            System.out.println();

            ArrayList<ArrayList<Integer>> komponenten = m.zusammenhang();

            for (ArrayList<Integer> komponente : komponenten) {
                System.out.print("Komponenten: ");
                ArrayList<String> temp = new ArrayList<>();
                for(int knoten : komponente) {
                    temp.add(String.valueOf(alpha.charAt(knoten)));
                }
                System.out.println("[" + String.join(", ", temp) + "]");
            }

            ArrayList<Integer> art = m.artikulationen();
            System.out.print("Artikulationen: ");
            ArrayList<String> artikultationen = new ArrayList<>();
            for(int knoten : art) {
                artikultationen.add(String.valueOf(alpha.charAt(knoten)));
            }
            System.out.println("[" + String.join(", ", artikultationen) + "]");


            ArrayList<int[]> brucken = m.brucken();
            System.out.println("Brücken: ");
            for(int[] brucke : brucken) {
                System.out.println("[" + alpha.charAt(brucke[0]) + "," + alpha.charAt(brucke[1]) + "]");
            }




            System.out.println("\nDistanzmatrix: ");
            m.distanzMatrix().anzeigen();


            System.out.println("\nA:");
            m.anzeigen();

            System.out.println("\nA^2:");
            m.hoch(2).anzeigen();

            System.out.println("\nA^3:");
            m.hoch(3).anzeigen();

            System.out.println("\nA^4:");
            m.hoch(4).anzeigen();

            System.out.println("\nA^5:");
            m.hoch(5).anzeigen();

            System.out.println("\nA^6:");
            m.hoch(6).anzeigen();

            System.out.println();

            System.out.println("Eulersche Linie / Euler-Zyklus: ");
            ArrayList<int[]> euLinie = m.eulerscheLinie();
            if(euLinie != null)
                for(int[] kanten : euLinie)
                    System.out.println("[" + alpha.charAt(kanten[0]) + " " + alpha.charAt(kanten[1]) + "]");
                    // System.out.println("[" + (kanten[0] + 1) + " " + (kanten[1] + 1) + "]");
            else
                System.out.println("Keine Eulersche Linie");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
