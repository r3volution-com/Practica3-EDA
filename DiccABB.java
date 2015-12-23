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
    private int count;

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
        if (p != null && p.getOrigen() != null && !p.getOrigen().equalsIgnoreCase("") && p.getLenguas().length == lenguas.size()) {
            char[] len = p.getLenguas();
            boolean existe = false;
            NodoABB nodo = dicc;
            NodoABB n_anterior = null;
            NodoABB n_auxiliar = new NodoABB(p);
            int posicion = 0;
            for (int i = 0; i < len.length && i < lenguas.size(); i++) {
                if (len[i] != lenguas.get(i)) return false;
            }
            if (dicc == null){
                dicc = n_auxiliar;
                return true;
            }
            while (!existe && nodo != null){
                posicion = p.getOrigen().compareToIgnoreCase(nodo.getPalabra2().getOrigen());
                if (posicion == 0) existe = true;
                else if (posicion > 0) {
                    n_anterior = nodo;
                    nodo = nodo.getHijoDe();
                } else if (posicion < 0){
                    n_anterior = nodo;
                    nodo = nodo.getHijoIz();
                }
            }
            if (existe){
                boolean cambio = false;
                for (int i = 0; i< lenguas.size(); i++){
                    if (p.getTraduccion(lenguas.get(i)) != null && !p.getTraduccion(lenguas.get(i)).equals("")
                            && nodo.getPalabra2().setTrad(p.getTraduccion(lenguas.get(i)), lenguas.get(i)) != -1) cambio = true;
                }
                return cambio;
            } else {
                if (posicion < 0) n_anterior.cambiaHijoIz(n_auxiliar);
                else if(posicion > 0) n_anterior.cambiaHijoDe(n_auxiliar);
                return true;
            }
        }
        return false;
    }

    public boolean borra(String s) {

        return false;
    }

    public int busca(String s) {
        int count = 0, posicion;
        boolean existe = false;
        NodoABB nodo=dicc;
        if (s != null && nodo.getPalabra2() != null && nodo.getPalabra2().getOrigen() != null){
            for (int i = 0; nodo!= null && !existe; i++){
                posicion = s.compareToIgnoreCase(nodo.getPalabra2().getOrigen());
                if (posicion == 0) existe = true;
                else if (posicion > 0) nodo = nodo.getHijoDe();
                else if (posicion < 0) nodo = nodo.getHijoIz();
                count = i+1;
            }
            if (existe) return count;
            else return count * -1;
        }
        return 0;
    }

    public String traduce(String s, char l) {
        int posicion;
        NodoABB nodo=dicc;
        if (s != null && nodo.getPalabra2() != null && nodo.getPalabra2().getOrigen() != null){
            while (nodo!= null){
                posicion = s.compareToIgnoreCase(nodo.getPalabra2().getOrigen());
                if (posicion == 0) return nodo.getPalabra2().getTraduccion(l);
                else if (posicion > 0) nodo = nodo.getHijoDe();
                else if (posicion < 0) nodo = nodo.getHijoIz();
            }
        }
        return null;
    }

    public void visualizaRec(NodoABB nodo){
        if(nodo != null){
            visualizaRec(nodo.getHijoIz());
            nodo.getPalabra2().escribeInfo();
            visualizaRec(nodo.getHijoDe());
        }
    }
    public void visualiza() {
        visualizaRec(dicc);
    }

    private void visualizaAux(NodoABB nodo, int j){
        if(nodo != null){
            visualizaAux(nodo.getHijoIz(), j);
            if(count < j){
                nodo.getPalabra2().escribeInfo();
                count++;
            }
            visualizaAux(nodo.getHijoDe(), j);
        }
    }

    public void visualiza(int j) {
        count = 0;
        visualizaAux(dicc, j);

    }

    private void visualiza2Aux(NodoABB nodo, int j, char l){
        if(nodo != null){
            visualizaAux(nodo.getHijoIz(), j);
            if(count < j){
                nodo.getPalabra2().escribeInfo(l);
                count++;
            }
            visualizaAux(nodo.getHijoDe(), j);
        }
    }
    
    public void visualiza(int j, char l) {
        count = 0;
        visualiza2Aux(dicc, j, l);
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
