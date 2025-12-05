// IzvozButton.jsx
import React from "react";
import { jsPDF } from "jspdf";

export default function IzvozButton({ recept, sestavine }) {
  const generirajPDF = () => {
    const doc = new jsPDF();

    // Заглавие со боја
    doc.setFontSize(22);
    doc.setTextColor("#ff7043");
    doc.text(recept.ime, 10, 20);

    // Основни информации
    doc.setFontSize(12);
    doc.setTextColor("#000000");
    doc.text(`Tip: ${recept.tip}`, 10, 35);
    doc.text(`Ocena: ${recept.povprecnaOcena || "Brez ocen"}`, 10, 45);

    // Дел за priprava со боја
    doc.setFontSize(14);
    doc.setTextColor("#ffb347");
    doc.text("Priprava:", 10, 60);

    doc.setFontSize(12);
    doc.setTextColor("#000000");
    const pripravaLines = doc.splitTextToSize(recept.priprava, 180);
    doc.text(pripravaLines, 10, 70);

    // Sestavine со боја заглавие
    doc.setFontSize(14);
    doc.setTextColor("#ffb347");
    doc.text("Sestavine:", 10, 90 + pripravaLines.length * 10);

    // Список на sestavine
    doc.setFontSize(12);
    doc.setTextColor("#000000");
    sestavine.forEach((s, i) => {
      doc.text(`- ${s.ime}`, 10, 100 + pripravaLines.length * 10 + i * 10);
    });

    doc.save(`${recept.ime}.pdf`);
  };

  return (
    <button onClick={generirajPDF} className="izvoz-button">
      Izvozi PDF
    </button>
  );
}