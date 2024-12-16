package ejercicio_LR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Compilador extends javax.swing.JFrame 
{
    boolean aceptar = false, impecable = true;
    Scanner scanner = new Scanner(System.in);
    String corridaDeEscritorio_cadena = "";
    Stack<String> principal = new Stack<>();
    boolean errSint = false; 
    boolean errTabSim = false;
    boolean errTabAsig = false;
    boolean errSem = false;  
    boolean banPosfija = false;
    String tipoSem = "";
    String asig = "";
    int t1, t2;
    Stack<String> pilaSint = new Stack<>();
    Stack<Integer> pilaSem = new Stack<>();
    Stack<String> temp = new Stack<>();
    Stack<String> pilaOpers = new Stack<>();
    Stack<String> expPosfija = new Stack<>();
    List<String[]> tablaSim = new ArrayList<>();
    ////mi desmadre
   ///////////////////
    String not[] = {"$", "P", "Tipo", "V", "A", "S", "E", "T", "F"};
    String columnas[] = {"id", "num", "int", "float", "char", ",", ";", "+", "-", "*", "/", "(", ")", "=", "$", "P", "Tipo", "V", "A", "S", "E", "T", "F"};
    String[][] anSint = {
        {"7", "", "4", "5", "6", "", "", "", "", "", "", "", "", "", "", "1", "2", "", "3", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "P' -> P", "", "", "", "", "", "", "", ""},
        {"8", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "P -> A", "", "", "", "", "", "", "", ""},
        {"Tipo -> int", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"Tipo -> float", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"Tipo -> char", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "9", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "11", "12", "", "", "", "", "", "", "", "", "", "", "10", "", "", "", "", ""},
        {"20", "21", "", "", "", "", "", "14", "15", "", "", "19", "", "", "", "", "", "", "", "13", "16", "17", "18"},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "P -> Tipo id V", "", "", "", "", "", "", "", ""},
        {"22", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"7", "", "4", "5", "6", "", "", "", "", "", "", "", "", "", "", "23", "2", "", "3", "", "", "", ""},
        {"", "", "", "", "", "", "24", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "25", "17", "18"},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "26", "17", "18"}, //hasta aqui todo  bien
        {"", "", "", "", "", "", "S -> E", "27", "28", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "E -> T", "E -> T", "E -> T", "29", "30", "", "E -> T", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "T -> F", "T -> F", "T -> F", "T -> F", "T -> F", "", "T -> F", "", "", "", "", "", "", "", "", "", ""},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "31", "17", "18"},
        {"", "", "", "", "", "", "F -> id", "F -> id", "F -> id", "F -> id", "F -> id", "", "F -> id", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "F -> num", "F -> num", "F -> num", "F -> num", "F -> num", "", "F -> num", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "11", "12", "", "", "", "", "", "", "", "", "", "", "32", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "V -> ; P", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "A -> id = S ;", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "S -> + E", "27", "28", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "S -> - E", "27", "28", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "", "33", "18"},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "", "34", "18"},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "", "", "35"},
        {"20", "21", "", "", "", "", "", "", "", "", "", "19", "", "", "", "", "", "", "", "", "", "", "36"},
        {"", "", "", "", "", "", "", "27", "28", "", "", "", "37", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "V -> , id V", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "E -> E + T", "E -> E + T", "E -> E + T", "29", "30", "", "E -> E + T", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "E -> E - T", "E -> E - T", "E -> E - T", "29", "30", "", "E -> E - T", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "T -> T * F", "T -> T * F", "T -> T * F", "T -> T * F", "T -> T * F", "", "T -> T * F", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "T -> T / F", "T -> T / F", "T -> T / F", "T -> T / F", "T -> T / F", "", "T -> T / F", "", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "F -> ( E )", "F -> ( E )", "F -> ( E )", "F -> ( E )", "F -> ( E )", "", "F -> ( E )", "", "", "", "", "", "", "", "", "", ""}        
    };    
    boolean[][] reglaAsig = {
        {true, false, false},
        {true, true, false},
        {false, false, true}
    };
    int[][] reglaExp = {
        {0, 1, -1},
        {1, 1, -1},
        {-1, -1, -1}
    };

    public Compilador() 
    {
        initComponents();
        setResizable(false);
        setSize(500, 280);
        setLocationRelativeTo(null);
    }
    
  
    
    private void Lexico()
    {
        boolean ban = true;
        int con = 0;
        File archivo = new File("Compilacion.yum");
        PrintWriter escribir;
        try {
            escribir = new PrintWriter(archivo);
            escribir.print(txtCadena.getText());
           escribir.close();
           
        }
        catch(FileNotFoundException e) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, e);            
        }
        try {
            Reader lector = new BufferedReader(new FileReader("Compilacion.yum"));
            Lexer lexer = new Lexer(lector);
            Tokens token = lexer.yylex();

            while(ban) {                
                if(token == null) {                    
                    Sintactico("$");
                    asig = temp.pop();
                    ban = false;
                    codInter();
                    if(errSint)
                        JOptionPane.showMessageDialog(null, "La cadena no es aceptada dentro de esta gramatica, se esperaba un " + Esperado());
                        //System.out.print( "La cadena no es aceptada dentro de esta gramatica, se esperaba un " + Esperado());
                    else
                        if(errSem)                            
                            System.out.println("La variable " + asig + " de tipo " + tipoStr(String.valueOf(t1)) + " no puede recibir un " + tipoStr(String.valueOf(t2)));                                                                                                                                           
                    return;
                }
                switch(token) {
                    case Error:
                        System.out.println( "Error, El lexema " + lexer.lexeme + " es irreconocible");
                        return;
                    case id, idI, idF, idC, num:                              
                        if(token == Tokens.idI || token == Tokens.idF || token == Tokens.idC) {
                            Tabla(String.valueOf(token), String.valueOf(lexer.lexeme));
                            token = Tokens.id;               
                        } else 
                            if(token == Tokens.id) {
                                temp.push(lexer.lexeme);
                                if(!buscarAsig(lexer.lexeme)) {                                    
                                    errTabAsig = true;               
                                    break;
                                }
                            } else
                                if(token == Tokens.num)
                                    temp.push(lexer.lexeme);
                        con = 0;
                        Sintactico(String.valueOf(token));        
                        if(banPosfija)
                            Posfija(String.valueOf(token), String.valueOf(lexer.lexeme));
                        break;
                    case semicolon:
                        con++;
                        if(con == 1) {
                            Sintactico(String.valueOf(lexer.lexeme));
                            if(banPosfija)
                                Posfija(String.valueOf(token), String.valueOf(lexer.lexeme));                                                    
                        }
                        else {
                            System.out.println("CADENA INVALIDA, La cadena no es aceptada dentro de esta gramatica, existe un ; adicional");                    
                            return;
                        }                            
                        break;
                    default:
                        con = 0;
                        Sintactico(String.valueOf(lexer.lexeme));    
                        if(banPosfija)
                            Posfija(String.valueOf(token), String.valueOf(lexer.lexeme));
                        break;
                }
                if(errSint) {
                    System.out.println("Estado en el que se quedó: " + pilaSint.peek());
                     JOptionPane.showMessageDialog(null,"CADENA INVALIDA, La cadena no es aceptada dentro de esta gramatica. Se recibició un " + lexer.lexeme + " cuando se esperaba un " + Esperado());
                    //System.out.println("CADENA INVALIDA, La cadena no es aceptada dentro de esta gramatica. Se recibició un " + lexer.lexeme + " cuando se esperaba un " + Esperado());
                    return;
                } else
                    if(errTabSim) {
                        System.out.println("ERROR, La variable " + lexer.lexeme + " ya se encuentra definida como " + tipoStr(tipoSem));                                            
                        return;
                    } else
                        if(errTabAsig) {
                            System.out.println("ERROR, La variable " + lexer.lexeme + " no se encuentra definida");                                                                        
                            return;
                        } else
                            if(errSem) {
                                System.out.println("ERROR,  La operación entre " + tipoStr(String.valueOf(t1)) + " y " + tipoStr(String.valueOf(t2)) + " no puede realizarse");
                                return;
                            }
                            
                token = lexer.yylex();
            }  
        }
        catch(FileNotFoundException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);            
        } catch(IOException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }
    //////////////////////////////////////////////////////////////////7
      /*private String recorrido(Stack<String> pila) {
        String cadena = "";
        if (pila.isEmpty()) {
            System.out.println("Pila Vacia");
        } else {
            for (int i = 0; i < pila.size(); i++) {
                cadena += pila.get(i);
                System.out.print(pila.get(i));
            }
        }
        System.out.println("");
        return cadena;
    }
      
       public String Recorrer(ArrayList<String> tokens) {
        principal.clear();
        principal.push("$");
        principal.push("I0");
        tokens.add("$");
        int columna, fila;
        int col = buscarColumna(tokens);
        int renglon = Integer.parseInt(pilaSint.peek());
        String dato = anSint[renglon][col];

        corridaDeEscritorio_cadena = "";
        corridaDeEscritorio_cadena += recorrido(principal);
        aceptar = false;
        impecable = true;
        
        for (int i = 0, tam = tokens.size(); i < tam; i++) {
            boolean avanzamos = false;
            do {
                if (tokens.get(i).equals("$") && principal.peek().equals("I1")) {
                    aceptar = true;
                    break;
                }

                fila = Buscar_estado(principal.peek());
                columna = Buscar_simbolo(tokens.get(i));

                if (dato.isEmpty()) {
                    impecable = false;
                    break;
                }

                if ((anSint[fila][columna].charAt(0) + "").equals("I")) {
                    principal.push(tokens.get(i));
                    principal.push(anSint[fila][columna]);
                    corridaDeEscritorio_cadena += recorrido(principal);
                    avanzamos = true;
                }

                if ((anSint[fila][columna].charAt(0) + "").equals("P")) {
                    switch (anSint[fila][columna]) {
                        case "P0" ->
                            procesoProducciones(0);
                        case "P1" ->
                            procesoProducciones(1);
                        case "P2" ->
                            procesoProducciones(2);
                        case "P3" ->
                            procesoProducciones(3);
                        case "P4" ->
                            procesoProducciones(4);
                        case "P5" ->
                            procesoProducciones(5);
                        case "P6" ->
                            procesoProducciones(6);
                        case "P7" ->
                            procesoProducciones(7);
                        case "P8" ->
                            procesoProducciones(8);
                        case "P9" ->
                            procesoProducciones(9);
                        case "P10" ->
                            procesoProducciones(10);
                        case "P11" ->
                            procesoProducciones(11);
                        case "P12" ->
                            procesoProducciones(12);
                        case "P13" ->
                            procesoProducciones(13);
                        case "P14" ->
                            procesoProducciones(14);
                        case "P15" ->
                            procesoProducciones(15);
                        case "P16" ->
                            procesoProducciones(16);
                        case "P17" ->
                            procesoProducciones(17);
                        case "P18" ->
                            procesoProducciones(18);
                        case "P19" ->
                            procesoProducciones(19);
                        case "P20" ->
                            procesoProducciones(20);
                        default ->
                            System.out.println("Algun error ");
                    }
                }
            } while (!avanzamos);
        }

        return corridaDeEscritorio_cadena;
    }
       
    public void procesoProducciones(int i) {
        int pops = 2 * (p[i].split("_")[1].split("#").length);
        for (int k = 0; k < pops; k++) {
            principal.pop();
        }
        int filaP = Buscar_estado(principal.peek());
        int columnaP = Buscar_simbolo(p[i].split("_")[0]);
        principal.push(p[i].split("_")[0]);
        principal.push(anSint[filaP][columnaP]);
        corridaDeEscritorio_cadena += recorrido(principal);
    }
        private int Buscar_simbolo(String car) {
        for (int i = 0; i < columnas.length; i++) {
            if (columnas[i].equals(car)) {
                return i;
            }
        }
        return -1;
    }

    private int Buscar_estado(String car) {
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equals(car)) {
                return i;
            }
        }
        return -1;
    }*/
    ////////////////////////////////////////////////////////////////////////////
    private void Sintactico(String token)
    {
        boolean ban = true;
        int col = buscarColumna(token);
        int renglon = Integer.parseInt(pilaSint.peek());
        String dato = anSint[renglon][col];
        String entero = "^[+-]?\\d+$";
        
        if(!dato.isEmpty()) {
            while(!dato.matches("^[1-9][0-9]*$")) {
                if(dato.isEmpty()) { 
                    errSint = true;
                    ban = false;
                    break;
                }
                
                String[] prod = dato.split("->");
                String[] simb = prod[1].trim().split(" ");
                
                for(int i = 0; i < simb.length * 2; i++)
                    pilaSint.pop();

                String nuevo = anSint[Integer.parseInt(pilaSint.peek())][buscarColumna(prod[0].trim())];
                pilaSint.push(prod[0]);
                pilaSint.push(nuevo);
                             
                if(dato.equals("F -> num")) {                    
                    String variable = temp.pop();
                    
                    if(variable.matches(entero))
                        pilaSem.push(0);
                    else
                        pilaSem.push(1);                                   
                }
                if(dato.equals("F -> id")){
                    temp.pop();
                    pilaSem.push(Integer.parseInt(tipoSem));
                }
                
                if(dato.equals("A -> id = S ;")) {
                    t2 = pilaSem.pop();
                    t1 = pilaSem.pop();
                    
                    if(reglaAsig[t1][t2])
                        pilaSem.push(reglaExp[t1][t2]);
                    else {
                        errSem = true;
                        ban = false;
                        break;
                    }
                }
                if(dato.equals("E -> E + T") || dato.equals("E -> E - T") || dato.equals("T -> T * F") || dato.equals("T -> T / F")) {
                    t2 = pilaSem.pop();
                    t1 = pilaSem.pop();
                    
                    if(reglaExp[t1][t2] != -1) 
                        pilaSem.push(reglaExp[t1][t2]);
                    else {
                        errSem = true;
                        ban = false;
                        break;
                    }
                }                    
                
                renglon = Integer.parseInt(pilaSint.peek());
                col = buscarColumna(token);
                dato = anSint[renglon][col];                
                
                if(dato.equals("P' -> P")) {
                    System.out.println("CADENA ACEPTADA, La cadena es aceptada dentro de esta gramatica");
                    ban =  false;
                    break;
                }
            }    
            if(ban) {             
                if(col == 0 && renglon == 12)
                    if(buscarAsig(temp.peek()))
                        pilaSem.push(Integer.parseInt(tipoSem));
                
                if(col == 13 && renglon == 7)
                    banPosfija = true;                
                                            
                pilaSint.push(token);
                pilaSint.push(dato);
            }
        } else
            errSint = true;        
    }
    
    private int buscarColumna(String token)
    {
        int col = -1;
        for(int i = 0; i < columnas.length; i++)
            if(token.equals(columnas[i])) {
                col = i;
                break;
            }
        return col;
    }
    
    private String Esperado()
    {
        int ren = Integer.parseInt(pilaSint.peek());
        String msj = "";        
        for(int i = 0; i < anSint[ren].length; i++)
            if(!anSint[ren][i].isEmpty() && !Arrays.asList(not).contains(columnas[i]))
                msj += columnas[i] + ", ";  
        return msj.substring(0, msj.length() - 2);
    }
    
    private void Tabla(String token, String lexema)
    {
        if(!tablaSim.isEmpty()) {
            if(!Buscar(lexema))
                Tipo(token, lexema);            
        } else
            Tipo(token, lexema);
    }
    
    private boolean Buscar(String lexema)
    {
        for(String[] vars : tablaSim)
            if(vars[0].equals(lexema)) {
                errTabSim = true;    
                tipoSem = vars[1];
                return true;
            }
        return false;
    }
    
    private void Tipo(String token, String lexema)
    {
        switch(token) {
            case "idI":
                tablaSim.add(new String[]{lexema, "0"});                
                break;
            case "idF":
                tablaSim.add(new String[]{lexema, "1"});
                break;
            case "idC":
                tablaSim.add(new String[]{lexema, "2"});
                break;
        }
    }
    
    private String tipoStr(String tipo)
    {
        switch(tipo) {
            case "0":
                return "int";
            case "1":
                return "float";
            case "2":
                return "char";
        }
        return "";
    }
    
    private boolean buscarAsig(String lexema)
    {
        for(String[] vars: tablaSim)
            if(vars[0].equals(lexema)) {
                tipoSem = vars[1];
                return true;
            }
        return false;
    }
    
    private void Posfija(String token, String lexema)
    {
        int prioCima, prioToken;
        if(token.equals("id") || token.equals("num"))
            expPosfija.push(lexema);
        else {
            switch(lexema) {
                case "(":
                    pilaOpers.push(lexema);
                    break;
                case ")":
                    while(!pilaOpers.peek().equals("("))
                        expPosfija.push(pilaOpers.pop());
                    pilaOpers.pop();
                    break;
                case ";":
                    while(!pilaOpers.isEmpty())
                        expPosfija.push(pilaOpers.pop());
                    break;
                default:  
                    if(!pilaOpers.isEmpty()) {
                        prioCima = Prioridad(pilaOpers.peek());
                        prioToken = Prioridad(lexema);
                                                
                        if(prioCima == 0)
                            pilaOpers.push(lexema);
                        else {
                            while(!pilaOpers.isEmpty() && prioCima >= prioToken) {
                                expPosfija.push(pilaOpers.pop());
                                prioCima = Prioridad(pilaOpers.peek());
                            }                            
                            pilaOpers.push(lexema);
                        }
                    }
                    else
                        if(!lexema.equals("="))
                            pilaOpers.push(lexema);
            }
        }
    }
    
    private int Prioridad(String lexema)
    {
        switch(lexema) {
            case "+", "-":
                return 1;
            case "*", "/":
                return 2;
        }
        return 0;
    }
    
    private void codInter()
    {                
        int cont = 1, ci = 0, cf = 0, cc = 0;
        for(int i = expPosfija.size() - 1; i >= 0; i--)
            temp.push(expPosfija.pop()); 
        
        for(String[] var: tablaSim) {
            String lexema = var[0];
            String tipo = var[1];
            
            if(tipoStr(tipo).equals("int")) {
                ++ci;
                if(ci == 1) {
                    expPosfija.push("int");
                    expPosfija.push(lexema + " ;");
                } else
                    expPosfija.push("int " + lexema + " ;");
            }
            if(tipoStr(tipo).equals("float")) {
                ++cf;
                if(cf == 1) {
                    expPosfija.push("float");
                    expPosfija.push(lexema + " ;");
                } else
                    expPosfija.push("float " + lexema + " ;");
            }
            if(tipoStr(tipo).equals("char")) {
                ++cc;
                if(cc == 1) {
                    expPosfija.push("char");
                    expPosfija.push(lexema + " ;");
                } else
                    expPosfija.push("char " + lexema + " ;");
            }           
        }            
        
        while(!temp.isEmpty()) {            
            String var = temp.pop();
            
            if(var.matches("[a-zA-Z]") || var.matches("\\d+"))
                expPosfija.push("v" + cont++ + " = " + var + " ;");
            else
                switch(var) {
                    case "+":
                        cont -= 2;
                        if(cont == 0)
                            expPosfija.push("v" + ++cont + " = +v" + cont + " ;");
                        else
                            expPosfija.push("v" + cont + " = v" + cont + " + v" + ++cont + " ;");
                        break;
                    case "-":
                        cont -= 2;
                        if(cont == 0)
                            expPosfija.push("v" + ++cont + " = -v" + cont + " ;"); 
                        else
                            expPosfija.push("v" + cont + " = v" + cont + " - v" + ++cont + " ;");
                        break;
                    case "/":
                        cont -= 2;
                        expPosfija.push("v" + cont + " = v" + cont + " / v" + ++cont + " ;");
                        break;
                    case "*":
                        cont -= 2;
                        expPosfija.push("v" + cont + " = v" + cont + " * v" + ++cont + " ;");                       
                }            
        }
        cont--;
        if(cont == 0)
            expPosfija.push(asig + " = v" + ++cont + " ;");
        else
            expPosfija.push(asig + " = v" + cont + " ;");
    }
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCadena = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnValidar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));

        txtCadena.setBackground(new java.awt.Color(245, 236, 219, 170));
        txtCadena.setFont(new java.awt.Font("Constantia", 0, 20)); // NOI18N
        txtCadena.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel6.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(70, 52, 19));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnValidar.setBackground(new java.awt.Color(153, 153, 153));
        btnValidar.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        btnValidar.setForeground(new java.awt.Color(255, 255, 255));
        btnValidar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnValidar.setText("Validar Cadena");
        btnValidar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnValidar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnValidar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnValidar.setOpaque(true);
        btnValidar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnValidarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnValidar, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(txtCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnValidar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValidarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnValidarMouseClicked
            ArrayList<String> tokens = new ArrayList<>();
this.dispose();
        pilaSint.clear(); 
        pilaSint.push("$");
        pilaSint.push("0");
        pilaSem.clear();
        temp.clear();
        pilaOpers.clear();
        expPosfija.clear();
        errSint = false;
        errTabSim = false;
        errTabAsig = false;
        errSem = false;
        banPosfija = false;
        tipoSem = "";
        tablaSim = new ArrayList<>();
        
        Lexico();
        
        System.out.println("TABLA DE SIMBOLOS");
        for(String[] fila : tablaSim) {
            for(String dato : fila)
                System.out.print(dato + "\t");            
            System.out.println();
        }
        System.out.println();
        
        System.out.println("PILA SEMANTICA");
        for(Integer numero : pilaSem)
            System.out.println(numero);
        System.out.println();/*
         try {
        Reader lector = new BufferedReader(new FileReader("Compilacion.yum"));
          Lexer lexer = new Lexer(lector);
            Tokens token = lexer.yylex();
                     System.out.println("\n CORRIDA DE ESCRITORIO:");

             try {            
                while ((token = lexer.yylex()) != null) {
                    if (token == Tokens.id) {
                        tokens.add("id");
                        System.out.println("Token: id");
                    } else if (token == Tokens.id) {
                        tokens.add("num");
                        System.out.println("Token: num");
                    } else if (token == Tokens.intType) {
                        tokens.add("int");
                        System.out.println("Token: int");
                    } else if (token == Tokens.floatType) {
                        tokens.add("float");
                        System.out.println("Token: float");
                    } else if (token == Tokens.charType) {
                        tokens.add("char");
                        System.out.println("Token: char");
                    } else if (token == Tokens.coma) {
                        tokens.add(",");
                        System.out.println("Token: ,");
                    } else if (token == Tokens.semicolon) {
                        tokens.add(";");
                        System.out.println("Token: ;");
                    } else if (token == Tokens.plus) {
                        tokens.add("+");
                        System.out.println("Token: +");
                    } else if (token == Tokens.minus) {
                        tokens.add("-");
                        System.out.println("Token: -");
                    } else if (token == Tokens.mult) {
                        tokens.add("*");
                        System.out.println("Token: *");
                    } else if (token == Tokens.div) {
                        tokens.add("/");
                        System.out.println("Token: /");
                    } else if (token == Tokens.open_parenth) {
                        tokens.add("(");
                        System.out.println("Token: (");
                    } else if (token == Tokens.close_parenth) {
                        tokens.add(")");
                        System.out.println("Token: )");
                    } else if (token == Tokens.equal) {
                        tokens.add("=");
                        System.out.println("Token: =");
                    } else if (token == Tokens.Error) {
                        System.out.println("Error encontrado, deteniendo el programa.");
                        System.exit(0);
                    } else {
                        tokens.add("desconocido");
                        
                    }
                }                      
            } catch (IOException e) {
               e.printStackTrace();
            } 
            
         } catch(FileNotFoundException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);            
        } catch(IOException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);            
        }
           
              
       
         
         this.Recorrer(tokens);
        */
        System.out.println("CODIGO INTERMEDIO");
            for(String var : expPosfija)
                System.out.println(var);
    }//GEN-LAST:event_btnValidarMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compilador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnValidar;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtCadena;
    // End of variables declaration//GEN-END:variables
}


