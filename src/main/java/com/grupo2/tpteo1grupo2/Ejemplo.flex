package com.grupo2.tpteo1grupo2;
import java_cup.runtime.*;
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
CON_AND = \&\&
CON_OR = \|\|

ABRIR_CORCHETE = \[
CERRAR_CORCHETE = \]
ABRIR_PARENTESIS = \(
CERRAR_PARENTESIS = \)
ABRIR_LLAVE = \{
CERRAR_LLAVE = \}
PUNTO_Y_COMA = \;
ADMIRACION = \!
INTERROGACION = \?
COMA = \,

CONST_STRING = \"([^\"\n\r])*\"

OP_MAYOR_IGUAL = >=
OP_MENOR_IGUAL = <=
OP_MAYOR = >
OP_MENOR = <
OP_IGUAL = =

OP_SUMA = \+
OP_RESTA = \-
OP_MULT = \*
OP_DIV = \/
//OPLIST = (OPLIST) \[ (\+ | \* )  \[ {ID} \; {ID} \] \[ ( {CONST_INT} | { CONST_REAL} )  (\;  ({CONST_INT}|{CONST_REAL}))*  \]  \]
IN_COMENTARIO = \/\/\*
FIN_COMENTARIO = \*\/\/
SIMBOLO = {ABRIR_PARENTESIS} | {CERRAR_PARENTESIS} | {ABRIR_LLAVE} | {CERRAR_LLAVE} | {ABRIR_CORCHETE} | {CERRAR_CORCHETE} | {PUNTO_Y_COMA} | {ADMIRACION} | {INTERROGACION} | {COMA}
COMENTARIO_BLOQUE = {IN_COMENTARIO} ({LETRA}|{DIGITO}|{ESPACIO}|{WhiteSpace}|{SIMBOLO})* {FIN_COMENTARIO}
COMENTARIO_BLOQUE1 = {IN_COMENTARIO} ({LETRA}|{DIGITO}|{ESPACIO}|{WhiteSpace}|{SIMBOLO}|{COMENTARIO_BLOQUE})* {FIN_COMENTARIO}

%%

