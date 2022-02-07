package valutatore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import esercitazione2.*;
public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }
    private void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }
    
    private void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    private void match(int t){
        if ( look.tag == t){
            if ( look.tag != Tag.EOF)
                move();
        }
        else
            error("syntax error");
    }
    private static void println(String x) {
        System.out.println(x);
    }
    public void start() {
        int expr_val=0;
        var firstFact = look.tag == 40 || look.tag == Tag.NUM;
        var firstTermp = look.tag== '*' || look.tag == '/';
        var firstTerm= firstFact || firstTermp;
        var firstExprp = look.tag == '+' || look.tag == '/';
        var firstExpr = firstTerm || firstExprp;
        if( firstExpr){
            expr_val=expr();
            match(Tag.EOF);
            println(Integer.toString(expr_val));
        } else
            error("syntax error in start()");
        }
    
    private int expr() {
       
        var firstFact = look.tag == '(' || look.tag == Tag.NUM;
        var firstTermp = look.tag== '*' || look.tag == '/';
        var firstTerm= firstFact || firstTermp;
        var firstExprp = look.tag == '+' || look.tag == '/';
        var firstExpr = firstTerm || firstExprp;
        int  exprp_val=0; 
        if( firstExpr){
            var term_val = term();
            exprp_val=exprp(term_val);
        } else
            error("syntax error in expr()");
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val=0, exprp_val=0;
        switch (look.tag) {
        case '+':
            match(43);   // 43 è il tag di +
            term_val=term();
            exprp_val=exprp(exprp_i+term_val);
        break;
        case '-':
            match(45); // 45 è il tag di -
            term_val=term();
            exprp_val=exprp(exprp_i-term_val);
        break;
        case Tag.EOF:
        case Tag.NUM:
        case ')':      
            exprp_val=exprp_i;
            break;    
        default:
            error("syntax error in exprp");
        }
        return exprp_val;
    }
    private int term() {
        var firstFact = look.tag == '(' || look.tag == Tag.NUM;
        var firstTermp = look.tag== '*' || look.tag == '/';
        var firstTerm= firstFact || firstTermp;
        int term_val=0;
        if(firstTerm){
                var termp_val=fact();
                
                term_val=termp(termp_val);
        }else{
            error("syntax error in term");
        }
        return term_val;
    }
    
    private int termp(int termp_i) {
        int term_val=0,termp_val=0;
        switch (look.tag) {
            case '*':  
                match(42);
                term_val=fact();
                termp_val=termp(term_val * termp_i);                
                break;
                case '/':
                match(47);
          
                var valDiv=fact();
                
                termp_val=termp( termp_i / valDiv);                
                break;
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                termp_val=termp_i;
                break;   
            default:
            error("syntax error in termp"); 
        }
        return termp_val;
    }

    private int fact() {
        int fact_value=0;
        if (look.tag == Tag.NUM){
            
            fact_value= Integer.valueOf(((NumberTok)look).lexeme);  
            match(Tag.NUM);
            
        }else if(look.tag == 40){
            match(40);  // il tag di (
            fact_value=expr();
            match(41);  // codice 41 indica )
        } else{
            error("fact");
        }
        return fact_value;
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "valutatore\\input\\test12.txt"; // il percorso del file da leggere
        try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Valutatore valutatore = new Valutatore(lex, br);
        valutatore.start();
        br.close();
        } catch (IOException e) {
            e.printStackTrace();}
            }
}
