package org.educa.entity;

import generated.Producto;

import java.math.BigDecimal;

public class ProductoEntity {
    private Producto producto;

    private String codigo;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private String categoria;
    private Integer anioLanzamiento;
    private Integer garantiaMeses;
    private String tipoConexion;

    // Elementos proveedor
    private String empresaProveedor;
    private String ciudadProveedor;
    private String paisProveedor;
    private String codigoPostalProveedor;

    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal costesEnvio;
    private BigDecimal costesAlmacenaje;
    private BigDecimal precioFinal;
    private BigDecimal cost;
    private BigDecimal profit;

    public BigDecimal getCostesAlmacenaje() {
        return costesAlmacenaje;
    }

    public void setCostesAlmacenaje(BigDecimal costesAlmacenaje) {
        this.costesAlmacenaje = costesAlmacenaje;
    }

    public BigDecimal getCostesEnvio() {
        return costesEnvio;
    }

    public void setCostesEnvio(BigDecimal costesEnvio) {
        this.costesEnvio = costesEnvio;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCodigoPostalProveedor() {
        return codigoPostalProveedor;
    }

    public void setCodigoPostalProveedor(String codigoPostalProveedor) {
        this.codigoPostalProveedor = codigoPostalProveedor;
    }

    public String getPaisProveedor() {
        return paisProveedor;
    }

    public void setPaisProveedor(String paisProveedor) {
        this.paisProveedor = paisProveedor;
    }

    public String getCiudadProveedor() {
        return ciudadProveedor;
    }

    public void setCiudadProveedor(String ciudadProveedor) {
        this.ciudadProveedor = ciudadProveedor;
    }

    public String getEmpresaProveedor() {
        return empresaProveedor;
    }

    public void setEmpresaProveedor(String empresaProveedor) {
        this.empresaProveedor = empresaProveedor;
    }

    public String getTipoConexion() {
        return tipoConexion;
    }

    public void setTipoConexion(String tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public Integer getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    public Integer getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(Integer anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String toPrint() {
        StringBuilder sb = new StringBuilder();

        // Construir provedor
        String proveedor = "";
        if (empresaProveedor != null) proveedor += empresaProveedor;
        if (ciudadProveedor != null) proveedor += (proveedor.isEmpty() ? "" : " + ") + ciudadProveedor;
        if (paisProveedor != null) proveedor += (proveedor.isEmpty() ? "" : " + ") + paisProveedor;
        if (codigoPostalProveedor != null) proveedor += (proveedor.isEmpty() ? "" : " + ") + codigoPostalProveedor;

        sb.append("• Codigo: ").append(codigo != null ? codigo : "").append("\n");
        sb.append("• Número de Serie: ").append(numeroSerie != null ? numeroSerie : "").append("\n");
        sb.append("• Marca: ").append(marca != null ? marca : "").append("\n");
        sb.append("• Modelo: ").append(modelo != null ? modelo : "").append("\n");
        sb.append("• Categoria: ").append(categoria != null ? categoria : "").append("\n");
        sb.append("• Anio Lanzamiento: ").append(anioLanzamiento != null ? anioLanzamiento : "").append("\n");
        sb.append("• Garantia (Meses): ").append(garantiaMeses != null ? garantiaMeses : "").append("\n");
        sb.append("• Proveedor: ").append(proveedor).append("\n");
        sb.append("• Tipo Conexion: ").append(tipoConexion != null ? tipoConexion : "").append("\n");

        // Variables con decimales
        sb.append("• Precio: ").append(precio != null ? precio.toString() : "0.00").append("\n");
        sb.append("• Descuento: ").append(descuento != null ? descuento.toString() : "0.00").append("%\n");
        sb.append("• Precio final: ").append(precioFinal != null ? String.format("%.2f", precioFinal.doubleValue()) : "0,00").append("\n");
        sb.append("• Coste: ").append(cost != null ? String.format("%.2f", cost.doubleValue()) : "0,00").append("\n");
        sb.append("• Beneficio: ").append(profit != null ? String.format("%.2f", profit.doubleValue()) : "0,00").append("\n");
        sb.append("--------------------------------------------------");

        return sb.toString();
    }
}
