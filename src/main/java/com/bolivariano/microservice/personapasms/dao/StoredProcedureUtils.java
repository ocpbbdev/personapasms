package com.bolivariano.microservice.personapasms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.bolivariano.microservice.personapasms.configuration.DataSourceManager;

@Component
public class StoredProcedureUtils {
	Logger logger = LoggerFactory.getLogger(StoredProcedureUtils.class);
	
	@Autowired
    private DataSourceManager dataSourceManager;
    /*
    public Map<?, ?> callStoredProcedure(String schemaName, String procedureName, Map<String, Object> parameters, SqlParameter[] sqlParameters){
    	DataSource storeDataSource = dataSourceManager.storeDataSource();
    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.storeSimpleJdbcCall(storeDataSource);
		simpleJdbcCall.withCatalogName(schemaName);
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withProcedureName(procedureName);
		simpleJdbcCall.declareParameters(sqlParameters);
		simpleJdbcCall.compile();
		MapSqlParameterSource inParams = new MapSqlParameterSource();
        if(null!=parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        logger.debug("PROCEDURE {} IS CALLED",procedureName);
        return simpleJdbcCall.execute(inParams);
        
    }
    
    public Map<?, ?> callStoredFunction(String schemaName, String functionName, Map<String, Object> parameters){
    	DataSource storeDataSource = dataSourceManager.storeDataSource();
    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.storeSimpleJdbcCall(storeDataSource);
		simpleJdbcCall.withCatalogName(schemaName);
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withFunctionName(functionName);
		simpleJdbcCall.withReturnValue();
		simpleJdbcCall.compile();
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        if(null!=parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        logger.debug("FUNCTION {} IS CALLED",functionName);
        
        return simpleJdbcCall.execute(inParams);
        
    }
    */
    public Map<?, ?> callStoredProcedureSybase(String schemaName, String procedureName, Map<String, Object> parameters, SqlParameter[] sqlParameters){
    	DataSource sybaseDataSource = dataSourceManager.sybaseDataSource();
    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.sybaseSimpleJdbcCall(sybaseDataSource);
		simpleJdbcCall.withCatalogName(schemaName);
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withProcedureName(procedureName);
		simpleJdbcCall.declareParameters(sqlParameters);
		// Para que springboot no saque la metadata 
		// withoutProcedureColumnMetaDataAccess();
		simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
		simpleJdbcCall.compile();
		MapSqlParameterSource inParams = new MapSqlParameterSource();
        if(null!=parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        logger.debug("PROCEDURE {} IS CALLED",procedureName);
        return simpleJdbcCall.execute(inParams);
        
    }
    
    public Map<?, ?> callStoredFunctionSybase(String schemaName, String functionName, Map<String, Object> parameters){
    	DataSource sybaseDataSource = dataSourceManager.sybaseDataSource();
    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.sybaseSimpleJdbcCall(sybaseDataSource);
		simpleJdbcCall.withCatalogName(schemaName);
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withFunctionName(functionName);
		simpleJdbcCall.withReturnValue();
		simpleJdbcCall.compile();
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        if(null!=parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        logger.debug("FUNCTION {} IS CALLED",functionName);
        return simpleJdbcCall.execute(inParams);
    }
    
    public String[] getParamaters(Map<String, Object> parameters){
    	List<String> list = new ArrayList<>();
    	for(String key: parameters.keySet()) {
    		list.add(key);
    	}
    	String array[] = new String[parameters.size()];
    	array = list.toArray(array);
    	return array;
    }
    /*
    public Map<?, ?> callStoredProcedureDWH(String schemaName, String procedureName, Map<String, Object> parameters, SqlParameter[] sqlParameters){
    	DataSource dwhDataSource = dataSourceManager.dwhDataSource();
    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.dwhSimpleJdbcCall(dwhDataSource);
		simpleJdbcCall.withCatalogName(schemaName);
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withProcedureName(procedureName);
		simpleJdbcCall.declareParameters(sqlParameters);
		simpleJdbcCall.compile();
		MapSqlParameterSource inParams = new MapSqlParameterSource();
        if(null!=parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        logger.debug("PROCEDURE {} IS CALLED",procedureName);
        return simpleJdbcCall.execute(inParams);
        
    }
    */
    /*
    public Map<?, ?> callStoredFunctionDWH(String schemaName, String functionName, Map<String, Object> parameters){
    	DataSource dwhDataSource = dataSourceManager.dwhDataSource();
    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.dwhSimpleJdbcCall(dwhDataSource);
		simpleJdbcCall.withCatalogName(schemaName);
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withFunctionName(functionName);
		simpleJdbcCall.withReturnValue();
		simpleJdbcCall.compile();
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        if(null!=parameters) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                inParams.addValue(parameter.getKey(), parameter.getValue());
            }
        }
        logger.debug("FUNCTION {} IS CALLED",functionName);
        
        return simpleJdbcCall.execute(inParams);
        
    }*/
    
}
