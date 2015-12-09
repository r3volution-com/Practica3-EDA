import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

public class DiccTM implements Diccionario {
    private int nlenguas;
    private ArrayList<Character> lenguas;
    private TreeMap<String, Vector<String>> dicc;

    public DiccTM(){
        nlenguas = -1;
        lenguas = new ArrayList<Character>();
        dicc = new TreeMap<String, Vector<String>>(String.CASE_INSENSITIVE_ORDER);
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
        if (p != null && p.getOrigen() != null && !p.getOrigen().equalsIgnoreCase("") && p.getLenguas().length == lenguas.size()){
            char[] len = p.getLenguas();
            for (int i = 0; i < len.length && i < lenguas.size(); i++){
                if (len[i] != lenguas.get(i)){
                    return false;
                }
            }
            Vector<String> aux = dicc.get(p.getOrigen());
            Vector<String> aux2 = p.getTraducciones();
            if (aux == null){
                dicc.put(p.getOrigen(), aux2);
                return true;
            } else {
                boolean changed = false;
                for (int i = 0; i < lenguas.size(); i++){
                    if (aux2.get(i) != null){
                        aux.set(i, aux2.get(i));
                        changed = true;
                    }
                }
                if (changed) {
                    dicc.replace(p.getOrigen(), aux);
                    return true;
                }
            }
        }
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
}