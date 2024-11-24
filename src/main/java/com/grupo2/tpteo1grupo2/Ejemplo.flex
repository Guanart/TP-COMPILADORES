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
    private final Utils utils = new Utils();
    String nombreArchivo;

    int MAX_CONST_INT = 1000;    // Ejemplo de límite máximo para CONST_INT
    double MAX_CONST_REAL = 1000.0; // Ejemplo de límite máximo para CONST_REAL

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
LETRA = [a-zA-Z]+
DIGITO = [0-9]
ESPACIO = \t|\f
ID = {LETRA}({LETRA}|{DIGITO}|_)*
CONST_INT = {DIGITO}+
CONST_REAL = ({DIGITO}+ \. {DIGITO}*) | (\. {DIGITO}+)
CONST_B = 0b(0|1)+
CON_LOGICO = \&\&|\|\|

ABRIR_PARENTESIS = \(
CERRAR_PARENTESIS = \)
ABRIR_LLAVE = \{
CERRAR_LLAVE = \}
ABRIR_CORCHETE = \[
CERRAR_CORCHETE = \]
PUNTO_Y_COMA = \;
ADMIRACION = \!
INTERROGACION = \?
COMA = \,

CONST_STRING = \"([^\"\n\r])*\"
OP_LOGICO = <=|>=|<>|<|>|=
OP_ARITMETICO = \+|-|\*|\/
//OPLIST = (OPLIST) \[ (\+ | \* )  \[ {ID} \; {ID} \] \[ ( {CONST_INT} | { CONST_REAL} )  (\;  ({CONST_INT}|{CONST_REAL}))*  \]  \]
IN_COMENTARIO = \/\/\*
FIN_COMENTARIO = \*\/\/
SIMBOLO = {ABRIR_PARENTESIS} | {CERRAR_PARENTESIS} | {ABRIR_LLAVE} | {CERRAR_LLAVE} | {ABRIR_CORCHETE} | {CERRAR_CORCHETE} | {PUNTO_Y_COMA} | {ADMIRACION} | {INTERROGACION} | {COMA}
COMENTARIO_BLOQUE = {IN_COMENTARIO} ({LETRA}|{DIGITO}|{ESPACIO}|{WhiteSpace}|{SIMBOLO})* {FIN_COMENTARIO}
COMENTARIO_BLOQUE1 = {IN_COMENTARIO} ({LETRA}|{DIGITO}|{ESPACIO}|{WhiteSpace}|{SIMBOLO}|{COMENTARIO_BLOQUE})* {FIN_COMENTARIO}

%%

<YYINITIAL> {
"IF"                    { resultados += "Token IF encontrado, Lexema " + yytext() + "\n"; }
"ELSE"                  { resultados += "Token ELSE encontrado, Lexema " + yytext() + "\n"; }
"ENDIF"                 { resultados += "Token ENDIF encontrado, Lexema " + yytext() + "\n"; }
"WHILE"                 { resultados += "Token WHILE encontrado, Lexema " + yytext() + "\n"; }
"ENDWHILE"              { resultados += "Token ENDWHILE encontrado, Lexema " + yytext() + "\n"; }
"ENDFOR"                { resultados += "Token ENDFOR encontrado, Lexema " + yytext() + "\n"; }
"WRITE"                 { resultados += "Token WRITE encontrado, Lexema " + yytext() + "\n"; }
"FLOAT"                 { resultados += "Token FLOAT encontrado, Lexema " + yytext() + "\n"; }
"INTEGER"               { resultados += "Token INTEGER encontrado, Lexema " + yytext() + "\n"; }
"STRING"                { resultados += "Token STRING encontrado, Lexema " + yytext() + "\n"; }
"OPLIST"                { resultados += "Token OPLIST encontrado, Lexema " + yytext() + "\n"; }
{ID}                    { resultados += "Token ID encontrado, Lexema " + yytext() + "\n"; }

{CONST_INT} {
    int valor = Integer.parseInt(yytext());
    if (valor > MAX_CONST_INT) {
        resultados = ""; this.utils.mostrarIntLimit(); throw new RuntimeException();
    } else {
        resultados += "Token CONST_INT encontrado, Lexema: " + yytext() + "\n";
    }
}

{CONST_REAL} {
    double valor = Double.parseDouble(yytext());
    if (valor > MAX_CONST_REAL) {
        resultados = ""; this.utils.mostrarRealLimit(); throw new RuntimeException();
    } else {
        resultados += "Token CONST_REAL encontrado, Lexema: " + yytext() + "\n";
    }
}

{CONST_STRING} {
    String valor = yytext();
    // Remueve las comillas para contar solo los caracteres internos
    valor = valor.substring(1, valor.length() - 1);

    if (valor.length() > 30) {
        resultados = ""; this.utils.mostrarStringLimit(); throw new RuntimeException();
    } else {
        resultados += "Token CONST_STRING encontrado, Lexema: " + yytext() + "\n";
    }
}

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

{ABRIR_PARENTESIS}   { resultados += "Token ABRIR_PARENTESIS encontrado, Lexema " + yytext() + "\n"; }
{CERRAR_PARENTESIS}  { resultados += "Token CERRAR_PARENTESIS encontrado, Lexema " + yytext() + "\n"; }
{ABRIR_LLAVE}        { resultados += "Token ABRIR_LLAVE encontrado, Lexema " + yytext() + "\n"; }
{CERRAR_LLAVE}       { resultados += "Token CERRAR_LLAVE encontrado, Lexema " + yytext() + "\n"; }
{ABRIR_CORCHETE}     { resultados += "Token ABRIR_CORCHETE encontrado, Lexema " + yytext() + "\n"; }
{CERRAR_CORCHETE}    { resultados += "Token CERRAR_CORCHETE encontrado, Lexema " + yytext() + "\n"; }
{PUNTO_Y_COMA}       { resultados += "Token PUNTO_Y_COMA encontrado, Lexema " + yytext() + "\n"; }
{ADMIRACION}         { resultados += "Token ADMIRACION encontrado, Lexema " + yytext() + "\n"; }
{INTERROGACION}      { resultados += "Token INTERROGACION encontrado, Lexema " + yytext() + "\n"; }
{COMA}               { resultados += "Token COMA encontrado, Lexema " + yytext() + "\n"; }

{COMENTARIO_BLOQUE}     {}
{COMENTARIO_BLOQUE1}     {}
/* whitespace */
{WhiteSpace}            {}

[^] { resultados = ""; this.utils.mostrarBadToken(); throw new RuntimeException(); }
}