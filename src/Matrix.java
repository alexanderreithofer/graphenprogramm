import java.util.*;

public class Matrix {

    private int[][] m;


    public Matrix() {
        m = new int[3][3];
        m[0][0] = 3;
        m[0][1] = 3;
        m[0][2] = 3;
        m[1][0] = 3;
        m[1][1] = 3;
        m[1][2] = 3;
        m[2][0] = 3;
        m[2][1] = 3;
        m[2][2] = 3;
    }

    public Matrix(int rows, int columns) {
        m = new int[rows][columns];
    }

    public Matrix(int[][] m) {
        setM(m);
    }

    public void setM(int[][] m) {
        this.m = m;
    }

    public int[][] getM() {
        return this.m;
    }

    public int getRows() {
        return this.m.length;
    }

    public int getColumns() {
        return this.m[0].length;
    }

    public Matrix multipliziere(Matrix that) throws GraphenprogrammException {
        if (this.getRows() != that.getColumns()) {
            throw new GraphenprogrammException("Error: Matrix cannot be multiplied.");
        }

        int rows = this.getRows();
        int columns = that.getColumns();

        Matrix newM = new Matrix(rows, columns);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                for(int k = 0; k < this.getColumns(); k++) {
                    newM.m[i][j] += this.m[i][k] * that.m[k][j];
                }
            }
        }

        return newM;
    }

    public Matrix hoch(int n) throws GraphenprogrammException {
        int rows = this.getRows();
        int columns = this.getColumns();

        if(rows != columns) {
            System.out.println("Error: Rows and columns must have the same length.");
            return null;
        }

        Matrix newM = this.clone();

        for(int i = 1; i < n; i++) {
            newM = newM.multipliziere(this);
        }

        return newM;
    }

    public Matrix distanzMatrix() throws GraphenprogrammException {
        Matrix disMatrix = this.cloneSize();

        for(int i = 0; i < disMatrix.getRows(); i++)
            for(int j = 0; j < disMatrix.getColumns(); j++)
                if(i != j)
                    disMatrix.m[i][j] = -1;

        int hochzahl = 1;


        while(disMatrix.contains(-1) && hochzahl <= disMatrix.getRows()) {

            Matrix tempM = this.hoch(hochzahl);

            for(int i = 0; i < disMatrix.getRows(); i++)
                for(int j = 0; j < disMatrix.getColumns(); j++)
                    if(disMatrix.getM()[i][j] == -1 && tempM.getM()[i][j] != 0)
                        disMatrix.m[i][j] = hochzahl;

            hochzahl++;
        }
        return disMatrix;
    }

    public boolean contains(int n) {
        for(int i = 0; i < this.getRows(); i++)
            for(int j = 0; j < this.getColumns(); j++)
                if(this.getM()[i][j] == n)
                    return true;
        return false;
    }

    public int exzentritaet(int i) throws GraphenprogrammException{
        if(i >= 0 && i < this.getRows()) {
            Matrix newM = this.distanzMatrix();
            int max = -1;

            for(int y : newM.m[i]) {
                if(y == -1)
                    return -1;
                if(y > max)
                    max = y;
            }

            return max;
        } else {
            throw new GraphenprogrammException("Fehler exzentrizitaet: Falscher Index");
        }
    }

    // funktioniert
    public int radius() throws GraphenprogrammException {
        int rad = this.exzentritaet(0);

        for(int i = 1; i < this.getRows(); i++) {
            int temp = this.exzentritaet(i);
            if(rad > temp)
                rad = temp;
        }


        return rad;
    }

    // noch anschauen
    public int durchmesser() throws GraphenprogrammException {
        int dur = this.exzentritaet(0);

        for(int i = 1; i < this.getRows(); i++) {
            int temp = this.exzentritaet(i);
            if(dur < temp)
                dur = temp;
        }

        return dur;
    }

    public char[] zentrum() throws GraphenprogrammException {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<Integer> liste = new ArrayList<>();
        int rad = this.radius();

        for(int i = 0; i < this.getRows(); i++)
            if(exzentritaet(i) == rad)
                liste.add(i);

        char[] zen = new char[liste.size()];

        for(int i = 0; i<zen.length; i++) {
            zen[i] = alpha.charAt(liste.get(i));
        }

        return zen;
    }

    public String zentrumString() throws GraphenprogrammException {
        String s = "";
        char[] zen = this.zentrum();
        s+="{" + zen[0];
        for(int i = 1; i<zen.length; i++)
            s+=", " + zen[i];

        s+="}";

        return s;
    }




    public ArrayList<ArrayList<Integer>> zusammenhang() {
        int knoten = getRows();
        boolean[] besucht = new boolean[knoten];
        ArrayList<ArrayList<Integer>> komponenten = new ArrayList<>();

        for (int i = 0; i < knoten; i++) {
            if (!besucht[i]) {
                komponenten.add(bfs(i, besucht));
            }
        }

        return komponenten;
    }

    private ArrayList<Integer> bfs(int start, boolean[] besucht) {
        int knoten = getRows();
        Queue<Integer> queue = new LinkedList<>();
        ArrayList<Integer> komponente = new ArrayList<>();

        queue.add(start);
        besucht[start] = true;

        while(!queue.isEmpty()) {
            int temp = queue.remove();
            komponente.add(temp);

            for (int i = 0; i < knoten; i++) {
                if (m[temp][i] == 1 && !besucht[i]) {
                    queue.add(i);
                    besucht[i] = true;
                }
            }
        }

        return komponente;
    }

    public ArrayList<Integer> artikulationen() {
        ArrayList<Integer> art = new ArrayList<>();
        int knoten = getRows();
        int komponenten = zusammenhang().size();

        for(int i = 0; i < knoten; i++) {

            Matrix tempM = new Matrix(knoten-1, knoten-1);

            for(int j = 0; j < knoten; j++) {
                for (int k = 0; k < knoten; k++) {
                    if(j != i && k != i) {
                        if(j > i && k > i) {
                            tempM.m[j-1][k-1] = this.getM()[j][k];
                        } else if (j > i) {
                            tempM.m[j-1][k] = this.getM()[j][k];
                        } else if (k > i) {
                            tempM.m[j][k-1] = this.getM()[j][k];
                        } else {
                            tempM.m[j][k] = this.getM()[j][k];
                        }
                    }
                }
            }
            if(tempM.zusammenhang().size() > komponenten) {
                art.add(i);
            }
        }


        return art;
    }

    public ArrayList<int[]> brucken() {
        ArrayList<int[]> bru = new ArrayList<>();
        int knoten = getRows();
        int komponenten = zusammenhang().size();

        for(int i = 0; i < knoten; i++) {
            for(int j = i; j < knoten; j++) {
                if(i != j) {
                    Matrix temp = clone();
                    temp.m[i][j] = 0;
                    temp.m[j][i] = 0;
                    if (temp.zusammenhang().size() != komponenten)
                        bru.add(new int[]{i, j});
                }
            }
        }

        return bru;
    }

    

    public ArrayList<int[]> eulerscheLinie() {
        int knoten = getRows();

        for(int start = 0; start < knoten; start++) {
            ArrayList<int[]> euLinie = new ArrayList<>();
            int[][] besucht = new int[knoten][knoten];
            euLinie = rekrusiveDFS(start, besucht, euLinie);
            Collections.reverse(euLinie);
            boolean passt = true;

            for(int i = 1; i < euLinie.size(); i++)
                if (euLinie.get(i - 1)[1] != euLinie.get(i)[0]) {
                    passt = false;
                    break;
                }

            if(passt)
                return euLinie;
        }

        return null;
    }

    public ArrayList<int[]> rekrusiveDFS(int start, int[][] besucht, ArrayList<int[]> euLinie) {
        for(int i = 0; i < getRows(); i++) {
            if(m[start][i] == 1 && besucht[start][i] == 0) {
                besucht[start][i] = 1;
                besucht[i][start] = 1;
                rekrusiveDFS(i, besucht, euLinie);
                euLinie.add(new int[]{start, i});
            }
        }
        return euLinie;
    }



    public Matrix clone() {
        Matrix newM = new Matrix(this.getRows(), this.getColumns());

        for(int i = 0; i < this.getRows(); i++) {
            for(int j = 0; j < this.getColumns(); j++) {
                newM.m[i][j] = this.getM()[i][j];
            }
        }

        return newM;
    }

    public Matrix cloneSize() {
        Matrix newM = new Matrix(this.getRows(), this.getColumns());

        return newM;
    }


    public void anzeigen() {
        for(int i = 0; i < this.getRows(); i++){
            for(int j = 0; j < this.getColumns(); j++) {
                if(m[i][j] == -1)
                    System.out.print("∞ ");
                else
                    System.out.print(m[i][j] + " ");
            }

            System.out.println();
        }
    }

}
