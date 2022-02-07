package esercitazione1;

/**
 * Esercizio 1.7. Progettare e implementare un DFA con alfabeto {a, b}che riconosca il linguaggio
 * delle stringhe tali che a occorre almeno una volta in una delle ultime tre posizioni della stringa.
 * Come nell’esercizio 1.6, il DFA deve accettare anche stringhe che contengono meno di tre simboli
 * (ma almeno uno dei simboli deve essere a).
 * Esempi di stringhe accettate: “abb”, “bbaba”, “baaaaaaa”, “aaaaaaa”, “a”, “ba”, “bba”,
 * “aa”, “bbbababab”
 * Esempi di stringhe non accettate: 
 * “abbbbbb”, “bbabbbbbbbb”, “b”
 */

public class Automa7 {
    private static boolean belongsAlphabet(char ch){
        boolean isLetter=Character.isLetter(ch);
        boolean isLowerCase=Character.isLowerCase(ch);
        boolean isInAlphabet= ch=='a' || ch=='b';
        return isLetter && isLowerCase && isInAlphabet;
    }
    public static boolean scan(String s){
	int state = 0;
    
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
            case 0:  //* stato iniziale 
                    if(belongsAlphabet(ch)){
                        if(ch=='a')
                            state=1;
                        else
                            state=0;
                    }
                    else 
                        state=-1;
            break;
            case 1:
                    if(belongsAlphabet(ch)){
                        if(ch=='a')
                            state=1;
                        else
                            state=2; 
                    }else{ //* secondo b
                        state=-1;
                    }
                    break;
            case 2:
                    if(belongsAlphabet(ch)){
                        if(ch=='a')
                            state=1;
                        else
                            state=3; 
                    }else{ //* secondo b
                        state=-1;
                    }
                    
            break;
            case 3:
                    if(belongsAlphabet(ch)){
                        if(ch=='a')
                            state=1;
                        else    
                            state=0;
                    }
                    else
                        state=-1;
            break;
	    }

	}
        	return state == 1 || state==2 || state==3;
    }

    private static void controlloAutoma(){
        System.out.println("I seguenti input dovrebbero funzionare:");
        System.out.println("Input abb da come output dall'automa="+(scan("abb") ? "OK" : "NOPE"));
        System.out.println("Input bbaba da come output dall'automa="+(scan("bbaba") ? "OK" : "NOPE"));
        System.out.println("Input baaaaaaa da come output dall'automa="+(scan("baaaaaaa") ? "OK" : "NOPE"));
        System.out.println("Input aaaaaaa da come output dall'automa="+(scan("aaaaaaa") ? "OK" : "NOPE"));
        System.out.println("Input a da come output dall'automa="+(scan("a") ? "OK" : "NOPE"));
        System.out.println("Input ba da come output dall'automa="+(scan("ba") ? "OK" : "NOPE"));
        System.out.println("Input bba da come output dall'automa="+(scan("bba") ? "OK" : "NOPE"));
        System.out.println("Input aa da come output dall'automa="+(scan("aa") ? "OK" : "NOPE"));
        System.out.println("Input bbbababab da come output dall'automa="+(scan("bbbababab") ? "OK" : "NOPE"));
        
        
        System.out.println("I seguenti input non dovrebbero funzionare:");
        System.out.println("Input abbbbbb da come output dall'automa="+(scan("abbbbbb") ? "OK" : "NOPE"));
        System.out.println("Input bbabbbbbbbb da come output dall'automa="+(scan("bbabbbbbbbb") ? "OK" : "NOPE"));
        System.out.println("Input b da come output dall'automa="+(scan("b") ? "OK" : "NOPE"));
        
    }
    public static void main(String[] args) {
        //System.out.println(scan(args[0]) ? "OK" : "NOPE");
        controlloAutoma();
    }
}
