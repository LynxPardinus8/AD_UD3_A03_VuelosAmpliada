package modelo;

public class Vuelo {

	private int id;
	private String codigo;
	private String origen;
	private String destino;
	private String fecha;
	private String hora;
	private int totales;
	private int disponibles;

	public Vuelo() {

	}

	public Vuelo(int id) {
		this.id = id;
	}

	public Vuelo(int id, String codigo, String origen, String destino, String fecha, String hora, int totales,
			int disponibles) {
		this.id = id;
		this.codigo = codigo;
		this.origen = origen;
		this.destino = destino;
		this.fecha = fecha;
		this.hora = hora;
		this.totales = totales;
		this.disponibles = disponibles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public int getTotales() {
		return totales;
	}

	public void setTotales(int totales) {
		this.totales = totales;
	}

	public int getDisponibles() {
		return disponibles;
	}

	public void setDisponibles(int disponibles) {
		this.disponibles = disponibles;
	}

}
