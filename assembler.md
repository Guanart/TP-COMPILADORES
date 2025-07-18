# Generación de Código Assembler - Explicación Detallada

## Índice
1. [Flujo General - NodoPrograma](#1-flujo-general---nodoprograma)
2. [Operaciones Aritméticas](#2-operaciones-aritméticas)
3. [Asignaciones - NodoAsignacion](#3-asignaciones---nodoasignacion)
4. [Comparaciones](#4-comparaciones)
5. [Operadores Lógicos](#5-operadores-lógicos)
6. [Estructura IF - NodoIf](#6-estructura-if---nodoif)
7. [Ciclo WHILE - NodoCiclo](#7-ciclo-while---nodociclo)
8. [Escritura - NodoEscritura](#8-escritura---nodoescritura)
9. [OPLIST - NodoOplist](#9-oplist---nodooplist)
10. [Ejemplo Completo - Flujo del archivo prueba.txt](#10-ejemplo-completo---flujo-del-archivo-pruebatxt)

## 1. Flujo General - NodoPrograma

El proceso comienza en [`NodoPrograma.generarAssembler()`](src/main/java/com/grupo2/tpteo1grupo2/clases/NodoPrograma.java):

### Paso 1: Generación de la Data Section
Primero recorre la tabla de símbolos y genera declaraciones para cada símbolo:

```assembly
.DATA
_a          DD 0.0      ; Variable FLOAT
_var1       DD 0.0      ; Variable INTEGER (como DD 0.0)
_var2       DD 0.0      ; Variable FLOAT
_b          DD 0.0      ; Variable FLOAT
_var3       DD 0.0      ; Variable INTEGER
_var4       DD 0.0      ; Variable FLOAT
_mensaje    DB 30 DUP (?), '$'  ; Variable STRING
_contador   DD 0.0      ; Variable INTEGER

; Constantes
_15         DD 15.0     ; CONST_INT
_20_9       DD 20.9     ; CONST_REAL
_"Hola_Mundo" DB "Hola Mundo", '$'  ; CONST_STRING
_0b1101     DD 13.0     ; CONST_B (binario convertido)
```

### Paso 2: Generación del Code Section
Luego llama a `generarAssembler()` de cada sentencia del programa.

### Estructura Final del Archivo Assembler:
```assembly
include macros.asm
include macros2.asm
include number.asm
.MODEL LARGE
.386
.STACK 200h

.DATA
; ... declaraciones de variables y constantes ...

.CODE
START:
MOV EAX,@DATA ; inicializa el segmento de datos
MOV DS,EAX
MOV ES,EAX

; ... código generado por las sentencias ...

MOV EAX,4C00h ; Indica que debe finalizar la ejecución
INT 21h
END START
```

## 2. Operaciones Aritméticas

### NodoSuma, NodoResta, NodoMultiplicacion, NodoDivision

**Estrategia común**: Usan la pila del coprocesador (FPU) y variables auxiliares.

#### Implementación en NodoSuma:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String code = "";

    // Cargar lado izquierdo
    if (!izquierda.soyHoja()) {
        izquierda.generarAssembler(dataSection, codeSection);
        code += "FLD _@" + izquierda.getIdNodo() + "\n";
    } else {
        code += "FLD _" + izquierda.getDescripcion() + "\n";
    }

    // Cargar lado derecho
    if (!derecha.soyHoja()) {
        derecha.generarAssembler(dataSection, codeSection);
        code += "FLD _@" + derecha.getIdNodo() + "\n";
    } else {
        code += "FLD _" + derecha.getDescripcion() + "\n";
    }

    code += "FADD\n"; // Suma en la pila FPU
    code += "FSTP _@" + this.getIdNodo() + "\n"; // Guarda resultado

    // Declara variable auxiliar
    dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
    codeSection.append(code);
}
```

#### Ejemplo: `var1 + 1`
**Assembler generado**:
```assembly
FLD _var1       ; Carga var1 en pila FPU
FLD _1          ; Carga 1 en pila FPU
FADD            ; Suma: ST(0) = ST(1) + ST(0)
FSTP _@nodo123  ; Guarda resultado en variable auxiliar
```

#### Operaciones específicas:
- **NodoSuma**: `FADD`
- **NodoResta**: `FSUB`
- **NodoMultiplicacion**: `FMUL`
- **NodoDivision**: `FDIV`

#### Para expresiones complejas como `(98.9 + 1)/2 * 5`:

1. Se evalúa `98.9 + 1` → guarda en `_@nodo1`
2. Se evalúa `_@nodo1 / 2` → guarda en `_@nodo2`  
3. Se evalúa `_@nodo2 * 5` → guarda en `_@nodo3`

**Variables auxiliares generadas**:
```assembly
_@nodo1 DD ?    ; Para 98.9 + 1
_@nodo2 DD ?    ; Para resultado / 2
_@nodo3 DD ?    ; Para resultado * 5
```

## 3. Asignaciones - NodoAsignacion

### Implementación:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String code = "";
    
    if (!expresion.soyHoja()) {
        // Expresión compleja: usar resultado en variable auxiliar
        expresion.generarAssembler(dataSection, codeSection);
        code += "FLD _@" + expresion.getIdNodo() + "\n";
        code += "FSTP _" + identificador.getId() + "\n";
    } else {
        // Expresión simple: cargar directamente
        if (expresion.getTipoValorExpresion().equals("STRING")) {
            // Caso especial para strings
            code += "LEA SI, _" + expresion.getDescripcion() + "\n";
            code += "LEA DI, _" + identificador.getId() + "\n";
            code += "STRCPY\n";
        } else {
            // Números
            code += "FLD _" + expresion.getDescripcion() + "\n";
            code += "FSTP _" + identificador.getId() + "\n";
        }
    }
    codeSection.append(code);
}
```

### Ejemplos de Assembler generado:

#### Asignación simple: `var1 ::= 15`
```assembly
FLD _15         ; Carga constante 15
FSTP _var1      ; Guarda en var1
```

#### Asignación de string: `mensaje ::= "Hola Mundo"`
```assembly
LEA SI, _"Hola_Mundo"  ; SI apunta al string fuente
LEA DI, _mensaje       ; DI apunta al string destino
STRCPY                 ; Macro para copiar string
```

#### Asignación compleja: `a ::= var1 + 2.5`
```assembly
; Primero se evalúa var1 + 2.5 → _@nodo_suma
FLD _var1
FLD _2_5
FADD
FSTP _@nodo_suma

; Luego se asigna a 'a'
FLD _@nodo_suma
FSTP _a
```

## 4. Comparaciones

### NodoMayor, NodoMenor, NodoIgual, NodoMayorIgual, NodoMenorIgual

**Estrategia**: Usan el coprocesador para comparar y saltos condicionales.

#### Implementación general:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String etiquetaTrue = "CMP_TRUE_" + this.getIdNodo();
    String etiquetaEnd = "CMP_END_" + this.getIdNodo();
    
    // Cargar lado izquierdo
    if (!izquierda.soyHoja()) {
        izquierda.generarAssembler(dataSection, codeSection);
        code.append("FLD _@" + izquierda.getIdNodo() + "\n");
    } else {
        code.append("FLD _" + izquierda.getDescripcion() + "\n");
    }
    
    // Comparar con lado derecho
    if (!derecha.soyHoja()) {
        derecha.generarAssembler(dataSection, codeSection);
        code.append("FCOM _@" + derecha.getIdNodo() + "\n");
    } else {
        code.append("FCOM _" + derecha.getDescripcion() + "\n");
    }
    
    // Procesar flags del coprocesador
    code.append("FSTSW AX\n");  // Status word a AX
    code.append("SAHF\n");      // AX a flags del procesador
    
    // Salto condicional específico
    code.append("J? " + etiquetaTrue + "\n");  // J? depende del tipo
    
    // Falso: poner 0
    code.append("MOV _@" + this.getIdNodo() + ", 0\n");
    code.append("JMP " + etiquetaEnd + "\n");
    
    // Verdadero: poner 1
    code.append(etiquetaTrue + ":\n");
    code.append("MOV _@" + this.getIdNodo() + ", 1\n");
    code.append(etiquetaEnd + ":\n");
    
    // Declarar variable auxiliar
    dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
}
```

#### Saltos específicos por operador:
- **NodoMenor** (`<`): `JB` (Jump Below)
- **NodoMayor** (`>`): `JA` (Jump Above)
- **NodoIgual** (`=`): `JE` (Jump Equal)
- **NodoMenorIgual** (`<=`): `JBE` (Jump Below or Equal)
- **NodoMayorIgual** (`>=`): `JAE` (Jump Above or Equal)

#### Ejemplo: `var1 < var2`
**Assembler generado**:
```assembly
FLD _var1           ; Carga var1
FCOM _var2          ; Compara con var2
FSTSW AX           ; Status word a AX
SAHF               ; AX a flags CPU
JB CMP_TRUE_nodo5  ; Si var1 < var2, salta
MOV _@nodo5, 0     ; Falso: resultado = 0
JMP CMP_END_nodo5
CMP_TRUE_nodo5:
MOV _@nodo5, 1     ; Verdadero: resultado = 1
CMP_END_nodo5:
```

## 5. Operadores Lógicos

### NodoAnd y NodoOr

**Estrategia**: Operaciones bit a bit sobre los resultados de las comparaciones.

#### Implementación de NodoAnd:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String code = "";

    // Generar código de ambas subexpresiones
    if (!izquierda.soyHoja()) {
        izquierda.generarAssembler(dataSection, codeSection);
    }
    if (!derecha.soyHoja()) {
        derecha.generarAssembler(dataSection, codeSection);
    }

    // Cargar resultados y aplicar AND
    if (!izquierda.soyHoja()) {
        code += "MOV EAX, _@" + izquierda.getIdNodo() + "\n";
    } else {
        code += "MOV EAX, _" + izquierda.getDescripcion() + "\n";
    }

    if (!derecha.soyHoja()) {
        code += "MOV EBX, _@" + derecha.getIdNodo() + "\n";
    } else {
        code += "MOV EBX, _" + derecha.getDescripcion() + "\n";
    }

    code += "AND EAX, EBX\n";  // AND bit a bit
    code += "MOV _@" + this.getIdNodo() + ", EAX\n";

    dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
    codeSection.append(code);
}
```

#### NodoOr es similar pero usa `OR EAX, EBX`

#### Ejemplo: `(condicion1) && (condicion2)`
**Assembler generado**:
```assembly
; Primero se evalúan las comparaciones individuales
; ... código de comparación 1 → resultado en _@nodo1
; ... código de comparación 2 → resultado en _@nodo2

; Luego el AND
MOV EAX, _@nodo1   ; Carga resultado 1
MOV EBX, _@nodo2   ; Carga resultado 2  
AND EAX, EBX       ; AND lógico
MOV _@nodo3, EAX   ; Guarda resultado final
```

## 6. Estructura IF - NodoIf

**Estrategia**: Saltos condicionales con etiquetas únicas.

### Implementación:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String labelElse = "ELSE_" + this.getIdNodo();
    String labelEndIf = "ENDIF_" + this.getIdNodo();
    
    // Evaluar condición
    condicion.generarAssembler(dataSection, codeSection);
    code.append("MOV EAX, _@" + condicion.getIdNodo() + "\n");
    code.append("CMP EAX, 0\n");
    
    boolean tieneElse = sentenciasElse != null && !sentenciasElse.isEmpty();
    if (tieneElse) {
        code.append("JE " + labelElse + "\n");  // Si falso, ir a ELSE
    } else {
        code.append("JE " + labelEndIf + "\n"); // Si falso, ir a ENDIF
    }
    
    // Código del THEN
    for (NodoSentencia sentencia : sentenciasThen) {
        sentencia.generarAssembler(dataSection, codeSection);
    }
    
    if (tieneElse) {
        code.append("JMP " + labelEndIf + "\n"); // Saltar ELSE
        code.append(labelElse + ":\n");
        
        // Código del ELSE
        for (NodoSentencia sentencia : sentenciasElse) {
            sentencia.generarAssembler(dataSection, codeSection);
        }
    }
    
    code.append(labelEndIf + ":\n");
    codeSection.append(code);
}
```

### Ejemplo del código:
```
IF (contador >= 10) THEN
    WRITE "Contador alcanzo el limite"
ELSE
    WRITE "Valor contador: "
ENDIF
```

**Assembler generado**:
```assembly
; Evaluación de (contador >= 10)
; ... código que deja resultado en _@nodo_condicion

MOV EAX, _@nodo_condicion
CMP EAX, 0
JE ELSE_nodo_if           ; Si falso, ir a ELSE

; THEN: 
DisplayString _"Contador_alcanzo_el_limite"
newLine 1
JMP ENDIF_nodo_if

ELSE_nodo_if:
DisplayString _"Valor_contador_"
newLine 1

ENDIF_nodo_if:
```

## 7. Ciclo WHILE - NodoCiclo

**Estrategia**: Etiqueta de inicio + evaluación + salto condicional + salto de retorno.

### Implementación:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String labelInicio = "WHILE_START_" + this.getIdNodo();
    String labelFin = "WHILE_END_" + this.getIdNodo();
    
    code += labelInicio + ":\n";
    
    // Evaluar condición
    condicion.generarAssembler(dataSection, codeSection);
    code += "MOV EAX, _@" + condicion.getIdNodo() + "\n";
    code += "CMP EAX, 0\n";
    code += "JE " + labelFin + "\n";  // Si falso, salir del ciclo
    
    // Cuerpo del ciclo
    for (NodoSentencia sentencia : cuerpo) {
        sentencia.generarAssembler(dataSection, codeSection);
    }
    
    code += "JMP " + labelInicio + "\n";  // Volver al inicio
    code += labelFin + ":\n";
    
    codeSection.append(code);
}
```

### Ejemplo:
```
WHILE (var1 < var2) THEN
    contador ::= contador + 1
    var1 ::= var1 + 1
ENDWHILE
```

**Assembler generado**:
```assembly
WHILE_START_nodo_while:
    ; Evaluación de (var1 < var2)
    FLD _var1
    FCOM _var2
    FSTSW AX
    SAHF
    JB CMP_TRUE_nodo_comp
    MOV _@nodo_comp, 0
    JMP CMP_END_nodo_comp
CMP_TRUE_nodo_comp:
    MOV _@nodo_comp, 1
CMP_END_nodo_comp:
    
    ; Chequear condición
    MOV EAX, _@nodo_comp
    CMP EAX, 0
    JE WHILE_END_nodo_while
    
    ; Cuerpo del ciclo:
    ; contador ::= contador + 1
    FLD _contador
    FLD _1
    FADD
    FSTP _contador
    
    ; var1 ::= var1 + 1
    FLD _var1
    FLD _1
    FADD  
    FSTP _var1
    
    JMP WHILE_START_nodo_while

WHILE_END_nodo_while:
```

## 8. Escritura - NodoEscritura

**Estrategia**: Usa macros predefinidas según el tipo de dato.

### Implementación:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    String variable;
    String prefijo;
    
    if (!parametro.soyHoja()) {
        parametro.generarAssembler(dataSection, codeSection);
        prefijo = "_@";  // Variable auxiliar
    } else {
        prefijo = "_";   // Variable normal o constante
    }
    
    // Determinar nombre de la variable
    if (parametro instanceof NodoIdentificador) {
        variable = prefijo + ((NodoIdentificador) parametro).getId();
    } else {
        variable = prefijo + parametro.getDescripcion();
    }
    
    String tipo = parametro.getTipoValorExpresion();
    
    switch (tipo) {
        case "INTEGER":
        case "FLOAT":
            codeSection.append("DisplayFloat ").append(variable).append(",2\n");
            break;
        case "STRING":
            codeSection.append("DisplayString ").append(variable).append("\n");
            break;
    }
    codeSection.append("newLine 1\n");
}
```

### Ejemplos de Assembler generado:

#### `WRITE var1`
```assembly
DisplayFloat _var1,2
newLine 1
```

#### `WRITE "Hola Mundo"`
```assembly
DisplayString _"Hola_Mundo"
newLine 1
```

#### `WRITE (expresión compleja)`
```assembly
; Primero se evalúa la expresión → _@nodo_resultado
DisplayFloat _@nodo_resultado,2
newLine 1
```

### Macros utilizadas:
- **DisplayFloat**: Para mostrar números (INTEGER y FLOAT)
- **DisplayString**: Para mostrar cadenas
- **newLine**: Para salto de línea

## 9. OPLIST - NodoOplist

**Estrategia**: Delega en una asignación especial que ya contiene la lógica de aplicar la operación a toda la lista.

### Implementación:
```java
public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    asignacion.generarAssembler(dataSection, codeSection);
}
```

### Cómo funciona OPLIST internamente:

Cuando el parser encuentra:
```
OPLIST [+ [a;b] [2;3;5;9]]
```

Lo construye como una asignación equivalente a:
```
b ::= a + 2 + 3 + 5 + 9
```

### Assembler generado:
```assembly
; b ::= a + 2 + 3 + 5 + 9 (resultado de OPLIST [+ [a;b] [2;3;5;9]])
FLD _a          ; Cargar valor inicial
FLD _2          ; Cargar primer elemento
FADD            ; Sumar
FLD _3          ; Cargar segundo elemento  
FADD            ; Sumar
FLD _5          ; Cargar tercer elemento
FADD            ; Sumar
FLD _9          ; Cargar cuarto elemento
FADD            ; Sumar
FSTP _b         ; Guardar resultado final en b
```

### Para OPLIST con multiplicación:
```
OPLIST [* [a;b] [10;3;3;5]]
```
Se convierte en: `b ::= a * 10 * 3 * 3 * 5`

```assembly
FLD _a          ; Cargar valor inicial
FLD _10         ; Cargar primer elemento
FMUL            ; Multiplicar
FLD _3          ; Cargar segundo elemento
FMUL            ; Multiplicar
FLD _3          ; Cargar tercer elemento
FMUL            ; Multiplicar
FLD _5          ; Cargar cuarto elemento
FMUL            ; Multiplicar
FSTP _b         ; Guardar resultado final en b
```

## 10. Ejemplo Completo - Flujo del archivo prueba.txt

### Línea compleja del código:
```
IF ((var2 = (98.9 + 1)/2 * 5) && (var1 * 2 + 1 >= 20)) THEN
```

### Proceso de generación paso a paso:

#### 1. Subexpresión: `98.9 + 1`
```assembly
_@nodo1 DD ?    ; Variable auxiliar declarada

FLD _98_9       ; Carga 98.9
FLD _1          ; Carga 1
FADD            ; Suma
FSTP _@nodo1    ; Guarda resultado
```

#### 2. Subexpresión: `_@nodo1 / 2`
```assembly
_@nodo2 DD ?    ; Variable auxiliar declarada

FLD _@nodo1     ; Carga resultado anterior
FLD _2          ; Carga 2
FDIV            ; Divide
FSTP _@nodo2    ; Guarda resultado
```

#### 3. Subexpresión: `_@nodo2 * 5`
```assembly
_@nodo3 DD ?    ; Variable auxiliar declarada

FLD _@nodo2     ; Carga resultado anterior
FLD _5          ; Carga 5
FMUL            ; Multiplica
FSTP _@nodo3    ; Guarda resultado
```

#### 4. Comparación: `var2 = _@nodo3`
```assembly
_@nodo4 DD ?    ; Variable auxiliar declarada

FLD _var2       ; Carga var2
FCOM _@nodo3    ; Compara con resultado anterior
FSTSW AX        ; Status word a AX
SAHF            ; AX a flags
JE CMP_TRUE_nodo4
MOV _@nodo4, 0  ; Falso
JMP CMP_END_nodo4
CMP_TRUE_nodo4:
MOV _@nodo4, 1  ; Verdadero
CMP_END_nodo4:
```

#### 5. Subexpresión: `var1 * 2`
```assembly
_@nodo5 DD ?    ; Variable auxiliar declarada

FLD _var1       ; Carga var1
FLD _2          ; Carga 2
FMUL            ; Multiplica
FSTP _@nodo5    ; Guarda resultado
```

#### 6. Subexpresión: `_@nodo5 + 1`
```assembly
_@nodo6 DD ?    ; Variable auxiliar declarada

FLD _@nodo5     ; Carga resultado anterior
FLD _1          ; Carga 1
FADD            ; Suma
FSTP _@nodo6    ; Guarda resultado
```

#### 7. Comparación: `_@nodo6 >= 20`
```assembly
_@nodo7 DD ?    ; Variable auxiliar declarada

FLD _@nodo6     ; Carga resultado anterior
FCOM _20        ; Compara con 20
FSTSW AX        ; Status word a AX
SAHF            ; AX a flags
JAE CMP_TRUE_nodo7  ; Jump Above or Equal
MOV _@nodo7, 0  ; Falso
JMP CMP_END_nodo7
CMP_TRUE_nodo7:
MOV _@nodo7, 1  ; Verdadero
CMP_END_nodo7:
```

#### 8. AND lógico: `_@nodo4 && _@nodo7`
```assembly
_@nodo8 DD ?    ; Variable auxiliar declarada

MOV EAX, _@nodo4    ; Carga resultado primera comparación
MOV EBX, _@nodo7    ; Carga resultado segunda comparación
AND EAX, EBX        ; AND lógico
MOV _@nodo8, EAX    ; Guarda resultado final
```

#### 9. IF final: Usa `_@nodo8` para decidir el salto
```assembly
MOV EAX, _@nodo8    ; Carga resultado del AND
CMP EAX, 0          ; Compara con 0
JE ELSE_nodo_if     ; Si falso, ir a ELSE

; Código del THEN
; ...

JMP ENDIF_nodo_if

ELSE_nodo_if:
; Código del ELSE
; ...

ENDIF_nodo_if:
```

### Variables auxiliares generadas para esta expresión:
```assembly
_@nodo1 DD ?    ; 98.9 + 1
_@nodo2 DD ?    ; resultado / 2
_@nodo3 DD ?    ; resultado * 5
_@nodo4 DD ?    ; var2 = resultado
_@nodo5 DD ?    ; var1 * 2
_@nodo6 DD ?    ; resultado + 1
_@nodo7 DD ?    ; resultado >= 20
_@nodo8 DD ?    ; AND final
```

### Ventajas de esta estrategia:

1. **Variables auxiliares únicas**: Cada operación intermedia tiene su propia variable, evitando conflictos.

2. **Evaluación correcta**: Se respeta la precedencia de operadores al evaluar subexpresiones primero.

3. **Reutilización**: Las variables auxiliares pueden ser referenciadas múltiples veces.

4. **Claridad**: El código assembler refleja claramente la estructura del AST.

5. **Debugging**: Cada paso intermedio es visible y verificable.

Esta estrategia asegura que expresiones complejas se evalúen correctamente, respetando la precedencia de operadores y manteniendo la semántica del lenguaje fuente.

---

## Conceptos Clave del Coprocesador (FPU)

### Pila del Coprocesador:
- **ST(0)**: Tope de la pila (registro más reciente)
- **ST(1)**: Segundo elemento de la pila
- Las operaciones trabajan con estos registros

### Instrucciones principales:
- **FLD**: Carga un valor en la pila (push)
- **FSTP**: Almacena el tope de la pila y lo desapila (store and pop)
- **FADD/FSUB/FMUL/FDIV**: Operaciones aritméticas
- **FCOM**: Comparación de punto flotante
- **FSTSW**: Copia status word del coprocesador a un registro

### Flujo típico:
1. Cargar operandos con FLD
2. Realizar operación (FADD, FSUB, etc.)
3. Almacenar resultado con FSTP
4. Para comparaciones: FCOM + FSTSW + SAHF + salto condicional

Esta arquitectura permite manejar tanto números enteros como reales de manera uniforme, simplificando la generación de código.