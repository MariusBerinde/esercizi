# istruzioni del linguaggio

## = ID ⟨expr ⟩
Assegna il valore dell’espressione ⟨expr ⟩ all’identificatore rappresentato da ID. Ad esem-
pio, = x 3 corrisponde all’assegnamento del valore 3 all’identificatore x.

## print ( ⟨exprlist ⟩)
Stampa sul terminale il valore di tutte le espressioni incluse nella lista di espressioni ⟨exprlist ⟩.
Ad esempio, print(+(2 3) 4) stampa sul terminale il valore 5 seguito dal valore 4

## read ( ID )
Permette l’inserimento di un numero intero dalla tastiera e l’assegnamento del valore del numero all’identificatore scritto tra parentesi.
 Ad esempio, il comando read(a) specifica che l’utente del programma scritto in linguaggio P deve inserire un numero intero con la
tastiera, che poi `e assegnato all’identificatore a.

## {⟨statlist ⟩}
Permette di raggruppare una sequenza di istruzioni. Ad esempio, un blocco di istruzioni
che legge in input un numero e successivamente stampa sul terminale il numero incremen-
tato di 1 pu `o essere scritto nella maniera seguente: {read(x); print(+(x 1))}.


## Traduzioni di riferimento
= x 5;
= y 3;
= z 8;
cond when(> x 0) do {print(x);print(y)}
else print(z)

### tradotto diventa
```
ldc 5
istore 0
L1: 
    ldc 3
    istore 1
L2:
    ldc 8
    istore 2
L3:
    load 0
    ldc 0
    if_icmpgt L4
    goto L6
L4:
    load 0
    invokestatic Output/print(I)V
L5:
    load 1
    invokestatic Output/print(I)V
L6:
    load 3
    invokestatic Output/print(I)V
L0:
```

### traduzione while
= x 11;

while (> x 0) { = x - x 1; print(x)} 

tradotto diventa
```
ldc 11
istore 0
L1:
    load 0
    ldc 0
    if_icmpgt L2
    goto L0
L2:
    load 0
    ldc 1
    isub
    istore 0
L3:
    load 0
    invokestatic Output/print(I)V
    goto L1
L0:
```