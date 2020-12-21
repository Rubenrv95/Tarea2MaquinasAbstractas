import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static Stack<String> pila;

    public static void main(String[] args){
        
        pila = new Stack<>();
        Scanner entrada = new Scanner(System.in);
        
        String linea = entrada.nextLine();
        while(entrada.hasNextLine()){
            pila.push(linea);
            linea = entrada.nextLine();
            if( linea.isEmpty()){
                break;
            }
        }
        Collections.reverse(pila);
        Parser p = new Parser(pila);
        if(p.parseCodigo()){
            System.out.println("comienza ejecucion");
            p.ejecutar(p.getPilaTermino());
            System.out.println("termina ejecucion");
        }
        p.parseOperacion("( 5 + 8 ( 4 - 1 ) - 4 )");
        p.parseCondicion("( 9 * 8 ) > ( 4 + 2 * ( 2 ) )");
        entrada.close();
    }

    //para ejecutar: cmd /c 'java -jar T2MALF.jar < ejemplo.txt'
}
