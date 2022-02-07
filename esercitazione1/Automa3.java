package esercitazione1;
/**
 * progettare e implementare un DFA che riconosca il linguaggio di stringhe che
    contengono un numero di matricola seguito (subito) da un cognome, dove la combinazione di
    matricola e cognome corrisponde a studenti del turno 2 o del turno 3 del laboratorio di Linguaggi
    Formali e Traduttori
 */
public class Automa3 {
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
            case 0: //!Stato iniziale
                    if(Character.isDigit(ch)){
                        state=Integer.parseInt(String.valueOf(ch))%2==0?2:1;   
                    }
                    else 
                        state=-1;
                    break;
            case 1: //* Scremo i numeri dispari 
                    if(Character.isDigit(ch)){
                        state=Integer.parseInt(String.valueOf(ch))%2==0?2:1;   
                    }
                    else if(Character.isLetter(ch)){
                        if('a'<= Character.toLowerCase(ch) && Character.toLowerCase(ch)<= 'k')  //* Turno T1
                            state=3;
                        else if('l'<= Character.toLowerCase(ch) && Character.toLowerCase(ch)<= 'z')  //* turno T3
                        state=4;
                        i=s.length();
                    }else{
                        state=-1;
                    }

                    break;
            case 2: //* Scremo i numeri pari
                    if(Character.isDigit(ch)){
                        state=Integer.parseInt(String.valueOf(ch))%2==0?2:1;   
                    }
                    else if(Character.isLetter(ch)){
                        if('a'<= Character.toLowerCase(ch) && Character.toLowerCase(ch)<= 'k') //* turno T2
                            state=5;
                        else if('l'<= Character.toLowerCase(ch) && Character.toLowerCase(ch)<= 'z') //*Turno t4
                            state=6;
                        i=s.length();
                    }else{
                        state=-1;
                    }
                    
                    break;
	    }

	}
        	return state == 4 || state==5 ;
    }

    private static void controlloAutoma(){
        System.out.println("I seguenti input dovrebbero funzionare:");
        System.out.println("Input 123456Bianchi da come output dall'automa="+(scan("123456Bianchi") ? "OK" : "NOPE"));
        System.out.println("Input 654321Rossi da come output dall'automa="+(scan("654321Rossi") ? "OK" : "NOPE"));
        System.out.println("Input 2Bianchi da come output dall'automa="+(scan("2Bianchi") ? "OK" : "NOPE"));
        System.out.println("Input 122B da come output dall'automa="+(scan("122B") ? "OK" : "NOPE"));
        
        System.out.println("I seguenti input non dovrebbero funzionare:");
        System.out.println("Input 654321Bianchi da come output dall'automa="+(scan("654321Bianchi") ? "OK" : "NOPE"));
        System.out.println("Input 123456Rossi da come output dall'automa="+(scan("123456Rossi") ? "OK" : "NOPE"));
        System.out.println("Input 654322 da come output dall'automa="+(scan("654322") ? "OK" : "NOPE"));
        System.out.println("Input Rossi da come output dall'automa="+(scan("Rossi") ? "OK" : "NOPE"));
    
    }    
   public static void main(String[] args) {
    //  System.out.println(scan(args[0]) ? "OK" : "NOPE");
       controlloAutoma();
   } 
}
