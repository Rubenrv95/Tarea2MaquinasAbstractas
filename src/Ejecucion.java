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

        op2 = op2.replace(" ", "");

        String o = postfijo.InfijaAPostfija(op2);

        System.out.println("postfix: "+ o);

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
    }

    public void write(String sublinea)
    {
        String str = sublinea.substring(6, sublinea.length()-1); //le quito el write y el ;
        //System.out.println(str);
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
                BigInteger valor;
                if(!variables.containsKey(var)){
                    variables.put(var, BigInteger.valueOf(1));
                    //System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
                }

                Iterator it = variables.entrySet().iterator();

                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry) it.next();

                    if(pair.getKey().equals(var))
                    {
                        valor = (BigInteger) (pair.getValue());
                        String operacion = linea.substring(i+2, linea.length()-1);

                        if(operacion.contains(" ")){
                            System.out.println("op:"+ operacion);
                            BigInteger resultado = calcular(operacion);
                            System.out.println("Resultado: "+resultado);
                            System.out.println("Antes: "+pair.getKey() + " = " + pair.getValue());
                            variables.replace(var, resultado);
                            System.out.println("Despues: "+pair.getKey() + " = " + pair.getValue());
                            //Calcular y reemplazar
                        }
                        else{
                            variables.replace(var, BigInteger.valueOf(Integer.parseInt(operacion)));
                            System.out.println(variables.get(var));
                        }
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
}
