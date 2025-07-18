# Explicación Completa del Compilador - Grupo 2

## Índice
1. [Arquitectura General](#arquitectura-general)
2. [Análisis Léxico (Scanner)](#análisis-léxico-scanner)
3. [Análisis Sintáctico (Parser)](#análisis-sintáctico-parser)
4. [Análisis Semántico y Validaciones](#análisis-semántico-y-validaciones)
5. [Tabla de Símbolos](#tabla-de-símbolos)
6. [Árbol Sintáctico Abstracto (AST)](#árbol-sintáctico-abstracto-ast)
7. [Generación de Código Ensamblador](#generación-de-código-ensamblador)
8. [Interfaz Gráfica](#interfaz-gráfica)
9. [Flujo de Compilación](#flujo-de-compilación)

## Arquitectura General

El compilador sigue la arquitectura clásica de compiladores con las siguientes fases:

```
Código Fuente → Análisis Léxico → Análisis Sintáctico → Análisis Semántico → Generación de Código
```

### Componentes Principales:
- **Lexico.java**: Analizador léxico generado por JFlex
- **Parser.java**: Analizador sintáctico generado por CUP
- **Clases AST**: Representación del árbol sintáctico abstracto
- **TablaSimbolos.java**: Manejo de símbolos y tipos
- **Generación de Assembler**: Traducción a código ensamblador

## Análisis Léxico (Scanner)

El analizador léxico ([`Lexico.java`](src/main/java/com/grupo2/tpteo1grupo2/Lexico.java)) es generado a partir del archivo [`Ejemplo.flex`](src/main/java/com/grupo2/tpteo1grupo2/Ejemplo.flex).

### Tokens Reconocidos:
- **Palabras reservadas**: `IF`, `THEN`, `ELSE`, `ENDIF`, `WHILE`, `ENDWHILE`, `WRITE`, `OPLIST`
- **Tipos de datos**: `FLOAT`, `INTEGER`, `STRING`
- **Operadores**: `+`, `-`, `*`, `/`, `=`, `<`, `>`, `<=`, `>=`, `&&`, `||`
- **Delimitadores**: `[`, `]`, `(`, `)`, `;`, `,`
- **Constantes**: 
  - Enteras: `123`
  - Reales: `123.45`
  - Binarias: `0b1101`
  - Cadenas: `"Hola Mundo"`
- **Identificadores**: Variables que comienzan con letra

### Validaciones Léxicas:
- **Límites de constantes**: 
  - Enteros: máximo 1000
  - Reales: máximo 1000.0
  - Strings: máximo 30 caracteres
- **Formato binario**: Solo acepta `0b` seguido de dígitos 0 y 1

## Análisis Sintáctico (Parser)

El parser ([`Parser.cup`](src/main/java/com/grupo2/tpteo1grupo2/Parser.cup)) define la gramática del lenguaje y construye el AST.

### Gramática Principal:

```
pgm ::= declare_sec program_sec
      | program_write

declare_sec ::= DECLARE_SECTION declaracion_n ENDDECLARE_SECTION

declaracion ::= [ID_list] := [TYPE_list]

program_sec ::= PROGRAM_SECTION sentencia_n ENDPROGRAM_SECTION

sentencia ::= asignacion | iteracion | decision | escritura | oplist
```

### Precedencia de Operadores:
1. Paréntesis `()`
2. Multiplicación y División `*`, `/`
3. Suma y Resta `+`, `-`
4. Comparaciones `<`, `>`, `=`, `<=`, `>=`
5. Operadores lógicos `&&`, `||`

## Análisis Semántico y Validaciones

### 1. Validación de Declaración de Variables

**Ubicación**: [`Parser.cup`](src/main/java/com/grupo2/tpteo1grupo2/Parser.cup) - método `verificarID()`

```java
public void verificarID(String nombre) {
   if (!tablaSimbolos.contieneIdentificador(nombre)) {
       throw new InvalidIDException("La variable '" + nombre + "' no fue declarada");
   }
}
```

**¿Cuándo se ejecuta?**
- Antes de usar cualquier variable en asignaciones
- Antes de usar variables en expresiones
- Antes de usar variables en operaciones OPLIST

### 2. Validación de Tipos en Asignaciones

**Ubicación**: [`NodoAsignacion.java`](src/main/java/com/grupo2/tpteo1grupo2/clases/NodoAsignacion.java)

```java
public static Boolean isValid(NodoExpresion id, NodoExpresion exp) {
    String idType = id.getTipoValorExpresion();
    String expType = exp.getTipoValorExpresion();
    return (idType.equals("FLOAT") && (expType.equals("INTEGER"))) || idType.equals(expType);
}
```

**Reglas de compatibilidad**:
- `INTEGER` a `INTEGER` ✅
- `FLOAT` a `FLOAT` ✅  
- `STRING` a `STRING` ✅
- `INTEGER` a `FLOAT` ✅ (conversión implícita)
- `FLOAT` a `INTEGER` ❌
- `STRING` a números ❌
- Números a `STRING` ❌

### 3. Validación de Operaciones Aritméticas

**Ubicación**: [`NodoExpresionBinaria.java`](src/main/java/com/grupo2/tpteo1grupo2/clases/NodoExpresionBinaria.java)

```java
public String getTipoValorExpresion() {
    String tipo1 = izquierda.getTipoValorExpresion();
    String tipo2 = derecha.getTipoValorExpresion();
    if (tipo1.equals("STRING") || tipo2.equals("STRING")) {
        throw new InvalidTypeException("No se permiten operaciones matemáticas con STRING");
    } else if (tipo1.equals("FLOAT") || tipo2.equals("FLOAT")) {
        return "FLOAT";
    } else {
        return "INTEGER";
    }
}
```

**Reglas de tipos resultantes**:
- `INTEGER` op `INTEGER` = `INTEGER`
- `FLOAT` op `INTEGER` = `FLOAT`
- `INTEGER` op `FLOAT` = `FLOAT`  
- `FLOAT` op `FLOAT` = `FLOAT`
- Cualquier operación con `STRING` = **ERROR**

### 4. Validación de Comparaciones

**Ubicación**: [`NodoComparacion.java`](src/main/java/com/grupo2/tpteo1grupo2/clases/NodoComparacion.java)

```java
public static Boolean isValid(NodoExpresion izquierda, NodoExpresion derecha) {
    String izType = izquierda.getTipoValorExpresion();
    String derType = derecha.getTipoValorExpresion();
    return !izType.equals("STRING") && !derType.equals("STRING");
}
```

**Restricciones**:
- No se pueden comparar variables de tipo `STRING`
- Números pueden compararse entre sí (con conversión implícita)

## Tabla de Símbolos

La clase [`TablaSimbolos.java`](src/main/java/com/grupo2/tpteo1grupo2/TablaSimbolos.java) gestiona todos los símbolos del programa.

### Estructura del Símbolo:
```java
public class Simbolo {
    public final String nombre;
    public final String valor;
    public final String token;
    public final String longitud;
    public final String tipo;
}
```

### Tipos de Símbolos:
1. **Identificadores (ID)**: Variables declaradas por el usuario
2. **Constantes enteras (CONST_INT)**: Números enteros
3. **Constantes reales (CONST_REAL)**: Números decimales
4. **Constantes binarias (CONST_B)**: Números binarios convertidos a decimal
5. **Constantes string (CONST_STRING)**: Cadenas de texto

### Generación de Nombres:
- **Variables**: Mantienen su nombre original
- **Constantes**: Se les agrega prefijo `_` + valor
- **Strings**: Se "limpian" para ser compatibles con Assembler

## Árbol Sintáctico Abstracto (AST)

### Jerarquía de Clases:

```
Nodo (abstracta)
├── NodoPrograma
├── NodoSentencia (abstracta)
│   ├── NodoAsignacion
│   ├── NodoIf
│   ├── NodoCiclo
│   ├── NodoEscritura
│   └── NodoOplist
├── NodoExpresion (abstracta)
│   ├── NodoExpresionBinaria (abstracta)
│   │   ├── NodoSuma
│   │   ├── NodoResta
│   │   ├── NodoMultiplicacion
│   │   └── NodoDivision
│   ├── NodoIdentificador
│   ├── NodoConstanteEntera
│   ├── NodoConstanteReal
│   ├── NodoConstanteBinaria
│   └── NodoConstanteString
└── NodoExpresionBooleana (abstracta)
    ├── NodoComparacion (abstracta)
    │   ├── NodoMayor
    │   ├── NodoMenor
    │   ├── NodoIgual
    │   ├── NodoMayorIgual
    │   └── NodoMenorIgual
    ├── NodoAnd
    └── NodoOr
```

### Responsabilidades de cada Nodo:
- **Validación semántica**: Cada nodo valida su consistencia
- **Generación de código**: Método `generarAssembler()`
- **Graficación**: Para visualizar el AST

## Generación de Código Ensamblador

### Arquitectura objetivo:
- **Procesador**: Intel 8086
- **Sintaxis**: MASM (Microsoft Macro Assembler)
- **Modelo de memoria**: Small (64KB código + 64KB datos)

### Secciones generadas (checkear esto):

#### Data Section:
```assembly
.data
_a          DQ ?           ; Variable FLOAT
_var1       DQ ?           ; Variable INTEGER  
_mensaje    DB 31 DUP(?)   ; Variable STRING
_123        DQ 123.0       ; Constante
```

#### Code Section:
```assembly
.code
start:
    ; Código generado para cada sentencia
    FLD _var1          ; Cargar variable
    FADD _2            ; Sumar constante
    FSTP _resultado    ; Guardar resultado
```

### Estrategias de generación:

#### 1. Expresiones Aritméticas:
- Usa la pila de punto flotante (FPU)
- Genera variables auxiliares para resultados intermedios
- Respeta la precedencia de operadores

#### 2. Asignaciones:
- Valida compatibilidad de tipos
- Genera conversiones implícitas cuando es necesario
- Para strings usa macros de copia

#### 3. Estructuras de Control:
- Genera etiquetas únicas
- Implementa saltos condicionales
- Maneja anidamiento correctamente

#### 4. OPLIST:
- Aplica operación a todos los elementos de una lista
- Genera bucles para procesar cada elemento
- Acumula el resultado en la variable destino

## Interfaz Gráfica

### Componentes principales:
- **HelloApplication.java**: Clase principal de JavaFX
- **HelloController.java**: Controlador de la ventana principal  
- **ResultadoCompilacion.java**: Controlador de la ventana de resultados

### Funcionalidades:
1. **Editor de código**: TextArea para escribir programas
2. **Carga de archivos**: FileChooser para seleccionar archivos .txt
3. **Compilación**: Botón que ejecuta todo el proceso
4. **Visualización de resultados**: Muestra código assembler generado
5. **Manejo de errores**: Alerts para diferentes tipos de errores

## Flujo de Compilación

### Paso a paso del proceso:

1. **Entrada**: El usuario ingresa código o carga un archivo
2. **Creación de archivo temporal**: Se guarda en directorio temporal del sistema
3. **Análisis léxico**: JFlex tokeniza el código fuente
4. **Análisis sintáctico**: CUP parsea los tokens y construye AST
5. **Análisis semántico**: Se ejecutan las validaciones durante la construcción del AST
6. **Generación de tabla de símbolos**: Se crea archivo CSV con todos los símbolos
7. **Generación de código**: El AST genera código ensamblador
8. **Presentación de resultados**: Se muestra el código generado al usuario

### Ejemplo con `prueba.txt`:

**Código fuente**:
```
DECLARE_SECTION
[a,var1]:=[FLOAT,INTEGER]
ENDDECLARE_SECTION

PROGRAM_SECTION
var1 ::= 15
a ::= var1 + 2.5
WRITE a
ENDPROGRAM_SECTION
```

**Proceso**:
1. **Léxico**: Reconoce tokens DECLARE_SECTION, ABRIR_CORCHETE, ID(a), etc.
2. **Sintáctico**: Aplica reglas gramaticales y construye AST
3. **Semántico**: 
   - Valida que `var1` está declarada antes de usarla
   - Valida que `INTEGER + FLOAT` es válido (resultado FLOAT)
   - Valida que asignar FLOAT a variable FLOAT es válido
4. **Generación**: Produce código assembler equivalente

**Validaciones específicas ejecutadas**:
- ✅ Variables `a` y `var1` declaradas correctamente
- ✅ `var1` existe al momento de la asignación
- ✅ `var1 + 2.5` es válido (INTEGER + FLOAT = FLOAT)
- ✅ Asignar FLOAT a variable `a` (FLOAT) es válido
- ✅ Imprimir variable FLOAT es válido

### Manejo de Errores:

El compilador maneja varios tipos de errores:

1. **SintaxException**: Errores de sintaxis
2. **InvalidIDException**: Variables no declaradas
3. **InvalidTypeException**: Incompatibilidades de tipos
4. **Errores léxicos**: Caracteres no válidos, límites excedidos

Cada error se captura y se muestra al usuario con un mensaje descriptivo.

---

## Puntos Clave para la Evaluación

### 1. Validaciones Semánticas Implementadas:
- **Declaración obligatoria**: Todas las variables deben declararse
- **Compatibilidad de tipos**: Sistema estricto con conversiones limitadas
- **Operaciones válidas**: No se permiten operaciones matemáticas con strings
- **Comparaciones válidas**: No se pueden comparar strings

### 2. Arquitectura Limpia:
- **Separación de responsabilidades**: Cada clase tiene una función específica
- **Patrón Visitor implícito**: Para generación de código
- **Manejo centralizado de símbolos**: Una sola tabla para todo el programa

### 3. Robustez:
- **Manejo completo de errores**: Diferentes tipos de excepciones
- **Validación en múltiples niveles**: Léxico, sintáctico y semántico
- **Interfaz amigable**: Mensajes de error claros

Este compilador implementa un lenguaje completo con todas las fases necesarias y validaciones robustas para garantizar la correctitud