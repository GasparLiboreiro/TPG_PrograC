package modelo.persistencia;

public interface DAOUsuarios {
    /**
     * Informa mediante un booleano si el nombre y contraseña son de un usuario registrado en la db
     * <b>Pre:</b> usuario no sebe ser nulo, sus atributos no deben ser nulos ni de logitud menor a 1 caracter</br>
     * @param usuario DTO con la informacion de inicio de sesion
     * @return false -> si la combinacion de nombre y contraseña es incorrecta</br>true -> si habia un usuario de nombre y contraseña ingresadas por el param usuario
     */
    public boolean login(DTOUsuario usuario);

    /**
     * Registra al operador con una nueva cuenta de nombre y contraseña ingresados
     * <b>Pre:</b> Parametros no deben ser nulos ni de longitud cero</br>
     * @param usuario DTO con la informacion de la cuenta a crear
     */
    /**
     * Informa mediante un booleano si se consiguió registrar el nuevo usuario
     * <b>Pre:</b> usuario no sebe ser nulo, sus atributos no deben ser nulos ni de logitud menor a 1 caracter</br>
     * @param usuario DTO con la informacion del nuevo usuario
     * @return false -> si el nombre ya esta en uso</br>true -> si se consiguio registrar al usuario satisfactoriamente
     */
    public boolean register(DTOUsuario usuario);
}
