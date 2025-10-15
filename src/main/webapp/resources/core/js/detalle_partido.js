async function unirseAPartido(partidoId) {
  const url = `/spring/partidos/${partidoId}/inscripcion`;
  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      // Get the error message from the response body
      const errorMessage = await response.text();
      alert(errorMessage);
      return;
    }

    // Opcional: mostrar un mensaje de éxito o recargar la página
    alert("Te has unido al partido exitosamente!");
    window.location.reload(); // Refresh the page after a successful response
  } catch (error) {
    console.error(error);
    alert("Error al unirse al partido");
  }
}

// Hacer la función globalmente disponible
window.unirseAPartido = unirseAPartido;
