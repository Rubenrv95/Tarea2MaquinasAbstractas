import java.math.BigInteger;
import java.util.*;

public class Ejecucion {
    Map<String, BigInteger> variables;
    Scanner scanner;

    public Ejecucion()
    {
        this.variables = new HashMap<String, BigInteger>();
        this.scanner = new Scanner(System.in);
        //variables.put("$n", BigInteger.valueOf(1));
        //variables.put("$fib1", BigInteger.valueOf(1));
        //variables.put("$fib2", BigInteger.valueOf(1));

        //ejecutar("read $n;");

        //ejecutar("write $n + 1;");

        //ejecutar("$n = $fib1 + $fib2;");

        //System.out.println(infixToPostfix("( 1000 + (354 * ( 2 * 355 ) ) + 1"));
        //System.out.println(infixToPostfix("( 1 + 3 * ( 2 * 3 ) ) + 1"));
    }


    public BigInteger calcular (String operacion)
    {
        System.out.println(operacion);
        // primero hay que reemplazar todas las $ por su valor, si no se encuentra.. ERROR
        String[] terminos = operacion.split(" ");
        BigInteger rFinal = null;
        for(int i = 0; i < terminos.length; i++){
            String t = terminos[i];
            if(terminos[i].startsWith("$")){
                BigInteger r = obtenerValor(terminos[i]);
                if(r != null){
                    terminos[i] = String.valueOf(r);
                    System.out.println("r: "+r);
                }
                else{
                    break;
                }
            }
        }


        String op2 = "";
        for(int i = 0; i < terminos.length; i++){

            op2 = op2+" "+terminos[i];
        }
        System.out.println("Fin: "+op2);

        //String o = infixToPostfix("(1000 + (354 * ( 2 * 355 ) ) + 1");
        String o = infixToPostfix(op2);
        //operacion = opPrueba.replace(" ", "");
        //System.out.println(o);

        Stack<Integer> stack = new Stack<>();

        int i;

        for(i=0; i < o.length(); i++)
        {
            char c = o.charAt(i);

            if(c == ' ')
            {

            }
            else if(Character.isDigit(c))
            {
                int n = 0;

                while(Character.isDigit(c))
                {
                    n = n*10 + (int)(c-'0');
                    i++;
                    c = o.charAt(i);
                }
                i--;
                stack.push(n);
            }
            else
            {
                if(stack.size()>1){
                    int val1 = stack.pop();
                    int val2 = stack.pop();

                    switch(c)
                    {
                        case '+':
                            stack.push(val2+val1);
                            break;

                        case '-':
                            stack.push(val2- val1);
                            break;

                        case '/':
                            stack.push(val2/val1);
                            break;

                        case '*':
                            stack.push(val2*val1);
                            break;
                    }
                }
            }
        }
        //System.out.println("R:" +stack.pop());

        return BigInteger.valueOf(stack.pop());
    }

    public BigInteger obtenerValor(String key){
        BigInteger r = null;
        if (variables.containsKey(key)) {
            return variables.get(key);
        }
        System.out.println("ERROR: la variable "+key+" no esta inicializada");
        return null;
    }

    public void ejecutar(String linea){
        String[] sublinea = linea.split("");
        String str = null;
        if(linea.startsWith("read") && linea.endsWith(";")){
            //le quito el ;
            str = linea.substring(0, linea.length() - 1);
            read(str);
        }
        else if(linea.startsWith("write") && linea.endsWith(";")){
            write(linea);
        }
        else if(linea.startsWith("$") && linea.endsWith(";")){
            init(linea);
        }
    }

    public void read(String sublinea)
    {
        String[] str = sublinea.split(" ");
        Iterator it = variables.entrySet().iterator();
        boolean loEncontro = false;

        BigInteger n = null;
        boolean errorBigInt = true;

        while(errorBigInt){
            try{
                n = this.scanner.nextBigInteger();
                errorBigInt = false;
            }catch(InputMismatchException e){
                System.out.println("Error BigInt: debe ingresar un valor valido");
                this.scanner.next();
            }
        }
        if(variables.containsKey(str[1]))
        {
            System.out.println("true");
            variables.replace(str[1], n);
        }
        else{
            variables.put(str[1], n);
        }

        /*
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();

            if(pair.getKey().equals(str[1]))
            {
                System.out.println("Antes: "+pair.getKey() + " = " + pair.getValue());
                pair.setValue(n);
                System.out.println("Dps: "+pair.getKey() + " = " + pair.getValue());
            }
        }*/
        /*
        if(loEncontro == false){
            //Aqui se le agrega como value el resultado del calcular
            variables.put(str[1], n);
            System.out.println("put NUEVO: "+str[1]+" "+n);
        }*/
    }

