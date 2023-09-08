package Casino;

import java.util.Random;

import javax.swing.ImageIcon;

public class Imagen3 extends Thread {
	boolean activo = true;
	Random img = new Random();
	Random tiem = new Random();
	int i = 0;
	int tiempo = 0;
	int c = 0;
	
	 private int velocidad = 100; // Inicialmente, gira a una velocidad de 200 milisegundos

	    public void setVelocidad(int velocidad) {
	        this.velocidad = velocidad;
	    }

	    public int getVelocidad() {
	        return velocidad;
	    }
	@Override
	public void run() {
		while (activo == true) {
			i = img.nextInt(4);
			tiempo = tiem.nextInt(100);
			if (i == 1) {
				Interfaz.tres.setIcon(new ImageIcon(Interfaz.img1));
				c = 1;
			}
			if (i == 2) {
				Interfaz.tres.setIcon(new ImageIcon(Interfaz.img2));
				c = 2;
			}
			if (i == 3) {
				Interfaz.tres.setIcon(new ImageIcon(Interfaz.img3));
				c = 3;
			}
			try {
				sleep(tiempo);
			} catch (InterruptedException e) {
			}
		}
	}
}
