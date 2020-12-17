import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static Stack<String> pila;

    public static void main(String[] args){
        pila = new Stack<>();
        Scanner entrada = new Scanner(System.in);
        
        String linea = entrada.nextLine();
        while(linea.length()>0){
            pila.push(linea);
            linea = entrada.nextLine();
        }
        Collections.reverse(pila);
        entrada.close();

        Parser p = new Parser(pila);
        p.parseCodigo();
    }

    //para ejecutar: cmd /c 'java -jar T2MALF.jar < ejemplo.txt'
}
