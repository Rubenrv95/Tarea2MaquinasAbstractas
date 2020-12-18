import java.math.BigInteger;
import java.util.*;

public class Ejecucion {
    Map<String, BigInteger> variables;
    Scanner scanner;

    public Ejecucion()
    {
        this.variables = new HashMap<String, BigInteger>();
        this.scanner = new Scanner(System.in);
        //ejecutar("read $n;");
        variables.put("$n", BigInteger.valueOf(1));
        //ejecutar("write $n + 1;");
        //ejecutar("write $n + 1;");

        ejecutar("$n = $fib1 + $fib2;");
    }


    public int calcular (String s) {

        return 0;
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
                n = scanner.nextBigInteger();
                errorBigInt = false;
            }catch(InputMismatchException e){
                System.out.println("Error BigInt: debe ingresar un valor valido");
                scanner.next();
            }
        }

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            //Si encuentro la key, por el de la operac
            if(pair.getKey().equals(str[1]))
            {
                pair.setValue(n);
            }
            System.out.println("Reemplazo: "+pair.getKey() + " = " + pair.getValue());
        }
        if(loEncontro == false){
            //Aqui se le agrega como value el resultado del calcular
            variables.put(str[1], n);
            System.out.println("put NUEVO: "+str[1]+" "+n);
        }
    }

    public void write(String sublinea)
    {
        String str = sublinea.substring(6, sublinea.length()-1); //le quito el write y el ;
        System.out.println(str);
        String var = null;
        boolean esValido = false;
        if(str.startsWith("$")){
            int i;
            for(i = 0; i < str.length(); i++){
                if(str.charAt(i) == ' '){
                    var = str.substring(0, i);
                    System.out.println(var);
                    esValido = true;
                    break;
                }
            }
            if(esValido)
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
                        //Calcular y reemplazar
                    }
                }
                if(!loEncontro){
                    System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
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
                boolean loEncontro = false;

                BigInteger valor;

                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry) it.next();
                    //Si encuentro la key, por el de la operac
                    if(pair.getKey().equals(var))
                    {
                        valor = (BigInteger) (pair.getValue());
                        String operacion = linea.substring(i+1, linea.length());
                        operacion = valor +" "+ operacion;
                        System.out.println("op:"+ operacion);
                        loEncontro = true;
                        //Calcular y reemplazar
                    }
                }
                if(!loEncontro){
                    System.out.println("ERROR: La variable "+var+" no se encuentra inicializada");
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
