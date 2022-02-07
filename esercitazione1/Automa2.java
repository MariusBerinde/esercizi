package esercitazione1;
/**
 * Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
in un linguaggio in stile Java: un identificatore `e una sequenza non vuota di lettere, numeri, ed il
simbolo di “underscore” _ che non comincia con un numero e che non pu `o essere composto solo
dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x 1”, “lft lab”, “ temp”, “x 1 y 2”,
“x__”, “__5”
Esempi di stringhe non accettate: “5”, “221B”, “123”, “9_to_5”, “___”
 */
public class Automa2 {
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
    
	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
            case 0 : //!Stato iniziale
                    if ( Character.isDigit(ch) ){
                        state=-1;
                    } else if ( Character.isLetter(ch)){
                        
                               
                                state=1;
                            }
                            else if( ch == '_')
                                    state=4;
            break;
            case 1 : //!Stato che controlla che sia lettera 
                    if ( Character.isLetter(ch)){
                        state=1;
                    }
                    else if( Character.isDigit(ch) ){
                            state=2;
                    }
                        else    if ( ch == '_') {
                            state=4;
                        }
                    break;
            case 2: //!Stato che controlla che sia numero
                    if ( Character.isLetter(ch)){
                         state=1;
                    }
                        else if( Character.isDigit(ch) ){
                            state=2;
                            }
                    else    if ( ch == '_') {
                            state=3;
                    }
            
                break;
            case 3: //! Controllo _ proveniente come secondo carattere letto
                if ( Character.isLetter(ch)){
                    state=1;
               
                    }
                    else if( Character.isDigit(ch) ){
                            state=2;
                        }
                    else  if ( ch == '_') {
                            state=3;
                            }
            break;
            case 4: //! Controllo _ come primo carattere
                    if ( Character.isLetter(ch)){
                             state=1;
                            }
                        else if( Character.isDigit(ch) ){
                                state=2;
                                }
                            else  if ( ch == '_') {
                                state=4;
                            }

        }
	}
    boolean lastLetter= state == 1;
    boolean lastNum = state == 2;
    boolean lastUnderScore = state == 3 ;
	return lastLetter  || lastNum || lastUnderScore;
    }

    private static void controlloAutoma(){
        System.out.println("I seguenti input dovrebbero funzionare:");
        System.out.println("Input x da come output dall'automa="+(scan("x") ? "OK" : "NOPE"));
        System.out.println("Input flag1 da come output dall'automa="+(scan("flag1") ? "OK" : "NOPE"));
        System.out.println("Input x2y2 da come output dall'automa="+(scan("x2y2") ? "OK" : "NOPE"));
        System.out.println("Input x_1 da come output dall'automa="+(scan("x_1") ? "OK" : "NOPE"));
        System.out.println("Input lft_lab da come output dall'automa="+(scan("lft_lab") ? "OK" : "NOPE"));
        System.out.println("Input _temp da come output dall'automa="+(scan("_temp") ? "OK" : "NOPE"));
        System.out.println("Input x_1_y_2 da come output dall'automa="+(scan("x_1_y_2") ? "OK" : "NOPE"));
        System.out.println("Input x__ da come output dall'automa="+(scan("x__") ? "OK" : "NOPE"));
        System.out.println("Input __5 da come output dall'automa="+(scan("__5") ? "OK" : "NOPE"));
        
        System.out.println("I seguenti output dovrebbero dare nope:");
        System.out.println("Input 5 da come output dall'automa="+(scan("5") ? "OK" : "NOPE"));
        System.out.println("Input 221B da come output dall'automa="+(scan("221B") ? "OK" : "NOPE"));
        System.out.println("Input 123 da come output dall'automa="+(scan("123") ? "OK" : "NOPE"));
        System.out.println("Input ___ da come output dall'automa="+(scan("___") ? "OK" : "NOPE"));
    }
    public static void main(String[] args)
    {
	//System.out.println(scan(args[0]) ? "OK" : "NOPE");
        controlloAutoma();
    }
}
