package ejercicio_LR;
import java.io.*;
import static ejercicio_LR.Tokens.*;

%%
%class Lexer
%type Tokens
L=[a-zA-Z_]+
D=-?[0-9]+|-?(([0-9]+[.]?[0-9]*)|([.][0-9]+))(e[+-]?[0-9]+)?
espacio=[ ]+
%{
    public String lexeme;
    int estado = 0;
    int nu = 0;
%}
%%

{espacio} {/*Ignore*/}

<YYINITIAL> "int" {estado = 1; lexeme = yytext(); return intType;}
<YYINITIAL> "float" {estado = 2; lexeme = yytext(); return floatType;}
<YYINITIAL> "char" {estado = 3; lexeme = yytext(); return charType;}
<YYINITIAL> "," {lexeme = yytext(); return coma;}
<YYINITIAL> ";" {estado = 0; lexeme = yytext(); return semicolon;}
<YYINITIAL> "+" {lexeme = yytext(); return plus;}
<YYINITIAL> "-" {lexeme = yytext(); return minus;}
<YYINITIAL> "*" {lexeme = yytext(); return mult;}
<YYINITIAL> "/" {lexeme = yytext(); return div;}
<YYINITIAL> "(" {lexeme = yytext(); return open_parenth;}
<YYINITIAL> ")" {lexeme = yytext(); return close_parenth;}
<YYINITIAL> "=" {lexeme = yytext(); return equal;}
<YYINITIAL> {L}({L}{D})* {if(estado == 1) {
        lexeme = yytext();
        return idI;
    } else if(estado == 2) {
        lexeme = yytext();
        return idF;
    } else if(estado == 3) {
        lexeme = yytext();
        return idC;
    } else {
        lexeme = yytext();
        return id;
    }
}
<YYINITIAL> {D} {lexeme = yytext(); return num;}
. {lexeme = yytext(); return Error;}