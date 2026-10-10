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

    /**
     * Lee los productos de un fichero XML y calcula sus datos economicos.
     *  El precio final se calcula precio * (100 - decuento)/100.
     *  El coste se calula sumando los costes de envío más coste de almacenaje.
     *  Bewneficio se calcula precio final - costes.
     * @param fileXml ruta del fichero Xml con el inventario
     * @return lista de productos con el precio final, costes y beneficios.
     * @throws JAXBException si el XML no se puede leer o
     * no cumple el esquema lanza una excepcion
     */
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

    /**
     *Crea un fichero de texto donde resume el inventario.
     * El resumen incleye mes, año, número de productos,
     * beneficio total y datos del XML(ruta, nombre y extension y tamaño en bytes)
     * @param path ruta a la carpeta donde se guarda.
     * @param fileXml ruta al ficheroXML con el inventario
     * @throws JAXBException si el XML no se lee o no cumple el esquema.
     * @throws IOException si falla la escritura.
     */
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

    /** Crea un fichero excel con los datos del inventario.
     * El fichero se llama export_<mes y año>.xlsx, donde el mes y año se
     * obtienen del nombre del XML llamando al metodo obtenerMesAnio.
     * Los datos se leen con {@link #readFile(String)}.
     * @param path Carpeta donde se guarda el Excel.
     * @param fileXml ruta donde se encuentra el Fichero XML.
     * @throws JAXBException si el XML no se puede leer o no cumple el esquema
     * @throws IOException si falla la escritura del fichero Excel
     */
    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        List<ProductoEntity> productos = readFile(fileXml);
        String nombreExcel = "export_" + obtenerMesAnio(fileXml) + ".xlsx";
        productoDao.guardarExcel(path + nombreExcel, productos);
    }

    /**
     * Extrae el mes y el año del nombre dle Fichero XML
     * @param fileXml ruta del Fichero Xml.
     * @return el mes y el año tal y como aparace en el nombre del fichero.
     */
    private String obtenerMesAnio(String fileXml) {
        String nombre = new File(fileXml).getName();
        return nombre.substring(nombre.indexOf('_') + 1, nombre.lastIndexOf('.'));
    }


}
