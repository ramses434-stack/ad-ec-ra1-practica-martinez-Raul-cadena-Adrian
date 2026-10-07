package org.educa.dao;

import generated.Producto;
import jakarta.xml.bind.JAXBException;

import java.util.List;

public interface FicheroDao {
    List<Producto> leerFichero(String ficheroxml) throws JAXBException;
}
