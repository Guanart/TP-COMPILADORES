import java_cup.runtime.Symbol;

%%

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
"FOR"                   {System.out.println("Token FOR encontrado, Lexema "+yytext());}
"IF"                    {System.out.println("Token IF encontrado, Lexema "+yytext());}
"ELSE"                  {System.out.println("Token ELSE encontrado, Lexema "+yytext());}
"ENDIF"                 {System.out.println("Token ENDIF encontrado, Lexema "+yytext());}
"WHILE"                 {System.out.println("Token WHILE encontrado, Lexema "+yytext());}
"FOREACH"               {System.out.println("Token FOREACH encontrado, Lexema "+yytext());}
"ENDWHILE"              {System.out.println("Token ENDWHILE encontrado, Lexema "+yytext());}
"ENDFOR"                {System.out.println("Token ENDFOR encontrado, Lexema "+yytext());}
"WRITE"                 {System.out.println("Token WRITE encontrado, Lexema "+yytext());}
"FLOAT"                 {System.out.println("Token FLOAT encontrado, Lexema "+yytext());}
"INTEGER"               {System.out.println("Token INTEGER encontrado, Lexema "+yytext());}
"STRING"                {System.out.println("Token STRING encontrado, Lexema "+yytext());}
"BOOLEAN"               {System.out.println("Token BOOLEAN encontrado, Lexema "+yytext());}
"RANGE"                 {System.out.println("Token RANGE encontrado, Lexema "+yytext());}
"TRUE"                  {System.out.println("Token TRUE encontrado, Lexema "+yytext());}
"FALSE"                 {System.out.println("Token FALSE encontrado, Lexema "+yytext());}
"OPLIST"                {System.out.println("Token OPLIST encontrado, Lexema "+yytext());}
{ID}	{System.out.println("Token ID encontrado, Lexema "+ yytext());}
{CONST_INT}	{System.out.println("Token CONST_INT, encontrado Lexema "+ yytext());}
{CONST_REAL}	{System.out.println("Token CONST_REAL, encontrado Lexema "+ yytext());}
{CONST_STRING}      {System.out.println("Token CONST_STRING encontrado, Lexema "+ yytext());}
{CONST_B}           {System.out.println("Token CONST_B encontrado, Lexema "+ yytext());}
{OP_LOGICO}         {System.out.println("Token OP_LOGICO encontrado, Lexema "+ yytext());}
{OP_ARITMETICO}     {System.out.println("Token OP_ARITMETICO encontrado, Lexema "+ yytext());}
{CON_LOGICO}        {System.out.println("Token CON_LOGICO encontrado, Lexema "+ yytext());}
{OPLIST}            {System.out.println("Token OPLIST encontrado, Lexema "+ yytext());}
"::="	{System.out.println("Token ASIGN encontrado, Lexema "+yytext());}
":="	{System.out.println("Token DECLARATION encontrado, Lexema "+yytext());}
"DECLARE.SECTION"       {System.out.println("Token DECLARE.SECTION encontrado, Lexema "+yytext());}
"ENDDECLARE.SECTION"    {System.out.println("Token ENDDECLARE.SECTION encontrado, Lexema "+yytext());}
"PROGRAM.SECTION"       {System.out.println("Token PROGRAM.SECTION encontrado, Lexema "+yytext());}
"ENDPROGRAM.SECTION"    {System.out.println("Token ENDPROGRAM.SECTION encontrado, Lexema "+yytext());}
{SIMBOLO}               {System.out.println("Token SIMBOLO encontrado, Lexema "+yytext());}
{COMENTARIO_BLOQUE}               {System.out.println("Token COMENTARIO encontrado, Lexema "+yytext());}
{COMENTARIO_LINEA}               {System.out.println("Token COMENTARIO encontrado, Lexema "+yytext());}
/* whitespace */
{WhiteSpace}                   { /* ignore */ }

[^] { throw new Error("Caracter no permitido: " + yytext() + "> en la linea " + yyline); }

}