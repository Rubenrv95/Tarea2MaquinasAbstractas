import java.util.Stack;

public class Parser {
    private Stack<String> pila;

    public Parser(Stack<String> pila){
        this.pila = pila;
    }

    public boolean parseLinea(){
        while(pila.size() > 0){
            String linea = pila.peek();
            if(linea.length() > 0){
                if(linea.startsWith("$")){
                    if( linea.endsWith(";")){
                        String aux = linea.substring(0, linea.length()-1);
                        String[] split = aux.split(" ");
                        if(split.length == 3){
                            if( !parseVariable(split[0]) || split[1] == "=" || !parseValor(split[2])){
                                System.out.println("F1");
                                return false;
                            }
                        }
                        else{
                            System.out.println("F2");
                            return false;
                        }
                    }
                    else{
                        System.out.println("F3");
                        return false;
                    }
                }
                else{
                    System.out.println("F4");
                    return false;
                }
                pila.pop();
            }                        
        }
        System.out.println("buenardo");
        return true;
    }

    public boolean parseValor(String linea){
        if(parseVariable(linea)){
            return true;
        }
        else if(parseNumero(linea)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean parseVariable(String linea){
        if(linea.startsWith("$")){
            String nombre = linea.substring(1);
            String primera = Character.toString(nombre.charAt(0));
            String resto = nombre.substring(1);
            if( !parseLetra(primera) || !parseAlfanumerico(resto)){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean parseAlfanumerico(String texto){
        if(texto.length() > 0){            
            String letra = Character.toString(texto.charAt(0));            
            if( !parseLetra(letra) && !parseNumero(letra)){
                return false;
            }
            if(texto.length() > 1){
                if(!parseAlfanumerico(texto.substring(1))){
                    return false;
                }
            }
            else{
                return true;
            }
            
        }        
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

    public boolean parseNumero(String str)
    {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }

        for (int i= 0; i < length; i++) {
            char c = str.charAt(i);
            if (!parseDigito(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean parseDigito(char c){
        if (c < '0' || c > '9') {
            return false;
        }
        return true;
    }

    public boolean parseOperando(String c) {
        switch (c) {
            case "+":
                return true;
            case "-":
                return true;
            case "*":
                return true;
            case "/":
                return true;
            case "%":
                return true;
        }
        return false;
    }

    public boolean parseCondicional(String cond) {
        switch (cond) {
            case "<":
                return true;
            case ">":
                return true;
            case "<=":
                return true;
            case ">=":
                return true;
            case "==":
                return true;
            case "!=":
                return true;
        }
        return false;
    }




    /*
    public void setPila(String linea){

        this.pila.push(linea);
    }*/
    /*
    public void reverse(){
        Collections.reverse(this.pila);
    }*/
}
