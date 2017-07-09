package com.dcn.main;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dcn.services.DCNListener;
import com.dcn.services.DatabaseRegistration;

public class NotifyDBChange implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseRegistration dbRegistrar = new DatabaseRegistration();
		try {
			boolean unregisterFlag = dbRegistrar.unregisterDatabaseConnection();
			
			if(!unregisterFlag)
				System.err.println("Failed - DB UnRegistration");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseRegistration dbRegistrar = new DatabaseRegistration();

		if (dbRegistrar.registerDatabaseConnection())
			System.out.println("Registartion Success ");
		
	}

}
