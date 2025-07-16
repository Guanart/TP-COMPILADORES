package com.grupo2.tpteo1grupo2.clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.grupo2.tpteo1grupo2.Simbolo;
import com.grupo2.tpteo1grupo2.TablaSimbolos;

public class NodoPrograma extends Nodo {
    private final ArrayList<NodoSentencia> sentencias;

    public NodoPrograma(ArrayList<NodoSentencia> sentencias) {
        super("PGM");
        this.sentencias = sentencias;
    }

    public String graficar() {
        // Como un NodoPrograma no tiene padre, se inicia pasando null.
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {");
        resultado.append(miId + " [label=\"Programa\"]\n");
        // Graficamos cada sentencia
        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }
        resultado.append("}");
        return resultado.toString();
    }

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
                            // 30 porque es el límite definido para cada string
                            dataSection.append("_").append(simbolo.nombre).append(" DB 30 DUP (?), '$'\n");
                            break;
                        default:
                            // Otros tipos si los hubiera
                            break;
                    }
                    break;
                case "CONST_INT":
                    dataSection.append(simbolo.nombre).append(" DD ").append(simbolo.valor).append(".0").append("\n");
                    break;
                case "CONST_REAL":
                    dataSection.append(simbolo.nombre).append(" DD ").append(simbolo.valor).append("\n");
                    break;
                case "CONST_STRING":
                    dataSection.append(simbolo.nombre).append(" DB ").append(simbolo.valor).append(", '$'\n");
                    break;
                case "CONST_B":
                    dataSection.append(simbolo.nombre).append(" DD ").append(simbolo.valor).append(".0").append("\n");
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
        assembler.append("include macros.asm\n");
        assembler.append("include macros2.asm\n");
        assembler.append("include number.asm\n");
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