import java.util.Collections;
import java.util.Stack;


public class Parser {
    private Stack<String> pila;
    private Stack<String> pilaEjecucion;
    private Ejecucion ejecucion;

    public Parser(Stack<String> pila){
        this.pila = pila;   
        this.pilaEjecucion = new Stack<>();
        this.ejecucion = new Ejecucion();
    }

    public void ejecutar(){
        Collections.reverse(this.pilaEjecucion);
        while(!this.pilaEjecucion.empty()){

            String x = this.pilaEjecucion.peek();
            System.out.println(x);
            this.pilaEjecucion.pop();
        }
    }

    public boolean parseCodigo(){
        if(pila.size() != 0){
            String linea = pila.peek();
            if(!parseInstruccion(linea)){
                System.out.println("fallo");
                return false;
            }
            this.pila.pop();
            // parseCodigo();
        }
        System.out.println("exito");
        return true;
    }

    public boolean parseInstruccion(String linea){
        if(linea.length() > 0){
            if(linea.startsWith("$")){
                if(!parseInicializacion(linea)){
                    return false;
                }
            }
            else if(linea.startsWith("r")){
                if(!parseRead(linea)){
                    return false;
                }
            }
            else if(linea.startsWith("wr")){
                if(!parseWrite(linea)){
                    return false;
                }
            }
            else if(linea.startsWith("i")){
                if(!parseIf(linea)){
                    return false;
                }
            }
            else if(linea.startsWith("wh")){
                if(!parseWhile(linea)){
                    return false;
                }
            }
            else{
                return false;
            }
        }                        
        return true;
    }

