# Guía del Proyecto SI2025-PL23

## 📋 Descripción General

Este proyecto es un **Sistema de Gestión de Reportajes** para agencias de prensa, desarrollado en Java con interfaz Swing. El proyecto combina:

1. **Nueva Aplicación (`app/`)**: Sistema de gestión de reportajes en desarrollo activo
2. **Código de Ejemplo Obsoleto (`giis/`)**: Referencias y plantillas de un sistema de carreras anterior

---

## 🏗️ Estructura del Proyecto

```
SI2025-PL23/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/                    # ← NUEVA APLICACIÓN (desarrollar aquí)
│   │   │   │   ├── controller/         # Controladores MVC
│   │   │   │   ├── model/              # Modelos MVC (acceso a datos)
│   │   │   │   ├── view/               # Vistas MVC (Swing)
│   │   │   │   ├── dto/                # Data Transfer Objects
│   │   │   │   └── util/               # Utilidades y clases base
│   │   │   │
│   │   │   └── giis/                   # ← CÓDIGO OBSOLETO (solo referencia)
│   │   │       └── demo/
│   │   │           ├── jdbc/           # Ejemplo JDBC básico
│   │   │           ├── tkrun/          # Ejemplo MVC completo (carreras)
│   │   │           └── util/           # Utilidades antiguas
│   │   │
│   │   └── resources/
│   │       ├── application.properties  # Configuración BD
│   │       ├── schema.sql              # Esquema de tablas
│   │       └── data.sql                # Datos iniciales
│   │
│   └── test/
│       └── java/
│           └── giis/demo/              # Tests del código antiguo (ejemplo)
│
├── pom.xml                             # Configuración Maven
└── README.md                           # Documentación original
```

---

## 🆕 Nueva Aplicación (`app/`) - Sistema de Reportajes

### Historias de Usuario Asignadas

#### 🏢 Agencia de Prensa (Adrián / Iván)
| HU | Descripción | Responsable |
|----|-------------|-------------|
| #33537 | Asignación de reporteros | Adrián |
| #33543 | Modificar asignación | Adrián |
| #33548 | Informe de un evento | Adrián |
| #33539 | Ofrecer reportajes | Iván |
| #33544 | Modificar ofrecimiento | Iván |
| #33541 | Distribuir reportaje | Iván |

#### 📝 Reportero (Diego)
| HU | Descripción |
|----|-------------|
| #33538 | Entrega de reportaje |
| #33545 | Modificar entrega |
| #33547 | Restaurar versión previa |

#### 📺 Empresa de Comunicación (Irene)
| HU | Descripción |
|----|-------------|
| #33540 | Gestionar ofrecimientos |
| #33546 | Modificar decisión |
| #33542 | Acceder a reportaje |

### Modelo de Datos

```
AgenciaPrensa ──┬── Reportero ────┬── Asignacion ──── Evento
                │                  │
                │                  └── Reportaje ──── VersionReportaje
                │
                └── Evento ──────── Ofrecimiento ──── EmpresaComunicacion
```

**Tablas principales:**
- `AgenciaPrensa`: Agencias de prensa del sistema
- `Reportero`: Reporteros vinculados a agencias
- `Evento`: Eventos noticieros
- `Asignacion`: Asignación de reporteros a eventos
- `Reportaje`: Reportajes creados para eventos
- `VersionReportaje`: Versiones de los reportajes (historial)
- `EmpresaComunicacion`: Empresas que reciben ofrecimientos
- `Ofrecimiento`: Ofertas de reportajes a empresas

---

## 📁 Organización de Archivos por Capa

### Controller (`app/controller/`)
```
app/controller/
├── LoginController.java        # Ya existe - Controla el login
├── AsignacionController.java   # HU #33537, #33543 (Adrián)
├── OfrecimientoController.java # HU #33539, #33544, #33541 (Iván)
├── ReportajeController.java    # HU #33538, #33545, #33547 (Diego)
└── EmpresaController.java      # HU #33540, #33546, #33542 (Irene)
```

**Responsabilidades:**
- Recibir eventos de la vista
- Coordinar modelo y vista
- Gestionar el flujo de la aplicación

### Model (`app/model/`)
```
app/model/
├── LoginModel.java             # Ya existe - Obtiene agencias
├── AsignacionModel.java        # Lógica de asignaciones
├── OfrecimientoModel.java      # Lógica de ofrecimientos
├── ReportajeModel.java         # Lógica de reportajes
└── EmpresaModel.java           # Lógica de empresas
```

**Responsabilidades:**
- Acceso a base de datos
- Lógica de negocio
- Validaciones

