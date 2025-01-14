package com.connectionLayer.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
/**
 * trata conexiones con la base de datos externa local.
 * @author Lucas Alvarez Arguero
 *
 */
public class EConnection {
	private static Connection con = null;
/**
 * Establece (la primera vez que se llama) y devuelve una conexion con la base de datos externa que contiene los esquemas de los perifericos.
 * @return conexion sql
 */
	public static Connection getConnection() {

		try {
			if (con == null) {
				Runtime.getRuntime().addShutdownHook(new MiShDwnHookE());
				ResourceBundle rb = ResourceBundle.getBundle("ejdbc");
				String driver = rb.getString("driver");
				String url = rb.getString("url");

				String usr = rb.getString("usr");
				Class.forName(driver);

				con = DriverManager.getConnection(url + "user=" + usr);
			} else {
				if (!con.isValid(10)) {
					con = null;
					getConnection();
				}

			}
			return con;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error al acceder a la base de datos");
		}

	}
}

class MiShDwnHookE extends Thread {
	public void run() {
		try {
			Connection con = EConnection.getConnection();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

}