    public boolean parseIf(String linea){
        if(linea.startsWith("if")){
            String resto1 = linea.substring(2,linea.length());
            if( resto1.startsWith(" ") && resto1.endsWith(" then")){
                String resto2 = resto1.substring(1,resto1.length()-5);
                if(resto2.startsWith("(") && resto2.endsWith(")")){
                    resto2 = resto2.substring(0,resto2.length()-2);
                    resto2 = resto2.substring(2,resto2.length());
                    if(!parseCondicion(resto2)){
                        return false;
                    }
                    else{
                        this.pilaEjecucion.push(linea);
                        this.pila.pop();                   
                        String aux = this.pila.peek();
                        aux = aux.replace("\t","");
                        boolean hayElse = false;
                        while(!this.pila.empty()){
                            if(aux.equals("endif;")){
                                this.pilaEjecucion.push(aux);
                                return true;
                            }
                            else if ( aux.equals("else") && !hayElse){
                                hayElse = true;
                            }
                            else if(this.parseInstruccion(aux)){
                                if(!aux.startsWith("if") && !aux.startsWith("while")){
                                    this.pilaEjecucion.push(aux);
                                }
                            }
                            else{
                                return false;
                            }
                            this.pila.pop();
                            if(!this.pila.empty()){
                                aux = this.pila.peek();
                                aux = aux.replace("\t","");
                            }
                        }
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean parseWhile(String linea){
        if(linea.startsWith("while")){
            String resto1 = linea.substring(5,linea.length());
            if( resto1.startsWith(" ") && resto1.endsWith(" do")){
                String resto2 = resto1.substring(1,resto1.length()-3);
                if(resto2.startsWith("(") && resto2.endsWith(")")){
                    resto2 = resto2.substring(0,resto2.length()-2);
                    resto2 = resto2.substring(2,resto2.length());
                    if(!parseCondicion(resto2)){
                        return false;
                    }
                    else{
                        this.pilaEjecucion.push(linea);
                        this.pila.pop();                   
                        String aux = this.pila.peek();
                        aux = aux.replace("\t","");
                        while(!this.pila.empty()){
                            if(aux.equals("wend;")){
                                this.pilaEjecucion.push(aux);
                                return true;
                            }
                            else if(this.parseInstruccion(aux)){
                                if(!aux.startsWith("while") && !aux.startsWith("if")){
                                    this.pilaEjecucion.push(aux);
                                }
                            }
                            else{
                                return false;
                            }
                            this.pila.pop();
                            if(!this.pila.empty()){
                                aux = this.pila.peek();
                                aux = aux.replace("\t","");
                            }
                        }
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return false;
    }

    public boolean parseInicializacion(String linea){
        if( linea.endsWith(";")){
            if(linea.startsWith("$")){
                String linea2 = linea.substring(1,linea.length()-1);
                String[] caracteres = linea2.split("");
                String nombre = "";
                for(int i = 0; i < caracteres.length ; i++){
                    if(parseAlfanumerico(caracteres[i])){
                        nombre = nombre + caracteres[i];
                    }
                    else if(caracteres[i].equals(" ")){
                        if( caracteres[i+1].equals("=")){
                            String aux = linea.substring(i+4,linea.length()-1);
                            if(!parseValor(aux)){
                                System.out.println("owo");
                                return false;
                            }
                        }
                        else{
                            System.out.println("uwu");
                            return false;
                        }
                        //this.ejecucion.ejecutar(linea);
                        return true;
                    }
                    else{
                        System.out.println("aca entonces?");
                        return false;
                    }
                }
            }
            else{
                System.out.println("xd");
                return false;
            }
            
        }
        else{
            System.out.println("dx");
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
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
        //this.ejecucion.ejecutar(linea);
        return true;
    }

    public boolean parseWrite(String linea){
        if(linea.endsWith(";")){
            String contenido = linea.substring(0,linea.length()-1);
            String comando = contenido.substring(0, 5);
            if(linea.charAt(5) == ' '){
                String resto = contenido.substring(6,contenido.length());
                if(!comando.equals("write") || !parseValor(resto)){
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
        //this.ejecucion.ejecutar(linea);
        return true;
    }

    public boolean parseValor(String linea){
        if(parseVariable(linea)){
            return true;        
        }
        else if(parseNumero(linea)){
            return true;
        }
        else if(parseOperacion(linea)){
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

    public boolean parseCondicion (String op) {
        String[] token = op.split(" "); //separamos por espacio
        //System.out.println(op);
        for (int i=0; i< token.length; i++) {
            System.out.print("[" + token[i] + "] ");
        }
        //System.out.println();
        for (int i = 0; i < token.length; i++) {
            if (this.parseCondicional(token[i])) {
                int y = i;
                String[] aux = new String[y];
                for (int j = 0; j < y; j++) {
                    aux[j] = token[j];
                }
                String s = "";
                for (int j = 0; j < aux.length; j++) {
                    if (j==0) {
                        s = s + aux[j];
                    }
                    else {
                        s = s + " " + aux[j];
                    }
                }
                //System.out.println(s);
                if (this.parseOperacion(s) == true) {
                }
                else if (this.parseValor(s) == true) {
                }
                else {
                    //System.out.println("Error de sintaxis");
                    return false;
                }
                y = i+1;
                String[] aux_2 = new String[token.length-y];
                int z = 0;
                for (int j = y; j < token.length; j++) {
                    if (j==token.length) {
                        break;
                    }
                    aux_2[z] = token[j];
                    z++;
                }
                s = "";
                for (int j = 0; j < aux_2.length; j++) {
                    if (j==0) {
                        s = s + aux_2[j];
                    }
                    else {
                        s = s + " " + aux_2[j];
                    }
                }
                //System.out.println(s);
                if (this.parseOperacion(s) == true) {
                }
                else if (this.parseValor(s) == true) {
                }
                else {
                    //System.out.println("Error de sintaxis");
                    return false;
                }

                //System.out.println("Escrito correctamente");
                return true;
            }
        }
        //System.out.println("Error de sintaxis en el condicional");
        return false;
    }

    public boolean parseOperacion(String op) {

        String[] token = op.split(" "); //separamos por espacio
        //System.out.println(op);
        for (int i=0; i< token.length; i++) {
            System.out.print("[" + token[i] + "] ");
        }
        //System.out.println();
        int p_iniciales=0; // numero de parentesis iniciales
        int p_finales=0; //numero de parentesis finales
        for (int i = 0; i < token.length ; i++) {
            if (token[i].equals("(")) { //si la operacion está hecha en parentesis
                int cont = 0;
                p_iniciales++;
                if (i==0) {
                    if (token[i+1].equals("(") ) {
                        //sintaxis correcta
                    }
                }
                else if (i==token.length-1) {
                    //System.out.println("Error de sintaxis");
                    return false;
                }
                else {
                    if (this.parseOperando(token[i-1]) == true || token[i-1].equals("(") || token[i+1].equals("(")){
                    }
                }
                if (this.parseValor(token[i+1]) == true) {
                    String[] nuevo_arreglo = new String[token.length];
                    int aux_i=0;
                    int z = 0;
                    for (int j = i+1; j < token.length; j++) {
                        if (token[j].equals(")") && cont==0) {
                            aux_i = j;
                            break;
                        }
                        else if (token[j].equals(")") && cont>0) {
                            nuevo_arreglo[z] = token[j];
                            cont--;
                        }
                        else if (token[j].equals("(")) {
                            nuevo_arreglo[z] = token[j];
                            cont++;
                        }
                        else {
                            nuevo_arreglo[z] = token[j];
                        }
                        z++;
                    }


                    String[] aux = new String[token.length];
                    for (int j = 0; j < token.length; j++) {
                        aux[j] = nuevo_arreglo[j];
                    }
                    String s = "";
                    int y=0;
                    while (y<aux.length && aux[y]!=null) {
                        if (y==0) {
                            s = s + aux[y];
                        }
                        else {
                            s = s + " " + aux[y];
                        }
                        y++;
                    }
                    //System.out.println("Este es el string: " + s);
                    //System.out.println();
                    boolean b = parseOperacion(s);
                    if (b!=true) {
                        return false;
                    }
                    i=aux_i-1;
                }
                else {
                    //System.out.println(token[i]);
                    //System.out.println("Error de sintaxis paréntesis inicial");
                    return false;
                }
            }
            else if (token[i].equals(")")) {
                p_finales++;
                if (i == token.length-1) {
                    break;
                }
                else if (i==0) {
                   // System.out.println(token[i]);
                    //System.out.println("Error de sintaxis parentesis final");
                    return false;
                }
                else if (this.parseOperando(token[i+1])==true || this.parseValor(token[i-1]) == true) {
                    //sintaxis correcta
                }
                else if (token[i+1].equals(")") || token[i-1].equals(")")) {
                    //sintaxis correcta

                }
                else {
                    //System.out.println(token[i]);
                    //System.out.println("Error de sintaxis parentesis final");
                    return false;
                }
            }
            else if (this.parseOperando(token[i]) == true) {
                if (i==0 || i== token.length-1) {
                    //System.out.println("Error de sintaxis operando");
                    return false;
                }
                if (token[i+1].equals("(") || token[i-1].equals(")") || token[i-1].equals(")")) {
                    //sintaxis correcta
                }
                else if (this.parseValor(token[i-1])==true && this.parseValor(token[i+1])==true) {
                    //sintaxis correcta
                }

                

                else {
                    //System.out.println(token[i]);
                    //System.out.println("Error de sintaxis operando");
                    return false;
                }
            }
            else if (this.parseValor(token[i])==true) { //Lo primero que se encuentras en la linea es un valor
                if (i==token.length-1) {
                    break;
                }
                else  {
                    if (i-1<0) {
                        //sintaxis correcta
                    }
                    else if (this.parseOperando(token[i+1])== true && this.parseOperando(token[i-1])==true) {
                        //sintaxis correcta
                    }

                    else if (token[i-1].equals("(") || token[i+1].equals("(") || token[i-1].equals(")") || token[i+1].equals(")")) {
                        //sintaxis correcta
                    }

                    else {

                        //System.out.println(token[i]);
                        //System.out.println("Error de sintaxis valor");
                        return false;
                    }

                }
            }
            else {
                //System.out.println(token[i]);
                //System.out.println("Error de sintaxis general");
                return false;
            }
        }
        if (p_iniciales != p_finales) { //se compara el numero de parentesis iniciales y finales. Debe ser el mismo para que la sintaxis sea correcta
            //System.out.println(p_iniciales + " " + p_finales);
            //System.out.println("Error de sintaxis. Número de paréntesis no coincide");
            return false;
        }
        //System.out.println("Está escrito correctamente");
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
            default:
                return false;
        }
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
            default:
                return false;
        }
    }

}