<YYINITIAL> {
"DECLARE_SECTION"       { resultados += "Token DECLARE_SECTION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.DECLARE_SECTION, yytext()); }
"ENDDECLARE_SECTION"    { resultados += "Token ENDDECLARE_SECTION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ENDDECLARE_SECTION, yytext()); }
"PROGRAM_SECTION"       { resultados += "Token PROGRAM_SECTION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.PROGRAM_SECTION, yytext()); }
"ENDPROGRAM_SECTION"    { resultados += "Token ENDPROGRAM_SECTION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ENDPROGRAM_SECTION, yytext()); }
"IF"                    { resultados += "Token IF encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.IF, yytext()); }
"THEN"                  { resultados += "Token THEN encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.THEN, yytext()); }
"ELSE"                  { resultados += "Token ELSE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ELSE, yytext()); }
"ENDIF"                 { resultados += "Token ENDIF encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ENDIF, yytext()); }
"WHILE"                 { resultados += "Token WHILE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.WHILE, yytext()); }
"ENDWHILE"              { resultados += "Token ENDWHILE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ENDWHILE, yytext()); }
"ENDFOR"                { resultados += "Token ENDFOR encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ENDFOR, yytext()); }
"WRITE"                 { resultados += "Token WRITE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.WRITE, yytext()); }
"FLOAT"                 { resultados += "Token FLOAT encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.FLOAT, yytext()); }
"INTEGER"               { resultados += "Token INTEGER encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.INTEGER, yytext()); }
"STRING"                { resultados += "Token STRING encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.STRING, yytext()); }
"OPLIST"                { resultados += "Token OPLIST encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OPLIST, yytext()); }
{CON_AND}            { resultados += "Token CON_AND encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.CON_AND, yytext()); }
{CON_OR}            { resultados += "Token CON_OR encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.CON_OR, yytext()); }
{ID}                    { resultados += "Token ID encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ID, yytext()); }

{CONST_INT} {
    int valor = Integer.parseInt(yytext());
    if (valor > MAX_CONST_INT) {
        resultados = ""; this.utils.mostrarIntLimit(); throw new RuntimeException();
    } else {
        resultados += "Token CONST_INT encontrado, Lexema: " + yytext() + "\n";
        return new Symbol(sym.CONST_INT, yytext());
    }
}

{CONST_REAL} {
    double valor = Double.parseDouble(yytext());
    if (valor > MAX_CONST_REAL) {
        resultados = ""; this.utils.mostrarRealLimit(); throw new RuntimeException();
    } else {
        resultados += "Token CONST_REAL encontrado, Lexema: " + yytext() + "\n";
        return new Symbol(sym.CONST_REAL, yytext());
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
        return new Symbol(sym.CONST_STRING, yytext());
    }
}

{CONST_B}               { resultados += "Token CONST_B encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.CONST_B, yytext()); }

{OP_MAYOR_IGUAL}             { resultados += "Token OP_MAYOR_IGUAL encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_MAYOR_IGUAL, yytext()); }
{OP_MENOR_IGUAL}             { resultados += "Token OP_MENOR_IGUAL encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_MENOR_IGUAL, yytext()); }
{OP_MAYOR}             { resultados += "Token OP_MAYOR encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_MAYOR, yytext()); }
{OP_MENOR}             { resultados += "Token OP_MENOR encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_MENOR, yytext()); }
{OP_IGUAL}             { resultados += "Token OP_IGUAL encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_IGUAL, yytext()); }

{OP_SUMA}         { resultados += "Token OP_SUMA encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_SUMA, yytext()); }
{OP_RESTA}         { resultados += "Token OP_RESTA encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_RESTA, yytext()); }
{OP_MULT}         { resultados += "Token OP_MULT encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_MULT, yytext()); }
{OP_DIV}         { resultados += "Token OP_DIV encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.OP_DIV, yytext()); }


"::="                   { resultados += "Token ASIGN encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ASIGN, yytext()); }
":="                    { resultados += "Token DECLARACION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.DECLARACION, yytext()); }

{ABRIR_PARENTESIS}   { resultados += "Token ABRIR_PARENTESIS encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ABRIR_PARENTESIS, yytext()); }
{CERRAR_PARENTESIS}  { resultados += "Token CERRAR_PARENTESIS encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.CERRAR_PARENTESIS, yytext()); }
{ABRIR_LLAVE}        { resultados += "Token ABRIR_LLAVE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ABRIR_LLAVE, yytext()); }
{CERRAR_LLAVE}       { resultados += "Token CERRAR_LLAVE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.CERRAR_LLAVE, yytext()); }
{ABRIR_CORCHETE}   { resultados += "Token ABRIR_CORCHETE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ABRIR_CORCHETE, yytext()); }
{CERRAR_CORCHETE}    { resultados += "Token CERRAR_CORCHETE encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.CERRAR_CORCHETE, yytext()); }
{PUNTO_Y_COMA}       { resultados += "Token PUNTO_Y_COMA encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.PUNTO_Y_COMA, yytext()); }
{ADMIRACION}         { resultados += "Token ADMIRACION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.ADMIRACION, yytext()); }
{INTERROGACION}      { resultados += "Token INTERROGACION encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.INTERROGACION, yytext()); }
{COMA}               { resultados += "Token COMA encontrado, Lexema " + yytext() + "\n"; return new Symbol(sym.COMA, yytext()); }

{COMENTARIO_BLOQUE}     {}
{COMENTARIO_BLOQUE1}     {}
/* whitespace */
{WhiteSpace}            {}

[^] { resultados = ""; this.utils.mostrarBadToken(); throw new RuntimeException(); }
}