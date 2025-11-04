const actualizarEquipoModal = document.getElementById("actualizarEquipo");
if (actualizarEquipoModal) {
    actualizarEquipoModal.addEventListener("show.bs.modal", (event) => {
        const button = event.relatedTarget;
        const nombreJugador = button.getAttribute("data-bs-nombre");
        const equipoJugador = button.getAttribute("data-bs-equipo");
        const participanteId = button.getAttribute("data-bs-id");
        const modalTitle = actualizarEquipoModal.querySelector(".modal-title");
        const participanteIdInput =
            actualizarEquipoModal.querySelector("#participante-id");

        const equipoSelect =
            actualizarEquipoModal.querySelector("#equipo-select");
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

const eliminarParticipanteModal = document.getElementById(
    "eliminarParticipante"
);
if (eliminarParticipanteModal) {
    eliminarParticipanteModal.addEventListener("show.bs.modal", (event) => {
        const button = event.relatedTarget;
        const nombreJugador = button.getAttribute("data-bs-nombre");
        const participanteId = button.getAttribute("data-bs-id");

        const modalMessage = eliminarParticipanteModal.querySelector(
            "#mensaje-eliminar-participante"
        );

        const participanteIdInput = eliminarParticipanteModal.querySelector(
            "#participante-a-eliminar-id"
        );

        modalMessage.textContent = `¿Estás seguro que deseas eliminar a ${nombreJugador} del partido?`;
        participanteIdInput.value = participanteId;
    });
}

const btnAsignar = document.getElementById("btn-asignar");
if (btnAsignar) {
    btnAsignar.addEventListener("click", () => {
        const participanteId = document.getElementById("participante-id").value;
        const form = document.getElementById("form-asignar-equipo");
        form.action = `/spring/participantes/${participanteId}/asignacion-equipo`;
        form.submit();
    });
}

const btnEliminar = document.getElementById("btn-eliminar");
if (btnEliminar) {
    btnEliminar.addEventListener("click", () => {
        const participanteId = document.getElementById(
            "participante-a-eliminar-id"
        ).value;
        const form = document.getElementById("form-eliminar-participante");
        form.action = `/spring/participantes/${participanteId}`;
        form.method = "POST";
        form.submit();
    });
}
