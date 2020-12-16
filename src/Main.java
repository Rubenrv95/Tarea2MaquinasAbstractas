import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Main {    
    private Stack<String> pila;

    public static void main(String[] args){
        Main main = new Main();      
        Scanner entrada = new Scanner(System.in);
        
        String linea = entrada.nextLine();
        while(linea.length()>0){
            main.setPila(linea);
            linea = entrada.nextLine();
        }
        main.reverse();
        main.parseLinea();
        entrada.close();
    }

    //para ejecutar: cmd /c 'java -jar T2MALF.jar < ejemplo.txt'

    public Main() {      
        pila = new Stack<>();  
    }

    public boolean parseLinea(){
        while(pila.size() > 0){
            String linea = pila.peek();
            String[] aux = linea.split("");
            for(int i = 0; i < aux.length; i++){
                if(!parseLetra(aux[i])){
                    System.out.println("F");
                    return false;
                }
            }
            pila.pop();
        }
        System.out.println("buenardo");
        return true;
    }
    public boolean parseLetra(String letra){
        char aux = letra.charAt(0);
        int ascii = (int)aux;
        if( (ascii >= 65 && ascii<= 90) || (ascii >= 97 && ascii <=122)){
            return true;
        }
        return false;
    }

    public void setPila(String linea){

        this.pila.push(linea);
    }

    public void reverse(){
        Collections.reverse(this.pila);
    }


public class Main {
    public static void main(String[] args){

        String numero = "1";
        Lexer l = new Lexer();

        System.out.println(l.isDigito('c'));
        System.out.println(l.isNumero(numero));
    }
}
