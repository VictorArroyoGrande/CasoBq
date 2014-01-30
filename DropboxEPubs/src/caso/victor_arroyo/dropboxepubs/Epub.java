package caso.victor_arroyo.dropboxepubs;

public class Epub {
	
	private String nombre;
	private String fecha;
	private String nombreArchivo;
	private String dbPath;
	private String cachePath;
	
	public Epub(String nombre, String fecha, String nombreArchivo, String dbp, String cachep) {
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
