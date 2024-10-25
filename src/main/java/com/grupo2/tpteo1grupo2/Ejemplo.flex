package com.grupo2.tpteo1grupo2;
import java_cup.sym;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

%%

%{
    private String resultados;
    private Resultado resultado;
    String nombreArchivo;
    public static void guardarEnArchivo(String contenido, String nombreArchivo) {
      try (FileWriter escritor = new FileWriter(nombreArchivo)) {
        escritor.write(contenido);
      } catch (IOException e) {
      }
    }
%}

%init{
    resultados = "";
    nombreArchivo = "output.txt";
    resultado = Resultado.getInstance();
%init}

%eof{
    resultado.setContenido(resultados);
    guardarEnArchivo(resultado.getContenido(), nombreArchivo);
    SymbolTableGenerator prueba = new SymbolTableGenerator();
    String outputFilePath = "tabla_de_simbolos.csv";
    List<SymbolTableGenerator.Symbol> symbols = prueba.parseFile(nombreArchivo);
    prueba.saveToCSV(symbols, outputFilePath);
%eof}

/*%cupsym Simbolo*/
%cup 
%public
%class Lexico
%line
%column
%char

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
COM = \"
LETRA = [a-zA-Z]+
DIGITO = [0-9]
ESPACIO = \t|\f
ID = {LETRA}({LETRA}|{DIGITO}|_)*
CONST_INT = {DIGITO}+
CONST_REAL = ({DIGITO}+ \. {DIGITO}*) | (\. {DIGITO}+)
CONST_B = 0b(0|1)+
SIMBOLO = \(|\)|\{|\}|\[|\]|\;|\!|\?|\,
CONST_STRING = \"([^\"\n\r])*\"
OP_LOGICO = <=|>=|<>|<|>|=
OP_ARITMETICO = \+|-|\*|\/
CON_LOGICO = (AND)|(OR)|(XOR)
//OPLIST = (OPLIST) \[ (\+ | \* )  \[ {ID} \; {ID} \] \[ ( {CONST_INT} | { CONST_REAL} )  (\;  ({CONST_INT}|{CONST_REAL}))*  \]  \]
IN_COMENTARIO = \<\/
FIN_COMENTARIO = \/\>
AUX_COMENTARIO = \/\/
COMENTARIO_BLOQUE = {IN_COMENTARIO} ({LETRA}|{DIGITO}|{ESPACIO}|{WhiteSpace}|{SIMBOLO})* {FIN_COMENTARIO}
COMENTARIO_LINEA = {AUX_COMENTARIO} ({LETRA}|{DIGITO}|{ESPACIO}|{WhiteSpace}|{SIMBOLO})*

%%

<YYINITIAL> {
"FOR"                   { resultados += "Token FOR encontrado, Lexema " + yytext() + "\n"; }
"IF"                    { resultados += "Token IF encontrado, Lexema " + yytext() + "\n"; }
"ELSE"                  { resultados += "Token ELSE encontrado, Lexema " + yytext() + "\n"; }
"ENDIF"                 { resultados += "Token ENDIF encontrado, Lexema " + yytext() + "\n"; }
"WHILE"                 { resultados += "Token WHILE encontrado, Lexema " + yytext() + "\n"; }
"FOREACH"               { resultados += "Token FOREACH encontrado, Lexema " + yytext() + "\n"; }
"ENDWHILE"              { resultados += "Token ENDWHILE encontrado, Lexema " + yytext() + "\n"; }
"ENDFOR"                { resultados += "Token ENDFOR encontrado, Lexema " + yytext() + "\n"; }
"WRITE"                 { resultados += "Token WRITE encontrado, Lexema " + yytext() + "\n"; }
"FLOAT"                 { resultados += "Token FLOAT encontrado, Lexema " + yytext() + "\n"; }
"INTEGER"               { resultados += "Token INTEGER encontrado, Lexema " + yytext() + "\n"; }
"STRING"                { resultados += "Token STRING encontrado, Lexema " + yytext() + "\n"; }
"BOOLEAN"               { resultados += "Token BOOLEAN encontrado, Lexema " + yytext() + "\n"; }
"RANGE"                 { resultados += "Token RANGE encontrado, Lexema " + yytext() + "\n"; }
"TRUE"                  { resultados += "Token TRUE encontrado, Lexema " + yytext() + "\n"; }
"FALSE"                 { resultados += "Token FALSE encontrado, Lexema " + yytext() + "\n"; }
"OPLIST"                { resultados += "Token OPLIST encontrado, Lexema " + yytext() + "\n"; }
{ID}                    { resultados += "Token ID encontrado, Lexema " + yytext() + "\n"; }
{CONST_INT}             { resultados += "Token CONST_INT encontrado, Lexema " + yytext() + "\n"; }
{CONST_REAL}            { resultados += "Token CONST_REAL encontrado, Lexema " + yytext() + "\n"; }
{CONST_STRING}          { resultados += "Token CONST_STRING encontrado, Lexema " + yytext() + "\n"; }
{CONST_B}               { resultados += "Token CONST_B encontrado, Lexema " + yytext() + "\n"; }
{OP_LOGICO}             { resultados += "Token OP_LOGICO encontrado, Lexema " + yytext() + "\n"; }
{OP_ARITMETICO}         { resultados += "Token OP_ARITMETICO encontrado, Lexema " + yytext() + "\n"; }
{CON_LOGICO}            { resultados += "Token CON_LOGICO encontrado, Lexema " + yytext() + "\n"; }
"::="                   { resultados += "Token ASIGN encontrado, Lexema " + yytext() + "\n"; }
":="                    { resultados += "Token DECLARATION encontrado, Lexema " + yytext() + "\n"; }
"DECLARE.SECTION"       { resultados += "Token DECLARE.SECTION encontrado, Lexema " + yytext() + "\n"; }
"ENDDECLARE.SECTION"    { resultados += "Token ENDDECLARE.SECTION encontrado, Lexema " + yytext() + "\n"; }
"PROGRAM.SECTION"       { resultados += "Token PROGRAM.SECTION encontrado, Lexema " + yytext() + "\n"; }
"ENDPROGRAM.SECTION"    { resultados += "Token ENDPROGRAM.SECTION encontrado, Lexema " + yytext() + "\n"; }
{SIMBOLO}               { resultados += "Token SIMBOLO encontrado, Lexema " + yytext() + "\n"; }
{COMENTARIO_BLOQUE}     {}
{COMENTARIO_LINEA}      {}
/* whitespace */
{WhiteSpace}            {}

[^] { throw new Error("Caracter no permitido: " + yytext() + " en la linea " + yyline); }
}