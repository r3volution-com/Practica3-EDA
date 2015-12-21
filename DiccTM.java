import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DiccTM implements Diccionario {
    private int nlenguas;
    private ArrayList<Character> lenguas;
    private TreeMap<String, Vector<String>> dicc;

    public DiccTM(){
        nlenguas = -1;
        lenguas = new ArrayList<>();
        dicc = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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
        if (s != null && dicc.remove(s) != null) return true;
        return false;
    }

    public int busca(String s) {
        if (s != null && dicc.get(s) != null) return 1;
        return -1;
    }

    public String traduce(String s, char l) {
        if (s != null) {
            int pos = -1;
            for (int i = 0; i < lenguas.size() && pos == -1; i++) {
                if (l == lenguas.get(i)) {
                    pos = i;
                }
            }
            if (pos >= 0) {
                Vector<String> aux = dicc.get(s);
                return aux.get(pos);
            }
        }
        return null;
    }

    public void visualiza() {
        Set keys = dicc.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();){
            String key = (String)i.next();
            Vector<String> values =  dicc.get(key);
            String cadena = key;
            for (int j = 0; j < values.size(); j++){
                cadena += ":"+values.get(j);
            }
            System.out.println(cadena);
        }
    }

    public void visualiza(int j) {
        Set<String> keys = dicc.keySet();
        int k = 0;
        for (Iterator i = keys.iterator(); i.hasNext() && k<j ;k++){
            String key = (String)i.next();
            Vector<String> values =  dicc.get(key);
            String cadena = key;
            for (int l = 0; l < values.size(); l++){
                cadena += ":"+values.get(j);
            }
            System.out.println(cadena);
        }
    }

    public void visualiza(int j, char l) {
        Set<String> keys = dicc.keySet();
        int k = 0;
        for (Iterator i = keys.iterator(); i.hasNext() && k<j ;k++){
            String key = (String)i.next();
            String cadena = key+":"+traduce(key, l);
            System.out.println(cadena);
        }
    }
}