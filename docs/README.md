# TPG Programación C 2026 — Software embarcado de una nave interestelar

Trabajo Práctico Grupal de Programación C (Facultad de Ingeniería, UNMdP).
Asistente de navegación y comando de una nave tripulada: tripulación y haberes, motor warp,
misiones sobre un universo dinámico, login de usuarios y un visor de concurrencia
(productor-consumidor), con interfaz Swing organizada bajo MVC.

**Entrega:** Parte 2 — etiqueta Git `entrega-e2`

## Integrantes

| Integrante | Usuario GitHub |
|---|---|
| Juan Bautista Ortiz Erasun | juanchidev |
| Mateo Zoratti | mateozoratti |
| Stefano Merendino | cuantum98 |
| Gaspar Liboreiro | GasparLiboreiro |
| Valentin Villar | vvalenv |
| Cristian Tapia | CristianTapia97 |

## Requisitos

- **JDK 25** (el `pom.xml` compila con `source/target 25`). Con un JDK anterior, Maven falla con
  `invalid target release: 25`.
- **Maven 3.9** o superior.
- IDE recomendado: IntelliJ IDEA (el proyecto incluye la configuración en `.idea/`).
- Dependencia descargada automáticamente por Maven: `org.xerial:sqlite-jdbc:3.47.2.0`
  (persistencia de usuarios).

## Compilar

```
mvn compile
```

## Ejecutar

La clase principal es `Prueba` (en `src/test/java`).

**Desde IntelliJ:** abrir el proyecto como proyecto Maven, configurar el SDK en JDK 25
(File → Project Structure → Project SDK) y ejecutar `Prueba.main`.

**Desde la terminal:**

```
mvn compile exec:java -Dexec.mainClass=Prueba -Dexec.classpathScope=test
```

### Uso

1. **Acceso:** iniciar sesión con nombre y contraseña (1 a 50 caracteres). Para crear un usuario, "Registrarse" abre el formulario de registro; al crearlo se vuelve al login con el nombre ya cargado.
2. **Creación:** elegir el tipo de nave (exploradora, carguero o combate), cargar al capitán y al menos
   4 tripulantes, y finalizar.
3. **Control de la nave:** se muestran los recursos, la tripulación con el detalle de haberes, la bitácora,
   el último informe y el tablero en tiempo real. Elegir una misión (intercepción, recolección o retorno)
   y ejecutarla. "Configurar nueva nave" vuelve a la pantalla de creación cuando no hay una misión en curso.
4. **Visor recóndito (concurrencia):** desde la pantalla de creación, botón "Concurrencia". Se ingresa la
   capacidad de la plataforma y se agregan generadoras y naves consumidoras; el panel muestra los eventos en
   tiempo real.

## Configuración local

- Los usuarios registrados se guardan en `usuarios.db` (SQLite), que se crea en la carpeta desde donde se
  ejecuta el programa. Está excluido del repositorio por `.gitignore`.
- Existe una implementación alternativa del DAO en XML (`DAOUsuariosXML`, archivo `usuarios.xml`).
  Para usarla, cambiar `new DAOUsuariosSQLite()` por `new DAOUsuariosXML()` en `Prueba`.

## Estructura del proyecto

```
src/main/java
├── controlador/        Controlador (MVC) y ControladorConcurrencia
├── vista/              Ventana Swing, visor de concurrencia e interfaces IVista
└── modelo/
    ├── Asistente, Sistema, Bitacora, EventoBitacora, MotorWarp y estados del motor (StateMW*)
    ├── nave/           Nave, tipos de nave, NaveFactory, EstadoRecursos
    ├── tripulantes/    Tripulante, cargos, decoradores de origen, ConceptoHaber, TripulanteFactory
    ├── misiones/       Mision (Template Method), M-01/M-02/M-03, MisionFactory, InformeMision
    ├── destinoytablero/ Tablero, Destino (provistos por la cátedra), GeneradorDeUniverso, Observer
    ├── Elementos/      Asteroide, EstacionDeCarga
    ├── Concurrencia/   PlataformaDeCarga, GeneradoraDeEnergia, NaveConsumidora
    ├── persistencia/   DAOUsuarios, DAOUsuariosSQLite, DAOUsuariosXML, DTOUsuario
    └── excepciones/
src/test/java           Prueba (programa principal) y PruebaDAO
```

