# Product Backlog - Sprint 2

## HU #34060: Asignación de reporteros en función a su temática de especialización
ADRIÁN
> **Como** agencia de prensa, **quiero** asignar reporteros en función a su temática de especialización

- Incorporar un filtro de temáticas para la asignación de reporteros.
- Un evento puede tener múltiples temáticas (ej: moda, gastronomía, deportes).
- Los reporteros estarán especializados en una o varias temáticas.
- El filtro mostrará solo los reporteros especializados en alguna de las temáticas del evento seleccionado.
- El funcionamiento será simultáneo con los filtros existentes de disponibilidad.
- Se mantendrá la visualización de la lista de eventos sin reporteros asignados y con reporteros asignados.

---

## HU #34061: Añadir contenido multimedia a los reportajes
DIEGO
> **Como** reportero, **quiero** poder añadir contenido multimedia a los reportajes

- Modificación de la HU #33545 y #33547
- Un reportero asignado puede añadir imágenes/vídeos a un reportaje, que se guardan como "borrador" por defecto.
- No pueden existir dos elementos multimedia con la misma ruta dentro del mismo reportaje.
- El autor puede cambiar el estado de su multimedia entre "borrador" y "definitivo".
- Solo el autor puede eliminar un elemento multimedia, únicamente si está en "borrador".
- Las acciones sobre multimedia no generan nuevas versiones del reportaje.
- Todos los reporteros asignados pueden ver el multimedia del reportaje.

---

## HU #34062: Acceso al contenido multimedia de un reportaje de un evento
IRENE
> **Como** empresa de comunicación , **quiero** acceder al contenido multimedia de un reportaje de un evento

- Modificación de la HU #33542
- El usuario seleccionará un evento de la lista y se visualizará el contenido del reportaje: título, subtítulo, cuerpo y elementos multimedia
- Únicamente se mostrarán los contenidos multimedia definitivos (nunca los borradores)
- La visualización de los elementos multimedia se realizará en formato lista

---

## HU #34063: Filtrar por temática las empresas de comunicación disponibles al ofrecer un reportaje
IVÁN
> **Como** agencia de prensa, **quiero** filtrar por temática las empresas de comunicación disponibles al ofrecer un reportaje

- Modificación de la HU #33544
- En la funcionalidad de ofrecer reportajes de un evento a empresas de comunicación, incorporar un filtro sobre la lista de empresas de comunicación disponibles:
    - Ver las empresas de comunicación cuya temática coincida con al menos una de las temáticas del evento seleccionado.
    - Ver todas las empresas de comunicación disponibles para el evento seleccionado, independientemente de su temática

---

## HU #34064: Filtrar los ofrecimientos en función a su temática
IRENE
> **Como** empresa de comunicación, **quiero** filtrar los ofrecimientos en función a su temática

- Modificación de la HU #33546
- Incorporar un filtro que muestre únicamente los ofrecimientos de eventos cuya temática coincide con la temática de la empresa de comunicación
- Es suficiente con que coincidan en una de las temáticas para que se muestre el ofrecimiento

---

## HU #34065: Solicitar la revisión de un reportaje
DIEGO
> **Como** reportero, **quiero** poder solicitar la revisión de un reportaje

- Solo el autor puede solicitar revisión, cambiando el estado del reportaje a "en revisión".
- Mientras esté "en revisión", no se puede modificar ni restaurar el reportaje.
- Los reportajes "en revisión" aparecen en la lista de revisión de los reporteros asignados.
- Solo puede solicitarse revisión si el reportaje tiene al menos una versión creada.

---

## HU #34066: Revisar reportajes
DIEGO
> **Como** reportero, **quiero** poder revisar reportajes

- Un reportero puede ver los reportajes "en revisión" de eventos en los que esté asignado.
- Puede consultar el contenido actual y el multimedia del reportaje.
- Puede añadir y modificar su comentario mientras la revisión esté pendiente.
- Al marcar la revisión como finalizada, ya no puede modificar su comentario. 
- El autor no puede revisar su propio reportaje y solo puede existir una revisión activa por reportero.

---

## HU #34067: Gestionar los accesos de las empresas de comunicación al distribuir un reportaje
IVÁN
> **Como** agencia de prensa, **quiero** gestionar los accesos de las empresas de comunicación al distribuir un reportaje

- Modificación de la HU #33541
- Incorporar un filtro que permita elegir entre dos opciones:
    - Ver empresas de comunicación que tienen acceso al reportaje
    - Ver las empresas que no tienen acceso al reportaje
- Se podrá quitar el acceso al reportaje a una empresa de comunicación que lo tenga concedido.
- Si una empresa de comunicación ya ha descargado el reportaje, no se podrá quitar el acceso.
- Si una empresa aún no ha descargado el reportaje, sí se podrá quitar el acceso

---

## HU #34068: Filtrar los reporteros por su tipo para asignarlos a eventos
ADRIÁN
> **Como** agencia de prensa, **quiero** filtrar los reporteros por su tipo para asignarlos a eventos

- Incorporar un filtro para seleccionar reporteros por tipo: gráfico, camarógrafo o base.
- El filtro funcionará simultáneamente con los filtros de disponibilidad y temáticas.
- Se mantendrá la visualización de la lista de reporteros ya asignados al evento.
- Se podrán asignar más reporteros disponibles según el tipo seleccionado.
- Se podrán eliminar reporteros asignados manteniendo el filtro de tipo activo.
- Al modificar asignación, se puede filtrar por tipo: gráfico, camarógrafo o base.

---

## HU #34070: Elegir los reportajes a realizar
IVÁN
> **Como** reportero freelance, **quiero** poder elegir los reportajes a realizar

- Visualizar los eventos disponibles para realizar un reportaje
- Los eventos mostrados coincidirán en al menos una temática con las temáticas de especialización de este reportero.
- Se podrá indicar una de las siguientes decisiones respecto a cada evento: interesado, no interesado o dudoso.

---

## HU #34072: Filtrar los ofrecimientos en función de su precio
IRENE
> **Como** empresa de comunicación, **quiero** filtrar los ofrecimientos en función de su precio

- Modificación de la HU #34064
- Se debe mostrar el precio correspondiente a cada ofrecimiento
- Incorporar un filtro con tres opciones:
   - Ver los eventos con un precio superior al indicado
   - Ver los eventos con un precio inferior al indicado
   - Ver los eventos cuyo precio esté entre un rango indicado

---

## HU #34073: Informe de reportajes con acceso
ADRIÁN
> **Como** empresa de comunicación, **quiero** un informe de reportajes con acceso

- Permitir generar informes sobre reportajes a los que la empresa tiene acceso.
- El informe mostrará reportajes de eventos entre un rango de fechas indicado por pantalla.
- El informe es solo visual, no se guarda en ningún formato.
- Se mostrará información consolidada de los reportajes seleccionados.
- El informe incluirá datos relevantes de cada reportaje accesible.
- Se puede filtrar por fechas para ajustar el ámbito del informe.
- La generación del informe es inmediata y se muestra por pantalla.
- El informe debe indicar el precio total de todos los reportajes.
- Para cada reportaje se debe mostrar título, el evento que corresponde, la fecha del evento y el precio del reportaje.