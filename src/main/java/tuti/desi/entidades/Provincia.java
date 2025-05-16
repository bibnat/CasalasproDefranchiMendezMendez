package tuti.desi.entidades;

import java.util.List;

public class Provincia {
	
	private Long id;
	
	
	private String nombre;
	
	private List<Ciudad> ciudades;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