### View (`app/view/`)
```
app/view/
├── LoginView.java              # Ya existe - Pantalla de login
├── MainView.java               # Ventana principal (opcional refactor)
├── AsignacionView.java         # UI para asignaciones
├── OfrecimientoView.java       # UI para ofrecimientos
├── ReportajeView.java          # UI para reportajes
└── EmpresaView.java            # UI para empresas
```

**Responsabilidades:**
- Interfaz gráfica Swing
- Mostrar datos al usuario
- Capturar acciones del usuario

### DTO (`app/dto/`)
```
app/dto/
├── AgenciaDTO.java             # Ya existe
├── ReporteroDTO.java           # DTO para reporteros
├── EventoDTO.java              # DTO para eventos
├── AsignacionDTO.java          # DTO para asignaciones
├── ReportajeDTO.java           # DTO para reportajes
├── VersionDTO.java             # DTO para versiones
├── OfrecimientoDTO.java        # DTO para ofrecimientos
└── EmpresaDTO.java             # DTO para empresas
```

**Convenciones:**
- Un DTO por entidad principal
- Constructores vacíos para DbUtils
- Getters/Setters para todos los campos
- Sobrescribir `toString()` para JComboBox si aplica

### Util (`app/util/`)
```
app/util/
├── Database.java               # Ya existe - Conexión y scripts
├── DbUtil.java                 # Ya existe - Operaciones BD
├── SwingMain.java              # Ya existe - Punto de entrada
├── ApplicationException.java   # Ya existe
└── UnexpectedException.java    # Ya existe
```

---

## 📝 Patrones y Convenciones

### Patrón MVC
```
Vista → Evento → Controlador → Modelo → Datos
                ↓
              Vista ← Actualización
```

### Convenciones de Nomenclatura
- **Controladores**: `XxxController.java`
- **Modelos**: `XxxModel.java`
- **Vistas**: `XxxView.java`
- **DTOs**: `XxxDTO.java`
- **Tests**: `TestXxx.java`

### Flujo de Desarrollo Recomendado

1. **Crear DTOs** para las entidades necesarias
2. **Crear Model** con métodos de acceso a datos
3. **Crear View** con la interfaz Swing
4. **Crear Controller** que coordine modelo y vista
5. **Crear Tests** unitarios
6. **Integrar** en `SwingMain.java`

---

## 📚 Código de Ejemplo Obsoleto (`giis/`) - Solo Referencia

El paquete `giis.demo` contiene código de un proyecto anterior sobre **gestión de carreras populares**. Este código es **obsoleto** y no debe modificarse, pero sirve como referencia para:

### `giis.demo.tkrun` - Ejemplo MVC Completo
| Archivo | Qué aprender |
|---------|--------------|
| `CarrerasController.java` | Patrón controlador, manejo de eventos |
| `CarrerasModel.java` | Acceso a BD, validaciones, SQL |
| `CarrerasView.java` | Interfaz Swing con tablas |
| `CarreraEntity.java` | Ejemplo de entidad |
| `CarreraDisplayDTO.java` | Ejemplo de DTO |

### `giis.demo.jdbc` - Ejemplo JDBC Básico
| Archivo | Qué aprender |
|---------|--------------|
| `DemoJdbc.java` | Conexión JDBC básica |
| `Entity.java` | Mapeo manual de entidades |

### `giis.demo.util` - Utilidades Antiguas
> ⚠️ **No usar** - Usar las de `app.util` en su lugar

### Tests de Ejemplo (`giis.demo.tkrun.ut`)
| Archivo | Qué aprender |
|---------|--------------|
| `TestInscripcion.java` | Tests unitarios, asserts, excepciones |
| `TestInscripcionParametrized.java` | Tests parametrizados |
| `TestUpdates.java` | Tests de actualización BD |

---

## 🔧 Configuración de Base de Datos

### Inicialización
La BD se inicializa automáticamente al ejecutar `SwingMain.main()`:
1. Ejecuta `schema.sql` (crea tablas)
2. Ejecuta `data.sql` (inserta datos iniciales)

### Modificar Esquema
1. Editar `src/main/resources/schema.sql`
2. Editar `src/main/resources/data.sql` si hay nuevos datos
3. Reiniciar la aplicación

---

## 📌 Notas Importantes

### Punto de Entrada
- **Clase principal**: `app.util.SwingMain` (NO CONFUNDIR CON EL OBSOLETO DE GIIS.DEMO, que son los archivos del proyecto de muestra)
- Primero muestra `LoginView` para seleccionar agencia
- Luego abre ventana principal con las HU disponibles

### Login Actual
- Selecciona agencia de prensa (obligatorio)
- Pasa la agencia seleccionada a la ventana principal
- La agencia determina qué datos puede ver/manipular el usuario