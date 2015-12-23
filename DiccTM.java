//DNI 77842527Q GONZALEZ ALVARADO, MARIO
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DiccTM implements Diccionario {
    private int nlenguas;
    private ArrayList<Character> lenguas;
    private TreeMap<String, Vector<String>> dicc;
    /*Constructor, incializamos las variables*/
    public DiccTM(){
        nlenguas = -1;
        lenguas = new ArrayList<>();
        dicc = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }
    /*Lee el archivo que le pasamos por parametro y lo almacena*/
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
    /* Inserta una palabra en el diccionario */
    public boolean inserta(Palabra2 p) {
        //Comprobamos que la palabra que nos pasan no sea nula
        if (p != null && p.getOrigen() != null && !p.getOrigen().equalsIgnoreCase("") && p.getLenguas().length == lenguas.size()){
            char[] len = p.getLenguas();
            //Comprobamos que haya las mismas lenguas y en el mismo orden
            for (int i = 0; i < len.length && i < lenguas.size(); i++){
                if (len[i] != lenguas.get(i)){
                    return false;
                }
            }
            //Creamos 2 array auxiliares con las traducciones
            Vector<String> aux = dicc.get(p.getOrigen());
            Vector<String> aux2 = p.getTraducciones();
            //Si no tiene traducciones simplemente insertamos
            if (aux == null){
                dicc.put(p.getOrigen(), aux2);
                return true;
            } else {
                boolean changed = false;
                //En el caso de que tenga traducciones debemos sustituir las antiguas por las nuevas
                for (int i = 0; i < lenguas.size(); i++){
                    //Si la palabra existe en el diccionario la sustituye con set (en caso de que se le este enviando una)
                    if (aux2.get(i) != null){
                        if (!aux2.get(i).equalsIgnoreCase("") && !aux2.get(i).equalsIgnoreCase(aux.get(i))){
                            aux.set(i, aux2.get(i));
                            changed = true;
                        }
                    } else { //Si no existe se almacena directamente con set
                        if (aux2.get(i) != null) {
                            aux.set(i, aux2.get(i));
                            changed = true;
                        }
                    }
                }
                //Y finalmente guardamos los cambios con put
                if (changed) {
                    dicc.put(p.getOrigen(), aux);
                    return true;
                }
            }
        }
        return false;
    }
    /*Utilizamos la funcion remove de TreeMap para borrar del diccionario*/
    public boolean borra(String s) {
        if (s != null && dicc.remove(s) != null) return true;
        return false;
    }
    /*Utilizamos el metodo get para comprobar la existencia de una palabra*/
    public int busca(String s) {
        if (s != null && dicc.get(s) != null) return 1;
        return -1;
    }
    /*Obtiene una traduccion de una palabra a la lengua pasada por parametro*/
    public String traduce(String s, char l) {
        if (s != null) {
            int pos = -1;
            //Comprobamos que la lengua exista
            for (int i = 0; i < lenguas.size() && pos == -1; i++) {
                if (l == lenguas.get(i)) pos = i;
            }
            //Si existe la obtenemos con get
            if (pos >= 0) {
                Vector<String> aux = dicc.get(s);
                if (aux != null) return aux.get(pos);
                else return null;
            }
        }
        return null;
    }
    //Muestra una cadena con el formato de escribeinfo
    public void visualiza() {
        Set keys = dicc.keySet();//Almacenamos las claves en un keyset
        for (Iterator i = keys.iterator(); i.hasNext();){//Las recorremos con un iterator
            String key = (String)i.next();//Obtenemos la siguiente
            Vector<String> values =  dicc.get(key);
            String cadena = key;
            for (int j = 0; j < values.size(); j++){
                cadena += ":";
                if (values.get(j) != null) cadena += values.get(j); //concatenamos la clave con sus traducciones
            }
            System.out.println(cadena);
        }
    }
    //Muestra una cadena con el formato de escribeinfo de las primeras j lineas
    public void visualiza(int j) {
        Set<String> keys = dicc.keySet();
        int k = 0;
        for (Iterator i = keys.iterator(); i.hasNext() && k<j ;k++){
            String key = (String)i.next();
            Vector<String> values =  dicc.get(key);
            String cadena = key;
            for (int l = 0; l < values.size(); l++){
                cadena += ":";
                if (values.get(l) != null) cadena += values.get(l);
            }
            System.out.println(cadena);
        }
    }
    //Muestra una cadena con el formato de escribeinfo de las primeras j lineas, pero solo el lenguaje l
    public void visualiza(int j, char l) {
        Set<String> keys = dicc.keySet();
        int k = 0;
        for (Iterator i = keys.iterator(); i.hasNext() && k<j ;k++){
            String key = (String)i.next();
            String cadena = key+":";
            if(traduce(key, l) != null) cadena += traduce(key, l);
            System.out.println(cadena);
        }
    }
}