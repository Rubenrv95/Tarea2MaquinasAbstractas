import java.math.BigInteger;
import java.util.*;

public class Ejecucion {
    Map<String, BigInteger> variables;
    Scanner scanner;
    Postfijo postfijo = new Postfijo();

    public Ejecucion()
    {
        this.variables = new HashMap<String, BigInteger>();
        this.scanner = new Scanner(System.in);
        //variables.put("$n", BigInteger.valueOf(1));
        //variables.put("$fib1", BigInteger.valueOf(1));
        //variables.put("$fib2", BigInteger.valueOf(1));

        ejecutar("$d1 = 1000 + (354 * ( 2 * 355 ) ) + 1;");

        //ejecutar("write $n + 1;");

        //ejecutar("$n = $fib1 + $fib2;");
    }


    public BigInteger calcular (String operacion)
    {
        // primero hay que reemplazar todas las $ por su valor, si no se encuentra.. ERROR
        String[] terminos = operacion.split(" ");
        BigInteger rFinal = null;
        for(int i = 0; i < terminos.length; i++){
            String t = terminos[i];
            if(terminos[i].startsWith("$")){
                BigInteger r = obtenerValor(terminos[i]);
                if(r != null){
                    terminos[i] = String.valueOf(r);
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


        op2 = op2.replace(" ", "");

        String o = postfijo.InfijaAPostfija(op2);

        Stack<Integer> stack = new Stack<>();

        int i;
        for(i = 0; i < o.length(); i++)
        {
            char c = o.charAt(i);

            if(c == ' ')
            {
                //Borrar
            }
            else if(Character.isDigit(c))
            {
                int n = 0;

                while(Character.isDigit(c))
                {
                    n = n*10 + (int)(c -'0');
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
                this.scanner.next();
            }
        }
        if(variables.containsKey(str[1]))
        {
            variables.replace(str[1], n);
        }
        else{
            variables.put(str[1], n);
        }
    }

    public void write(String sublinea)
    {
        String str = sublinea.substring(6, sublinea.length()-1); //le quito el write y el ;
        String var = null;
        boolean esOperacion = false;
        if(str.startsWith("$")){
            if(!str.contains(" ")){
                if(this.variables.containsKey(str)){
                    //System.out.println(this.variables.get(str));
                }
            }
            else{
                int i;
                for(i = 0; i < str.length(); i++)
                {
                    if(str.charAt(i) == ' '){
                        //Obtiene el string de la operacion
                        var = str.substring(0, i);
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
                            loEncontro = true;
                            BigInteger resultado = calcular(operacion);
                            variables.replace(var, resultado);
                            System.out.println(pair.getValue());
                            //Calcular y reemplazar
                        }
                    }
                    /*
                    if(!loEncontro){
                        System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
                    }*/
                }
            }
        }
    }

    public void init(String linea)
    {
        String var = null;
        boolean esValido = false;

        if(linea.startsWith("$")){
            int i;
            for(i = 0; i < linea.length(); i++){
                if(linea.charAt(i) == '='){
                    var = linea.substring(0, i-1);
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
                }
                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry) it.next();

                    if(pair.getKey().equals(var))
                    {
                        valor = (BigInteger) (pair.getValue());
                        String operacion = linea.substring(i+1, linea.length()-1);

                        BigInteger resultado = calcular(operacion);
                        variables.replace(var, resultado);
                        //Calcular y reemplazar
                    }
                }
            }
        }
    }

    public boolean verificarCondicion(String s) {


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

        System.out.println(x);
        switch (condicional) {
            case "<":
                if (x.compareTo(y) == -1) {
                    return true;
                }
                else {
                    return false;
                }
            case ">":
                
                if (x.compareTo(y) == 1) {
                    System.out.println("hola");
                    return true;
                }
                else {
                    System.out.println("chao");
                    return false;
                }
            case "<=":
                if (x.compareTo(y) == -1 || x.compareTo(y) == 0) {
                    return true;
                }
                else {
                    return false;
                }
            case ">=":
                if (x.compareTo(y) == 1 || x.compareTo(y) == 0) {
                    return true;
                }
                else {
                    return false;
                }
            case "!=":
                if (x != y) {
                    return true;
                }
                else {
                    return false;
                }
            case "==":
                if (x == y) {
                    return true;
                }
                else {
                    return false;
                }
            default:
                return false;
        }
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

        return true;
    }
}
