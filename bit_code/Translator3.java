package bit_code;

/**
 * Versione finale del traduttore in cui non funzionano cicli annidati e cicli dentro if
 */
import esercitazione2.Lexer;
import esercitazione2.Tag;
import esercitazione2.Token;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import esercitazione2.Word;
import esercitazione2.NumberTok;
public class Translator3 {

  private Lexer lex;
  private BufferedReader pbr;
  private Token look;
  SymbolTable st = new SymbolTable();
  CodeGenerator code = new CodeGenerator();
  int count = 0;
  boolean isWhile=false; 
  boolean isEpr=false;
  private int labelIf=-1;
  private int nextIf=0;
  private int labelAfterIf=-1;
  private int labelWhile=-2;  // if ==-1 means that the value was used
  public Translator3(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
  }
  
  void move() {
    look = lex.lexical_scan(pbr);
    System.out.println("token = " + look);
  }

  void error(String s) {
    throw new Error("near line " + lex.line + ": " + s);
  }

  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error("syntax error");
  }

  public void prog() {
      boolean disableLookTag=false;
    while(look.tag!=Tag.EOF){
        int lnext_prog =-1; 
        if(labelAfterIf!=-1){
            lnext_prog=labelAfterIf;
            if (look.tag==Tag.WHILE || look.tag==Tag.COND) {
                disableLookTag=true;
            }
            statlist(lnext_prog,false);
            if(!disableLookTag)   
                labelAfterIf=-1;
            disableLookTag=false;
        }
        else{
            lnext_prog=code.newLabel();
            statlist(lnext_prog,false);

        }


    }
        
        match(Tag.EOF);
        
        if(labelAfterIf==-1)
            code.emitLabel(code.newLabel());
        else
            code.emitLabel(labelAfterIf);
        
    try {
    code.toJasmin();
    }
    catch(java.io.IOException e) {
    System.out.println("IO error\n");
    }
    
    }

    
    
    private void statlist(int label,boolean isExpression){
        
        stat(label);
        statlistp(isExpression);
        
    }
    
    private void statlistp(boolean isExpression){ 
        switch (look.tag) {
            case 59:
                match(';');
                if(isExpression){
                    int newLabel=-1;
                    if(labelAfterIf!=-1){
                        newLabel=labelAfterIf;
                        statlist(newLabel,true);
                        labelAfterIf=-1;
                    }else{
                        newLabel=code.newLabel();
                        statlist(newLabel, true);
                    }
                }
                        
            break;
            case Tag.EOF:
            case '}':
            case ')':   
                 if((code.label-1) == labelIf)
                   code.emit(OpCode.GOto,nextIf);
             //   if(look.tag=='}' )
               //     match('}');
                break;
            default:
                //stat(-1);
              //error("syntax error in stat");
               
        }
        
            }
    private void stat(int actualLabel){ //todo:check me
        if(actualLabel!=-1)
            code.emitLabel(actualLabel);
        switch(look.tag){
            case 61:    // = ID    
                match(61);
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                        }
                        match(Tag.ID);
                        expr(1);
                        code.emit(OpCode.istore,id_addr);
                    }
                    else 
                        error("Error in grammar (= id expr) " + look);
                                
                    
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match(40);
                exprlist();
                if(look.tag==')')
                    match(41);
                else 
                    error("missing ) in the print ");
                break;
            case Tag.READ:
                match(Tag.READ);
                    match('(');
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                        }
                    
                        match(Tag.ID);
                        code.emit(OpCode.invokestatic,0);
                        code.emit(OpCode.istore,id_addr);
                    }
                    else 
                        error("Error in grammar (stat) after read( with " + look);
                match(')');
                break;
            case Tag.COND:
                match(Tag.COND);
                int labelAfterCond=code.newLabel();
                int labelElse=whenlist(labelAfterCond);

                stat(labelElse);
                

                if(isWhile){
                    if( look.tag=='}' ){
                        code.emitLabel(labelAfterCond);
                        code.emit(OpCode.GOto, labelWhile);
                        labelWhile=-1;
                    }
                    else
                        statlist(labelAfterCond, true);
                }else{

                    code.emit(OpCode.GOto, labelAfterCond);
                    labelAfterIf=labelAfterCond;
                }
                break;
            case Tag.WHILE:
                isWhile=true;
                int labelExecution=-1;
                int labelAfterWhile=code.newLabel();
               this.labelWhile=actualLabel;
                match(Tag.WHILE);
                match(40);
                labelExecution=bexpr1(code.newLabel(), labelAfterWhile);
                System.out.println("code.label="+this.code.label);
                match(41);
                if(look.tag==Tag.WHILE)
                    statlist(labelExecution, true);
                else
                  stat(labelExecution);
                labelAfterIf=labelAfterWhile;
                if(labelWhile!=-1)
                    code.emit(OpCode.GOto, labelExecution-1);
                isWhile=false;
                break;
            case '{':  
                this.code.instructions.removeLast();
                match('{');
                statlist(actualLabel, true);
                match('}');
                break;
            case Tag.ELSE:
                this.code.instructions.removeLast();
                match(Tag.ELSE);
                statlist(actualLabel, false);    
            break;
            default:
                    error("syntax error in stat");
            
        }
        
    }
    private int whenlist(int labelAfterWhen){
        int labelFirstDo=code.newLabel();
     
        int nextLabel=-1;
        
        while(look.tag==Tag.WHEN){
              
            if(nextLabel==-1){
                nextLabel=whenitem(labelFirstDo);  
            }
            else{
                nextLabel=whenitem(nextLabel);
            }
            code.emit(OpCode.GOto, labelAfterWhen);
        }
        
        return nextLabel;
    }
    /**
     * 
     * @param mode 1 assegnamento , 0 lettura
     */
    private int whenitem(int labelDo){
        int labelElse=code.newLabel();
      
        match(Tag.WHEN);
        
        match(40);
       int labelExe= bexpr1(labelDo,labelElse);
        match(41);
        match(Tag.DO);
        stat(labelExe);
        
        return labelElse;
    }
    /**
     * 
     * @param labelDo label di stat
     * @param labelElse label ramo else
     * @param actualLabel
     */
    private int bexpr1(int labelDo,int labelElse){
        if(labelDo==-1)
            code.emitLabel(code.newLabel());
        else
            code.emitLabel(labelDo);
          int labelExe= code.newLabel();  
          var operazione=((Word)look).lexeme;
          System.out.println("operando="+operazione);      
          match(Tag.RELOP);
          expr(0);
          expr(0);
        switch (operazione) {
            case ">":
            System.out.println("tag >");
            code.emit(OpCode.if_icmpgt,labelExe);
            break;
            case ">=":
            System.out.println("tag >=");
                
            code.emit(OpCode.if_icmpge,labelExe);
            break;
            case "<":
            System.out.println("tag <");
            code.emit(OpCode.if_icmplt,labelExe);
            break;
            case "<=":
                System.out.println("tag <=");
            code.emit(OpCode.if_icmple,labelExe);
            break;
            case "<>":
            System.out.println("tag <>");
            code.emit(OpCode.if_icmpne,labelExe);
            break;
            case "==":
            System.out.println("tag ==");
            code.emit(OpCode.if_icmpeq,labelExe);
            break;
            default:
            System.out.println("errore:  RELOP non riconosciuto");
            break;
        }
        code.emit(OpCode.GOto, labelElse);
        return labelExe;
    }

    /**
     * 
     * @param mode 1 assegnamento , 0 lettura
     */
     private void expr(int mode){ 
        switch(look.tag){
            case '+':
                match('+');
                if(look.tag=='('){
                    match('(');
                    int nrExpr=0;
                    while(look.tag!=')'){
                        expr(0);
                        nrExpr++;
                    }
                    match(')');
                    for(int i=0; i < (nrExpr-1); code.emit(OpCode.iadd), i++);
                }else{
                    expr(0);
                    expr(0);
                    code.emit(OpCode.iadd);
                }
            break;
            case '-': 
                match('-');
                if(look.tag=='('){
                    match('(');
                    expr(0);
                    expr(0);
                    match(')');
                    }else{
                        expr(0);
                        expr(0);
                    }
                code.emit(OpCode.isub);
            break;
            case '*': 
                match('*');
                if(look.tag=='('){
                    match('(');
                    int nrExpr=0;
                    while(look.tag!=')'){
                        expr(0);
                        nrExpr++;
                    }
                    match(')');
                    for(int i=0; i < (nrExpr-1); code.emit(OpCode.imul), i++);

                }else{
                    expr(0);
                    expr(0);
                    code.emit(OpCode.imul);
                }
            break;
            case '/': 
                match('/');
                if(look.tag=='('){
                    match('(');
                    expr(0);
                    expr(0);
                    match(')');  
                }
                else{
                    expr(0);
                    expr(0);
                } 

                code.emit(OpCode.idiv);
            break;
            case Tag.NUM:
                code.emit(OpCode.ldc, Integer.valueOf(((NumberTok) look).lexeme));
                match(Tag.NUM);
            break;
            case Tag.ID:
                    if(mode==1){
                        int id_addr = st.lookupAddress(((Word)look).lexeme);
                        if (id_addr==-1) {
                            id_addr = count;
                            st.insert(((Word)look).lexeme,count++);
                            }
                            match(Tag.ID);
                            code.emit(OpCode.invokestatic,0);
                            code.emit(OpCode.istore,id_addr);
                            code.emit(OpCode.label);
                    }
                    if(mode==0){     
                        var addr=st.lookupAddress(((Word)look).lexeme);
                        code.emit(OpCode.iload, addr);
                        match(Tag.ID);
                    }
                
                break;        
            default: 
                error("syntax error in expr");  
        }
    }
    /* 
     *  @param manyArguments true l'operazione si aspetta più di 2 argomenti , false altrimenti
     */
    private void exprlist(){  
        var toPrint=look.tag=='+'||look.tag=='-'||look.tag=='/'||look.tag=='*'||look.tag==Tag.NUM || look.tag==Tag.ID;
        
        while(toPrint){
            expr(0);
            code.emit(OpCode.invokestatic,1);
            if(look.tag==')')
                break;
        }
        
    }
    
    
  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "bit_code\\test\\testFinali\\tf12.txt";  
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));

      Translator3 translator = new Translator3(lex, br);
      translator.prog();
      System.out.println("Input OK");
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}