# Registro de uso de inteligencia artificial

Registra los usos de IA que influyeron en el diseño, el código, las pruebas o la documentación entregada.

Para cada uso se indica: herramienta, propósito, componentes afectados, resultado aprovechado, revisión
del equipo y forma de verificación.



## Herramientas utilizadas

| Herramienta | Uso |
|---|---|
| Claude Code (Anthropic, modelo Claude Opus) | Análisis del repositorio y de la consigna, propuestas de código, diagnóstico de errores, pruebas de verificación y borradores de documentación. |


## Criterio de trabajo

- La IA **no modificó el repositorio ni hizo commits**: proponía cambios y los integrantes los revisaban,
  aplicaban y commiteaban.
- Las decisiones de comportamiento las tomó el equipo cuando la consigna no las fijaba. Por ejemplo: cómo
  funciona el warp, qué hace el radar, la consecuencia del mantenimiento y el pedido de M-01.
- Antes de proponer cada cambio, la IA lo compilaba y lo probaba en una copia temporal del proyecto, fuera
  del repositorio. Después de que el equipo lo aplicaba, se volvía a compilar y probar sobre el repositorio.

## Registro

### 1. Análisis del repositorio y de la consigna
- **Propósito:** entender la estructura del proyecto y contrastarla con el Enunciado de la Parte 2, el
  Anexo, la Aclaración y la guía (rúbrica de evaluación).
- **Componentes:** ninguno (solo análisis).
- **Resultado aprovechado:** lista de requisitos cumplidos y pendientes, que se usó para priorizar el trabajo.
- **Revisión del equipo:** La sugerencia de quitar el singleton de Destino se rechazó porque es una clase de la cátedra.
- **Verificación:** comparación manual con los PDFs de la consigna.

### 2. Generador del universo
- **Propósito:** crear el tablero con sus elementos desde el modelo y no desde el controlador.
- **Componentes:** `GeneradorDeUniverso` (nuevo) y su Javadoc.
- **Resultado aprovechado:** la clase y la documentación de sus métodos.
- **Revisión del equipo:** 	Antes de aceptar, se discutió si convenía mantener el constructor público de Tablero y si las naves aliadas eran elementos del universo. Se pidió una explicación de crearNaveEnemigaAleatoria.
- **Verificación:** compilación y ejecución de misiones sobre tableros generados.

### 3. Motor warp con el patrón State y bucle de la misión
- **Propósito:** el motor se trababa después de cada salto warp (bucle infinito) porque el estado estaba
  duplicado en banderas de `Mision`.
- **Componentes:** `MotorWarp`, `StateMotorWarp`, los cuatro `StateMW*`, `Mision.ejecutar()`, `Nave`, `Asistente`.
- **Resultado aprovechado:**
  - rediseño en el que cada estado decide el movimiento;
  - `Thread.sleep` entre turnos;
  - registro en la bitácora de los eventos del motor.

  El comportamiento final (warp cargado, salto normal cuando no es posible el warp, radar que no distingue
  tipos) lo definió el equipo en varias iteraciones.
- **Revisión del equipo:** 	El comportamiento se redefinió tres veces. Se descartó que la nave espere mientras carga el warp: decidimos que siempre se mueve, que el warp queda cargado y que, si no puede hacerlo, hace un salto normal. También que el radar no distingue tipos.
- **Verificación:** pruebas de la secuencia de estados y ejecución masiva de misiones sin cuelgues.

### 4. Reinicio seguro durante una misión
- **Propósito:** el botón "Configurar nueva nave" causaba `NullPointerException` si se tocaba con una
  misión en curso.
- **Componentes:** `Controlador.ejecutarMisionSeleccionada()`, `IVista`, `Ventana`.
- **Resultado aprovechado:**
  - deshabilitar el reinicio mientras corre la misión;
  - conservar el asistente durante el hilo;
  - habilitar los controles en un `finally`.

  El hilo de la misión ya existía en el proyecto.
- **Revisión del equipo:** 	Se pidió una explicación del cambio en Controlador antes de aplicarlo
- **Verificación:** prueba de la interfaz real con clics simulados.

### 5. Contratos, mantenimiento y bitácora
- **Propósito:** cumplir los contratos de la guía, el mantenimiento de la Ficha de Inicio y la bitácora
  de la Aclaración (R5).
- **Componentes:**
  - `Nave`: cargas, consumos, capacidad, `requiereMantenimiento` y `realizarMantenimiento`;
  - `Bitacora` y `EventoBitacora` (nuevo);
  - `Mision.preparar()`;
  - `Asistente`: operaciones de recursos;
  - `NaveFactory`, `MisionFactory`, `MotorWarp`, `Tripulante`, `EstacionDeCarga`.
- **Resultado aprovechado:**
  - validaciones con excepciones en lugar de `assert`;
  - bitácora con hora, inmutable y sincronizada.

  La consecuencia del mantenimiento la eligió el equipo.
- **Revisión del equipo:** Se pidió justificar EventoBitacora y los synchronized, se entendió que synchronized evita leer la bitácora mientras el hilo de la misión la modifica. Elegimos que el mantenimiento pendiente bloquee M-01/M-02 y permita M-03.
- **Verificación:** pruebas de cada contrato y de los escenarios B y D.

### 6. Finalizar Misiones
- **Propósito:** completar M-01, M-02 y M-03 según el Anexo y el Enunciado.
- **Componentes:** `Mision` (hooks `obtenerObjetivo`, `verificarEnDestino` y `aplicarResultado`),
  `MisionIntercepcion`, `MisionRecoleccion`, `MisionRetorno`, `InformeMision`, `EstadoRecursos` (nuevo),
  `Asistente` (base de origen) y `Nave` (gemas).
- **Resultado aprovechado:**
  - asistencia real a la nave aliada;
  - gemas en la nave;
  - retorno a la base;
  - recompensas en el cierre;
  - balance de recursos en el informe.

  El formato del pedido de M-01 lo eligió el equipo.
- **Revisión del equipo:** Se cuestionó si hacía falta EstadoRecursos y se aceptó después de la explicación. Definimos el pedido de M-01: energía y combustible por separado, de 0 a 50.
- **Verificación:** pruebas de cada misión y del balance del informe.

### 7. Detalle de haberes
- **Propósito:** informar el aporte de cada concepto del haber (guía, E1-08).
- **Componentes:** `ConceptoHaber` (nuevo), `Tripulante`, los cuatro cargos, los decoradores de origen y `Ventana`.
- **Resultado aprovechado:** el detalle por concepto y que el total se calcule como la suma del detalle.
  El equipo decidió mantener un solo decorador.
- **Revisión del equipo:** Decidimos agregar solo el detalle por concepto.
- **Verificación:** comparación de los totales antes y después del cambio (132 casos, sin diferencias).

### 8. Pruebas de verificación
- **Propósito:** comprobar los cambios y el cumplimiento de la consigna.
- **Componentes:** programas de prueba ejecutados fuera del repositorio. No forman parte de la entrega.
- **Resultado aprovechado:** detección de errores que no estaban a la vista. Por ejemplo:
  - inversión en los reslutados de las batallas;
  - excepciones en campañas largas;
  - el choque de mayúsculas en la carpeta de concurrencia.
- **Revisión del equipo:** Tambien se realizaron pruebas manuales.
- **Verificación:** no aplica.
