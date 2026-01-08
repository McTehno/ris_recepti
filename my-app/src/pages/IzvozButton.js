import React from "react";
import { jsPDF } from "jspdf";

export default function IzvozButton({ recept, sestavine, st_porcij, hranilneVrednosti}) {
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
    doc.text(`Stevilo porcij: ${st_porcij || recept.st_porcij || "1"}`, 10, 55);
    
    doc.setFontSize(14);
    doc.setTextColor("#ffb347");
    doc.text("Priprava:", 10, 65);

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
      doc.text(`- ${s.ime}: ${s.kolicina} ${s.enota}`, 10, 100 + pripravaLines.length * 10 + i * 10);
    });

    doc.setFontSize(14);
    doc.setTextColor("#ffb347");
    doc.text(`Hranilne vrednosti (stPorcij: ${st_porcij}):`, 10, 150);

    doc.setFontSize(12);
    doc.setTextColor("#000000");
    doc.text(`- Energijska vrednost: ${hranilneVrednosti.energija} (kcal)`, 10, 155);
    doc.text(`- Beljakovine: ${hranilneVrednosti.bjelankovine} (g)`, 10, 160);
    doc.text(`- Ogljikovi hidrati: ${hranilneVrednosti.ogljikoviHidrati} (g)`, 10, 165);
    doc.text(`- Mascobe: ${hranilneVrednosti.mascobe} (g)`, 10, 170);
    
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