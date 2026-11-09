package modelo.nave;

public interface Batalla {
	boolean gana(Nave obj);
	boolean ganaExploradora();
	boolean ganaCarguera();
	boolean ganaCombate();
}
