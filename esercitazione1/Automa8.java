package esercitazione1;
/**
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
 * contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome
 * con un altro qualsiasi. Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA deve
 * accettare la stringa “Paolo” (cio`e il nome scritto correttamente), ma anche le stringhe “Pjolo”,
 * “caolo”, “Pa%lo”, “Paola” e “Parlo” (il nome dopo la sostituzione di un carattere), ma non
 * “Eva”, “Perro”, “Pietro” oppure “P*o*o”.
 */
public class Automa8 {
    public static boolean scan(String s){
        int state = 0;
        
        int i = 0;
    
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
    
            switch (state) {
                case 0: 
                    state=(ch=='m' || ch=='M')?1:7;
                break;
                case 1:
                    state=(ch=='a' || ch=='A')?2:13;
                    break;
                case 2:
                    state=(ch=='r' || ch=='R')?3:18;
                    break;
                case 3:
                        state=(ch=='i' || ch=='I')?4:22;        
                break;    
                case 4: 
                    state=(ch=='u' || ch=='U')?5:25;
                    break;
                case 5:
                    state=(ch=='s' || ch=='S')?6:27;  
                    break;
                case 7:
                    state=(ch=='a' || ch=='A')?8:-1;
                    break;
                case 8:
                    state=(ch=='r' || ch=='R')?9:-1;
                    break;
                case 9:
                        state=(ch=='i' || ch=='I')?10:-1;        
                break;    
                case 10: 
                    state=(ch=='u' || ch=='U')?11:-1;
                    break;
                case 11:
                    state=(ch=='s' || ch=='S')?12:-1;
                   break;
               case 13:
                   state=(ch=='r' || ch=='R')?14:-1;
                   break;
               case 14:
                       state=(ch=='i' || ch=='I')?15:-1;        
               break;    
               case 15: 
                   state=(ch=='u' || ch=='U')?16:-1;
                   break;
               case 16:
                   state=(ch=='s' || ch=='S')?17:-1;
                  break;
                case 18:
                  state=(ch=='i' || ch=='I')?19:-1;        
                break;    
                case 19: 
                    state=(ch=='u' || ch=='U')?20:-1;
                     break;
                case 20:
                    state=(ch=='s' || ch=='S')?21:-1;
                break;
                case 22: 
                    state=(ch=='u' || ch=='U')?23:-1;
                     break;
                case 23:
                    state=(ch=='s' || ch=='S')?24:-1;
                break;
                case 25:
                state=(ch=='s' || ch=='S')?26:-1;
                    break;
            }
    
        }
                return state == 6 || state==12 || state==17 || state==21 || state==24 || state==26 || state==27 ;
        }
    
        private static void controlloAutoma(){
            System.out.println("I seguenti input dovrebbero funzionare:");
            System.out.println("Input marius da come output dall'automa="+(scan("marius") ? "OK" : "NOPE"));
            System.out.println("Input darius da come output dall'automa="+(scan("darius") ? "OK" : "NOPE"));
            System.out.println("Input madius da come output dall'automa="+(scan("madius") ? "OK" : "NOPE"));
            System.out.println("Input markus da come output dall'automa="+(scan("markus") ? "OK" : "NOPE"));
            System.out.println("Input marias da come output dall'automa="+(scan("marias") ? "OK" : "NOPE"));
            System.out.println("Input marium da come output dall'automa="+(scan("marium") ? "OK" : "NOPE"));
            
            
            System.out.println("I seguenti input non dovrebbero funzionare:");
            System.out.println("Input derkus da come output dall'automa="+(scan("derkus") ? "OK" : "NOPE"));
            System.out.println("Input markas da come output dall'automa="+(scan("markas") ? "OK" : "NOPE"));
            System.out.println("Input m*r*u* da come output dall'automa="+(scan("m*r*u*") ? "OK" : "NOPE"));
            
        }
   public static void main(String[] args) {
       //System.out.println(scan(args[0]) ? "OK" : "NOPE");
       controlloAutoma();
   } 
}
