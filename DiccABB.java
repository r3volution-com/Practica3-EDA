//DNI 77842527Q GONZALEZ ALVARADO, MARIO
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DiccABB implements Diccionario {
    private int nlenguas;
    private ArrayList<Character> lenguas;
    private NodoABB dicc;

    public DiccABB(){
        nlenguas = -1;
        lenguas = new ArrayList<>();
        dicc = null;
    }

    public void leeDiccionario(String f) {
        if (f != null) {
            FileReader fr;
            BufferedReader br;
            String linea;
            String[] aux, aux2;
            char[] leng;
            Palabra2 palabra;
            try {
                fr = new FileReader(f);
                br = new BufferedReader(fr);
                linea = br.readLine();
                nlenguas = Integer.parseInt(linea);
                leng = new char[nlenguas];
                linea = br.readLine();
                aux = linea.split(" ");
                for (int i = 0; i < aux.length; i++){
                    lenguas.add(aux[i].charAt(0));
                    leng[i] = aux[i].charAt(0);
                }
                linea = br.readLine();
                while (linea != null) {
                    aux2 = linea.split("[ ]*\\*[ ]*");
                    palabra = new Palabra2(aux2[0],leng);
                    for (int i = 1; i < aux2.length; i++){
                        if (aux2[i] != null && !aux2[i].equals("") && !aux2[i].equals(" ")) {
                            palabra.setTrad(aux2[i], leng[i - 1]);
                        }
                    }
                    inserta(palabra);
                    linea = br.readLine();
                }
                br.close();
                fr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean inserta(Palabra2 p) {

        return false;
    }

    public boolean borra(String s) {
        return false;
    }

    public int busca(String s) {
        return 0;
    }

    public String traduce(String s, char l) {
        return null;
    }

    public void visualiza() {

    }

    public void visualiza(int j) {

    }

    public void visualiza(int j, char l) {

    }

    public void preordenABB(){

    }

    public void nivelesABB(){

    }

    public String anterior(String s){

        return null;
    }

    public void camino(String s){

    }

    public boolean equilibrado(){

        return false;
    }

    private class NodoABB {
        private Palabra2 pal;
        private NodoABB hiz;
        private NodoABB hde;
        public NodoABB(Palabra2 p){
            pal = p;
            hiz = null;
            hde = null;
        }
        public void cambiaHijoIz(NodoABB n){
            hiz = n;
        }
        public void cambiaHijoDe(NodoABB n){
            hde = n;
        }
        public void setPalabra2(Palabra2 p){
            pal = p;
        }
        public NodoABB getHijoIz(){
            return hiz;
        }
        public NodoABB getHijoDe(){
            return hde;
        }
        public Palabra2 getPalabra2(){
            return pal;
        }
    }
}
