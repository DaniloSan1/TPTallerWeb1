const exampleModal = document.getElementById("actualizarEquipo");
if (exampleModal) {
  exampleModal.addEventListener("show.bs.modal", (event) => {
    const button = event.relatedTarget;
    const nombreJugador = button.getAttribute("data-bs-nombre");
    const equipoJugador = button.getAttribute("data-bs-equipo");
    const participanteId = button.getAttribute("data-bs-id");
    const modalTitle = exampleModal.querySelector(".modal-title");
    const participanteIdInput = exampleModal.querySelector("#participante-id");

    const equipoSelect = exampleModal.querySelector("#equipo-select");
    if (equipoSelect) {
      for (const option of equipoSelect.options) {
        if (option.value === equipoJugador) {
          option.selected = true;
        }
      }
    }

    modalTitle.textContent = `Asignar equipo a ${nombreJugador}`;
    participanteIdInput.value = participanteId;
  });
}

// Handle assign button
const btnAsignar = document.getElementById("btn-asignar");
if (btnAsignar) {
  btnAsignar.addEventListener("click", () => {
    const participanteId = document.getElementById("participante-id").value;
    const form = document.getElementById("form-asignar-equipo");
    form.action = `/spring/participantes/${participanteId}/asignacion-equipo`;
    form.submit();
  });
}
