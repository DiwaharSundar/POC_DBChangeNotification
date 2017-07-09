package com.dcn.services;

import java.sql.ResultSet;
import static com.dcn.constants.ApplicationConstants.DCN_QUERY;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.driver.OracleConnection;

import com.dcn.factory.DataSourceFactory;

public class DatabaseRegistration {

	static OracleConnection oracleConnection = null;
	static DatabaseChangeRegistration dcr = null;
	boolean isRegisterSuccessful = false;

	public boolean registerDatabaseConnection() {

		try {

			isRegisterSuccessful = run();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isRegisterSuccessful;
	}

	public boolean run() throws SQLException {
		OracleConnection conn = DataSourceFactory.connect();

		System.out.println("Connection Established " + conn);

		// first step: create a registration on the server:
		Properties prop = new Properties();

		// Ask the server to send the ROWIDs as part of the DCN events (small
		// performance
		// cost):
		prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
		prop.setProperty(OracleConnection.DCN_IGNORE_INSERTOP, "false");
		prop.setProperty(OracleConnection.DCN_IGNORE_UPDATEOP, "false");
		prop.setProperty(OracleConnection.DCN_IGNORE_DELETEOP, "true");
		
		
		// Set the DCN_QUERY_CHANGE_NOTIFICATION option for query registration
		// with finer granularity.
		prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");

		// The following operation does a roundtrip to the database to create a
		// new
		// registration for DCN. It sends the client address (ip address and
		// port) that
		// the server will use to connect to the client and send the
		// notification
		// when necessary. Note that for now the registration is empty (we
		// haven't registered
		// any table). This also opens a new thread in the drivers. This thread
		// will be
		// dedicated to DCN (accept connection to the server and dispatch the
		// events to
		// the listeners).
		dcr = conn
				.registerDatabaseChangeNotification(prop);

		try {
			// add the listenerr:
			DCNListener dcnListener = new DCNListener(this);
			dcr.addListener(dcnListener);
			System.out.println("Registered DCR ID --> ["+dcr.getRegistrationId()+"]");

			// second step: add objects in the registration:
			Statement stmt = conn.createStatement();
			// associate the statement with the registration:

			((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
			ResultSet rs = stmt
					.executeQuery(DCN_QUERY);

			while (rs.next()) {
			}

			String[] tableNames = dcr.getTables();

			for (int i = 0; i < tableNames.length; i++)
				System.out.println(tableNames[i]
						+ " is part of the registration.");

			rs.close();
			stmt.close();

			isRegisterSuccessful = true;

		} catch (SQLException ex) {
			// if an exception occurs, we need to close the registration in
			// order
			// to interrupt the thread otherwise it will be hanging around.
			unregisterDatabaseConnection();
			throw ex;
		} finally {
			try {
				// Note that we close the connection!
				conn.close();
			} catch (Exception innerex) {
				innerex.printStackTrace();
			}

		}

		return isRegisterSuccessful;

	}

	
	public boolean unregisterDatabaseConnection() throws SQLException {

		OracleConnection conn = DataSourceFactory.connect();
		boolean isDBUnRegistered = false;

		if (dcr != null) {
			try {
				if (conn != null)
					conn.unregisterDatabaseChangeNotification(dcr);
				System.out.println("Unregistered DCR ID --> ["+dcr.getRegistrationId()+"] Successfully !!!" );
				isDBUnRegistered = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isDBUnRegistered;
	}

}