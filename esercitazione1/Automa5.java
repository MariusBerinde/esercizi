package esercitazione1;
/**
 * Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il linguaggio di 
 * stringhe che contengono matricola e cognome di studenti del turno 2 o del turno 3 del 
 * laboratorio, ma in cui il cognome precede il numero di matricola (in altre parole, le posizioni del
 * cognome e matricola sono scambiate rispetto all’Esercizio 1.3). Assicurarsi che il DFA sia minimo.
 */
public class Automa5 {
    public static boolean scan(String s)
    {
	int state = 0;
    int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
            case 0:  //* caso base 
                    if(Character.isLetter(ch)){
                        if(Character.isUpperCase(ch)){
                            if('a'<= Character.toLowerCase(ch) && Character.toLowerCase(ch)<= 'k')
                                state=1;
                            else if('l'<= Character.toLowerCase(ch) && Character.toLowerCase(ch)<= 'z')
                                state=2;
                        }
                           
                    }
            break;
            case 1:
                    if(Character.isLetter(ch)){
                        state=1;
                    }else if(Character.isDigit(ch)){
                        state=Integer.parseInt(String.valueOf(ch))%2==0?4:3; //** Scelgo tra turno 1 e turno 2 */
                        }
            break;
            case 2:
                        if(Character.isLetter(ch)){
                            state=2;
                        }else if(Character.isDigit(ch)){
                            state=Integer.parseInt(String.valueOf(ch))%2==0?6:5; //** Scelgo tra turno 3 e turno 4 */
                        }
            break;
            case 3: //* turno 1
                    if(Character.isDigit(ch)){
                         state=Integer.parseInt(String.valueOf(ch))%2==0?4:3; //** Scelgo tra turno 1 e turno 2 */
                    }
            break;
            case 4://* turno 2
                    if(Character.isDigit(ch)){
                        state=Integer.parseInt(String.valueOf(ch))%2==0?4:3; //** Scelgo tra turno 1 e turno 2 */
                    }
            
            break;
            case 5: //* turno 3
                    if(Character.isDigit(ch)){
                        state=Integer.parseInt(String.valueOf(ch))%2==0?6:5; //** Scelgo tra turno 3 e turno 4 */
                    }
            break;
            case 6: //* turno 4
                if(Character.isDigit(ch)){
                    state=Integer.parseInt(String.valueOf(ch))%2==0?6:5; //** Scelgo tra turno 3 e turno 4 */
                }
            break;
	    }

	}
        	return state == 4 || state==5 ;
    }

    private static void controlloAutoma(){
        System.out.println("I seguenti input dovrebbero funzionare:");
        System.out.println("Input Bianchi123456 da come output dall'automa="+(scan("Bianchi123456") ? "OK" : "NOPE"));
        System.out.println("Input Rossi654321 da come output dall'automa="+(scan("Rossi654321") ? "OK" : "NOPE"));
        System.out.println("Input Bianchi2 da come output dall'automa="+(scan("Bianchi2") ? "OK" : "NOPE"));
        System.out.println("Input B122 da come output dall'automa="+(scan("B122") ? "OK" : "NOPE"));
        
        System.out.println("I seguenti input non dovrebbero funzionare:");
        System.out.println("Input Bianchi654321 da come output dall'automa="+(scan("Bianchi654321") ? "OK" : "NOPE"));
        System.out.println("Input Rossi123456 da come output dall'automa="+(scan("Rossi123456") ? "OK" : "NOPE"));
        System.out.println("Input 654322 da come output dall'automa="+(scan("654322") ? "OK" : "NOPE"));
        System.out.println("Input Rossi da come output dall'automa="+(scan("Rossi") ? "OK" : "NOPE"));
        
    }    
    
   public static void main(String[] args) {
       //System.out.println(scan(args[0]) ? "OK" : "NOPE");
       controlloAutoma();
   } 
}
