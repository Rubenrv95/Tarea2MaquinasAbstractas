public class Lexer {

    public Lexer(){

    }

    public boolean isNumero(String str)
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
            if (!isDigito(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDigito(char c){
        if (c < '0' || c > '9') {
            return false;
        }
        return true;
    }

    public boolean isOperando(char c) {
        switch (c) {
            case'+':
                return true;
            case'-':
                return true;
            case'*':
                return true;
            case'/':
                return true;
            case'%':
                return true;
        }

        return false;
    }
}