## Patrones de diseño

| Patrón | Dónde | Para qué |
|---|---|---|
| State | `MotorWarp` + `StateMWDisponible`, `StateMWPreparandoSalto`, `StateMWEnWarp`, `StateMWEnfriamiento` | Cada estado decide cuánto avanza la nave y a qué estado pasa; el motor solo expone `saltar()` y `saltarWarp()`. |
| Template Method | `Mision.ejecutarCiclo()` | Ciclo fijo preparar → ejecutar → evaluar → cerrar. Después de cada etapa se invoca su hook (prepararHook, ejecutarHook, evaluarHook, cerrarHook); además cada misión redefine obtenerObjetivo, verificarEnDestino, aplicarResultado y exigeNaveSinMantenimientoPendiente |
| Factory | `NaveFactory`, `TripulanteFactory`, `MisionFactory` | El cliente pide el tipo sin instanciar clases concretas. |
| Decorator | `TripulanteOrigenDecorator` y orígenes | Agrega el subsidio por origen al haber; `getDetalleHaberes()` mantiene identificable cada concepto. |
| Double Dispatch | `Nave.gana(...)` / `ganaExploradora()`, `ganaCarguera()`, `ganaCombate()` | Resultado de las batallas según el tipo de ambas naves. |
| Observer | `Tablero` → `Ventana`; `PlataformaDeCarga` → `VentanaVisorConcurrencia` | Actualización automática del tablero y del log del visor. |
| MVC | `Controlador`, `Ventana`/`IVista`, modelo | Separación entre acciones del usuario, lógica y presentación. |
| DAO / DTO | `DAOUsuarios` + `DTOUsuario` | Persistencia de credenciales de usuarios. |
| Productor-Consumidor (monitores) | `PlataformaDeCarga` | `synchronized`, `wait()` en `while` y `notifyAll()`. |

## Decisiones de diseño

- **Motor warp:** la nave siempre avanza. Al detectar el primer elemento con el radar se carga el warp
  (2 turnos, con saltos normales); el siguiente elemento detectado dispara el warp (2 casillas, -5 energía)
  y luego hay un turno de enfriamiento (+3 desgaste). Si el warp no es posible, se hace un salto normal
  (-2 combustible) y queda registrado en la bitácora.
- **Radar de corto alcance:** solo informa si hay un elemento en la próxima casilla, no su tipo.
- **M-01:** la nave aliada es el objetivo de la misión. Pide entre 0 y 50 de energía y de combustible; al
  llegar se verifica que la nave los conserve y la entrega se descuenta en el cierre.
- **M-02:** recolecta entre 20 y 100 gemas, que se suman a la nave en el cierre.
- **M-03:** vuelve a la base de origen (punto de partida), recarga al 100 % y realiza el mantenimiento.
- **Mantenimiento:** con desgaste ≥ 80 la nave requiere mantenimiento; M-01 y M-02 se rechazan y M-03 se permite.
- **Capacidad:** combustible y energía hasta 100 para las tres naves (Ficha de Inicio); los valores
  iniciales dependen del tipo.
- **Bitácora:** cada evento guarda fecha y hora, no puede modificarse y no acepta eventos vacíos.
- **Destrucción:** la nave se destruye con combustible 0, con desgaste 100 o al perder una batalla. La energía puede llegar a 0: no destruye la nave, solo impide el salto warp.


## Verificación

Escenarios reproducibles desde la interfaz:

- **A — Ejecución correcta:** crear una nave con tripulación válida y ejecutar M-01, M-02 y M-03; revisar
  recursos, bitácora e informe (con el balance de la travesía).
- **B — Recursos insuficientes:** ejecutar misiones hasta agotar recursos; la misión se rechaza sin cambios
  y el motivo queda en la bitácora.
- **C — Motor warp:** observar en la bitácora la carga, el warp, el enfriamiento y los rechazos.
- **D — Contrato inválido:** con desgaste ≥ 80, intentar M-01 o M-02 (rechazo) y luego M-03 (permitida).

## Documentación

- Informe de la Parte 1: `Informe trabajo práctico grupal entrega 1 progra C.docx`
- Uso de herramientas de IA: `docs/uso-ia.md`