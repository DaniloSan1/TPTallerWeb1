const exampleModal = document.getElementById("actualizarEquipo");
if (exampleModal) {
  exampleModal.addEventListener("show.bs.modal", (event) => {
    const button = event.relatedTarget;
    const nombreJugador = button.getAttribute("data-bs-nombre");
    const equipoJugador = button.getAttribute("data-bs-equipo");
    const modalTitle = exampleModal.querySelector(".modal-title");
    const modalBodyInput = exampleModal.querySelector(".modal-body input");

    const equipoSelect = exampleModal.querySelector(".modal-body select");
    if (equipoSelect) {
      for (const option of equipoSelect.options) {
        if (option.value === equipoJugador) {
          option.selected = true;
        }
      }
    }

    modalTitle.textContent = `Asignar equipo a ${nombreJugador}`;
    modalBodyInput.value = nombreJugador;
  });
}
