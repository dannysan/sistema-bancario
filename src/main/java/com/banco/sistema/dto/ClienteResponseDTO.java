package com.banco.sistema.dto;

public class ClienteResponseDTO {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;

    public ClienteResponseDTO(Long clienteId, String nombre, String identificacion, Boolean estado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
    }

    public Long getClienteId() { return clienteId; }
    public String getNombre() { return nombre; }
    public String getIdentificacion() { return identificacion; }
    public Boolean getEstado() { return estado; }
}