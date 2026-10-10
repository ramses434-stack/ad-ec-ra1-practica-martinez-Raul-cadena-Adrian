package org.educa.dao;

import org.educa.entity.SummaryEntity;

import java.io.FileWriter;
import java.io.IOException;

public class XmlDaoImpl implements XmlDao {
    @Override
    public void exportarResumen(SummaryEntity summary, String path, String mesAnio) throws IOException {

        //Construir la ruta
        String rutaDestino = path + "result_" + mesAnio + ".txt";

        //Escribir la información en el fichero
        try (FileWriter writer = new FileWriter(rutaDestino)) {
            writer.write(summary.toPrint());
        }
    }
}
