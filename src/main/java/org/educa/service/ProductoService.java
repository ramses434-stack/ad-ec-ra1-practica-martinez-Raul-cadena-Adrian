package org.educa.service;

import generated.Producto;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductoDao;
import org.educa.dao.ProductoDaoImpl;
import org.educa.dao.XmlDao;
import org.educa.dao.XmlDaoImpl;
import org.educa.entity.ProductoEntity;
import org.educa.entity.SummaryEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private final ProductoDao productoDao = new ProductoDaoImpl();
    private final XmlDao xmlDao = new XmlDaoImpl();

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        List<Producto> productos = productoDao.leerFichero(fileXml);
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
        File archivoXml = new File(fileXml);
        String nombreArchivo = archivoXml.getName();
        String rutaAbsoluta = archivoXml.getAbsolutePath();
        long tamanoBytes = archivoXml.length();


        String nombreSinExtension = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));
        String mesAnio = nombreArchivo.substring(nombreArchivo.indexOf('_') + 1, nombreArchivo.lastIndexOf('.'));


        List<ProductoEntity> productos = this.readFile(fileXml);
        int numeroDeProductos = productos.size();


        BigDecimal beneficioTotal = BigDecimal.ZERO;
        for (ProductoEntity producto : productos) {
            beneficioTotal = beneficioTotal.add(producto.getProfit());
        }

        SummaryEntity summary = new SummaryEntity();

        summary.setName(mesAnio);
        summary.setNumberOfProducts(numeroDeProductos);
        summary.setTotalProfit(beneficioTotal);
        summary.setFileAbsolutePath(rutaAbsoluta);
        summary.setFileName(nombreSinExtension);
        summary.setFileSize(tamanoBytes);

        xmlDao.exportarResumen(summary, path, mesAnio);

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        List<ProductoEntity> productos = readFile(fileXml);
        String nombreExcel = "export_" + obtenerMesAnio(fileXml) + ".xlsx";
        productoDao.guardarExcel(path + nombreExcel, productos);
    }

    private String obtenerMesAnio(String fileXml) {
        String nombre = new File(fileXml).getName();
        return nombre.substring(nombre.indexOf('_') + 1, nombre.lastIndexOf('.'));
    }


}
