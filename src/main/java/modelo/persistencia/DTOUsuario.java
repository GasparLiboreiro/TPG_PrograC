package modelo.persistencia;

public class DTOUsuario {
    private String nombre;
    private String contrasenia;

    public DTOUsuario(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }
    public DTOUsuario() {
        this.nombre = null;
        this.contrasenia = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String toString(){
        String ret = "nombre:\""+nombre+"\"";
        if(contrasenia!=null)
            ret+=" contrasenia:\n"+contrasenia+"\"";
        return ret;
    }
}
