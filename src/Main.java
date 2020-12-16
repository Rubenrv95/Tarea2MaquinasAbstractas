

public class Main {
    public static void main(String[] args){

        String numero = "1";
        Lexer l = new Lexer();

        System.out.println(l.isDigito('c'));
        System.out.println(l.isNumero(numero));
    }
}
