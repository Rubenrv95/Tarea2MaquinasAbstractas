import java.util.ArrayList;
import java.util.Stack;

public class Postfijo {

    private ArrayList<String> salida;
    private Stack<String> operadores;

    public Postfijo(){
        this.salida = new ArrayList<>();
        this.operadores = new Stack<>();
    }

    public int getPrecedencia(String s){
        int valor = 3;
        if(s.equals("*") || s.equals("/")){
            valor = 3;
        }
        if(s.equals("+") || s.equals("-")){
            valor = 2;
        }
        if(s.equals("(")){
            valor = 1;
        }
        return valor;
    }

    public String InfijaAPostfija(String cadena)
    {
        String[] array = cadena.split("");

        String numero = "";
        char aux;
        for(int i = 0; i < array.length; i++){
            char c = array[i].charAt(0);

            if(Character.isDigit(c))
            {
                numero += array[i];

                if(i+1 < array.length)
                {
                    aux = array[i+1].charAt(0);
                    if(!Character.isDigit(aux) && (numero.length() > 0))
                    {
                        salida.add(numero);
                        numero = "";
                    }
                }
                else{
                    salida.add(numero);
                }
            }
            else{
                if(array[i].equals("(") || operadores.size() == 0){
                    operadores.push(array[i]);
                }
                else{
                    if(array[i].equals(")")){
                        while(!operadores.peek().equals("(")){
                            salida.add(operadores.pop());
                        }
                        operadores.pop();
                    }
                    else{
                        if(this.getPrecedencia(operadores.peek()) >= this.getPrecedencia(array[i])){
                            salida.add(operadores.pop());
                            operadores.push(array[i]);
                        }
                        else if(this.getPrecedencia(operadores.peek()) < this.getPrecedencia(array[i])){
                            operadores.push(array[i]);
                        }
                    }
                }
            }
        }
        if(this.operadores.size() != 0){
            while(this.operadores.size() != 0){
                this.salida.add(this.operadores.pop());
            }
        }


        return mostrarOperacion();
    }

    public String mostrarOperacion(){
        String salida = "";
        for (int i = 0; i < this.salida.size(); i++) {
            salida += this.salida.get(i)+" ";
        }
        return salida;
    }
}
