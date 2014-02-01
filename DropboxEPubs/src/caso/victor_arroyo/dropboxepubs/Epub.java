package caso.victor_arroyo.dropboxepubs;

import java.sql.Date;

public class Epub {
	
	private String nombre;
	private Date fecha;
	private String nombreArchivo;
	private String dbPath;
	private String cachePath;
	
	public Epub(String nombre, Date fecha, String nombreArchivo, String dbp, String cachep) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
		this.nombreArchivo=nombreArchivo;
		this.dbPath=dbp;
		this.cachePath=cachep;
	}
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public String getCachePath() {
		return cachePath;
	}

	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
