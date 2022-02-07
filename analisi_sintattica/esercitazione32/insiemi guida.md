---
useMath: true
---
# Grammatica di partenza
![grammatica](grammatica.png)
# Calcolo giude
| Simbolo    | Guide                                                                  | follow                      | first                                           |
|------------|------------------------------------------------------------------------|-----------------------------|-------------------------------------------------|
| prog       | (prog->statlistp)=<br>First(statlistp U EOF)=; ,EOF                    | $                           | first(statlist)                                 |
| statlist   | (->stat statlistp)=FIRST(stat,statlistp)===,ID,PRINT,READ,WHILE,COND,{ | EOF,}                       | stat,statlistp==,ID,PRINT,READ,WHILE,COND,{,;   |
| statilistp | (->;...)=;<br>(->$\epsilon$)=FOLLOW(statlistp)==EOF, }                 | EOF, }                      | ;                                               |
| stat       | (->=..)= {=}<br>(->print..)={print}<br>(->cond..)={cond}<br>(->while..)={while}<br>(->{...)={'{'} | ;  EOF  }                   | =,ID,PRINT,READ,WHILE,COND,{                    |
| whenlist   | (->whenitem, whenlistp)=FIRST(whenitem, whenlistp)                     | else                        | FIRST(whenitem) U FIRST(whenlistp)=when         |
| whenlistp  | (->whenitem, whenlistp)=FIRST(whenitem, whenlistp)=when,else<br>(->$\epsilon$)=follow(whenlistp)= else | else                        | FIRST(whenitem)=when                            |
| whenitem   | (->when ...)=when                                                      | when,else                   | when                                            |
| bexpr      | ->RELOP=RELOP                                                          | )                           | RELOP                                           |
| expr       | (->+ ... )=+<br>(->- ... )=-<br>(->* ... )=*<br>(->/... )= /<br>(->NUM )=NUM<br>(->ID)=ID | ;  EOF , +,-,*,/,  NUM,ID,) | +,-,*,/,  NUM,ID                                |
| exprlist   | (->expr exprlistp)={+,-,*,/,ID,NUM}<br>                                | )                           | FIRST(expr) U FIRST(exprlistp)=+,-,*,/,  NUM,ID |
| exprlistp  | (->expr,exprlistp)={+,-,*,/,ID,NUM}<br>(->$\epsilon$)=follow(exprlist)=) | )                           | FIRST(expr)=+,-,*,/,  NUM,ID                    |
