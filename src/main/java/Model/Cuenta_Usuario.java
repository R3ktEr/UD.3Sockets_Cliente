package Model;

import java.io.Serializable;

public class Cuenta_Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected int id;
	protected Usuario usuario;
	protected Cuenta cuenta;
	
	public Cuenta_Usuario(int id, Usuario usuario, Cuenta cuenta) {
		this.id = id;
		this.usuario = usuario;
		this.cuenta = cuenta;
	}
	
	public Cuenta_Usuario() {
		this(-1,new Usuario(), new Cuenta());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuenta == null) ? 0 : cuenta.hashCode());
		result = prime * result + id;
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta_Usuario other = (Cuenta_Usuario) obj;
		if (cuenta == null) {
			if (other.cuenta != null)
				return false;
		} else if (!cuenta.equals(other.cuenta))
			return false;
		if (id != other.id)
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cuenta_Usuario [id=" + id + ", usuario=" + usuario.username + ", cuenta=" + cuenta.id + "]";
	}
	
	
	
	
}
