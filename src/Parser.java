import java.util.Collections;
import java.util.Stack;


public class Parser {
    private Stack<String> pila;
    private Stack<String> pilaTermino;
    private Ejecucion ejecucion;

    public Parser(Stack<String> pila){
        this.pila = pila;   
        this.pilaTermino = new Stack<>();
        this.ejecucion = new Ejecucion();
    }

    public void ejecutar( Stack<String> pilaEjecucion){
        while(!pilaEjecucion.empty()){
            
            String linea = pilaEjecucion.peek();
            if( linea.startsWith("$") || linea.startsWith("write") || linea.startsWith("read") ){
                this.ejecucion.ejecutar(linea);
                pilaEjecucion.pop();
            }
            else if (linea.startsWith("if")){
                String inicio = pilaEjecucion.peek();
                pilaEjecucion.pop();
                boolean ifStat = true;
                boolean elseStat = false;
                int ifCount = 1;
                Stack<String> ifIns = new Stack<>();
                Stack<String> elseIns = new Stack<>();
                String instruccion = pilaEjecucion.peek();
                while(ifCount != 0){
                    if(instruccion.equals("else")){
                        elseStat = true;
                        ifStat = false;
                    }
                    else{
                        if(ifStat){
                            ifIns.push(instruccion);
                        }
                        else if (elseStat){
                            elseIns.push(instruccion);
                        }
                    }
                    pilaEjecucion.pop();
                    instruccion = pilaEjecucion.peek();
                    if(instruccion.equals("endif;")){
                        ifCount--;
                    }
                    else if ( instruccion.startsWith("if")){
                        ifCount++;
                    }
                    
                }
                Collections.reverse(ifIns);
                Collections.reverse(elseIns);
                pilaEjecucion.pop();
                
                System.out.println(ifIns);
                System.out.println(elseIns);
                String condicion = inicio.substring(3,inicio.length()-5);
                if(this.ejecucion.comprobarVariables(condicion)){
                    if(this.ejecucion.verificarCondicion(condicion)){
                        this.ejecutar(ifIns);
                    }
                    else{
                        this.ejecutar(elseIns);
                    }
                }
            }
            else if( linea.startsWith("while")){
                String inicio = pilaEjecucion.peek();
                pilaEjecucion.pop();
                Stack<String> instrucciones = new Stack<>();
                String instruccion = pilaEjecucion.peek();
                while(!instruccion.equals("wend;")){
                    instrucciones.push(instruccion);
                    pilaEjecucion.pop();
                    instruccion = pilaEjecucion.peek();
                }
                pilaEjecucion.pop();

                String condicion = inicio.substring(6, inicio.length()-3);
                Collections.reverse(instrucciones);
                System.out.println(instrucciones);
                if(this.ejecucion.comprobarVariables(condicion)){
                    System.out.println("sale?");
                    while(this.ejecucion.verificarCondicion(condicion)){
                        Stack<String> aux = (Stack<String>) instrucciones.clone();
                        this.ejecutar(aux);
                    }
                }
            }
        }
    }

    public boolean parseCodigo(){
        while(!this.pila.empty()){
            String linea = pila.peek();
            if(!parseInstruccion(linea)){
                System.out.println("ERROR");
                return false;
            }
            this.pila.pop();
        }
        System.out.println("Se parseo el codigo con Exito");
        Collections.reverse(this.pilaTermino);
        return true;
    }

    public boolean parseInstruccion(String linea){
        if(linea.length() > 0){
            if(linea.startsWith("$")){
                if(!parseInicializacion(linea)){
                    return false;
                }
                this.pilaTermino.push(linea);
            }
            else if(linea.startsWith("r")){
                if(!parseRead(linea)){
                    return false;
                }
                this.pilaTermino.push(linea);
            }
            else if(linea.startsWith("wr")){
                if(!parseWrite(linea)){
                    return false;
                }
                this.pilaTermino.push(linea);
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
                        this.pilaTermino.push(linea);
                        this.pila.pop();                   
                        String aux = this.pila.peek();
                        aux = aux.replace("\t","");
                        boolean hayElse = false;
                        while(!this.pila.empty()){
                            if(aux.equals("endif;")){
                                this.pilaTermino.push(aux);
                                return true;
                            }
                            else if ( aux.equals("else") && !hayElse){
                                this.pilaTermino.push(aux);
                                hayElse = true;
                            }
                            else if(this.parseInstruccion(aux)){
                                if(!aux.startsWith("if") && !aux.startsWith("while")){
                                    
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
                        this.pilaTermino.push(linea);
                        this.pila.pop();                   
                        String aux = this.pila.peek();
                        aux = aux.replace("\t","");
                        while(!this.pila.empty()){
                            if(aux.equals("wend;")){
                                this.pilaTermino.push(aux);
                                return true;
                            }
                            else if(this.parseInstruccion(aux)){
                                if(!aux.startsWith("while") && !aux.startsWith("if")){
                                    
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
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
            else{
                return false;
            }
        }
        else{
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
        // for (int i=0; i< token.length; i++) {
        //     System.out.print("[" + token[i] + "] ");
        // }
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
                if (this.parseOperacion(s) == true) {
                }
                else if (this.parseValor(s) == true) {
                }
                else {
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
                if (this.parseOperacion(s) == true) {
                }
                else if (this.parseValor(s) == true) {
                }
                else {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean parseOperacion(String op) {

        String[] token = op.split(" "); //separamos por espacio
        // for (int i=0; i< token.length; i++) {
        //     System.out.print("[" + token[i] + "] ");
        // }
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
                    boolean b = parseOperacion(s);
                    if (b!=true) {
                        return false;
                    }
                    i=aux_i-1;
                }
                else {
                    return false;
                }
            }
            else if (token[i].equals(")")) {
                p_finales++;
                if (i == token.length-1) {
                    break;
                }
                else if (i==0) {
                    return false;
                }
                else if (this.parseOperando(token[i+1])==true || this.parseValor(token[i-1]) == true) {
                    //sintaxis correcta
                }
                else if (token[i+1].equals(")") || token[i-1].equals(")")) {
                    //sintaxis correcta

                }
                else {
                    return false;
                }
            }
            else if (this.parseOperando(token[i]) == true) {
                if (i==0 || i== token.length-1) {
                    return false;
                }
                if (token[i+1].equals("(") || token[i-1].equals(")") || token[i-1].equals(")")) {
                    //sintaxis correcta
                }
                else if (this.parseValor(token[i-1])==true && this.parseValor(token[i+1])==true) {
                    //sintaxis correcta
                }

                

                else {
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
                        return false;
                    }

                }
            }
            else {
                return false;
            }
        }
        if (p_iniciales != p_finales) { //se compara el numero de parentesis iniciales y finales. Debe ser el mismo para que la sintaxis sea correcta
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

    public Stack<String> getPilaTermino() {
        return pilaTermino;
    }

    public void setPilaTermino(Stack<String> pilaTermino) {
        this.pilaTermino = pilaTermino;
    }
}
