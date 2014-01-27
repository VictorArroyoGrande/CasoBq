package caso.victor_arroyo.dropboxepubs;

public class Libro {
	
	private String nombre;
	private String fecha;
	
	public Libro(String nombre, String fecha) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
