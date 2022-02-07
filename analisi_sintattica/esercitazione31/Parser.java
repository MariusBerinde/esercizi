package analisi_sintattica.esercitazione31;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import esercitazione2.Lexer;
import esercitazione2.Tag;
import esercitazione2.Token;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public Parser(Lexer l, BufferedReader br) {
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

    public void start() {
      //  move();
        var firstFact = look.tag == 40 || look.tag == Tag.NUM;
        var firstTermp = look.tag== '*' || look.tag == '/';
        var firstTerm= firstFact || firstTermp;
        var firstExprp = look.tag == '+' || look.tag == '/';
        var firstExpr = firstTerm || firstExprp;
        if( firstExpr){
            expr();
            match(Tag.EOF);
        } else
            error("syntax error in start()");
        }
    
    private void expr() {
       // move();
        var firstFact = look.tag == '(' || look.tag == Tag.NUM;
        var firstTermp = look.tag== '*' || look.tag == '/';
        var firstTerm= firstFact || firstTermp;
        var firstExprp = look.tag == '+' || look.tag == '/';
        var firstExpr = firstTerm || firstExprp;
        if( firstExpr){
         
                term();
                exprp();
        } else
            error("syntax error in expr()");
    }

    private void exprp() {
        
        switch (look.tag) {
        case '+':
            match(43);   // 43 è il tag di +
            term();
            exprp();
        break;
        case '-':
            match(45); // 45 è il tag di -
            term();
            exprp();
        break;
        case Tag.EOF:
        case Tag.NUM:
        case ')':
        
            break;    
        default:
            error("syntax error in exprp");
        }
    }
    private void term() {
        var firstFact = look.tag == '(' || look.tag == Tag.NUM;
        var firstTermp = look.tag== '*' || look.tag == '/';
        var firstTerm= firstFact || firstTermp;
        if(firstTerm){
      //      if( firstFact )
                fact();
                //move();
          //  if( firstTermp )
                termp();
        }else{
            error("syntax error in term");
        }
    }
    
    private void termp() {
        switch (look.tag) {
            case '*':  
                match(42);
                fact();
                termp();
                
                break;
            case '/':
                    match(47);
                    fact();
                    termp();
                break;
            case '+':
            case '-':
            case ')':
           // case Tag.NUM:
            case Tag.EOF:
            //    move();
                break;   
            default:
            error("syntax error in termp"); 
        }
    }

    private void fact() {
        if (look.tag == Tag.NUM){
            match(Tag.NUM);
        }else if(look.tag == 40){
            match('(');  // il tag di (
            expr();
            match(')');  // codice 41 indica )
        } else{
            error("fact");
        }
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "analisi_sintattica\\input\\test1.txt"; // il percorso del file da leggere
        try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Parser parser = new Parser(lex, br);
        parser.start();
        System.out.println("Input OK");
        br.close();
        } catch (IOException e) {e.printStackTrace();}
        }
}
