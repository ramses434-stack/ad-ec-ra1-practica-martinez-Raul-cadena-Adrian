package org.educa.dao;

import generated.Producto;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.util.List;

public interface ProductoDao {
    List<Producto> leerFichero(String ficheroxml) throws JAXBException;

    void guardarExcel(String rutaFichero, List<ProductoEntity> productosEntities) throws IOException;
}
