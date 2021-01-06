package com.bolivariano.microservice.personapasms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class GenericRowMapper implements RowMapper<Map<String, Object>>{

	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		return createRow(rs);
	}
	
	private Map<String, Object> createRow(ResultSet rs)  throws SQLException {
		Map<String, Object> row = new HashMap<String, Object>();
		for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			String columnName = rs.getMetaData().getColumnName(i);
			if(columnName == null || columnName.isEmpty()) {
				columnName = String.valueOf(i);
			}
			int columnType = rs.getMetaData().getColumnType(i);
			switch(columnType) {
				case Types.CHAR:
				case Types.VARCHAR:
					row.put(columnName, rs.getString(i));
					break;
				case Types.INTEGER:
				case Types.SMALLINT:
				case Types.TINYINT:
					row.put(columnName, rs.getInt(i));
					break;
				case Types.DATE:
					row.put(columnName, rs.getDate(i));
					break;
				case Types.TIMESTAMP:
					row.put(columnName, rs.getTimestamp(i));
					break;
				case Types.DOUBLE:
					row.put(columnName, rs.getDouble(i));
				case Types.NUMERIC:
					row.put(columnName, rs.getBigDecimal(i));
					break;
			}
		}
		return row;
	}

}
