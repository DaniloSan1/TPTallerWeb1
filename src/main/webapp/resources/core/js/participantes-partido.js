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

const promoverCapitanModal = document.getElementById("promoverCapitanModal");
if (promoverCapitanModal) {
    promoverCapitanModal.addEventListener("show.bs.modal", (event) => {
        console.log("promoverCapitanModal show event triggered");
        const button = event.relatedTarget;
        const nombreJugador = button.getAttribute("data-bs-nombre");
        const participanteId = button.getAttribute("data-bs-id");
        console.log("participanteId from button:", participanteId);
        const modalMessage = promoverCapitanModal.querySelector(
            "#mensaje-promover-capitan"
        );
        const participanteIdInput = promoverCapitanModal.querySelector(
            "#participante-a-promover-id"
        );

        modalMessage.textContent = `¿Estás seguro que deseas promover a ${nombreJugador} a capitán? Esto removerá al capitán actual si existe.`;
        participanteIdInput.value = participanteId;
        console.log(
            "participanteIdInput.value set to:",
            participanteIdInput.value
        );
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

const btnPromoverCapitan = document.getElementById("btn-promover-capitan");
if (btnPromoverCapitan) {
    btnPromoverCapitan.addEventListener("click", () => {
        const participanteId = document.getElementById(
            "participante-a-promover-id"
        ).value;
        const form = document.getElementById("form-promover-capitan");
        form.action = `/spring/participantes/${participanteId}/promover-capitan`;
        form.method = "POST";
        form.submit();
    });
}
const modalCalificar = document.getElementById("modalCalificar");
if (modalCalificar) {
  modalCalificar.addEventListener("show.bs.modal", (event) => {
    const button = event.relatedTarget;
    const calificadoId = button.getAttribute("data-bs-id");
    const partidoId = document.body.getAttribute("data-partido-id");
    document.getElementById("jugador-calificar-id").value = calificadoId;
    const form = document.getElementById("form-calificar-jugador");
    form.action = `/spring/calificaciones/partido/${partidoId}/jugador/${calificadoId}`;
  });
}
