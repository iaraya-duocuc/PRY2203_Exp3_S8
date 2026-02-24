package cl.speedfast.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO utilizado para mostrar entregas en la vista.
 * No representa una tabla, sino una proyección con JOIN.
 */
public class EntregaDTO {

    private int id;
    private int idPedido;
    private String tipoPedido;
    private String nombreRepartidor;
    private String direccionPedido;
    private LocalDate fecha;
    private LocalTime hora;

    public EntregaDTO() {
    }

    public EntregaDTO(int id,
                      int idPedido,
                      String tipoPedido,
                      String nombreRepartidor,
                      String direccionPedido,
                      LocalDate fecha,
                      LocalTime hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.tipoPedido = tipoPedido;
        this.nombreRepartidor = nombreRepartidor;
        this.direccionPedido = direccionPedido;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    public String getDireccionPedido() {
        return direccionPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getIdPedido() {
        return idPedido;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    public void setDireccionPedido(String direccionPedido) {
        this.direccionPedido = direccionPedido;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    @Override
    public String toString() {
        return "EntregaDTO{" +
                "id=" + id +
                ", tipoPedido='" + tipoPedido + '\'' +
                ", nombreRepartidor='" + nombreRepartidor + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                '}';
    }
}