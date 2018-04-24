package com.cnpc.pms.base.dynamicds;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;

public class DynamicDataSource extends AbstractRoutingDataSource {

	public static final String DEFAULT_DATA_SOURCE = DataSourceConfigurer.DEFAULT_DATA_SOURCE;// "Main";

	@Override
	protected Object determineCurrentLookupKey() {
		return CurrentDSContext.getCurrent();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}



}
