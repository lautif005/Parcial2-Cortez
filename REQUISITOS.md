# Examen Mercadolibre - Detector de Mutantes

## Descripción del Problema

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Mens.

Te ha contratado a ti para que desarrolles un proyecto que detecte si un humano es mutante basándose en su secuencia de ADN.

## Requerimientos Funcionales

### Método Principal

Crear un programa con un método o función con la siguiente firma:

```java
boolean isMutant(String[] dna);
```

**Parámetros:**
- `dna`: Array de Strings que representan cada fila de una tabla (NxN) con la secuencia del ADN
- Las letras de los Strings solo pueden ser: **A, T, C, G** (bases nitrogenadas del ADN)

**Retorno:**
- `true` si es mutante
- `false` si es humano

### Criterio de Detección de Mutante

Un humano es mutante si se encuentra **más de una secuencia de cuatro letras iguales**, de forma:
- Horizontal
- Vertical
- Diagonal (ambas direcciones: \ y /)

### Ejemplo de ADN Mutante

```
A T G C G A
C A G T G C
T T A T T T  <- Secuencia horizontal de 4 T's
A G A C G G
G C G T C A
T C A C T G
```

En este caso hay 2 secuencias de 4 letras iguales:
- Horizontal: TTTT (fila 3)
- Vertical u otra secuencia

Por lo tanto, este ADN corresponde a un mutante.

## Estructura de Datos

- Matriz NxN (cuadrada)
- Solo caracteres válidos: A, T, C, G
- Cada String del array representa una fila de la matriz