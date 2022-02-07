package esercitazione1;
/**
 * Progettare e implementare un DFA con alfabeto {/, *, a}che riconosca il linguaggio 
 * di “commenti” delimitati da / * (all’inizio) e * / (alla fine): cio`e l’automa deve accettare le 
 * stringhe che contengono almeno 4 caratteri che iniziano con / *, che finiscono con * /, e che contengono 
 * una sola occorrenza della sequenza * /, quella finale (dove l’asterisco della sequenza * / 
 * non deve essere in comune con quello della sequenza /* all’inizio).
 * Esempi di stringhe accettate: “/ * * * * /”, “/ * a * a * / ”, “/ * a / * * / ”, “ / * * a/
 * / / a /a * * / ”, “/* * / ”, “/ * / * / ”Esempi di stringhe non accettate: “/ * /”, “/ * * /* * * /”
 */
public class Automa9 {

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
                    }else{
                     state=-1;   
                    }
                break;
                case 1:
                        if(belongsLanguage(ch)){
                                if(ch=='*')
                                    state=2;
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
                            state=-1;
                }else{
                    state=-1;   
                }
            }
    
        }
                return  state == 4 ;
        }
    
        private static void controlloAutoma(){
            System.out.println("I seguenti input dovrebbero funzionare:");
            System.out.println("Input /****/ da come output dall'automa="+(scan("/****/") ? "OK" : "NOPE"));
            System.out.println("Input /*a*a*/ da come output dall'automa="+(scan("/*a*a*/") ? "OK" : "NOPE"));
            System.out.println("Input /*a/**/ da come output dall'automa="+(scan("/*a/**/") ? "OK" : "NOPE"));
            System.out.println("Input /**a///a/a**/ da come output dall'automa="+(scan("/**a///a/a**/") ? "OK" : "NOPE"));
            System.out.println("Input /**/ da come output dall'automa="+(scan("/**/") ? "OK" : "NOPE"));
            System.out.println("Input /*/*/ da come output dall'automa="+(scan("/*/*/") ? "OK" : "NOPE"));
            
            
            System.out.println("I seguenti input non dovrebbero funzionare:");
            System.out.println("Input /*/ da come output dall'automa="+(scan("/*/") ? "OK" : "NOPE"));
            System.out.println("Input /**/***/ da come output dall'automa="+(scan("/* */ * * */") ? "OK" : "NOPE"));
            
        }
   public static void main(String[] args) {
    // System.out.println(scan(args[0]) ? "OK" : "NOPE");
       controlloAutoma();
   }
           
}
