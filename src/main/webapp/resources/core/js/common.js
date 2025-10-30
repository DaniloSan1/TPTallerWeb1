setTimeout(() => {
  document.querySelectorAll(".alert-success").forEach((el) => el.remove());
  document.querySelectorAll(".error").forEach((el) => el.remove());
}, 3000);
