setTimeout(() => {
  document.querySelectorAll("#alertSuccess").forEach((el) => el.remove());
  document.querySelectorAll("#alertError").forEach((el) => el.remove());
}, 3000);
