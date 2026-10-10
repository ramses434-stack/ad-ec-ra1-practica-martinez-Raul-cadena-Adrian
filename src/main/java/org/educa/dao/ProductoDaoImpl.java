package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {
    private static final String Formato_Euro = "#,##0.00\" €\"";
    private static final String Formato_Porcentaje = "0.00\"%\"";

    @Override
    public List<Producto> leerFichero(String ficheroxml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Productos productos = (Productos) unmarshaller.unmarshal(new File(ficheroxml));
        return productos.getProducto();
    }

    @Override
    public void guardarExcel(String rutaFichero, List<ProductoEntity> productosEntities) throws IOException {
        try (Workbook libro = new XSSFWorkbook();
             FileOutputStream salida = new FileOutputStream(rutaFichero)) {
            Sheet hoja = libro.createSheet("inventario");
            //formato de cabecera
            Font negrita = libro.createFont();
            negrita.setBold(true);
            CellStyle estiloCabecera = crearEstilo(libro, IndexedColors.SEA_GREEN,
                    null, HorizontalAlignment.LEFT);
            estiloCabecera.setFont(negrita);

            //formato de datos de las celdas
            CellStyle[] estiloTexto = {
                    crearEstilo(libro, IndexedColors.LIGHT_GREEN, null,
                            HorizontalAlignment.LEFT),
                    crearEstilo(libro, IndexedColors.WHITE, null,
                            HorizontalAlignment.LEFT)};
            CellStyle[] estiloEuro = {
                    crearEstilo(libro, IndexedColors.LIGHT_GREEN, Formato_Euro,
                            HorizontalAlignment.RIGHT),
                    crearEstilo(libro, IndexedColors.WHITE, Formato_Euro,
                            HorizontalAlignment.RIGHT)};
            CellStyle[] estiloPorcentaje = {
                    crearEstilo(libro, IndexedColors.LIGHT_GREEN, Formato_Porcentaje,
                            HorizontalAlignment.RIGHT),
                    crearEstilo(libro, IndexedColors.WHITE, Formato_Porcentaje,
                            HorizontalAlignment.RIGHT)};


            //Cabeceras
            Row filaCabecera = hoja.createRow(0);
            String[] cabeceras = {"Codigo", "Número de Serie", "Precio",
                    "Descuento", "Precio Final",
                    "Costes Envío", "Costes Almacenaje", "Beneficio"};
            for (int i = 0; i < cabeceras.length; i++) {
                escribirCelda(filaCabecera, i, cabeceras[i], estiloCabecera);
            }
            //datos
            for (int i = 0; i < productosEntities.size(); i++) {
                ProductoEntity productoEntity = productosEntities.get(i);
                Producto producto = productoEntity.getProducto();
                Row filaProducto = hoja.createRow(i + 1);
                int par = (i % 2);

                escribirCelda(filaProducto, 0, producto.getCodigo(),
                        estiloTexto[par]);
                escribirCelda(filaProducto, 1, producto.getNumeroSerie(),
                        estiloTexto[par]);
                escribirCelda(filaProducto, 2, producto.getPrecio().doubleValue(),
                        estiloEuro[par]);
                escribirCelda(filaProducto, 3, producto.getDescuento().doubleValue(),
                        estiloPorcentaje[par]);
                escribirCelda(filaProducto, 4, productoEntity.getPrecioFinal().doubleValue(),
                        estiloEuro[par]);
                escribirCelda(filaProducto, 5, producto.getCostes().getCostesEnvio().doubleValue(),
                        estiloEuro[par]);
                escribirCelda(filaProducto, 6, producto.getCostes().getCostesAlmacenaje().doubleValue(),
                        estiloEuro[par]);
                escribirCelda(filaProducto, 7, productoEntity.getProfit().doubleValue(),
                        estiloEuro[par]);
            }
            libro.write(salida);
            for (int i = 0; i < cabeceras.length; i++) {
                hoja.autoSizeColumn(i);
            }
        }
    }

    /**
     *
     * @param libro
     * @param color
     * @param formato
     * @param alineacion
     * @return
     */

    private CellStyle crearEstilo(Workbook libro, IndexedColors color, String formato, HorizontalAlignment alineacion) {
        CellStyle estilo = libro.createCellStyle();
        estilo.setFillForegroundColor(color.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(alineacion);
        if (formato != null) {
            estilo.setDataFormat(libro.createDataFormat().getFormat(formato));
        }
        return estilo;
    }

    private void escribirCelda(Row fila, int columna, double valor, CellStyle style) {
        Cell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(style);
    }

    private void escribirCelda(Row fila, int columna, String valor, CellStyle style) {
        Cell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(style);
    }
}
