package ni.edu.uam.BFA.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 */

public class BFA {

	public static void main(String[] args) throws Exception {
		DBServer.start("BFA-db"); // Para usar tu propia base de datos comenta esta línea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("BFA"); // Usa AppServer.run("") para funcionar en el contexto raíz
	}

}
