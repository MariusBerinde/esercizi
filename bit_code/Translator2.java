package bit_code;

/**
 * Versione finale del traduttore
 */
import esercitazione2.Lexer;
import esercitazione2.Tag;
import esercitazione2.Token;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import esercitazione2.Word;
import esercitazione2.NumberTok;
public class Translator2 {

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
  private List<Integer> labelList;  
  public Translator2(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
    labelList=new ArrayList<>();
  }

  private boolean singleLabel(int label){
      boolean risultato=true;
      if(labelList.size()==0){
          labelList.add(label);
          return true;
      }
     if(labelList.indexOf(label)==-1){
         labelList.add(label);
         return true;
     }
     return false;
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
    while(look.tag!=Tag.EOF){

        int lnext_prog = code.newLabel();
        while(!singleLabel(lnext_prog)){
            lnext_prog++;
        }
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
    }
        match(Tag.EOF);
    try {
    code.toJasmin();
    }
    catch(java.io.IOException e) {
    System.out.println("IO error\n");
    }
    // ... completare ...


  
    }

    
    private void statlist(int label){
        stat(-1);
       /* if( !isEpr)
            nextIf=label;*/
        statlistp(label);
        /*if(isWhile)
            goto(1)*/
    }
    private void statlistp(int label){ 
        switch (look.tag) {
            case 59:
                match(';');
                int new_label=code.newLabel();
                while(!singleLabel(new_label)){
                    new_label++;
                }
                code.emitLabel(new_label);
                stat(-1);
                statlistp(label);    
            break;
            case Tag.EOF:
            case '}':
            case ')':   
                 if((code.label-1) == labelIf)
                    code.emit(OpCode.GOto,nextIf);
                if(look.tag=='}')
                    match('}');
                break;
            default:
            break;
                //stat(-1);
              //  error("syntax error in sta");
               
        }
        
            }
    private void stat(int actualLabel){
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

                exprlist(false,false,false);
                if(look.tag==')')
                    match(41);
                code.emit(OpCode.invokestatic,1);
                /*if((code.label-1)==labelIf)
                    code.emit(OpCode.GOto, nextIf);*/
                break;
            case Tag.READ:
                match(Tag.READ);
                match(40);
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
                match(41);
                break;
            case Tag.COND:
                match(Tag.COND);
                
                int labelElse=whenlist();
                stat(labelElse);
                break;
            case Tag.WHILE:
                isWhile=true;
                int labelExecution=code.newLabel();
                while(!singleLabel(labelExecution)){
                    labelExecution++;
                }
                match(Tag.WHILE);
                match(40);
                bexpr(labelExecution,0);
                System.out.println("code.label="+this.code.label);
                match(41);
                stat(labelExecution);
                code.emit(OpCode.GOto, labelExecution-1);
                isWhile=false;
                break;
            case '{':    
                match('{');  
                labelIf++;
                statlist(code.label);
                if(look.tag==')')
                    match(41);
               // if(look.tag=='}')
                //    match('}');  // tag di }    
                isEpr=false;
                break;
            case Tag.ELSE:
                
               // code.emitLabel(actualLabel);
                match(Tag.ELSE);
                if(look.tag=='{')
                    match('{');
                stat(-1);
                if(look.tag=='}')
                    match('}');
                    
            break;
            default:
                    error("syntax error in stat");
            
        }
        
    }
    private int whenlist(){
        int labelFirstDo=code.newLabel();
        while(!singleLabel(labelFirstDo)){
            labelFirstDo++;
        }
        int nextLabel=-1;
        int nrWhen=0;
        while(look.tag==Tag.WHEN){
            if(nextLabel==-1)
                nextLabel=whenitem(true,labelFirstDo);
                else{
                int labelIF=code.newLabel();
                while(!singleLabel(labelIF)){
                    labelIF++;
                }
                code.emitLabel(labelIF);
                nextLabel=whenitem(false,nextLabel);
            }
            nrWhen++;
        }
        if(nrWhen==1)
            nextLabel++;
        return nextLabel;
    }
    /**
     * 
     * @param mode 1 assegnamento , 0 lettura
     */
    private int whenitem(boolean firstWhen,int labelDo){
        int labelElse=code.newLabel();
        while(!singleLabel(labelElse)){
            labelElse++;
        }
        match(Tag.WHEN);
        
        match(40);
        if(firstWhen)
            bexpr(labelDo,labelElse+1);
        else
            bexpr(labelDo,labelElse);

        match(41);
        match(Tag.DO);
        stat(labelDo);
        return labelElse;
    }
    /**
     * 
     * @param labelDo label di stat
     * @param labelElse label ramo else
     */
    private void bexpr(int labelDo,int labelElse){
        
            

          var operazione=((Word)look).lexeme;
          System.out.println("operando="+operazione);      
          match(Tag.RELOP);
          expr(0);
          expr(0);
        switch (operazione) {
            case ">":
            System.out.println("tag >");
            code.emit(OpCode.if_icmpgt,labelDo);
            break;
            case ">=":
            System.out.println("tag >=");
                
            code.emit(OpCode.if_icmpge,labelDo);
            break;
            case "<":
            System.out.println("tag <");
            code.emit(OpCode.if_icmplt,labelDo);
            break;
            case "<=":
                System.out.println("tag <=");
            code.emit(OpCode.if_icmple,labelDo);
            break;
            case "<>":
            //    match(Tag.RELOP);
              //  expr(0);
             //   expr(0);
            System.out.println("tag <>");
            code.emit(OpCode.if_icmpne,labelDo);
            break;
            case "==":
            System.out.println("tag ==");
            code.emit(OpCode.if_icmpeq,labelDo);
            break;
            default:
            System.out.println("errore:  RELOP non riconosciuto");
            break;
        }
        code.emit(OpCode.GOto, labelElse);
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
                    exprlist(true,true,false);

                }else{
                    exprlist(false,true,false);
                }
                  
                
                if(look.tag==')')
                    match(')');
                code.emit(OpCode.iadd);
            break;
            case '-': 
                match('-');
                if(look.tag=='(')
                    match('(');
                expr(0);
                expr(0);
                if(look.tag==')')
                    match(')');
                code.emit(OpCode.isub);
            break;
            case '*': 
                match('*');
                if(look.tag=='('){
                    match('(');
                    exprlist(true,false,true);

                }else{
                    exprlist(false,false,true);
                }
                if(look.tag==')')
                    match(')');
                code.emit(OpCode.imul);
            break;
            case '/': 
                match('/');
                if(look.tag=='(')
                    match('(');
                expr(0);
                expr(0);
                if(look.tag==')')
                    match(')');
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
    private void exprlist(boolean manyArguments,boolean isAdd,boolean isMul){
        if(isAdd || isAdd){
            if(manyArguments){
                while(look.tag!=')'){
                    expr(0);
                }
                match(')');
            }else{
                expr(0);
                expr(0);
            }
        }else{

            expr(0);
            exprlistp();
        }
    }
    private void exprlistp(){
        switch (look.tag) {
            case '+':
            case  '-':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr(0);
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
    String path = "bit_code\\test\\input_esame.txt"; 
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));

      Translator2 translator = new Translator2(lex, br);
      translator.prog();
      System.out.println("Input OK");
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
