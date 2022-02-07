package esercitazione1;
/**
 * Modificare l’automa dell’esercizio precedente in modo che riconosca le combina-zioni di matricola e cognome di studenti 
 * del turno 2 o del turno 3 del laboratorio, dove il numero 
 * di matricola e il cognome possono essere separati da una sequenza di spazi, e possono essere
 * precedute e/o seguite da sequenze eventualmente vuote di spazi. Per esempio, l’automa deve
 * accettare la stringa “654321 Rossi” e “ 123456 Bianchi ” (dove, nel secondo esempio, ci
 * sono spazi prima del primo carattere e dopo l’ultimo carattere), ma non “1234 56Bianchi” e
 * “123456Bia nchi”. Per questo esercizio, i cognomi composti (con un numero arbitrario di parti) 
 * possono essere accettati: per esempio, la stringa “123456De Gasperi” deve essere accettato
 */
public class Automa4 {
    public static boolean scan(String s)
    {
	int state = 0;
    int prevState=0;
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
                        
                    }else if(Character.isWhitespace(ch)){
                        state=1;
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
                        
                    }else if (Character.isWhitespace(ch)) {
                        state=2;
                    }else{
                        state=-1;
                    }
                    
                    break;
                    case 3:
                             if(Character.isLetter(ch)){
                                    state=3;
                            }
                            else if(Character.isWhitespace(ch)){
                                prevState=3;
                                state=7;
                            }
                    break;
                    case 4: 
                             if(Character.isLetter(ch)){
                                        state=4;
                            }
                            else if(Character.isWhitespace(ch)){
                                prevState=4;
                                state=7;
                            }
                    break;
                    case 5:
                        
                         if(Character.isLetter(ch))
                            state=5;
                        else if(Character.isWhitespace(ch)){
                                prevState=5;
                                state=7;
                            }
                
                    break;
                    case 6:
                             if(Character.isLetter(ch)){
                                    state=6;
                            }else if(Character.isWhitespace(ch)){
                                prevState=6;
                                state=7;
                            }
                    break;
                    case 7: 
                            if(Character.isUpperCase(ch)){
                                state=prevState;
                                prevState=7;
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
        System.out.println("Input 123456 Bianchi da come output dall'automa="+(scan("123456 Bianchi") ? "OK" : "NOPE"));
        System.out.println("Input 123456De Gasperi da come output dall'automa="+(scan("123456De Gasperi") ? "OK" : "NOPE"));
        System.out.println("Input 654321Rossi da come output dall'automa="+(scan("654321Rossi") ? "OK" : "NOPE"));
        System.out.println("Input 2Bianchi da come output dall'automa="+(scan("2Bianchi") ? "OK" : "NOPE"));
        System.out.println("Input 122B da come output dall'automa="+(scan("122B") ? "OK" : "NOPE"));
        
        System.out.println("I seguenti input non dovrebbero funzionare:");
        System.out.println("Input 654321Bianchi da come output dall'automa="+(scan("654321Bianchi") ? "OK" : "NOPE"));
        System.out.println("Input 123456Rossi da come output dall'automa="+(scan("123456Rossi") ? "OK" : "NOPE"));
        System.out.println("Input 654322 da come output dall'automa="+(scan("654322") ? "OK" : "NOPE"));
        System.out.println("Input Rossi da come output dall'automa="+(scan("Rossi") ? "OK" : "NOPE"));
        System.out.println("Input 123456Bian chi da come output dall'automa="+(scan("123456Bian chi") ? "OK" : "NOPE"));
        
    }    
   public static void main(String[] args) {
    //System.out.println(scan(args[0]) ? "OK" : "NOPE");
       controlloAutoma();
   } 
}