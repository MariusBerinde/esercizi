package esercitazione2;

import java.io.*; 

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

	// ... gestire i casi di (, ), {, }, +, -, *, /, ; ... //
            case '(': 
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg; 
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br);
                
                    
                if (peek == '/'){

                    while(peek != '\n' ){
                        if( peek == (char)-1 )
                            break;
                        else
                            readch(br);
                            
                        } 
                        
                    
                }else if(peek == '*'){
                    var end=false;
                    if(peek == ' ')
                        readch(br);

                    while (!end) {
                        if(peek == ' ')
                        readch(br);
                        
                        if( peek == (char)-1 )
                            break;
                        if(peek == '*'){
                            readch(br);
                            if(peek == '/')
                                end=true;
                        }
                        readch(br);
                        peek = (char) -1;
                        readch(br);
                    }
                    if( !end ){
                        System.err.println("Error: the comment is not closed at line="+line);
                    }
                    
                }else{
                    // peek = ' '; 
                    
                    return Token.div;
                } 
                 this.peek='\n';      
                 readch(br);
            case ';':
                peek = ' ';
                return Token.semicolon;     
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }

	// ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //
            case '=':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.eq;
                } 
                else
                    return Token.assign;
            case '<':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.le;
                } if(peek=='>'){
                    peek=' ';
                    return Word.ne;
                }else 
                        return Word.lt;
            case '>':
                    readch(br);
                    if(peek == '='){
                            peek = ' ';
                            return Word.ge;
                        } 
                    else
                        return Word.gt;    

            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {

	// ... gestire il caso degli identificatori e delle parole chiave //
                    var word=""; 
                    word=word+peek;
                    readch(br);
                    while(peek != ' '){
                        
                            if(Character.isLetter(peek) || peek == '_'|| Character.isDigit(peek)){
                                
                                word=word+peek;
                                readch(br);
                                
                            }
                            else
                                break;

                        
                    }
                    
                    if (validWord(word)){

                        switch(word) {
                            case "cond":
                                return Word.cond;
                            case "when":
                                return Word.when;
                            case "then":
                                return Word.then;
                            case "else":
                                return Word.elsetok;
                            case "while":
                                return Word.whiletok;
                            case "do":
                                return Word.dotok;
                            case "seq":
                                return Word.seq;
                            case "print":
                                return Word.print;
                            case "read":    
                                return Word.read;
                            default: 
                                return new Word(Tag.ID,word);
                        }
                    }
                    else{
                        System.err.println("Erroneous forma word at word: " 
                            + word );
                    return null;
                    }
                } else if (Character.isDigit(peek)) {

                    var num="";
                    while (peek != ' '){
                        if( Character.isDigit(peek) ){
                            
                            num+=peek;
                            readch(br);
                        }
                        else
                            break;
                    }
                    return new NumberTok(num);
                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }
    }
		
    private boolean validWord(String word) {
        var validStart=Character.isLetter(word.charAt(0));
        var endChar=   Character.isLetter(word.charAt(word.length()-1)) || Character.isDigit(word.charAt(word.length()-1)) ;
        return validStart && endChar;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "esercitazione2\\input\\while1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}