import java.util.Stack;

public class Parser {
    private Stack<String> pila;

    public Parser(Stack<String> pila){
        this.pila = pila;
    }

    public boolean parseCodigo(){
        if(pila.size() != 0){
            String linea = pila.peek();
            if(!parseInstruccion(linea)){
                System.out.println("F en codigo");
                return false;
            }
            pila.pop();
            parseCodigo();
        }
        System.out.println("buenardo");
        return true;
    }

    public boolean parseInstruccion(String linea){
        if(linea.length() > 0){
            if(linea.startsWith("$")){
                if(!parseInicializacion(linea)){
                    System.out.println("F ini");
                    return false;
                }
            }
            else if(linea.startsWith("r")){
                if(!parseRead(linea)){
                    System.out.println("F read");
                    return false;
                }
            }
            else if(linea.startsWith("w")){
                if(!parseWrite(linea)){
                    System.out.println("F write");
                    return false;
                }
            }
        }                        
        return true;
    }

    public boolean parseInicializacion(String linea){
        if( linea.endsWith(";")){
            String aux = linea.substring(0, linea.length()-1);
            String[] split = aux.split(" ");
            if(split.length == 3){
                if( !parseVariable(split[0]) || !split[1].equals("=") || !parseValor(split[2])){
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
        return true;
    }

    public boolean parseRead(String linea){
        if(linea.endsWith(";")){
            String contenido = linea.substring(0,linea.length()-1);
            String[] partes = contenido.split(" ");
            if(partes.length == 2){
                if( !partes[0].equals("read") || !parseVariable(partes[1])){
                    System.out.println("read malo o no var");
                    return false;
                }
            }
            else{
                System.out.println("formato read incorrecto");
                return false;
            }
        }
        else{
            System.out.println("no ;");
            return false;
        }
        return true;
    }

    public boolean parseWrite(String linea){
        if(linea.endsWith(";")){
            String contenido = linea.substring(0,linea.length()-1);
            String[] partes = contenido.split(" ");
            if(partes.length == 2){
                if(!partes[0].equals("write") || !parseValor(partes[1])){
                    System.out.println("write o var/num malo");
                    return false;
                }
            }
            else{
                System.out.println("formato malo");
                return false;
            }
        }
        else{
            System.out.println("no ;");
            return false;
        }
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

    public boolean parseOperacion(String op) {

        String[] token = op.split(" "); //separamos por espacio
        System.out.println(op);
        for (int i=0; i< token.length; i++) {
            System.out.print("[" + token[i] + "] ");
        }

        System.out.println();
        int p_iniciales=0;
        int p_finales=0;

        for (int i = 0; i < token.length ; i++) {
            System.out.println(token[i]);
            if (token[i].equals("(")) { //si la operacion está hecha en parentesis
                if (i == 0 || i == token.length-1) {

                }
                else if (this.parseOperando(token[i-1]) == true || token[i-1].equals("(") || token[i+1].equals("(") ) {

                }
                else if (this.parseValor(token[i+1]) == true) {
                    String[] aux = new String[token.length];
                    int aux_i=0;
                    int z = 0;
                    for (int j = i+1; j < token.length; j++) {
                        if (token[j].equals(")")) {
                            aux_i = j;
                            break;
                        }
                        else {
                            aux[z] = token[j];
                        }
                        z++;
                    }

                    String s = "";
                    int y=0;
                    while (y<z) {
                        if (y==0) {
                            s = s + aux[y];
                        }
                        else {
                            if (this.parseOperando(String.valueOf(aux[y])) == false) {
                                s= s + aux[y];
                            }
                            else {
                                s = s + " " + aux[y] + " ";
                            }
                        }
                        y++;
                    }
                    System.out.println("Este es el string: " + s);
                    System.out.println();

                    boolean b = parseOperacion(s);
                    if (b!=true) {
                        return false;
                    }
                    i=aux_i;

                }
                else {
                    System.out.println(token[i]);
                    System.out.println("Error de sintaxis paréntesis inicial");
                    return false;
                }
                p_iniciales++;

            }


            else if (this.parseValor(token[i])==true) { //Lo primero que se encuentras en la linea es un valor
                if (i==token.length-1) {
                    break;
                }
                else  {
                    if (i-1<0) {

                    }
                    else if (this.parseOperando(token[i+1])== true && this.parseOperando(token[i-1])==true) {

                    }

                    else if (token[i-1].equals("(") || token[i+1].equals("(") || token[i-1].equals(")") || token[i+1].equals(")")) {

                    }

                    else {

                        System.out.println(token[i]);
                        System.out.println("Error de sintaxis valor");
                        return false;
                    }

                }
            }

            else if (this.parseOperando(token[i]) == true) {
                if (this.parseValor(token[i-1])==true && this.parseValor(token[i+1])==true) {

                }

                else if (token[i-1].equals("(") || token[i+1].equals("(") || token[i-1].equals(")") || token[i-1].equals(")")) {

                }

                else {
                    System.out.println(token[i]);
                    System.out.println("Error de sintaxis operando");
                    return false;
                }
            }

            else if (token[i].equals(")")) {
                if (i == token.length-1) {
                    break;
                }
                else if (this.parseOperando(token[i+1])==true || this.parseValor(token[i-1]) == true) {

                }
                else {
                    System.out.println(token[i]);
                    System.out.println("Error de sintaxis parentesis final");
                    return false;
                }
                p_finales++;

            }
            else {
                System.out.println(token[i]);
                System.out.println("Error de sintaxis general");
                return false;
            }
        }

        if (p_iniciales != p_finales) {
            System.out.println("Error de sintaxis. Número de paréntesis no coincide");
            return false;
        }

        System.out.println("Está escrito correctamente");
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
