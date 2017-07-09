package com.dcn.services;	

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.dcn.constants.ApplicationConstants.QUERY;

import com.dcn.factory.DataSourceFactory;

import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.RowChangeDescription;

/**
 * @author Diwahar Sundar
 * 
 *
 */
public class DCNListener implements DatabaseChangeListener {

	DatabaseRegistration dr = new DatabaseRegistration();
	static Connection sqlConnection = null;

	public DCNListener(DatabaseRegistration dr) {
		super();
		this.dr = dr;
	}

	@Override
	public void onDatabaseChangeNotification(DatabaseChangeEvent dcn) {
		// TODO Auto-generated method stub

		RowChangeDescription[] rowsChanged = null;

		System.err.println("Event Received From DB !!!");


		try{

			for(int i=0; i <dcn.getQueryChangeDescription().length ; i++){
				for(int j=0; j<dcn.getQueryChangeDescription()[i].getTableChangeDescription().length ; j++){
					rowsChanged = dcn.getQueryChangeDescription()[i].getTableChangeDescription()[j].getRowChangeDescription();
				}
			}

			getDataFromDB(rowsChanged);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}


	private void getDataFromDB(RowChangeDescription[]  rowsChanged) throws ClassNotFoundException, SQLException{
		StringBuilder queryBuilder = null;

		sqlConnection = DataSourceFactory.getSqlConnection();


		for(int i=0; i< rowsChanged.length ; i++ ){
			System.out.println("Rows Chnaged ---->  ["+rowsChanged[i].getRowid().stringValue()+"]");

			String query = QUERY +rowsChanged[i].getRowid().stringValue()+"\'";	  

			PreparedStatement pStmt = (PreparedStatement) sqlConnection.prepareCall(query);
			ResultSet rs = pStmt.executeQuery(query);

			while(rs.next()){
				System.out.println(rs.getInt(1) +"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getDate(4));
			}

		}


	}


}
