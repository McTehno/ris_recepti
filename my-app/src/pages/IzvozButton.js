import React from "react";
import { jsPDF } from "jspdf";

export default function IzvozButton({ recept, sestavine }) {
  const generirajPDF = () => {
    const doc = new jsPDF();

    doc.setFillColor(255, 239, 225); 
    doc.rect(0, 0, 210, 297, "F");   


    doc.setFontSize(22);
    doc.setTextColor("#ff7043");
    doc.text(recept.ime, 10, 20);

 
    doc.setFontSize(12);
    doc.setTextColor("#000000");
    doc.text(`Tip: ${recept.tip}`, 10, 35);
    doc.text(`Ocena: ${recept.povprecnaOcena || "Brez ocen"}`, 10, 45);

    
    doc.setFontSize(14);
    doc.setTextColor("#ffb347");
    doc.text("Priprava:", 10, 60);

    doc.setFontSize(12);
    doc.setTextColor("#000000");
    const pripravaLines = doc.splitTextToSize(recept.priprava, 180);
    doc.text(pripravaLines, 10, 70);

    
    doc.setFontSize(14);
    doc.setTextColor("#ffb347");
    doc.text("Sestavine:", 10, 90 + pripravaLines.length * 10);

    
    doc.setFontSize(12);
    doc.setTextColor("#000000");
    sestavine.forEach((s, i) => {
      doc.text(`- ${s.ime}`, 10, 100 + pripravaLines.length * 10 + i * 10);
    });
    // Background 
    doc.setFillColor(255, 239, 225); 

    doc.save(`${recept.ime}.pdf`);
  };

  return (
    <button onClick={generirajPDF} className="izvoz-button">
      Izvozi PDF
    </button>
  );
}