    public void write(String sublinea)
    {
        String str = sublinea.substring(6, sublinea.length()-1); //le quito el write y el ;
        System.out.println(str);
        String var = null;
        boolean esOperacion = false;
        if(str.startsWith("$")){
            if(!str.contains(" ")){
                if(this.variables.containsKey(str)){
                    System.out.println(this.variables.get(str));
                }
                else{
                    System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
                }
            }
            else{
                int i;
                for(i = 0; i < str.length(); i++)
                {
                    if(str.charAt(i) == ' '){
                        //Obtiene el string de la operacion
                        var = str.substring(0, i);
                        System.out.println(var);
                        esOperacion = true;
                        break;
                    }
                }
                if(esOperacion)
                {
                    Iterator it = variables.entrySet().iterator();
                    boolean loEncontro = false;

                    BigInteger valor;

                    while (it.hasNext())
                    {
                        Map.Entry pair = (Map.Entry) it.next();
                        //Si encuentro la key, por el de la operac
                        if(pair.getKey().equals(var))
                        {
                            valor = (BigInteger) (pair.getValue());
                            String operacion = str.substring(i+1, str.length());
                            operacion = valor +" "+ operacion;
                            System.out.println(operacion);
                            loEncontro = true;
                            BigInteger resultado = calcular(operacion);
                            System.out.println("Resultado: "+operacion);
                            System.out.println("Antes: "+pair.getKey() + " = " + pair.getValue());
                            variables.replace(var, resultado);
                            System.out.println("Despues: "+pair.getKey() + " = " + pair.getValue());
                            //Calcular y reemplazar
                        }
                    }
                    if(!loEncontro){
                        System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
                    }
                }
            }
        }
    }

