# Product Backlog - Sprint 3

---

## HU #34426: Finalizar la asignación de reporteros
ADRIÁN
> **Como** agencia de prensa, **quiero** poder finalizar la asignación de reporteros de un evento para cerrar el proceso y evitar modificaciones posteriores.

- Modificación de las HU #33537, #33543, #34060 y #34068
- En la pantalla de asignación, para los eventos que ya tienen al menos un reportero asignado, se debe poder designar a uno de ellos como Reportero Responsable (RR).
- Cada asignación debe tener un RR y un Reportero Base asignados para poder finalizar la asignación.
- El RR puede cambiarse mientras la asignación no esté finalizada.
- Una vez finalizada la asignación, no se podrá añadir ni eliminar reporteros, ni cambiar el Reportero Responsable.
- Los eventos con asignación finalizada se distinguen visualmente (en el listado) de los que no la tienen.
- Los eventos con asignación finalizada seguirán siendo visibles en el listado pero no editables.

### Plan de pruebas HU #34426

Todas las pruebas usan: **Agencia 1 (Agencia Norte)** → botón **"HU #33543: Asignar/modificar reporteros"**.
Antes de empezar, pulsar **"Cargar Datos Iniciales para Pruebas"** para partir de un estado limpio.

**Datos relevantes (Agencia 1):**
| Evento | Reporteros asignados | Tipos |
|---|---|---|
| Ev.10 Final Copa Local (01/03) | Carlos Martinez, Laura Garcia | GRAFICO, CAMAROGRAFO |
| Ev.11 Inauguracion Museo (01/03) | Miguel Fernandez | BASE |
| Ev.14 Festival de Cine (20/03) | Carlos Martinez, Laura Garcia, Miguel Fernandez | GRAFICO, CAMAROGRAFO, BASE |

---

#### TEST 1 — Designar RR (opcional)
1. Filtro → **"Con asignados"**
2. Seleccionar **Ev.14 Festival de Cine**
3. En "Reporteros del Evento", seleccionar **Carlos Martinez**
4. Pulsar **"Designar como RR"**
5. **Esperado:** Carlos aparece con "★ RR" en la columna RR, fila azul. El resto sin marca.

#### TEST 2 — Cambiar RR
1. (Seguir desde TEST 1) Seleccionar **Laura Garcia** en la tabla
2. Pulsar **"Designar como RR"**
3. **Esperado:** Laura pasa a ser "★ RR". Carlos ya no tiene marca. Solo una estrella en toda la tabla.

#### TEST 3 — Intentar finalizar sin RR (solo BASE)
1. Reiniciar datos. Filtro → **"Con asignados"**
2. Seleccionar **Ev.11 Inauguracion Museo** (tiene Miguel Fernandez, BASE)
3. **No** designar ningún RR
4. Pulsar **"Finalizar Asignación"**
5. **Esperado:** Error → "debe designar un Reportero Responsable (RR)"

#### TEST 4 — Intentar finalizar sin reportero BASE (solo RR)
1. Filtro → **"Con asignados"**
2. Seleccionar **Ev.10 Final Copa Local** (solo tiene GRAFICO y CAMAROGRAFO, ningún BASE)
3. Designar a **Carlos Martinez** (GRAFICO) como RR
4. Pulsar **"Finalizar Asignación"**
5. **Esperado:** Error → "debe haber al menos un reportero de tipo BASE asignado que no sea el RR"

#### TEST 5 — Intentar finalizar con RR de tipo BASE pero sin otro BASE adicional
1. Reiniciar datos. Filtro → **"Con asignados"**
2. Seleccionar **Ev.11 Inauguracion Museo** (solo tiene Miguel Fernandez, BASE)
3. Designar a **Miguel Fernandez** (BASE) como RR
4. Pulsar **"Finalizar Asignación"**
5. **Esperado:** Error → "debe haber al menos un reportero de tipo BASE asignado que no sea el RR". Aunque el RR sea BASE, se necesita al menos un BASE adicional.

