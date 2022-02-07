package analisi_sintattica.esercitazione32;

import esercitazione2.Lexer;
import esercitazione2.Tag;
import esercitazione2.Token;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

  private void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error("syntax error");
  }

  private void prog() {
    var isAssign = look.tag == '=';
    var isBracket = look.tag == '{'; 
    var isCond = look.tag == Tag.COND;
    var isPoint = look.tag == ';';
    var isPrint = look.tag == Tag.PRINT;
    var isRead = look.tag == Tag.READ;
    var isWhile = look.tag == Tag.WHILE;
    var isId = look.tag == Tag.ID;

    var firstStatlist =isAssign || isBracket || isCond || isPoint || isRead || isWhile || isId;
    if (firstStatlist) {
      /* switch(look.tag){
                case '=':
                    match('=');
                break;
                case '{':
                    match('{');
                break;
                case Tag.COND:
                    match(Tag.COND);
                    break;
                case Tag.READ:
                    match(Tag.READ);
                    break;
                case Tag.WHILE:
                    match(Tag.WHILE);
                    break;
                case Tag.ID:
                    match(Tag.ID);
                break;
                default:
                    System.out.println("prog: tag not find");
            }*/
      statilist();
      match(Tag.EOF);
    } else error("syntax error in prog");
  }

  private void statilist() {
    stat();
    statilistp();
  }

  private void statilistp() {
    switch (look.tag) {
      case 59:
        match(';');
        stat();
        statilistp();
        break;
      case Tag.EOF:
      case '}':
        //     case ')':
        break;
      default:
        error("syntax error in sta");
        break;
    }
  }

  private void stat() {
    switch (look.tag) {
      case 61: // =ID
        match(61);
        match(Tag.ID);
        expr();
        break;
      case Tag.PRINT:
        match(Tag.PRINT);
        match(40);
        exprlist();
        match(41);
        break;
      case Tag.READ:
        match(Tag.READ);
        match(Tag.ID);
        break;
      case Tag.COND:
        match(Tag.COND);
        whenlist();
        match(Tag.ELSE);
        stat();
        break;
      case Tag.WHILE:
        match(Tag.WHILE);
        match(40);
        bexpr();
        match(41);
        stat();
        break;
      case 123: // case {
        match(123); // tag di {
        statilist();
        match(125); // tag di }
        break;
      default:
        error("syntax error in stat");
    }
  }

  private void whenlist() {
    if (look.tag == Tag.WHEN) {
      whenitem();
      whenlistp();
    } else error("syntax error in whenlist");
  }

  private void whenlistp() {
    switch (look.tag) {
      case Tag.WHEN:
        whenitem();
        whenlistp();
        break;
      case Tag.ELSE:
        break;
      default:
        error("syntax error in whenlistp");
    }
  }

  private void whenitem() {
    match(Tag.WHILE);
    match(40);
    bexpr();
    match(41);
    match(Tag.DO);
    stat();
  }

  private void bexpr() {
    match(Tag.RELOP);
    expr();
    expr();
  }

  private void expr() {
    switch (look.tag) {
      case '+':
        match('+');
        match('(');
        exprlist();
        match(')');
        break;
      case '-':
        match('-');
        expr();
        expr();
        break;
      case '*':
        match('*');
        match('(');
        exprlist();
        match(')');
        break;
      case '/':
        match('/');
        expr();
        expr();
        break;
      case Tag.NUM:
        match(Tag.NUM);
        break;
      case Tag.ID:
        match(Tag.ID);
        break;
      default:
        error("syntax error in expr");
    }
  }

  private void exprlist() {
    expr();
    exprlistp();
  }

  private void exprlistp() {
    switch (look.tag) {
      case '+':
      case '-':
      case '/':
      case '*':
      case Tag.NUM:
      case Tag.ID:
        expr();
        exprlistp();
        
        break;
      case ')':
        break;
      default:
        break;
    }
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "analisi_sintattica\\input\\stampa2.txt"; // il percorso del file da leggere
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Parser parser = new Parser(lex, br);
      parser.prog();
      System.out.println("Input OK");
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
