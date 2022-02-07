package esercitazione1;
/**
 * Modificare l’automa dell’esercizio precedente in modo che riconosca il linguaggio 
 * di stringhe (sull’alfabeto {/, *, a}) che contengono “commenti” delimitati da /* e * /, 
 * ma con la possibilità di avere stringhe prima e dopo come specificato qui di seguito. 
 * L’idea `e che sia possibile avere eventualmente commenti (anche multipli) immersi in una sequenza di simboli
 * dell’alfabeto. Quindi l’unico vincolo `e che l’automa deve accettare le stringhe in cui un’occorrenza 
 * della sequenza /* deve essere seguita (anche non immediatamente) da un’occorrenza della
 * sequenza * /. Le stringhe del linguaggio possono non avere nessuna occorrenza della sequenza
 * /* (caso della sequenza di simboli senza commenti). Implementare l’automa seguendo la costruzione vista in Listing 1.
 * Esempi di stringhe accettate: “ a a a/ * * * * /a a”, “a a/ * a * a * /”, “ a a a a ”, “ / * * * * /”, “ / * a a * /”,
 *  “ * / a ”, “ a / * * / * * * a ”, “ a / * * /* * * / a ”, “ a / * * / a a / * * * / a”
 * Esempi di stringhe non accettate: “ a a a / * / a a ”, “ a / * * / / * * * a”, “ a a / * a a ”
 */
public class Automa10 {
    private static boolean belongsLanguage(char ch){
        boolean isStar = ch == '*';
        boolean isSlash = ch == '/';
        boolean isLetter = ch == 'a';
        return isStar || isSlash || isLetter;
    }
    public static boolean scan(String s){
        int state = 0;
        
        int i = 0;
    
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
    
            switch (state) {
                case 0: 
                    if(belongsLanguage(ch)){
                        if(ch=='/')
                            state=1;
                        else
                            state=0;
                    }else{
                     state=-1;   
                    }
                break;
                case 1:
                        if(belongsLanguage(ch)){
                                if(ch=='*')
                                    state=2;
                                else
                                    state=0;
                                }else{
                        state=-1;   
                        }
                break;
                case 2:
                        if(belongsLanguage(ch)){
                            if( ch == 'a' || ch == '/' )
                                state=2;
                            else
                                state=3;
                            }else{
                            state=-1;   
                        }
                break;
                case 3:
                        if(belongsLanguage(ch)){
                            if( ch == 'a' )
                                state=2;
                            else if( ch == '*')
                                state=3;
                                else 
                                    state=4;
                        }else{
                            state=-1;   
                        }
                break;
                case 4: 
                    if(belongsLanguage(ch)){
                        if( ch == '/' )
                            state=1;
                        else 
                            state=0;
                }else{
                    state=-1;   
                }
            }
    
        }
                return  state == 0 || state == 4 ;
        }
    
        private static void controlloAutoma(){
            System.out.println("I seguenti input dovrebbero funzionare:");
            System.out.println("Input aaa/****/aa da come output dall'automa="+(scan("aaa/****/aa") ? "OK" : "NOPE"));
            System.out.println("Input aa/*a*a*/ da come output dall'automa="+(scan("aa/*a*a*/") ? "OK" : "NOPE"));
            System.out.println("Input aaaa da come output dall'automa="+(scan("aaaa") ? "OK" : "NOPE"));
            System.out.println("Input /****/ da come output dall'automa="+(scan("/****/") ? "OK" : "NOPE"));
            System.out.println("Input /*aa*/ da come output dall'automa="+(scan("/*aa*/") ? "OK" : "NOPE"));
            System.out.println("Input */a da come output dall'automa="+(scan("*/a") ? "OK" : "NOPE"));
            System.out.println("Input a/**/***a da come output dall'automa="+(scan("a/**/***a") ? "OK" : "NOPE"));
            System.out.println("Input a/**/aa/***/a da come output dall'automa="+(scan("a/**/aa/***/a") ? "OK" : "NOPE"));
            
            
            System.out.println("\nI seguenti input non dovrebbero funzionare:");
            System.out.println("Input aaa/*/aa da come output dall'automa="+(scan("aaa/*/aa") ? "OK" : "NOPE"));
            var inputProblematico="a/**//***a";
            System.out.println("Input  a /**/ /***a da come output dall'automa="+(scan(inputProblematico) ? "OK" : "NOPE"));
            System.out.println("Input aa/*aa da come output dall'automa="+(scan("aa/*aa") ? "OK" : "NOPE"));
            
        }
   public static void main(String[] args) {
       controlloAutoma();
   }
}
