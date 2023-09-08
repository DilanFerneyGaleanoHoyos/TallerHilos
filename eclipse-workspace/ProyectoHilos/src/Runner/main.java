package Runner;

import Casino.Interfaz;

public class main {

	public static void main(String[] args) {
		Interfaz ventana = new Interfaz();
		ventana.setSize(559, 280);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setTitle("Casino (Hilos)");
		ventana.setVisible(true);
	}

}
