package org.educa.service;

import generated.Producto;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.FicheroDao;
import org.educa.dao.FicheroDaoImpl;
import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private static final FicheroDao ficheroDao = new FicheroDaoImpl();

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        List<Producto> productos = ficheroDao.leerFichero(fileXml);
        List<ProductoEntity> productoEntities = new ArrayList<>();
        BigDecimal cien = new BigDecimal("100");

        for (Producto p : productos) {

            ProductoEntity productoEntity = new ProductoEntity();
            productoEntity.setProducto(p);
            BigDecimal porcentaje = cien.subtract(p.getDescuento());
            BigDecimal precioFinal = p.getPrecio().multiply(porcentaje).
                    divide(cien, 2, RoundingMode.HALF_UP);
            BigDecimal coste = p.getCostes().getCostesEnvio().
                    add(p.getCostes().getCostesAlmacenaje());
            BigDecimal beneficio = precioFinal.subtract(coste);

            productoEntity.setPrecioFinal(precioFinal);
            productoEntity.setCost(coste);
            productoEntity.setProfit(beneficio);

            productoEntities.add(productoEntity);
        }

        return productoEntities;
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        //TODO: Implementar

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }


}
