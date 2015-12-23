//DNI 77842527Q GONZALEZ ALVARADO, MARIO
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DiccABB implements Diccionario {
    private int nlenguas;
    private ArrayList<Character> lenguas;
    private NodoABB dicc;
    private int count;
    //Inicializamos a los valores por defecto
    public DiccABB() {
        nlenguas = -1;
        lenguas = new ArrayList<>();
        dicc = null;
    }
    //Lee
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
                for (int i = 0; i < aux.length; i++) {
                    lenguas.add(aux[i].charAt(0));
                    leng[i] = aux[i].charAt(0);
                }
                linea = br.readLine();
                while (linea != null) {
                    aux2 = linea.split("[ ]*\\*[ ]*");
                    if (aux2.length > 0) {
                        palabra = new Palabra2(aux2[0], leng);
                        for (int i = 1; i < aux2.length; i++) {
                            if (aux2[i] != null && !aux2[i].equals("") && !aux2[i].equals(" ")) {
                                palabra.setTrad(aux2[i], leng[i - 1]);
                            }
                        }
                        inserta(palabra);
                    }
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
            if (dicc == null) {
                dicc = n_auxiliar;
                return true;
            }
            while (!existe && nodo != null) {
                posicion = p.getOrigen().compareToIgnoreCase(nodo.getPalabra2().getOrigen());
                if (posicion == 0) existe = true;
                else if (posicion > 0) {
                    n_anterior = nodo;
                    nodo = nodo.getHijoDe();
                } else if (posicion < 0) {
                    n_anterior = nodo;
                    nodo = nodo.getHijoIz();
                }
            }
            if (existe) {
                boolean cambio = false;
                for (int i = 0; i< lenguas.size(); i++) {
                    if (p.getTraduccion(lenguas.get(i)) != null && !p.getTraduccion(lenguas.get(i)).equals("")
                            && nodo.getPalabra2().setTrad(p.getTraduccion(lenguas.get(i)), lenguas.get(i)) != -1) cambio = true;
                }
                return cambio;
            } else {
                if (posicion < 0) n_anterior.cambiaHijoIz(n_auxiliar);
                else if (posicion > 0) n_anterior.cambiaHijoDe(n_auxiliar);
                return true;
            }
        }
        return false;
    }

    public boolean borra(String s) {
        if (s != null) {
            boolean deleted = false;
            NodoABB nodoanterior, nodohijo, nodomayor;
            Palabra2 mayorIzquierda;
            int posicion;
            nodoanterior = null;
            nodohijo = dicc;
            while (nodohijo != null && !deleted) {
                posicion = s.compareToIgnoreCase(nodohijo.getPalabra2().getOrigen());
                if (posicion < 0) {
                    nodoanterior = nodohijo;
                    nodohijo = nodohijo.getHijoIz();
                } else if (posicion > 0) {
                    nodoanterior = nodohijo;
                    nodohijo = nodohijo.getHijoDe();
                } else deleted = true;
            }
            if (deleted) { 
                if (nodohijo.getHijoIz() == null && nodohijo.getHijoDe() == null) {
                     if (nodoanterior == null) dicc = null;
                     else {
                        posicion = s.compareToIgnoreCase(nodoanterior.getPalabra2().getOrigen());
                        if (posicion < 0) nodoanterior.cambiaHijoIz(null);
                        else nodoanterior.cambiaHijoDe(null);
                     }
                } else {
                    if (nodohijo.getHijoIz() != null && nodohijo.getHijoDe() == null) {
                        if (nodoanterior == null)  dicc = nodohijo.getHijoIz();
                        else {
                            posicion = s.compareToIgnoreCase(nodoanterior.getPalabra2().getOrigen());
                            if (posicion < 0) nodoanterior.cambiaHijoIz(nodohijo.getHijoIz());
                            else nodoanterior.cambiaHijoDe(nodohijo.getHijoIz());
                        }
                    } else {
                        if (nodohijo.getHijoDe() != null && nodohijo.getHijoIz() == null) {
                            if (nodoanterior == null) dicc = nodohijo.getHijoDe();
                            else {
                                posicion = s.compareToIgnoreCase(nodoanterior.getPalabra2().getOrigen());
                                if (posicion < 0) nodoanterior.cambiaHijoIz(nodohijo.getHijoDe());
                                else nodoanterior.cambiaHijoDe(nodohijo.getHijoDe());
                            }
                        } else {
                            nodomayor = nodohijo.getHijoIz();
                            while (nodomayor.getHijoDe() != null) {
                                nodomayor = nodomayor.getHijoDe();
                            }
                            mayorIzquierda = nodomayor.getPalabra2();
                            borra(mayorIzquierda.getOrigen());
                            nodohijo.setPalabra2(mayorIzquierda);
                        }
                    }
                }
            }
            return deleted;
        }
        return false;
    }

    public int busca(String s) {
        int count = 0, posicion;
        boolean existe = false;
        NodoABB nodo=dicc;
        if (s != null && nodo.getPalabra2() != null && nodo.getPalabra2().getOrigen() != null) {
            for (int i = 0; nodo!= null && !existe; i++) {
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
        if (s != null && nodo != null && nodo.getPalabra2() != null && nodo.getPalabra2().getOrigen() != null) {
            while (nodo!= null) {
                posicion = s.compareToIgnoreCase(nodo.getPalabra2().getOrigen());
                if (posicion == 0) return nodo.getPalabra2().getTraduccion(l);
                else if (posicion > 0) nodo = nodo.getHijoDe();
                else if (posicion < 0) nodo = nodo.getHijoIz();
            }
        }
        return null;
    }

    public void visualizaRec(NodoABB nodo) {
        if (nodo != null) {
            visualizaRec(nodo.getHijoIz());
            nodo.getPalabra2().escribeInfo();
            visualizaRec(nodo.getHijoDe());
        }
    }

    private void visualizaRec2(NodoABB nodo, int j) {
        if (nodo != null) {
            visualizaRec2(nodo.getHijoIz(), j);
            if (count < j) {
                nodo.getPalabra2().escribeInfo();
                count++;
            }
            visualizaRec2(nodo.getHijoDe(), j);
        }
    }

    private void visualizaRec3(NodoABB nodo, int j, char l) {
        if (nodo != null) {
            visualizaRec3(nodo.getHijoIz(), j, l);
            if (count < j) {
                nodo.getPalabra2().escribeInfo(l);
                count++;
            }
            visualizaRec3(nodo.getHijoDe(), j, l);
        }
    }

    public void visualiza(int j) {
        count = 0;
        visualizaRec2(dicc, j);

    }

    public void visualiza(int j, char l) {
        count = 0;
        visualizaRec3(dicc, j, l);
    }

    public void visualiza() {
        visualizaRec(dicc);
    }

    private void preordenAux(NodoABB nodo) {
        if (nodo != null) {
            nodo.getPalabra2().escribeInfo();
            preordenAux(nodo.getHijoIz());
            preordenAux(nodo.getHijoDe());
        }
    }

    public void preordenABB() {
        preordenAux(dicc);
    }

    public void nivelesABB() {
        LinkedList<NodoABB> stack;
        stack = new LinkedList<>();
        NodoABB aux;
        stack.add(dicc);
        for(;!stack.isEmpty();) {
            aux = stack.poll();
            if (aux != null) {
                aux.getPalabra2().escribeInfo();
                stack.add(aux.getHijoIz());
                stack.add(aux.getHijoDe());
            }
        }
    }

    public String anterior(String s) {
        String palabra;
        NodoABB nodoanterior, nodohijo;

        if (s != null) {
            int posicion;
            palabra = null;
            nodoanterior = null;
            nodohijo = dicc;
            while (palabra == null && nodohijo != null) {
                posicion = s.compareToIgnoreCase(nodohijo.getPalabra2().getOrigen());
                if (posicion < 0) nodohijo = nodohijo.getHijoIz();
                else {
                    if (posicion > 0) {
                        nodoanterior = nodohijo;
                        nodohijo = nodohijo.getHijoDe();
                    } else {
                        if (nodohijo.getHijoIz() == null) {
                            if (nodoanterior != null) palabra = nodoanterior.getPalabra2().getOrigen();
                            else nodohijo = null;
                        }
                        else {
                            nodohijo = nodohijo.getHijoIz();
                            while (nodohijo.getHijoDe() != null) {
                                nodohijo = nodohijo.getHijoDe();
                            }
                            palabra = nodohijo.getPalabra2().getOrigen();
                        }
                    }
                }
            }
            return palabra;
        } else return null;
    }

    public void camino(String s) {
        if (s != null && busca(s) > 0) {
            NodoABB nodo = dicc;
            boolean encontrado = false;
            int posicion;
            while (!encontrado) {
                System.out.print(nodo.getPalabra2().getOrigen());
                posicion = s.compareToIgnoreCase(nodo.getPalabra2().getOrigen());
                if (posicion == 0) encontrado = true;
                else {
                    System.out.print(" - ");
                    if (posicion < 0) nodo = nodo.getHijoIz();
                    else nodo = nodo.getHijoDe();
                }
            }
            System.out.println();
        } else {
            System.out.println("NO HAY CAMINO");
        }
    }

    public int altura(NodoABB abb){
        int altura, hijoizquierda, hijoderecha;
        if (abb == null) altura = 0;
        else {
            hijoizquierda = altura(abb.getHijoIz());
            hijoderecha = altura(abb.getHijoDe());
            if (hijoderecha > hijoizquierda) altura = hijoderecha + 1;
            else altura = hijoizquierda + 1;
        }
        return altura;
    }
    
    public boolean equilibradoAux(NodoABB abb){
        if (abb == null) return true;
        else {
            int total, hijoizquierda, hijoderecha;
            hijoderecha = altura(abb.getHijoDe());
            hijoizquierda = altura(abb.getHijoIz());
            total = hijoderecha - hijoizquierda;
            if (total < -1 || total > 1) return false;
            else {
                if (equilibradoAux(abb.getHijoIz()) && equilibradoAux(abb.getHijoDe())) return true;
                else return false;
            }
        }
    }

    public boolean equilibrado() {
        return equilibradoAux(dicc);
    }

    private class NodoABB {
        private Palabra2 pal;
        private NodoABB hijoizquierda;
        private NodoABB hijoderecha;
        public NodoABB(Palabra2 p) {
            pal = p;
            hijoizquierda = null;
            hijoderecha = null;
        }
        public void cambiaHijoIz(NodoABB n) {
            hijoizquierda = n;
        }
        public void cambiaHijoDe(NodoABB n) {
            hijoderecha = n;
        }
        public void setPalabra2(Palabra2 p) {
            pal = p;
        }
        public NodoABB getHijoIz() {
            return hijoizquierda;
        }
        public NodoABB getHijoDe() {
            return hijoderecha;
        }
        public Palabra2 getPalabra2() {
            return pal;
        }
    }
}
