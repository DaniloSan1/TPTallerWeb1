document
    .getElementById("modalAgregarAmigo")
    .addEventListener("show.bs.modal", function (e) {
        document.getElementById("lista-amigos").innerHTML =
            "<p>Cargando amigos...</p>";
        fetch("/spring/amigos")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Error en la respuesta");
                }
                return response.json();
            })
            .then((data) => {
                if (data && data.length > 0) {
                    let html = '<ul class="list-group">';
                    data.forEach(function (amigo) {
                        html +=
                            '<li class="list-group-item d-flex justify-content-between align-items-center">';
                        html +=
                            amigo.nombre +
                            " " +
                            amigo.apellido +
                            " (" +
                            amigo.username +
                            ")";
                        html +=
                            '<button class="btn btn-sm btn-success" onclick="agregarAmigo(' +
                            amigo.id +
                            ')">Agregar</button>';
                        html += "</li>";
                    });
                    html += "</ul>";
                    document.getElementById("lista-amigos").innerHTML = html;
                } else {
                    document.getElementById("lista-amigos").innerHTML =
                        "<p>No tienes amigos para agregar.</p>";
                }
            })
            .catch((error) => {
                console.error("Error:", error);
                document.getElementById("lista-amigos").innerHTML =
                    "<p>Error al cargar amigos.</p>";
            });
    });

function agregarAmigo(amigoId) {
    // Set the amigoId in the hidden form and submit it
    document.getElementById("jugadorIdInput").value = amigoId;
    document.getElementById("formAgregarJugador").submit();
}

// Handle promover capitan modal
document
    .getElementById("promoverCapitanModal")
    .addEventListener("show.bs.modal", function (event) {
        var button = event.relatedTarget;
        var miembroId = button.getAttribute("data-bs-id");
        var nombre = button.getAttribute("data-bs-nombre");
        var modal = this;
        modal.querySelector("#participante-a-promover-id").value = miembroId;
        modal.querySelector("#mensaje-promover-capitan").textContent =
            "¿Estás seguro que deseas promover a " +
            nombre +
            " a capitán? Esto removerá al capitán actual si existe.";
        modal.querySelector("#form-promover-capitan").action =
            "/spring/participantes/" + miembroId + "/promover-capitan";
    });

document
    .getElementById("btn-promover-capitan")
    .addEventListener("click", function () {
        document.getElementById("form-promover-capitan").submit();
    });

// Handle eliminar miembro modal
document
    .getElementById("eliminarMiembroModal")
    .addEventListener("show.bs.modal", function (event) {
        var button = event.relatedTarget;
        var miembroId = button.getAttribute("data-bs-id");
        var nombre = button.getAttribute("data-bs-nombre");
        var modal = this;
        modal.querySelector("#miembro-a-eliminar-id").value = miembroId;
        modal.querySelector("#mensaje-eliminar-miembro").textContent =
            "¿Estás seguro que deseas eliminar a " + nombre + " del equipo?";
        modal.querySelector("#form-eliminar-miembro").action =
            "/spring/participantes/" + miembroId;
    });

document
    .getElementById("btn-eliminar-miembro")
    .addEventListener("click", function () {
        var form = document.getElementById("form-eliminar-miembro");
        form.submit();
    });
