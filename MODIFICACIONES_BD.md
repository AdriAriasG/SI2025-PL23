# Modificaciones de Base de Datos - Sprint 2

Este documento detalla los cambios realizados en el esquema de la base de datos (`schema.sql`) para dar soporte a las historias de usuario del segundo sprint.

## 1. Gestión de Temáticas (HU #34060, #34063, #34064, #34070)
Se han añadido tablas para permitir la clasificación multicriterio por temáticas:
- **`Tematica`**: Catálogo maestro de temas (ej: Deportes, Moda).
- **`EventoTematica`**: Relación N:M para asignar múltiples temas a un evento.
- **`ReporteroTematica`**: Especializaciones de los reporteros.
- **`EmpresaTematica`**: Intereses temáticos de las empresas de comunicación.

## 2. Contenido Multimedia (HU #34061, #34062)
- **`Multimedia`**: Nueva tabla para gestionar imágenes y vídeos dentro de un reportaje. 
  - Incluye control de estado (`BORRADOR`, `DEFINITIVO`).
  - Restricción de unicidad para la ruta del archivo dentro de un mismo reportaje.

## 3. Revisión de Reportajes (HU #34065, #34066)
- **`Reportaje`**: Se añade la columna `estado` (`NORMAL`, `EN_REVISION`) para controlar el flujo de trabajo y bloquear ediciones/restauraciones mientras esté en revisión.
- **`RevisionReportaje`**: Nueva tabla para registrar los comentarios de los reporteros asignados y el estado de su revisión individual.

## 4. Reporteros y Asignación (HU #34068, #34070)
- **`Reportero`**: 
    - Se añade la columna `tipo` (`GRAFICO`, `CAMAROGRAFO`, `BASE`) para permitir el filtrado por especialidad técnica.
    - La columna `id_agencia` ahora es opcional (permite `NULL`) para dar soporte a los reporteros freelance.
- **`DecisionFreelance`**: Nueva tabla para que los reporteros freelance registren su interés (`INTERESADO`, `NO_INTERESADO`, `DUDOSO`) en eventos disponibles.

## 5. Ofrecimientos y Comercialización (HU #34067, #34072, #34073)
- **`Ofrecimiento`**:
    - **`precio`**: Almacena el precio específico para cada ofrecimiento realizado a una empresa.
    - **`descargado`**: Flag booleano para indicar si la empresa ya ha obtenido el reportaje, impidiendo la revocación del acceso si es `TRUE`.
