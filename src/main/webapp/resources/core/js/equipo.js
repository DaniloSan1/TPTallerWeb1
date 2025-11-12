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
