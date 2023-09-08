package Casino;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Interfaz extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int apuesta = 0; // Valor de la apuesta
	private int deposito = 0; // Valor de la deposito
	private int ganancia = 0;
	private int monto1;
	private int monto2;
	private JTextField apuestaField = new JTextField(10);
	private Timer temporizador;
	JButton detener1 = new JButton("Detener");
	JButton detener2 = new JButton("Detener");
	JButton detener3 = new JButton("Detener");
	JButton iniciar = new JButton("Iniciar");
	JButton detenerTodo = new JButton("Detener Todo");
	public static String img1 = "src/Img/7.png";
	public static String img2 = "src/Img/piña.png";
	public static String img3 = "src/Img/sandia.png";

	static JLabel uno = new JLabel();
	public static JLabel dos = new JLabel();
	public static JLabel tres = new JLabel();
	JPanel controles = new JPanel();
	JPanel imagenes = new JPanel();
	Imagen1 hilo1 = new Imagen1();
	Imagen2 hilo2 = new Imagen2();
	Imagen3 hilo3 = new Imagen3();
	int z = 0;
	String mensajeGanador;
	String mensajePerdedor;

	private Properties configProperties;

	public Interfaz() {
		// Cargar el archivo de propiedades
		configProperties = new Properties();
		try (FileInputStream input = new FileInputStream("src/Casino/config.properties")) {
			configProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Obtener valores de configuración
		monto1 = Integer.parseInt(configProperties.getProperty("ganancia.monto1"));
		monto2 = Integer.parseInt(configProperties.getProperty("ganancia.monto2"));
		mensajeGanador = configProperties.getProperty("mensaje.ganador");
		mensajePerdedor = configProperties.getProperty("mensaje.perdedor");
		Object[] opciones = { "Aceptar", "Rechazar" };
		int respuesta = JOptionPane.showOptionDialog(Interfaz.this,
				configProperties.getProperty("mensaje.Políticasdeganancia"), "Título del Diálogo",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (respuesta == JOptionPane.NO_OPTION || respuesta == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}

		// Mostrar las instrucciones del juego
		JOptionPane.showMessageDialog(Interfaz.this, configProperties.getProperty("mensaje.instrucciones"));

		// Deposito
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Depositar y ver Deposito");

		JMenuItem depositarItem = new JMenuItem("Depositar");
		JMenuItem verDepositoItem = new JMenuItem("Ver Depósito");

		depositarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					int cantidadDeposito = Integer
							.parseInt(JOptionPane.showInputDialog("Ingresa la cantidad a depositar:"));
					if (cantidadDeposito > 0) {
						deposito += cantidadDeposito;
						JOptionPane.showMessageDialog(Interfaz.this,
								String.format(configProperties.getProperty("mensaje.depositoExitoso"), deposito));
					} else {
						JOptionPane.showMessageDialog(Interfaz.this,
								configProperties.getProperty("mensaje.depositoInvalido"));
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(Interfaz.this,
							configProperties.getProperty("mensaje.valorDepositoInvalido"));
				}
			}
		});

		verDepositoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JOptionPane.showMessageDialog(Interfaz.this, "Depósito actual: " + deposito);
			}
		});

		menu.add(depositarItem);
		menu.add(verDepositoItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);

		// registrar Apuesta:
		setLayout(new BorderLayout());
		imagenes.setLayout(new FlowLayout());
		controles.setLayout(new GridLayout(3, 2));
		JPanel apuestaPanel = new JPanel(new FlowLayout());
		apuestaPanel.add(new JLabel("Apuesta:"));
		apuestaPanel.add(apuestaField);
		add("North", apuestaPanel);
		JButton registrarApuesta = new JButton("Registrar Apuesta");
		registrarApuesta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if (apuestaField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(Interfaz.this, configProperties.getProperty("mensaje.apuestaVacia"));
					apuestaField.setText("");
					return;
				}

				int apuestaActual = Integer.parseInt(apuestaField.getText());
				if (apuestaActual <= 0) {
					JOptionPane.showMessageDialog(Interfaz.this,
							configProperties.getProperty("mensaje.apuestaInvalida"));
					apuestaField.setText("");
				} else if (apuestaActual > deposito) {
					JOptionPane.showMessageDialog(Interfaz.this,
							configProperties.getProperty("mensaje.apuestaExcedeDeposito"));

				} else {
					apuesta = apuestaActual;
					JOptionPane.showMessageDialog(Interfaz.this,
							String.format(configProperties.getProperty("mensaje.apuestaRegistrada"), apuesta));

					apuestaField.setText("");
				}
			}
		});

		// Controles del juego
		uno.setIcon(new ImageIcon(img1));
		dos.setIcon(new ImageIcon(img2));
		tres.setIcon(new ImageIcon(img3));
		imagenes.add(uno);
		imagenes.add(dos);
		imagenes.add(tres);
		controles.setLayout(new GridLayout(2, 3, 10, 10));
		controles.add(detener1);
		controles.add(detener2);
		controles.add(detener3);
		controles.add(iniciar);
		controles.add(detenerTodo);
		controles.add(registrarApuesta);
		add("Center", imagenes);
		add("South", controles);

		// Metodos para jugar
		 iniciar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                if (deposito <= 0) {
	                    JOptionPane.showMessageDialog(Interfaz.this, "Debes realizar un depósito antes de iniciar el juego.");
	                } else if (apuesta <= 0) {
	                    JOptionPane.showMessageDialog(Interfaz.this, "Debes establecer una apuesta válida antes de iniciar.");
	                } else {
	                    if (apuesta > 0 && hilo1.isAlive() == false && hilo2.isAlive() == false && hilo3.isAlive() == false) {
	                        if (apuesta <= deposito) {
	                            hilo1 = new Imagen1();
	                            hilo2 = new Imagen2();
	                            hilo3 = new Imagen3();
	                            hilo1.activo = true;
	                            hilo2.activo = true;
	                            hilo3.activo = true;
	                            hilo1.start();
	                            hilo2.start();
	                            hilo3.start();
	                            z = 1;

	                            int delay = 8000; // Milisegundos
	                            temporizador = new Timer();
	                            temporizador.schedule(new TimerTask() {
	                                @Override
	                                public void run() {
	                                    hilo1.activo = false;
	                                    hilo2.activo = false;
	                                    hilo3.activo = false;
	                                    int figura1 = hilo1.c;
	                                    int figura2 = hilo2.c;
	                                    int figura3 = hilo3.c;
	                                    String mensaje;
	                                    if (figura1 == figura2 && figura2 == figura3) {
	                                        ganancia = apuesta * monto1;
	                                        mensaje = String.format(mensajeGanador, apuesta, ganancia);
	                                        deposito += ganancia;
	                                    } else if (figura1 == figura2 || figura1 == figura3 || figura2 == figura3) {
	                                        ganancia = apuesta * monto2;
	                                        mensaje = String.format(mensajeGanador, apuesta, ganancia);
	                                        deposito += ganancia;
	                                    } else {
	                                        ganancia = 0;
	                                        mensaje = mensajePerdedor;
	                                        deposito -= apuesta;
	                                    }

	                                    JOptionPane.showMessageDialog(Interfaz.this, mensaje);
	                                }
	                            }, delay + new Random().nextInt(6000));
	                        } else {
	                            JOptionPane.showMessageDialog(Interfaz.this, "Debes establecer una apuesta válida antes de iniciar.");
	                        }
	                    }
	                }
	            }
	        });


		detenerTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (z == 1) {
					if (temporizador != null) {
						temporizador.cancel();
					}

					hilo1.activo = false;
					hilo2.activo = false;
					hilo3.activo = false;

					int figura1 = hilo1.c;
					int figura2 = hilo2.c;
					int figura3 = hilo3.c;
					String mensaje;
					if (figura1 == figura2 && figura2 == figura3) {
						ganancia = apuesta * monto1;
						mensaje = String.format(mensajeGanador, apuesta, ganancia);
						deposito += ganancia;
					} else if (figura1 == figura2 || figura1 == figura3 || figura2 == figura3) {

						ganancia = apuesta * monto2;
						mensaje = String.format(mensajeGanador, apuesta, ganancia);
						deposito += ganancia;
					} else {

						ganancia = 0;
						mensaje = mensajePerdedor;
						deposito -= apuesta;
					}

					JOptionPane.showMessageDialog(Interfaz.this, mensaje);
				}
			}
		});

		 detener1.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                hilo1.activo = false;
	            }
	        });
	        detener2.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                hilo2.activo = false;
	            }
	        });
	        detener3.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                hilo3.activo = false;
	            }
	        });
	    }
	
}