    public void init(String linea)
    {
        System.out.println(linea);
        String var = null;
        boolean esValido = false;

        if(linea.startsWith("$")){
            int i;
            for(i = 0; i < linea.length(); i++){
                if(linea.charAt(i) == '='){
                    var = linea.substring(0, i-1);
                    System.out.println(var);
                    esValido = true;
                    break;
                }
            }
            if(esValido)
            {
                Iterator it = variables.entrySet().iterator();

                BigInteger valor;
                if(!variables.containsKey(var)){
                    variables.put(var, BigInteger.valueOf(1));
                    System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
                }
                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry) it.next();

                    if(pair.getKey().equals(var))
                    {
                        valor = (BigInteger) (pair.getValue());
                        String operacion = linea.substring(i+1, linea.length()-1);

                        System.out.println("op:"+ operacion);
                        BigInteger resultado = calcular(operacion);
                        System.out.println("Resultado: "+resultado);
                        System.out.println("Antes: "+pair.getKey() + " = " + pair.getValue());
                        variables.replace(var, resultado);
                        System.out.println("Despues: "+pair.getKey() + " = " + pair.getValue());
                        //Calcular y reemplazar
                    }
                }
            }
        }
    }



    public String verificarVariable(String s) {


        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '$') {
                String var = "";
                int y = i;
                while (s.charAt(y)!=' ') {
                    if (y==s.length()-1) {
                        var = var + s.charAt(y);
                        break;
                    }
                    var = var + s.charAt(y);
                    y++;
                }
                i= y;

                System.out.println(var);

                s = s.replaceAll(var, "pene"); //se reemplazan la variable por su valor en el string

                System.out.println(s);
                /*
                String valor_variable = (String) h.get(var); //obtenemos el valor de la variable del hash

                s = s.replaceAll(var, valor_variable); //se reemplazan la variable por su valor en el string

                i=0;

                 */
            }
        }

        return s;
    }

    public String infixToPostfix(String expression) {
        Stack<Character> stack = new Stack<Character>();
        String postfixString = "";

        for (int index = 0; index < expression.length(); ++index) {
            char value = expression.charAt(index);
            if (value == '(') {
                stack.push('('); // Code Added
            } else if (value == ')') {
                Character oper = stack.peek();

                while (!(oper.equals('(')) && !(stack.isEmpty())) {
                    stack.pop();
                    postfixString += oper.charValue();
                    if (!stack.isEmpty()) // Code Added
                        oper = stack.peek(); // Code Added
                }
                stack.pop(); // Code Added
            } else if (value == '+' || value == '-') {
                if (stack.isEmpty()) {
                    stack.push(value);
                } else {
                    Character oper = stack.peek();
                    while (!(stack.isEmpty() || oper.equals(('(')) || oper.equals((')')))) {
                        oper = stack.pop(); // Code Updated
                        postfixString += oper.charValue();
                    }
                    stack.push(value);
                }
            } else if (value == '*' || value == '/') {
                if (stack.isEmpty()) {
                    stack.push(value);
                } else {
                    Character oper = stack.peek();
                    // while condition updated
                    while (!oper.equals(('(')) && !oper.equals(('+')) && !oper.equals(('-')) && !stack.isEmpty()) {
                        oper = stack.pop(); // Code Updated
                        postfixString += oper.charValue();
                    }
                    stack.push(value);
                }
            } else {
                postfixString += value;
            }
        }

        while (!stack.isEmpty()) {
            Character oper = stack.peek();
            if (!oper.equals(('('))) {
                stack.pop();
                postfixString += oper.charValue();
            }
        }
        return postfixString;
    }

    public boolean verificarCondicion(String s) {

        System.out.println(s);

        //Quitamos los parentesis de la condicion
        s = s.substring(1, s.length() -1);
        String[] token = s.split(" ");

        String condicional = "";

        /*Buscamos la condicional utilizada en la condicion evaluada */
        for (int i = 0; i < token.length; i++) {
            if (token[i].equals("<") || token[i].equals(">") || token[i].equals("<=")  || token[i].equals(">=")  || token[i].equals("==")  || token[i].equals("!=") ) {
                condicional = token[i];
                break;
            }
        }

        //Separamos las partes de la izquierda y de la derecha de la condicion (Es decir, lo que viene antes y despues de la condicional)
        String[] partes = s.split(condicional);
        String parte1 = partes[0];
        String parte2 = partes[1];

        //ajustamos el string para quitar los espacios que sobran
        parte1 = parte1.substring(1, parte1.length()-1);
        parte2 = parte2.substring(1, parte2.length()-1);

        BigInteger x = this.calcular(parte1);
        BigInteger y = this.calcular(parte2);

        switch (condicional) {
            case "<":
                if (x.compareTo(y) == -1) {
                    System.out.println("Es cierta la condicion");
                    return true;
                }
                else {
                    System.out.println("Es falsa la condicion");
                    return false;
                }
            case ">":
                if (x.compareTo(y) == 1) {
                    System.out.println("Es cierta la condicion");
                    return true;
                }
                else {
                    System.out.println("Es falsa la condicion");
                    return false;
                }
            case "<=":
                if (x.compareTo(y) == -1 || x.compareTo(y) == 0) {
                    System.out.println("Es cierta la condicion. ");
                    return true;
                }
                else {
                    System.out.println("Es falsa la condicion");
                    return false;
                }
            case ">=":
                if (x.compareTo(y) == 1 || x.compareTo(y) == 0) {
                    System.out.println("Es cierta la condicion");
                    return true;
                }
                else {
                    System.out.println("Es falsa la condicion");
                    return false;
                }
            case "!=":
                if (x != y) {
                    System.out.println("Es cierta la condicion");
                    return true;
                }
                else {
                    System.out.println("Es falsa la condicion");
                    return false;
                }
            case "==":
                if (x == y) {
                    System.out.println("Es cierta la condicion");
                    return true;
                }
                else {
                    System.out.println("Es falsa la condicion");
                    return false;
                }
        }


        System.out.println("Error al ingresar la condicional. No coincide");
        return false;
    }

    public boolean comprobarVariables(String s) {

        //quitamos los parentesis inicial y final
        s = s.substring(1, s.length() -1);
        String[] token = s.split(" ");

        String condicional = "";

        /*Buscamos la condicional utilizada en la condicion evaluada */
        for (int i = 0; i < token.length; i++) {
            if (token[i].equals("<") || token[i].equals(">") || token[i].equals("<=")  || token[i].equals(">=")  || token[i].equals("==")  || token[i].equals("!=") ) {
                condicional = token[i];
                break;
            }
        }

        //Separamos las partes de la izquierda y de la derecha de la condicion (Es decir, lo que viene antes y despues de la condicional)
        String[] partes = s.split(condicional);
        String parte1 = partes[0];
        String parte2 = partes[1];

        //ajustamos los string para eliminar los espacios que sobran
        parte1 = parte1.substring(1, parte1.length()-1);
        parte2 = parte2.substring(1, parte2.length()-1);

        // Ahora hay que encontrar todas las variables que empiezan por $ y encontrar su valor
        String[] terminos1 = parte1.split(" ");
        String[] terminos2 = parte2.split(" ");

        for(int i = 0; i < terminos1.length; i++){
            if(terminos1[i].startsWith("$")){
                BigInteger r = obtenerValor(terminos1[i]);
                if(r == null){
                    return false;
                }
            }
        }

        for(int i = 0; i < terminos2.length; i++){
            if(terminos2[i].startsWith("$")){
                BigInteger r = obtenerValor(terminos2[i]);
                if(r == null){
                    return false;
                }
            }
        }

        System.out.println("Todas las variables son correctas y están almacenadas");
        return true;
    }
}