#### TEST 5b — Finalizar con RR y BASE (caso válido)
1. Reiniciar datos. Filtro → **"Con asignados"**
2. Seleccionar **Ev.14 Festival de Cine** (tiene GRAFICO, CAMAROGRAFO y BASE)
3. Designar a **Carlos Martinez** (GRAFICO) como RR
4. Pulsar **"Finalizar Asignación"** → Confirmar
5. **Esperado:** Mensaje de éxito. Hay un RR + al menos un BASE (Miguel Fernandez) que no es el RR → se puede finalizar.

#### TEST 6 — Evento finalizado: distinción visual
1. (Seguir desde TEST 5b) Mirar la tabla de eventos
2. **Esperado:** Ev.14 muestra "FINALIZADA" en la columna Estado, con fondo gris-azulado y texto en cursiva. El resto muestra "Abierta".

#### TEST 7 — Evento finalizado: no editable
1. (Seguir desde TEST 5b) Seleccionar **Ev.14 Festival de Cine** (ya finalizado)
2. **Esperado:**
   - Botones "Asignar", "Eliminar", "Designar como RR" y "Finalizar" → **deshabilitados**
   - Filtros de temática y tipo → **deshabilitados**
   - Tabla de disponibles → vacía y no interactiva
   - Los reporteros asignados se ven pero no se pueden modificar

#### TEST 8 — Designar RR a reportero pendiente (no guardado)
1. Reiniciar datos. Filtro → **"Con asignados"**
2. Seleccionar **Ev.10 Final Copa Local**
3. En la tabla de disponibles, marcar un reportero (queda como "Pendiente" en la tabla del evento)
4. Seleccionar ese reportero pendiente → Pulsar **"Designar como RR"**
5. **Esperado:** Error → "Solo se puede designar como RR a un reportero ya asignado"

---

## HU #34430: Cubrir eventos que duren más de un día
ADRIÁN
> **Como** agencia de prensa, **quiero** poder cubrir eventos que duren más de un día, asegurando que los reporteros asignados estén disponibles durante todo el rango de fechas.

- Modificación de las HU #33537, #33543, #34060 y #34068.
- Los eventos pasan a tener una **fecha de inicio** y una **fecha de fin**, permitiendo duración de uno o varios días.
- Los eventos creados en sprints anteriores se consideran de un día (fecha de inicio = fecha de fin).
- La comprobación de disponibilidad se actualiza: un reportero está disponible para un evento si no tiene ningún otro evento asignado cuyo rango de fechas se solape en algún día con el rango del nuevo evento.
- Dos eventos se solapan si el rango [inicio A, fin A] comparte al menos un día con el rango [inicio B, fin B].
- En la pantalla de asignación se muestra el rango de fechas completo (inicio y fin) del evento seleccionado.
- Los filtros de disponibilidad existentes se actualizan para operar sobre el rango completo de fechas.
- Esta HU es requisito previo para el cálculo de dietas (número de días del evento).

---

## HU #34437: Ver reporteros freelance en la asignación de reporteros
ADRIÁN
> **Como** agencia de prensa, **quiero** poder ver y asignar reporteros freelance a un evento desde la pantalla de asignación de reporteros.

- Modificación de las HU #33537, #33543, #34060, #34068 y #34426.
- Se incorpora un filtro adicional en la pantalla de asignación que permite elegir entre ver los "reporteros de la agencia" (comportamiento actual) o ver los "reporteros freelance".
- Los freelances mostrados son exclusivamente los que, para ese evento, tienen estado "interesado" o "en duda" (según HU #34070). Los freelances con estado "no interesado" no aparecerán nunca.
- El filtro de disponibilidad (HU #34430) se aplica igualmente a los freelances: solo se muestran los disponibles para el rango de fechas del evento.
- Los filtros de temática (HU #34060) y tipo (HU #34068) son compatibles y funcionan simultáneamente con el nuevo filtro de freelances.
- Al asignar un freelance al evento, el sistema enviará automáticamente un email de notificación a dicho reportero freelance.
- Un freelance puede ser designado como Reportero Responsable(RR) de la asignación.
- No se podrá finalizar la asignación (HU #34426) si algún freelance asignado tiene estado "en duda". El botón de finalizar permanecerá deshabilitado en ese caso.
- En el listado de reporteros asignados al evento se indicará visualmente si un reportero es freelance o de plantilla.
