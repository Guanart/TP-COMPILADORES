package com.grupo2.tpteo1grupo2.clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.grupo2.tpteo1grupo2.Simbolo;
import com.grupo2.tpteo1grupo2.TablaSimbolos;

public class NodoPrograma extends Nodo {
    //private final ArrayList<NodoDeclaracion> declaraciones;
    private final ArrayList<NodoSentencia> sentencias;

    public NodoPrograma(ArrayList<NodoDeclaracion> declaraciones, ArrayList<NodoSentencia> sentencias) {
        super("PGM");
        //this.declaraciones = declaraciones;
        this.sentencias = sentencias;
    }

    public String graficar() {
        // Acá se dispara la invocación a los métodos graficar() de los nodos.
        // Como un NodoPrograma no tiene padre, se inicia pasando null.
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();

        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {");

        resultado.append(miId + " [label=\"Programa\"]\n");

        // if (this.declaraciones != null) {
        //     for (NodoDeclaracion declaracion : this.declaraciones) {
        //         resultado.append(declaracion.graficar(miId));
        //     }
        // }

        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }

        resultado.append("}");

        return resultado.toString();
    }

    /*
     * EJEMPLO DE TABLA DE SIMBOLOS
     * NOMBRE,VALOR,TOKEN,LONGITUD,TIPO
b,b,ID,,FLOAT
var2,var2,ID,,FLOAT
var1,var1,ID,,INTEGER
a,a,ID,,FLOAT
contador,contador,ID,,INTEGER
mensaje,mensaje,ID,,STRING
var4,var4,ID,,FLOAT
var3,var3,ID,,INTEGER
var9,var9,ID,,STRING
var8,var8,ID,,FLOAT
var7,var7,ID,,INTEGER
var6,var6,ID,,FLOAT
var5,var5,ID,,STRING
_15,15,CONST_INT,,-
_99.9,99.9,CONST_REAL,,-
_"Hola_Mundo","Hola Mundo",CONST_STRII,12,-
_1,1,CONST_INT,,-
_10,10,CONST_INT,,-
_"Contador_alcanzo_el_limite","Contador alcanzo el limite",CONST_STRII,28,-
_"Limite_alcanzado","Limite alcanzado",CONST_STRII,18,-
_98.9,98.9,CONST_REAL,,-
_2,2,CONST_INT,,-
_5,5,CONST_INT,,-
_20,20,CONST_INT,,-
_"Limite_alcanzado_:)","Limite alcanzado :)",CONST_STRII,21,-
_3,3,CONST_INT,,-
_0b1101,13,CONST_B,,-
_9,9,CONST_INT,,-
_234,234,CONST_INT,,-
_53,53,CONST_INT,,-
_123,123,CONST_INT,,-
_123.45,123.45,CONST_REAL,,-
_0b1011,11,CONST_B,,-

     */

    public void generarAssembler(TablaSimbolos tablaSimbolos) {
        StringBuilder codeSection = new StringBuilder();
        StringBuilder dataSection = new StringBuilder();

        Map<String, Simbolo> tabla = tablaSimbolos.getTabla();
        for (Map.Entry<String, Simbolo> entry : tabla.entrySet()) {
            Simbolo simbolo = entry.getValue();
            System.out.println(
                    "Generando código ensamblador para el símbolo: " + simbolo.nombre + " de tipo: " + simbolo.tipo);
            switch (simbolo.token) {
                case "ID":
                    // Si es un identificador, ahora miramos el tipo
                    switch (simbolo.tipo) {
                        case "INTEGER":
                            dataSection.append("_").append(simbolo.nombre).append(" DD 0.0\n");
                            break;
                        case "FLOAT":
                            dataSection.append("_").append(simbolo.nombre).append(" DD 0.0\n");
                            break;
                        case "STRING":
                            dataSection.append("_").append(simbolo.nombre).append(" DB \"\", 0\n");
                            break;
                        default:
                            // Otros tipos si los hubiera
                            break;
                    }
                    break;
                case "CONST_INT":
                    dataSection.append(simbolo.nombre).append(" DD ").append(simbolo.valor).append("\n");
                    break;
                case "CONST_REAL":
                    dataSection.append(simbolo.nombre).append(" DD ").append(simbolo.valor).append("\n");
                    break;
                case "CONST_STRII":
                    dataSection.append(simbolo.nombre).append(" DB ").append(simbolo.valor).append(", 0\n");
                    break;
                case "CONST_B":
                    dataSection.append(simbolo.nombre).append(" DD ").append(simbolo.valor).append("\n");
                    break;
                default:
                    // Otros tokens si los hubiera
                    break;
            }
        }

        // Generar código ensamblador para las sentencias
        for (NodoSentencia sentencia : sentencias) {
            sentencia.generarAssembler(dataSection, codeSection);
        }

        // Generar la sección de datos y código ensamblador modelo MASM/TASM - MASM (Microsoft Macro Assembler) y TASM (Turbo Assembler) -> compilar con Turbo Assembler (TASM)
        StringBuilder assembler = new StringBuilder();
        assembler.append("include macros2.asm\n");
        assembler.append(".MODEL LARGE\n");
        assembler.append(".386\n");
        assembler.append(".STACK 200h\n\n");
        assembler.append(".DATA\n");
        assembler.append(dataSection);
        assembler.append("\n.CODE\n");
        assembler.append("START:\n");
        assembler.append("MOV EAX,@DATA ; inicializa el segmento de datos\n");
        assembler.append("MOV DS,EAX\n");
        assembler.append("MOV ES,EAX\n\n");
        assembler.append(codeSection);
        assembler.append("\nMOV EAX,4C00h ; Indica que debe finalizar la ejecución\n");
        assembler.append("INT 21h\n");
        assembler.append("END START\n");

        guardarAssemblerEnArchivo(assembler.toString(), "programa.asm");

        System.out.println("Código ensamblador generado y guardado en programa.asm");
    }
    
    public static void guardarAssemblerEnArchivo(String assemblerCode, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(assemblerCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}