package com.bolivariano.microservice.personapasms.dao;


import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.stereotype.Service;

import com.bolivariano.microservice.personapasms.bean.CatalogoBean;
import com.bolivariano.microservice.personapasms.bean.DatosCliente;
import com.bolivariano.microservice.personapasms.configuration.DataSourceManager;
import com.bolivariano.microservice.personapasms.dao.GenericRowMapper;
import com.bolivariano.microservice.personapasms.exception.CustomException;
import com.bolivariano.microservice.personapasms.service.PersonapasService;
import com.bolivariano.microservice.personapasms.utils.Util;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaEncerarDatosClientesMis;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaObtenerInformacionCliente;
import com.bolivariano.utilitario.common.Utils;
import com.google.common.base.Strings;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Service
public class PersonapasDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(PersonapasDAO.class);
	
	@Autowired
	StoredProcedureUtils storedProcedureUtils;
	@Autowired
    private DataSourceManager dataSourceManager;
	@Autowired
	private Util util;
	
	public  Map<?, ?> obtenerErrorVariablesSalida(String nombreObjeto, int numError, String mensajeError){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	
 		inParams.put("t_from", nombreObjeto);
		inParams.put("i_num", numError);
		inParams.put("i_msg", mensajeError);
		inParams.put("o_error", 0);
		inParams.put("o_msg_err", "");
		SqlParameter[] sqlParameters = {new SqlParameter("t_from", Types.VARCHAR),
										new SqlParameter("i_num", Types.INTEGER),
										new SqlParameter("i_msg", Types.VARCHAR),
										new SqlOutParameter("o_error", Types.INTEGER),
										new SqlOutParameter("o_msg_err", Types.VARCHAR)};
		return storedProcedureUtils.callStoredProcedureSybase("cob_cartera", "pa_cca_tparam_error", inParams, sqlParameters);
	}
	
	public  Map<?, ?> obtenerCatalogos(String nombreTabla){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	
 		inParams.put("e_nombre_tabla", nombreTabla);
		inParams.put("e_tipo", 'C');
		
		SqlParameter[] sqlParameters = {
				new SqlParameter("e_nombre_tabla", Types.VARCHAR),
				new SqlParameter("e_tipo", Types.VARCHAR),
				 new SqlReturnResultSet("result", new GenericRowMapper())
				 };
		return storedProcedureUtils.callStoredProcedureSybase("cobis", "pa_mis_cons_catalogo", inParams, sqlParameters);
	}
	
	
	public  Map<?, ?> obtenerParametrosGeneralesResultSet(){
		/*HashMap<String, Object> inParams = new HashMap<String, Object>();	
 		inParams.put("t_from", nombreObjeto);
		inParams.put("i_num", numError);
		inParams.put("i_msg", mensajeError);
		inParams.put("o_error", 0);
		inParams.put("o_msg_err", "");
		*/
		SqlParameter[] sqlParameters = {
				 new SqlReturnResultSet("result", new GenericRowMapper())};
		return storedProcedureUtils.callStoredProcedureSybase("cob_cartera", "pa_cca_tparam_error", null, sqlParameters);
	}
	
	public List<CatalogoBean> obtenerCatalogosNew(String nombreTabla) throws CustomException, Exception {
		List<CatalogoBean> lista = null;
		try {
			//SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			HashMap<String, Object> inParams = new HashMap<String, Object>();	
	 		inParams.put("e_nombre_tabla", nombreTabla);
			inParams.put("e_tipo", 'C');
			SqlParameter[] sqlParameters = {
					new SqlParameter("e_nombre_tabla", Types.VARCHAR),
					new SqlParameter("e_tipo", Types.VARCHAR)};
			DataSource sybaseDataSource = dataSourceManager.sybaseDataSource();
	    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.sybaseSimpleJdbcCall(sybaseDataSource);
			simpleJdbcCall.withCatalogName("cobis");
			simpleJdbcCall.withSchemaName("dbo");
			simpleJdbcCall.withProcedureName("pa_mis_cons_catalogo");
			simpleJdbcCall.declareParameters(sqlParameters)
					.returningResultSet("result", new RowMapper<CatalogoBean>() {
						@Override
						public CatalogoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
							final CatalogoBean pr = new CatalogoBean();
							pr.setCodigo(rs.getString("codigo"));
							pr.setDescripcion(rs.getString("descripcion"));
							return pr;
						}
					});
			Map<String, Object> result = simpleJdbcCall.execute(inParams);
			lista = (List) result.get("result");
		} catch (Exception e) {
			e.printStackTrace();
			
			//throw new CustomException(global.getCodError(), global.getErrorDatabase(), e.getMessage());
		}
		return lista;
	}
	
	
	
	//public List<CatalogoBean> obtenerCatalogosSpIntegrado(String nombreTabla, String tipo, 
	public List<CatalogoBean> obtenerCatalogosSpIntegrado(String nombreTabla, String tipo,
			String tipoOperacion, String tipoPersona) throws CustomException, Exception {
		List<CatalogoBean> lista = null;
		//List  lista = null;
		try {
			//SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			HashMap<String, Object> inParams = new HashMap<String, Object>();	
	 		inParams.put("e_nombre_tabla", nombreTabla);
			inParams.put("e_tipo", tipo);
			inParams.put("e_tipo_operacion", tipoOperacion);
			inParams.put("e_tipo_persona", tipoPersona);
			inParams.put("s_error", 0);
			inParams.put("s_msg_err", "");
			SqlParameter[] sqlParameters = {
					new SqlParameter("e_nombre_tabla", Types.VARCHAR),
					new SqlParameter("e_tipo", Types.VARCHAR),
					new SqlParameter("e_tipo_operacion", Types.VARCHAR),
					new SqlParameter("e_tipo_persona", Types.VARCHAR),
					new SqlOutParameter("s_error", Types.INTEGER),
					new SqlOutParameter("s_msg_err", Types.VARCHAR)};
			DataSource sybaseDataSource = dataSourceManager.sybaseDataSource();
	    	SimpleJdbcCall simpleJdbcCall = dataSourceManager.sybaseSimpleJdbcCall(sybaseDataSource);
			simpleJdbcCall.withCatalogName("cob_cartera");
			simpleJdbcCall.withSchemaName("dbo");
			simpleJdbcCall.withProcedureName("pa_cca_ccons_catalogos");
			simpleJdbcCall.declareParameters(sqlParameters)
					.returningResultSet("result", new RowMapper<CatalogoBean>() {
						@Override
						public CatalogoBean mapRow(ResultSet rs, int rowNum) throws SQLException {
							final CatalogoBean pr = new CatalogoBean();
							pr.setCodigo(rs.getString("codigo"));
							pr.setDescripcion(rs.getString("descripcion"));
							//pr.setValor("pruebaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
							return pr;
						}
					});
			Map<String, Object> result = simpleJdbcCall.execute(inParams);
			lista = (List) result.get("result");
			System.out.println("s_error "+result.get("s_error").toString());
			System.out.println("s_msg_err "+result.get("s_msg_err").toString());
		} catch (Exception e) {
			e.printStackTrace();
			
			//throw new CustomException(global.getCodError(), global.getErrorDatabase(), e.getMessage());
		}
		return lista;
	}
	
	/*
	public Map<?, ?> buscarRegistroCivilLocalDWH(String cedula) {
		HashMap<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("e_num_ced", cedula+";");
		inParams.put("s_filas_afectadas", 0);
		inParams.put("s_codigo_error", 0);
		inParams.put("s_mensaje_error", "");

		SqlParameter sqlParameters[] = {};
		return storedProcedureUtils.callStoredProcedureDWH("DWBOLIVARIANO", "pa_dwh_ccliente_ruc", inParams, sqlParameters);
	}
	*/
	
	public  Map<?, ?> validarExistenciaCliente(//String tipo_operacion, 
			String identificacion){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	

		//inParams.put("e_tipo_operacion", tipo_operacion);
		inParams.put("e_identificacion", identificacion);
		inParams.put("s_ente", "");
		inParams.put("s_nombre", "");
		inParams.put("s_estado ", "");
		inParams.put("s_existe ", "");
		inParams.put("s_error", 0);
		inParams.put("s_msg_err", "");
		SqlParameter[] sqlParameters = {new SqlParameter("e_identificacion", Types.VARCHAR),
										new SqlOutParameter("s_ente", Types.INTEGER),
										new SqlOutParameter("s_nombre", Types.VARCHAR),
										new SqlOutParameter("s_estado", Types.VARCHAR),
										new SqlOutParameter("s_existe", Types.VARCHAR),										
										new SqlOutParameter("s_error", Types.INTEGER),
										new SqlOutParameter("s_msg_err", Types.VARCHAR)
										};
		//return storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_gidentificacion", inParams, sqlParameters);
		return storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_cidentificacion", inParams, sqlParameters);
	}

	public  Map<?, ?> validarClienteImpedimento(String ente){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	

		inParams.put("e_ente", ente);
		inParams.put("s_cod_error", 0);
		inParams.put("s_msj_error", "");
		SqlParameter[] sqlParameters = {new SqlParameter("e_ente", Types.INTEGER ),									
										new SqlOutParameter("s_cod_error", Types.INTEGER),
										new SqlOutParameter("s_msj_error", Types.VARCHAR)
										};
		return storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_cte_gcerbancre", inParams, sqlParameters);
	}
	public  Map<?, ?> buscarRegistroCivilLocalSybase(//String tipo_operacion, 
			String identificacion){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	

		//inParams.put("e_tipo_operacion", tipo_operacion);
		inParams.put("e_identificacion", identificacion);
		inParams.put("s_nombre", "");
		inParams.put("s_sexo", "");
		inParams.put("s_ciudadania", "");
		inParams.put("s_fecha_nacimiento", "");
		inParams.put("s_nacionalidad", "");
		inParams.put("s_estado_civil", "");
		inParams.put("s_nombre_conyuge", "");
		inParams.put("s_nombre_padre", "");
		inParams.put("s_nombre_madre", "");
		inParams.put("s_nombre_calle", "");
		inParams.put("s_numero_casa", "");
		inParams.put("s_fecha_matrimonio", "");
		inParams.put("s_profesion", "");
		inParams.put("s_niv_instruccion", "");
		inParams.put("s_fecha_exped", "");
		inParams.put("s_existe", "");
		inParams.put("s_codigo_error", 0);
		inParams.put("s_msg_err", "");
		inParams.put("s_apellidos", "");
		inParams.put("s_nombres", "");
		inParams.put("s_rc_cod_niv_instruccion", 0);
		inParams.put("s_rc_cod_profesion", "");
		inParams.put("s_rc_cod_estado_civil", 0);
		inParams.put("s_rc_cod_sexo", 0);
		inParams.put("s_hn_cod_estciv", "");
		inParams.put("s_hn_profesion", "");
		inParams.put("s_hn_cod_nacionalidad", "");


		SqlParameter[] sqlParameters = {new SqlParameter("e_identificacion", Types.VARCHAR),
										new SqlOutParameter("s_nombre", Types.VARCHAR),
										new SqlOutParameter("s_sexo", Types.VARCHAR),
										new SqlOutParameter("s_ciudadania", Types.VARCHAR),
										new SqlOutParameter("s_fecha_nacimiento", Types.VARCHAR),
										new SqlOutParameter("s_nacionalidad", Types.VARCHAR),
										new SqlOutParameter("s_estado_civil", Types.VARCHAR),
										new SqlOutParameter("s_nombre_conyuge", Types.VARCHAR),										
										new SqlOutParameter("s_nombre_padre", Types.VARCHAR),
										new SqlOutParameter("s_nombre_madre", Types.VARCHAR),
										new SqlOutParameter("s_nombre_calle", Types.VARCHAR),
										new SqlOutParameter("s_numero_casa", Types.VARCHAR),
										new SqlOutParameter("s_fecha_matrimonio", Types.VARCHAR),
										new SqlOutParameter("s_profesion", Types.VARCHAR),
										
										new SqlOutParameter("s_niv_instruccion", Types.VARCHAR),
										new SqlOutParameter("s_fecha_exped", Types.VARCHAR),
										new SqlOutParameter("s_existe", Types.VARCHAR),										
										new SqlOutParameter("s_codigo_error", Types.INTEGER),
										new SqlOutParameter("s_msg_err", Types.VARCHAR),
										new SqlOutParameter("s_apellidos", Types.VARCHAR),
										new SqlOutParameter("s_nombres", Types.VARCHAR),
										new SqlOutParameter("s_rc_cod_niv_instruccion", Types.INTEGER),
										new SqlOutParameter("s_rc_cod_profesion", Types.VARCHAR),
										new SqlOutParameter("s_rc_cod_estado_civil", Types.INTEGER),
										new SqlOutParameter("s_rc_cod_sexo", Types.INTEGER),
										new SqlOutParameter("s_hn_cod_estciv", Types.VARCHAR),
										new SqlOutParameter("s_hn_profesion", Types.VARCHAR),
										new SqlOutParameter("s_hn_cod_nacionalidad", Types.VARCHAR)
										};
		return storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_cregistro_civil", inParams, sqlParameters);
		
	}

	public  Map<?, ?> consultarDatosCliente(//String tipo_operacion, 
			String identificacion){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	

		//inParams.put("e_tipo_operacion", tipo_operacion);
		inParams.put("e_identificacion", identificacion);
		inParams.put("s_ente", 0);
		inParams.put("s_nombres", "");
		inParams.put("s_primer_apellido", "");
		inParams.put("s_segundo_apellido", "");
		inParams.put("s_fecha_nacimiento ", "");
		//inParams.put("s_identificacion", "");
		inParams.put("s_oficina_manejo", 0);
		inParams.put("s_mail", "");
		inParams.put("s_nacionalidad", "");
		inParams.put("s_error", 0);
		inParams.put("s_msg_err", "");
		SqlParameter[] sqlParameters = {new SqlParameter("e_identificacion", Types.VARCHAR),
										new SqlOutParameter("s_ente", Types.INTEGER),
										new SqlOutParameter("s_nombres", Types.VARCHAR),
										new SqlOutParameter("s_primer_apellido", Types.VARCHAR),
										new SqlOutParameter("s_segundo_apellido", Types.VARCHAR),
										new SqlOutParameter("s_fecha_nacimiento", Types.VARCHAR),
										//new SqlOutParameter("s_identificacion", Types.VARCHAR),
										new SqlOutParameter("s_oficina_manejo", Types.INTEGER),
										new SqlOutParameter("s_mail", Types.VARCHAR),
										new SqlOutParameter("s_nacionalidad", Types.VARCHAR),
										new SqlOutParameter("s_envio_boletin", Types.CHAR),
										new SqlOutParameter("s_principal", Types.CHAR),
										new SqlOutParameter("s_error", Types.INTEGER),
										new SqlOutParameter("s_msg_err", Types.VARCHAR)
										};
		//return storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_gidentificacion", inParams, sqlParameters);
		return storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_ccliente", inParams, sqlParameters);
	}
	
	
	public  Map<?, ?> validarClientesInhabilitadosArchivosNegativos(
			String identificacion , String tipoIdentificacion, Integer ente, String tipocta, String cuenta , String categoria){
		HashMap<String, Object> inParams = new HashMap<String, Object>();	
				System.out.println("tipocta"+tipocta);
				inParams.put("e_user","ONB");
				inParams.put("e_ofi",3);
				inParams.put("e_term", "EQUIPO");
				inParams.put("t_trn", 166178);
				inParams.put("e_tipoid", tipoIdentificacion);
				inParams.put("e_cedula", identificacion);
				inParams.put("e_cliente", ente);
				inParams.put("e_tipocta", tipocta);
				inParams.put("e_cuenta", cuenta);
				inParams.put("e_categoria", categoria);
				inParams.put("o_error", 0);
				inParams.put("o_msjerror", "");
		SqlParameter[] sqlParameters = {
										new SqlParameter("e_user", Types.VARCHAR),
										new SqlParameter("e_ofi", Types.SMALLINT),
										new SqlParameter("e_term", Types.VARCHAR),
										new SqlParameter("t_trn", Types.INTEGER),
										new SqlParameter("e_tipoid", Types.CHAR),
										new SqlParameter("e_cedula", Types.VARCHAR),
										new SqlParameter("e_cliente", Types.INTEGER),
										new SqlParameter("e_tipocta", Types.VARCHAR),
										new SqlParameter("e_cuenta", Types.VARCHAR),
										new SqlParameter("e_categoria", Types.VARCHAR),
										new SqlOutParameter("o_error", Types.INTEGER),
										new SqlOutParameter("o_msjerror", Types.VARCHAR)
										};
		return storedProcedureUtils.callStoredProcedureSybase("db_bpm", "pa_bpm_cvalida_cta", inParams, sqlParameters);
	}
	
	
	
	
	
	public  Map<?, ?> obtenerCatalogoHomologacion(String codCatalgo,String descripcionOrigen) throws CustomException, Exception {
		List<CatalogoBean> lista = null;
		Map<String, Object> result = null;
	
			System.out.println("codCatalgo"+codCatalgo);
			System.out.println("descripcionOrigen"+descripcionOrigen);
			HashMap<String, Object> inParams = new HashMap<String, Object>();	

			inParams.put("cod_catalogo", codCatalgo);
			inParams.put("descri_origen", descripcionOrigen);
			inParams.put("codigo_hm", "");
			inParams.put("descri_hm", "");
			inParams.put("codigo_error", "");
			inParams.put("msg_err", "");
			SqlParameter[] sqlParameters = {new SqlParameter("cod_catalogo", Types.VARCHAR),
											new SqlParameter("descri_origen", Types.VARCHAR),
											new SqlOutParameter("codigo_hm", Types.VARCHAR),
											new SqlOutParameter("descri_hm", Types.VARCHAR),
											new SqlOutParameter("codigo_error", Types.INTEGER),
											new SqlOutParameter("msg_err", Types.VARCHAR),
											};
			
			return	storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_cregi_civ_munic", inParams, sqlParameters);
		
		
	}
	
	
	public  Map<?, ?> obtenerCatalogoHomologacion2(String genero, String estadoCivil, String nacionalidad, String profesion) throws CustomException, Exception {
		List<CatalogoBean> lista = null;
		Map<String, Object> result = null;
	
			System.out.println("genero "+genero);
			System.out.println("estadoCivil "+estadoCivil);
			System.out.println("nacionalidad "+nacionalidad);
			System.out.println("profesion "+profesion);
			
			HashMap<String, Object> inParams = new HashMap<String, Object>();	

			inParams.put("e_descri_origen_genero", genero);
			inParams.put("e_descri_origen_est_civil", estadoCivil);
			inParams.put("e_descri_origen_nacionalidad", nacionalidad);
			inParams.put("e_descri_origen_profesion", profesion);
			inParams.put("s_cod_hm_genero", "");
			inParams.put("s_cod_hm_est_civil", "");
			inParams.put("s_cod_hm_nacionalidad", "");
			inParams.put("s_cod_hm_profesion", "");
			inParams.put("s_nom_hm_profesion", "");
			inParams.put("s_codigo_error", "");
			inParams.put("s_msg_err", "");
			SqlParameter[] sqlParameters = {new SqlParameter("e_descri_origen_genero", Types.VARCHAR),
											new SqlParameter("e_descri_origen_est_civil", Types.VARCHAR),
											new SqlParameter("e_descri_origen_nacionalidad", Types.VARCHAR),
											new SqlParameter("e_descri_origen_profesion", Types.VARCHAR),
											
											new SqlOutParameter("s_cod_hm_genero", Types.VARCHAR),
											new SqlOutParameter("s_cod_hm_est_civil", Types.VARCHAR),
											new SqlOutParameter("s_cod_hm_nacionalidad", Types.VARCHAR),
											new SqlOutParameter("s_cod_hm_profesion", Types.VARCHAR),
											new SqlOutParameter("s_nom_hm_profesion", Types.VARCHAR),
											new SqlOutParameter("s_codigo_error", Types.INTEGER),
											new SqlOutParameter("s_msg_err", Types.VARCHAR),
											};
			
			return	storedProcedureUtils.callStoredProcedureSybase("cob_cuentas", "pa_obc_cregi_civ_munic", inParams, sqlParameters);
		
		
	}
	
	public Map<?, ?> validarExistenciaMedios(String correo, String telefono) {
		HashMap<String, Object> inParams = new HashMap<String, Object>();

		inParams.put("e_correo", correo);
		inParams.put("e_telefono", telefono);
		inParams.put("s_existe_correo", "");
		inParams.put("s_existe_telefono", "");
//		inParams.put("s_cod_error", 0);
//		inParams.put("s_msj_error", "");
		SqlParameter[] sqlParameters = { 
				new SqlParameter("e_correo", Types.VARCHAR),
				new SqlParameter("e_telefono", Types.VARCHAR), 
				new SqlOutParameter("s_existe_correo", Types.CHAR),
				new SqlOutParameter("s_existe_telefono", Types.CHAR)};
//				new SqlOutParameter("s_cod_error", Types.INTEGER), 
//				new SqlOutParameter("s_msj_error", Types.VARCHAR) };
		return storedProcedureUtils.callStoredProcedureSybase("cobis", "pa_onb_cvalmed", inParams,
				sqlParameters);
	}
	
	
	public Map<?, ?> obtenerInformacionCliente(MensajeEntradaObtenerInformacionCliente request) {

		Map<String, Object> result = null;
		try {
			HashMap<String, Object> inParams = new HashMap<String, Object>();
			inParams.put("e_ente", Integer.parseInt(request.getEnte()));
			inParams.put("s_cod_error", 0);
			inParams.put("s_msj_error", "");

			SqlParameter[] sqlParameters = { new SqlParameter("e_ente", Types.INTEGER),
					new SqlOutParameter("s_cod_error", Types.INTEGER),
					new SqlOutParameter("s_msj_error", Types.VARCHAR) };

			DataSource sybaseDataSource = dataSourceManager.sybaseDataSource();
			SimpleJdbcCall simpleJdbcCall = dataSourceManager.sybaseSimpleJdbcCall(sybaseDataSource);

			simpleJdbcCall.withCatalogName("cob_cuentas");
			simpleJdbcCall.withSchemaName("dbo");
			simpleJdbcCall.withProcedureName("pa_onb_ginformacioncliente");

			simpleJdbcCall.declareParameters(sqlParameters).
			returningResultSet("result", new RowMapper<DatosCliente>() {//#result-set-2
				@Override
				public DatosCliente mapRow(ResultSet rs, int rowNum) throws SQLException {
					final DatosCliente p = new DatosCliente();
					p.setEnte(rs.getString("en_ente"));
					p.setFechaNacimiento(rs.getString("p_fecha_nac"));
					p.setMedio(rs.getString("email"));
					p.setSecuenciaMedio(rs.getString("secuencia_email"));
					p.setTipoMedio(rs.getString("tipomedio"));
					p.setTipoNacionalidad(rs.getString("p_tipo_nacionalidad"));

					return p;
				}
			});
			
			/*returningResultSet("result", new RowMapper<DatosCliente>() {
				@Override
				public DatosCliente mapRow(ResultSet rs, int rowNum) throws SQLException {
					final DatosCliente p = new DatosCliente();

					p.setEnte(rs.getString("en_ente"));
					p.setDireccion(rs.getString("direccion"));
					p.setTipoDireccion(rs.getString("tipo_direccion"));
					p.setFechaNacimiento(rs.getString("p_fecha_nac"));
					p.setEnvioCorrespondencia(rs.getString("envio_correspondencia"));
					p.setCanton(rs.getString("canton"));
					p.setCiudad(rs.getString("codigo_ciudad"));
					p.setParroquia(rs.getString("parroquia"));
					// p.setMedio(rs.getString("email"));
					p.setSecuenciaDireccion(rs.getString("secuencia_dir"));
					// p.setSecuenciaMedio(rs.getString("secuencia_email"));
					// p.setTipoMedio(rs.getString("tipomedio"));
					p.setTipoNacionalidad(rs.getString("p_tipo_nacionalidad"));

					return p;
				}
			})
			.returningResultSet("#result-set-3", new RowMapper<DatosCliente>() {
				@Override
				public DatosCliente mapRow(ResultSet rs, int rowNum) throws SQLException {
					final DatosCliente p = new DatosCliente();
					p.setCodigoTrabajoCobis(rs.getString("codigo_trabajo_cobis"));
					p.setSecuenciaTrabajo(rs.getString("secuencia_trabajo"));
					p.setNombreEmpresa(rs.getString("nombre_empresa"));

					return p;
				}
			});*/

			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			result = simpleJdbcCall.execute(inParams);

			logger.debug("linea obtenerInformacionCliente " + result.toString());
		} catch (Exception e) {
			logger.error("error service" + util.getStackTrace(e));
		}

		return result;
	}


	public Map<?, ?> encerarDatosClientesMis(Map<String, Object> mapDataExtra) {

		Map<String, Object> result = null;
		HashMap<String, Object> inParams = new HashMap<String, Object>();

		inParams.put("e_term", mapDataExtra.get("terminal"));
		inParams.put("e_trn1", Integer.valueOf(mapDataExtra.get("trn1").toString()));
		inParams.put("e_trn2", Integer.valueOf(mapDataExtra.get("trn2").toString()));
		inParams.put("e_trn3", Integer.valueOf(mapDataExtra.get("trn3").toString()));
		inParams.put("e_trn4", Integer.valueOf(mapDataExtra.get("trn4").toString()));
		inParams.put("e_trn5", Integer.valueOf(mapDataExtra.get("trn5").toString()));
		inParams.put("e_trn6", Integer.valueOf(mapDataExtra.get("trn6").toString()));
		inParams.put("e_ente", Integer.valueOf(mapDataExtra.get("ente").toString()));
		inParams.put("e_srv", mapDataExtra.get("srv"));
		inParams.put("e_lsrv", mapDataExtra.get("lsrv"));
		inParams.put("s_cod_error", 0);
		inParams.put("s_msj_error", "");
		SqlParameter[] sqlParameters = { 
				new SqlParameter("e_term", Types.VARCHAR),
				new SqlParameter("e_trn1", Types.INTEGER), 
				new SqlParameter("e_trn2", Types.INTEGER),
				new SqlParameter("e_trn3", Types.INTEGER),
				new SqlParameter("e_trn4", Types.INTEGER),
				new SqlParameter("e_trn5", Types.INTEGER),
				new SqlParameter("e_trn6", Types.INTEGER),
				new SqlParameter("e_ente", Types.INTEGER),
				new SqlParameter("e_srv", Types.VARCHAR),
				new SqlParameter("e_lsrv", Types.VARCHAR),
				new SqlOutParameter("s_cod_error", Types.INTEGER),
				new SqlOutParameter("s_msj_error", Types.VARCHAR)
		};
			
		DataSource sybaseDataSource = dataSourceManager.sybaseDataSource();
		SimpleJdbcCall simpleJdbcCall = dataSourceManager.sybaseSimpleJdbcCall(sybaseDataSource);

		simpleJdbcCall.withCatalogName("cobis");
		simpleJdbcCall.withSchemaName("dbo");
		simpleJdbcCall.withProcedureName("pa_onb_tencerardatclimis");

		simpleJdbcCall.declareParameters(sqlParameters).returningResultSet("result", new GenericRowMapper());
		
		simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
		result = simpleJdbcCall.execute(inParams);

		logger.debug("linea obtenerInformacionCliente " + result.toString());
		
		return result;
		
	}

}

