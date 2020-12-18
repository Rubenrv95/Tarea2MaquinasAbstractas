import java.util.HashMap;

public class Ejecucion {
    HashMap h = new HashMap();

    public Ejecucion() {
    }


    public int calcular (String s) {

        return 0;
    }

    public String verificarVariable(String s) {


        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i));
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
