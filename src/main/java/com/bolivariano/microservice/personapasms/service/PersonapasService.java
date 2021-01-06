package com.bolivariano.microservice.personapasms.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.camel.Exchange;
import org.datacontract.schemas._2004._07.consultasrcg.ResponseConsultaCedulado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tempuri.ConsultarPersona;

import com.bolivariano.cashmanagement.mensajecliente.MensajeEntradaObtenerPaquetesMensajeriaActivos;
import com.bolivariano.cashmanagement.mensajecliente.MensajeSalidaObtenerPaquetesMensajeriaActivos;
import com.bolivariano.enumerados.TipoIdentificacionPersona;
import com.bolivariano.mensajeregistrocivil.MensajeEntradaConsultar;
import com.bolivariano.mensajeregistrocivil.MensajeSalidaConsultar;
import com.bolivariano.microservice.personapasms.bean.DatosCliente;
import com.bolivariano.microservice.personapasms.bean.HomologacionRegistroCivil;
import com.bolivariano.microservice.personapasms.consumeWS.ServiceBL;
import com.bolivariano.microservice.personapasms.dao.PersonapasDAO;
import com.bolivariano.microservice.personapasms.exception.CustomException;
import com.bolivariano.microservice.personapasms.utils.Util;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaConsultarDatosCliente;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaEncerarDatosClientesMis;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaObtenerInformacionCliente;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaOtenerCatalogoHomologacionRC;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaValidarClienteExiste;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaValidarClienteImpedimento;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaValidarClientesInhabilitadosArchivosNegativos;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaValidarExistenciaMedios;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaValidarListaNegra;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaValidarRegistroCivil;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeEntradaVerificarAviso24Activo;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaConsultarDatosCliente;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaEncerarDatosClientesMis;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaObtenerInformacionCliente;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaObtenerInformacionCliente.Cliente;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaObtenerInformacionCliente.Cliente.DatosMedios;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaOtenerCatalogoHomologacionRC;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarClienteExiste;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarClienteImpedimento;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarClientesInhabilitadosArchivosNegativos;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarExistenciaMedios;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarListaNegra;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil;
import com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaVerificarAviso24Activo;
import com.bolivariano.utilitario.common.StopWatch;

import generated.JsonResponsePersona;

@Service("personapas")
public class PersonapasService {
	private final static Logger logger = LoggerFactory.getLogger(PersonapasService.class);

	@Autowired
	WebServiceConfig config;
	/*
	 * @Value("${client.ws.address.homologarMensaje.sistema}") String sistema;
	 */
	@Autowired
	private PersonapasDAO personapasDAO;

	@Autowired
	private Util util;

	@Value("${client.ws.address.listaNegra.url}")
	private String listaNegraOsbMensajeURL;
	@Value("${client.ws.address.listaNegra.conntimeout}")
	private int listaNegraOsbConntimeout;
	@Value("${client.ws.address.listaNegra.readtimeout}")
	private int listaNegraOsbReadtimeout;

	@Value("${client.ws.address.framework.appId}")
	private String frameworkApp;

	@Value("${client.ws.address.framework.appUser}")
	private String frameworkusrApl;

	@Value("${server_user_rgcm}")
	private String serverUserRgcm;

	@Value("${server_clave_rgcm}")
	private String serverClaveRgcm;
	
	@Value("${personapasms.trn1}")
	private String trn1;
	
	@Value("${personapasms.trn2}")
	private String trn2;
	
	@Value("${personapasms.trn3}")
	private String trn3;
	
	@Value("${personapasms.trn4}")
	private String trn4;
	
	@Value("${personapasms.trn5}")
	private String trn5;
	
	@Value("${personapasms.trn6}")
	private String trn6;
	
	@Value("${personapasms.srv}")
	private String srv;
	
	@Value("${personapasms.lsrv}")
	private String lsrv;

	public MensajeSalidaValidarRegistroCivil validarRegistroCivilOsb(MensajeEntradaValidarRegistroCivil mensaje,
			Exchange exchange) {
		System.out.println("validarRegistroCivil OSb" + mensaje.isLocal());
		MensajeSalidaValidarRegistroCivil salida = new MensajeSalidaValidarRegistroCivil();

		try {

			String identificacion = mensaje.getIdentificacion();
			salida.setCodigoError("1");
			salida.setMensajeUsuario("No se han encontrado datos");
			salida.setMensajeSistema("No se han encontrado datos");

			MensajeEntradaConsultar consultarRegCivilOsb = new MensajeEntradaConsultar();
			MensajeSalidaConsultar salidaRegCivilOsb = new MensajeSalidaConsultar();
			consultarRegCivilOsb.setCanal("MIS");
			consultarRegCivilOsb.setUsuario("fmirandg");
			consultarRegCivilOsb.setIdentificacion(identificacion);
			System.out.println("consultarRegCivilOsb.getIdentificacion() " + consultarRegCivilOsb.getIdentificacion());

			com.bolivariano.ws.registrocivil.RegistroCivil regCiv = null;
			regCiv = config.clienteRegistroCivilOsb();

			System.out.println("Antes de regCiv.consulta(consultarRegCivilOsb)");
			// salidaRegCivilOsb = regCiv.consulta(consultarRegCivilOsb);
			salidaRegCivilOsb = regCiv.consulta(consultarRegCivilOsb);
			System.out.println("Luego de regCiv.consulta(consultarRegCivilOsb)");

			System.out.println("salidaRegCivilOsb. ERROR " + salidaRegCivilOsb.getCodigoError());
			if (salidaRegCivilOsb.getCodigoError().equals("0")) {
				List<com.bolivariano.registrocivil.RegistroCivil> cedulaBean = salidaRegCivilOsb.getRegistroCivil();

				if (cedulaBean.size() > 0) {
					String[] ArrayNombre;
					String nombres = "";
					Date dateLong = null;
					String[] parseDate = {};
					String fechaNac = "";
					String FechaFinal = "";
					int hour = 13;
					MensajeSalidaValidarRegistroCivil.RegistroCivil regCivBus = new MensajeSalidaValidarRegistroCivil.RegistroCivil();
					for (int i = 0; i < cedulaBean.size(); i++) {

						regCivBus.setNombre(cedulaBean.get(i).getNombre());

						regCivBus.setNombres(cedulaBean.get(i).getNombres());
						regCivBus.setApellidos(cedulaBean.get(i).getApellidos());

						if (cedulaBean.get(i).getNombres().trim().equals("")) {
							ArrayNombre = cedulaBean.get(i).getNombre().split(" ");
							if (ArrayNombre.length == 4) {
								regCivBus.setNombres(ArrayNombre[2] + " " + ArrayNombre[3]);
							}
							if (ArrayNombre.length == 3) {
								regCivBus.setNombres(ArrayNombre[1] + " " + ArrayNombre[2]);
							}
							if (ArrayNombre.length == 2) {
								regCivBus.setNombres(ArrayNombre[1]);
							}
							if (ArrayNombre.length == 1) {
								regCivBus.setNombres(ArrayNombre[0]);
							}
							if (ArrayNombre.length > 4) {
								for (int j = 0; j < ArrayNombre.length; j++) {
									if (j != 0 && j != 1) {
										nombres += " " + ArrayNombre[j];
									}

								}

								regCivBus.setNombres(nombres);
							}

						} else {
							regCivBus.setNombres(cedulaBean.get(i).getNombres());
						}

						if (cedulaBean.get(i).getApellidos().trim().equals("")) {
							ArrayNombre = cedulaBean.get(i).getNombre().split(" ");
							if (ArrayNombre.length == 4) {

								regCivBus.setApellidos(ArrayNombre[0] + " " + ArrayNombre[1]);
							}
							if (ArrayNombre.length == 3) {
								regCivBus.setApellidos(ArrayNombre[0] + " " + ArrayNombre[1]);
							}
							if (ArrayNombre.length == 2) {
								regCivBus.setApellidos(ArrayNombre[0]);
							}
							if (ArrayNombre.length == 1) {
								regCivBus.setApellidos(ArrayNombre[0]);
							}
							if (ArrayNombre.length > 4) {
								for (int j = 0; j < ArrayNombre.length; j++) {
									regCivBus.setApellidos(ArrayNombre[0] + " " + ArrayNombre[1]);
								}
							}

						} else {
							regCivBus.setApellidos(cedulaBean.get(i).getApellidos());
						}

						regCivBus.setCondicionCedulado(cedulaBean.get(i).getCondicionCedulado());
						// dateLong.setTimeZone(TimeZone.getTimeZone("UTC"));
						System.out.println(
								"cedulaBean.get(i).getFechaNacimiento()" + cedulaBean.get(i).getFechaNacimiento());
						if (cedulaBean.get(i).getFechaNacimiento() != null) {
							dateLong = new SimpleDateFormat("dd/MM/yyyy").parse(cedulaBean.get(i).getFechaNacimiento());
							dateLong.toGMTString();
							dateLong.setHours(00);
							dateLong.setMinutes(00);
							System.out.println("dateLong" + dateLong);
							// fechaNac=(String)dateLong;
							System.out.println("dateLong.toGMTString()" + dateLong.toGMTString().substring(0, 11));
							// fechaNac=dateLong.toGMTString().replace("05:00:00 GMT", "12:00AM");
							fechaNac = dateLong.toGMTString().substring(0, 11);
							System.out.println("fechaNac" + fechaNac);
							FechaFinal = fechaNac + " 12:00AM";
							parseDate = FechaFinal.split(" ");
							if (parseDate.length == 4) {
								FechaFinal = parseDate[1].trim() + " " + parseDate[0].trim() + " " + parseDate[2].trim()
										+ " " + parseDate[3].trim();
							}
							System.out.println("FechaFinal" + FechaFinal);
							regCivBus.setFechaNacimiento(FechaFinal);
						}

						regCivBus.setNacionalidad(cedulaBean.get(i).getNacionalidad());
						regCivBus.setEstadoCivil(cedulaBean.get(i).getEstadoCivil());
						regCivBus.setConyuge(cedulaBean.get(i).getConyuge());
						regCivBus.setInstruccion(cedulaBean.get(i).getInstruccion());
						regCivBus.setProfesion(cedulaBean.get(i).getProfesion());
						regCivBus.setFechaFallecimiento(cedulaBean.get(i).getFechaFallecimiento());
						regCivBus.setFechaCedulacion(cedulaBean.get(i).getFechaCedulacion());

						// mensaje.getFechaExpedicion

						/* VALIDAR FECHAS */
						/*
						 * if(mensaje.getFechaExpedicion()!==null && mensaje.getFechaExpedicion()!=="")
						 * {
						 * 
						 * }
						 */
						/*
						 * Date dateLongExp=null;
						 * System.out.println("regCiv.getFechaCedulacion()"+regCivBus.getFechaCedulacion
						 * ().toString().replace(" 12:00AM", "")); dateLongExp= new
						 * SimpleDateFormat("MMM dd yyyy").parse(regCivBus.getFechaCedulacion().toString
						 * ()); SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						 * 
						 * String formatted = format1.format(dateLong);
						 * System.out.println("formatted"+formatted);
						 * 
						 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date fechaDBOSB=
						 * sdf.parse(formatted); Date fechaFront= sdf.parse("2001-03-29");
						 * //mensaje.getFechaExpedicion() if(fechaFront.compareTo(fechaDBOSB)<0 ) {
						 * //devuelve un valor menor que 0 System.out.println("FechaExpe");
						 * mensaje.setLocal(false); salida.setCodigoError("INVALID"); //
						 * validarRegistroCivilOsb(mensaje,exchange); }
						 */

						regCivBus.setDomicilio(cedulaBean.get(i).getDomicilio());
						regCivBus.setCalle(cedulaBean.get(i).getCalle());
						regCivBus.setNumeroCasa(cedulaBean.get(i).getNumeroCasa());

						regCivBus.setSexo(cedulaBean.get(i).getSexo());

						if (cedulaBean.get(i).getSexo().equalsIgnoreCase("MUJER")) {
							regCivBus.setSexo("F");
						} else {
							if (cedulaBean.get(i).getSexo().equalsIgnoreCase("HOMBRE")) {
								// regCivBus.setSexo(cedulaBean.get(i).getSexo());
								regCivBus.setSexo("M");
							}
						}

						regCivBus.setNombrePadre(cedulaBean.get(i).getNombrePadre());
						regCivBus.setNombreMadre(cedulaBean.get(i).getNombreMadre());

						regCivBus.setLugarNacimiento(cedulaBean.get(i).getLugarNacimiento());
						regCivBus.setFechaMatrimonio(cedulaBean.get(i).getFechaMatrimonio());
						regCivBus.setHomMISNacionalidad(cedulaBean.get(i).getHomMISNacionalidad());

						regCivBus.setHomMISSexo(cedulaBean.get(i).getHomMISSexo());
						regCivBus.setHomMISEstCivil(cedulaBean.get(i).getHomMISEstCivil());
						regCivBus.setHomMISProfesion(cedulaBean.get(i).getHomMISProfesion());
						regCivBus.setLocal(mensaje.isLocal());
					}

					salida.setRegistroCivil(regCivBus);
					System.out.println("salida.getRegistroCivil().isLocal()" + salida.getRegistroCivil().isLocal());
					salida.setCodigoError("0");
					salida.setMensajeUsuario("TRANSACCION EXITOSA");
					salida.setMensajeSistema("TRANSACCION EXITOSA");
				}
			} else {

				salida.setCodigoError(salidaRegCivilOsb.getCodigoError());
				salida.setMensajeSistema(salidaRegCivilOsb.getMensajeSistema());
				salida.setMensajeUsuario("Inténtelo más tarde");
			}

		} catch (Exception exc) {
			salida.setCodigoError("1");
			salida.setMensajeUsuario("No ha sido posible recuperar datos con esa identificacion");
			salida.setMensajeSistema(exc.getMessage());
			logger.error(exc.getMessage());
		}
		return salida;
	}

	public MensajeSalidaValidarListaNegra validarListaNegra(MensajeEntradaValidarListaNegra mensaje) throws Exception {

		boolean esListaNegra = false;
		System.out.println("Ingresó a validarListaNegra ******************");
		MensajeSalidaValidarListaNegra salida = new MensajeSalidaValidarListaNegra();
		System.out.println("mensaje.getIdentificacion() " + mensaje.getIdentificacion() + " mensaje.getNombres() "
				+ mensaje.getNombres() + " mensaje.getApellidos() " + mensaje.getApellidos());

		// esListaNegra = execWSValidateListaNegra(mensaje.getIdentificacion(),
		// mensaje.getNombres(), mensaje.getApellidos(), mensaje.getFechaNacimiento());
		esListaNegra = execWSValidateListaNegra(mensaje.getIdentificacion(), mensaje.getNombres(),
				mensaje.getApellidos(), mensaje.getFechaNacimiento(), mensaje.getNacionalidad());
		System.out.println("esListaNegra " + esListaNegra);

		if (esListaNegra == false) {
			salida.setCodigoError("0");
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
		} else if (esListaNegra == true) {
			salida.setCodigoError("1");
			salida.setMensajeSistema("CLIENTE EN LISTA NEGRA");
			salida.setMensajeUsuario("CLIENTE EN LISTA NEGRA");
		}

		return salida;
	}

	// public List<DataResponse> execWSValidateListaNegra(RequestBlacklist request)
	// throws Exception {
	public boolean execWSValidateListaNegra(String identificacion, String nombre, String apellido,
			String fechaNacimiento, String nacionalidad) throws Exception {
		// DataResponse objetoIdentification = null;

		boolean esListaNegra = false;

		try {

			if (nacionalidad == null || nacionalidad == "") {
				nacionalidad = "ECUADOR";
			}
			System.out.println("LISTA NEGRA");
			System.out.println("Antes de iniciar el cliente");
			ServiceBL.Iniciar(listaNegraOsbMensajeURL);
			System.out.println("Luego de iniciar el cliente");
			ServiceBL serv = new ServiceBL();
			System.out.println("Luego de instanciar ServiceBL");

			String result = serv.getServiceSoap().verificaIDEnteAct(1, // tipoID,
					identificacion, // numeroID,
					nombre, // "OSAMA",//nombres,
					apellido, // "BIN LADEN",//apellidosNombreCompania,
					"onBoarding", // producto,
					"sa", // usuario,
					"sa@correo.com", // emailPredeterminado,
					nacionalidad, // "ECUADOR", //paisNacionalidad,
					"", // paisNacimiento,
					"", // paisNacionalidadOtra1,
					"", // paisNacionalidadOtra2,
					"", // paisOtraResidencia1,
					"", // paisOtraResidencia2,
					fechaNacimiento, 8, // idListaBusqueda,
					"", // codActEconomica,
					""// tipoEmpresa
			);

			esListaNegra = result.contains("DETIENETRAN=\"True\"");
			System.out.println("esListaNegra " + esListaNegra);
			System.out.println("Resultado de llamada soap ==> " + result);
			/*
			 * JAXBContext context = JAXBContext.newInstance(OFACType.class); Unmarshaller
			 * unmarshaller = context.createUnmarshaller(); StringReader reader = new
			 * StringReader(result); OFACType resultOfacType = (OFACType)
			 * unmarshaller.unmarshal(reader);
			 * 
			 * lista = resultOfacType.getRESULTADOS().getRESULTADO();
			 * 
			 * 
			 * 
			 * for(DataWsBlackList reg : request.getListaDataWsBL()) {
			 * 
			 * // Si existe cedula en el arreglo de respuesta de la lista negra, //
			 * entonces, setear el estado a Rechazado, caso contrario, el estado Aprobado //
			 * y no se retorna en el arreglo if(lista!=null) {
			 * if(MethodsHelper.validateExistResultBlacklist(lista,
			 * reg.getIdentification())) { objetoIdentification = new DataResponse();
			 * 
			 * objetoIdentification.setCampo1(reg.getIdentification());
			 * listDataResponse.add(objetoIdentification); } }
			 * 
			 * }
			 */

		} catch (Exception e) {
			System.out.println("catch " + e.getMessage());
			/*
			 * ByteArrayOutputStream out1 = new ByteArrayOutputStream(); PrintStream out2 =
			 * new PrintStream(out1); e.printStackTrace(out2); String message2 = ""; try {
			 * message2 = out1.toString("UTF8"); } catch (UnsupportedEncodingException e1) {
			 * // TODO Auto-generated catch block e1.printStackTrace(); }
			 * 
			 * LOGGER.error("Error printstack 1: " + message2);
			 * 
			 * LOGGER.error("Error execWSValidateListaNegra...");
			 * 
			 * throw new Exception(e.getMessage());
			 */
		} finally {
			// objetoIdentification = null;
			// arregloPersona = null;
			// personaInterfaz = null;
		}

		return esListaNegra;
	}

	public MensajeSalidaValidarClienteExiste validarClienteExiste(MensajeEntradaValidarClienteExiste mensaje,
			Exchange exchange) {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaValidarClienteExiste salida = new MensajeSalidaValidarClienteExiste();
		elapsedTime.start();

		Map<?, ?> out = personapasDAO.validarExistenciaCliente(mensaje.getIdentificacion());

		if (out.get("s_existe") == null) {
			salida.setExiste("S");
		} else {
			salida.setExiste(out.get("s_existe").toString().trim());
		}

		if (out.get("s_ente") == null)
			salida.setEnte("0");
		else
			salida.setEnte(out.get("s_ente").toString());

		if (out.get("s_estado") == null)
			salida.setEstado("");
		else
			salida.setEstado(out.get("s_estado").toString());

		if (out.get("s_error") == null) {
			salida.setCodigoError("");
		} else {
			salida.setCodigoError(out.get("s_error").toString().trim());
		}

		if (out.get("s_msg_err") == null) {
			salida.setMensajeSistema("");
			salida.setMensajeUsuario("");
		} else {
			salida.setMensajeSistema(out.get("s_msg_err").toString().trim());
			salida.setMensajeUsuario(out.get("s_msg_err").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	public MensajeSalidaConsultarDatosCliente consultarDatosCliente(MensajeEntradaConsultarDatosCliente mensaje,
			Exchange exchange) {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaConsultarDatosCliente salida = new MensajeSalidaConsultarDatosCliente();
		System.out.println("PersonaPasMS.consultarDatosCliente");
		elapsedTime.start();
		String[] parseDate = {};
		Map<?, ?> out = personapasDAO.consultarDatosCliente(mensaje.getIdentificacion());

		if (out.get("s_ente") == null) {
			salida.setEnte("0");
		} else {
			salida.setEnte(out.get("s_ente").toString().trim());
		}

		if (out.get("s_nombres") == null) {
			salida.setNombres("");
		} else {
			salida.setNombres(out.get("s_nombres").toString().trim());
		}

		if (out.get("s_primer_apellido") == null) {
			salida.setPrimerApellido("");
		} else {
			salida.setPrimerApellido(out.get("s_primer_apellido").toString().trim());
		}

		if (out.get("s_segundo_apellido") == null) {
			salida.setSegundoApellido("");
		} else {
			salida.setSegundoApellido(out.get("s_segundo_apellido").toString().trim());
		}

		if (out.get("s_fecha_nacimiento") == null) {
			salida.setFechaNacimiento("");
		} else {
			// salida.setFechaNacimiento(out.get("s_fecha_nacimiento").toString().trim());
			System.out.println("Va a obtener fechaNacimiento");
			System.out.println(out.get("s_fecha_nacimiento").toString().trim());
			String fechaNacimiento = "";
			fechaNacimiento = out.get("s_fecha_nacimiento").toString().trim();
			/*
			 * System.out.println("fechaNacimiento Antes de parse "+fechaNacimiento);
			 * parseDate=fechaNacimiento.split(" ");
			 * System.out.println("parseDate "+parseDate);
			 * System.out.println("parseDate.length "+parseDate.length);
			 * System.out.println("parseDate1 "+parseDate[1].trim());
			 * System.out.println("parseDate0 "+parseDate[0].trim());
			 * System.out.println("parseDate2 "+parseDate[2].trim());
			 * if(parseDate.length==4) {
			 * //fechaNacimiento=parseDate[1].trim()+" "+parseDate[0].trim()+" "+
			 * parseDate[2].trim()+" "+parseDate[3].trim();
			 * //fechaNacimiento=parseDate[1].trim()+" "+parseDate[0].trim()+" "+
			 * parseDate[2].trim(); //para bridget
			 * fechaNacimiento=parseDate[2].trim()+" "+parseDate[0].trim()+" "+
			 * parseDate[1].trim(); }
			 */
			// parseDate[1].trim()+" "+parseDate[0].trim()+" "+ parseDate[2].trim()+"
			// "+parseDate[3].trim();
			salida.setFechaNacimiento(fechaNacimiento);

		}

		/*
		 * if(out.get("s_identificacion")==null) { salida.setIdentificacion(""); }else {
		 * salida.setIdentificacion(out.get("s_identificacion").toString().trim()); }
		 */

		salida.setIdentificacion(mensaje.getIdentificacion());

		if (out.get("s_oficina_manejo") == null) {
			salida.setOficinaManejo("");
		} else {
			salida.setOficinaManejo(out.get("s_oficina_manejo").toString().trim());
		}

		if (out.get("s_mail") == null) {
			salida.setMail("");
		} else {
			salida.setMail(out.get("s_mail").toString().trim());
		}
		// System.out.println("mail "+salida.getMail());

		if (out.get("s_nacionalidad") == null) {
			salida.setNacionalidad("");
		} else {
			salida.setNacionalidad(out.get("s_nacionalidad").toString().trim());
		}

		if (out.get("s_envio_boletin") == null) {
			salida.setBoletin("");
		} else {
			salida.setBoletin(out.get("s_envio_boletin").toString().trim());
		}

		if (out.get("s_principal") == null) {
			salida.setPrincipal("");
		} else {
			salida.setPrincipal(out.get("s_principal").toString().trim());
		}

		if (out.get("s_error") == null) {
			salida.setCodigoError("");
		} else {
			salida.setCodigoError(out.get("s_error").toString().trim());
		}

		if (out.get("s_msg_err") == null) {
			salida.setMensajeSistema("");
			salida.setMensajeUsuario("");
		} else {
			salida.setMensajeSistema(out.get("s_msg_err").toString().trim());
			salida.setMensajeUsuario(out.get("s_msg_err").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	public MensajeSalidaValidarRegistroCivil validarRegistroCivilBaseLocal(MensajeEntradaValidarRegistroCivil mensaje,
			// public MensajeSalidaValidarRegistroCivil
			// validarRegistroCivil(MensajeEntradaValidarRegistroCivil mensaje,
			Exchange exchange) throws Exception {

		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaValidarRegistroCivil salida = new MensajeSalidaValidarRegistroCivil();
		elapsedTime.start();
		Util utilitario = new Util();
		com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil regCiv = new com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil();

		Map<?, ?> out = personapasDAO.buscarRegistroCivilLocalSybase(mensaje.getIdentificacion());

		String codigoError = out.get("s_codigo_error").toString().trim();
		salida.setCodigoError(out.get("s_codigo_error").toString().trim());
		ResponseConsultaCedulado response = new ResponseConsultaCedulado();
		/*
		 * if(codigoError!=null && !codigoError.equals("0")) {
		 * 
		 * mensaje.setLocal(false);
		 * 
		 * 
		 * response= consultarRegistroCivil(mensaje); regCiv.setNombre("");
		 * regCiv.setNombres(""); regCiv.setApellidos("");
		 * 
		 * System.out.println("NO ENCONTRADO LOCAL"); if(response!=null) {
		 * if(response.getData()!=null) {
		 * if(response.getData().getJsonResponsePersona()!=null) {
		 * if(response.getData().getJsonResponsePersona().size()>0) { for (int i = 0; i
		 * < response.getData().getJsonResponsePersona().size(); i++) {
		 * regCiv.setNombres(response.getData().getJsonResponsePersona().get(i).
		 * getNombresYApellidos());
		 * regCiv.setFechaNacimiento(response.getData().getJsonResponsePersona().get(i).
		 * getFechaNacimiento());
		 * regCiv.setSexo(response.getData().getJsonResponsePersona().get(i).getSexo());
		 * 
		 * if(response.getData().getJsonResponsePersona().get(i).getSexo().equals(
		 * "MUJER")) { regCiv.setSexo("F"); }else {
		 * if(response.getData().getJsonResponsePersona().get(i).getSexo().equals(
		 * "HOMBRE")) { regCiv.setSexo("M"); } }
		 * 
		 * regCiv.setNacionalidad(response.getData().getJsonResponsePersona().get(i).
		 * getNacionalidad());
		 * regCiv.setEstadoCivil(response.getData().getJsonResponsePersona().get(i).
		 * getEstadoCivil());
		 * regCiv.setIdentificacion(response.getData().getJsonResponsePersona().get(i).
		 * getCedulaCompleta());
		 * regCiv.setInstruccion(response.getData().getJsonResponsePersona().get(i).
		 * getInstruccion());
		 * regCiv.setNacionalidad(response.getData().getJsonResponsePersona().get(i).
		 * getNacionalidad());
		 * regCiv.setConyuge(response.getData().getJsonResponsePersona().get(i).
		 * getNombreConyugue());
		 * regCiv.setProfesion(response.getData().getJsonResponsePersona().get(i).
		 * getProfesionOcupacion());
		 * regCiv.setFechaFallecimiento(response.getData().getJsonResponsePersona().get(
		 * i).getFallecimiento());
		 * regCiv.setFechaCedulacion(response.getData().getJsonResponsePersona().get(i).
		 * getFechaEmision());
		 * regCiv.setDomicilio(response.getData().getJsonResponsePersona().get(i).
		 * getDomicilio());
		 * regCiv.setCodigoDactilar(response.getData().getJsonResponsePersona().get(i).
		 * getCodigoDactilar());
		 * regCiv.setFoto(response.getData().getJsonResponsePersona().get(i).getFoto());
		 * regCiv.setHomMISNacionalidad(""); regCiv.setHomMISProfesion("");
		 * regCiv.setNumeroCasa("");
		 * 
		 * 
		 * } }else {
		 * 
		 * 
		 * } } } }
		 * 
		 * 
		 * regCiv.setLocal(mensaje.isLocal()); salida.setRegistroCivil(regCiv);
		 * 
		 * if(response!=null) { salida.setCodigoError(response.getCodigoError());
		 * salida.setMensajeSistema(response.getMensajeSistema());
		 * salida.setMensajeUsuario(response.getMensajeUsuario()); }else {
		 * salida.setCodigoError("2");
		 * salida.setMensajeSistema("Registro Civil se encuentre fuera de servicio.");
		 * salida.setMensajeUsuario("Error en el servidor intentelo más tarde."); }
		 * 
		 * 
		 * // return validarRegistroCivilOsb(mensaje,exchange); return salida;
		 * 
		 * }else {
		 */

		regCiv.setNombre("");
		regCiv.setNombres("");
		regCiv.setApellidos("");

		if (out.get("s_nombre") != null) {
			regCiv.setNombre(utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nombre").toString().toLowerCase()));
		}

		if (out.get("s_nombres") != null) {
			regCiv.setNombres(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nombres").toString().toLowerCase()));
		}

		if (out.get("s_apellidos") != null) {
			regCiv.setApellidos(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_apellidos").toString().toLowerCase()));
		}

		Date date = new Date();
		XMLGregorianCalendar xmlDate = null;
		GregorianCalendar gc = new GregorianCalendar();

		gc.setTime(date);

		try {
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		regCiv.setFechaNacimiento("");
		salida.setCodigoError("");
		salida.setMensajeSistema("");
		salida.setMensajeUsuario("");
		regCiv.setFechaNacimiento("");
		regCiv.setSexo("");
		regCiv.setNacionalidad("");
		regCiv.setEstadoCivil("");
		regCiv.setIdentificacion("");
		regCiv.setInstruccion("");
		regCiv.setHomMISNacionalidad("");
		regCiv.setHomMISProfesion("");
		regCiv.setFechaCedulacion("");

		String anio = "";
		String mes = "";
		String dia = "";
		String fecha = "";
		if (out.get("s_fecha_nacimiento") != null) {
			// regCiv.setFechaNacimiento(out.get("s_fecha_nacimiento").toString().toLowerCase());
			fecha = out.get("s_fecha_nacimiento").toString().toLowerCase();
			// anio = fecha.substring(10, 14);
			System.out.println("anio " + anio);
			mes = fecha.substring(0, 3);
			System.out.println("mes " + mes);
			// dia = fecha.substring(5, 6);
			// System.out.println("dia "+dia);

			if (mes.equalsIgnoreCase("Jan")) {

				mes = "01";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Feb")) {
				mes = "02";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Mar")) {
				mes = "03";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Apr")) {
				mes = "04";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("May")) {
				mes = "05";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jun")) {
				mes = "06";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jul")) {
				mes = "07";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Aug")) {
				mes = "08";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Sep")) {
				mes = "09";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Oct")) {
				mes = "10";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Nov")) {
				mes = "11";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Dec")) {
				mes = "12";
				System.out.println("mes " + mes);
			}

			dia = fecha.substring(4, 6);
			System.out.println("dia " + dia);
			dia = dia.trim();
			if (dia.length() == 1) {
				dia = "0" + dia;
			}
			anio = fecha.substring(7, 11);
			System.out.println("anio " + anio);

			// regCiv.setFechaNacimiento(anio+"-"+mes+"-"+dia+" 12:00AM");
			regCiv.setFechaNacimiento(anio + "-" + mes + "-" + dia);
			// regCiv.setFechaNacimiento(out.get("s_fecha_nacimiento").toString().toLowerCase());
		}

		if (out.get("s_codigo_error") != null) {
			salida.setCodigoError(out.get("s_codigo_error").toString().trim());
		}

		if (out.get("s_msg_err") != null) {
			/*
			 * if (out.get("s_codigo_error") != "0") {
			 * salida.setMensajeSistema(out.get("s_msg_err").toString().trim());
			 * salida.setMensajeUsuario(out.get("s_msg_err").toString().trim()); }else {
			 */
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
			// }
		}

		/*
		 * if (out.get("s_fecha_nacimiento") != null) {
		 * regCiv.setFechaNacimiento(out.get("s_fecha_nacimiento").toString()); }
		 */

		if (out.get("s_sexo") != null) {
			regCiv.setSexo(out.get("s_sexo").toString());
		}

		if (out.get("s_nacionalidad") != null) {
			regCiv.setNacionalidad(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nacionalidad").toString().toLowerCase()));
		}

		if (out.get("s_estado_civil") != null) {
			regCiv.setEstadoCivil(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_estado_civil").toString().toLowerCase()));
		}
		if (out.get("e_identificacion") != null) {
			regCiv.setIdentificacion(mensaje.getIdentificacion());
		}

		if (out.get("s_niv_instruccion") != null) {
			regCiv.setInstruccion(out.get("s_niv_instruccion").toString());
		}

		if (out.get("s_hn_profesion") != null) {
			regCiv.setHomMISProfesion(out.get("s_hn_profesion").toString());
		}

		if (out.get("s_hn_cod_nacionalidad") != null) {
			regCiv.setHomMISNacionalidad(out.get("s_hn_cod_nacionalidad").toString().toLowerCase());
		}

		if (out.get("s_fecha_exped") != null) {

			fecha = out.get("s_fecha_exped").toString().toLowerCase();
			// anio = fecha.substring(10, 14);
			System.out.println("anio " + anio);
			mes = fecha.substring(0, 3);
			System.out.println("mes " + mes);
			// dia = fecha.substring(5, 6);
			// System.out.println("dia "+dia);

			if (mes.equalsIgnoreCase("Jan")) {

				mes = "01";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Feb")) {
				mes = "02";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Mar")) {
				mes = "03";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Apr")) {
				mes = "04";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("May")) {
				mes = "05";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jun")) {
				mes = "06";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jul")) {
				mes = "07";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Aug")) {
				mes = "08";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Sep")) {
				mes = "09";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Oct")) {
				mes = "10";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Nov")) {
				mes = "11";
				System.out.println("mes " + mes);
			} else if (mes.equalsIgnoreCase("Dec")) {
				mes = "12";
				System.out.println("mes " + mes);
			}

			dia = fecha.substring(4, 6);
			System.out.println("dia " + dia);
			dia = dia.trim();
			if (dia.length() == 1) {
				dia = "0" + dia;
			}
			anio = fecha.substring(7, 11);
			System.out.println("anio " + anio);

			// regCiv.setFechaCedulacion(anio+"-"+mes+"-"+dia+" 12:00AM");
			regCiv.setFechaCedulacion(anio + "-" + mes + "-" + dia);

			// regCiv.setFechaCedulacion(out.get("s_fecha_exped").toString());
		}
		/*
		 * System.out.println("salida.getCodigoError()"+ salida.getCodigoError());
		 * response= consultarRegistroCivil(mensaje); if(response!=null) {
		 * if(response.getData()!=null) {
		 * if(response.getData().getJsonResponsePersona()!=null) {
		 * if(response.getData().getJsonResponsePersona().size()>0) { for (int i = 0; i
		 * < response.getData().getJsonResponsePersona().size(); i++) {
		 * 
		 * regCiv.setCodigoDactilar(response.getData().getJsonResponsePersona().get(i).
		 * getCodigoDactilar());
		 * regCiv.setFoto(response.getData().getJsonResponsePersona().get(i).getFoto());
		 * regCiv.setFechaCedulacion(response.getData().getJsonResponsePersona().get(i).
		 * getFechaEmision());
		 * 
		 * }
		 * 
		 * 
		 * if(salida.getCodigoError().equals("0")) {
		 * salida.setMensajeSistema("TRANSACCION EXITOSA");
		 * salida.setMensajeUsuario("TRANSACCION EXITOSA"); }else {
		 * salida.setMensajeSistema(response.getMensajeSistema());
		 * salida.setMensajeUsuario(response.getMensajeUsuario()); }
		 * salida.setCodigoError(response.getCodigoError());
		 * 
		 * }else { if(salida.getCodigoError().equals("0")) {
		 * salida.setMensajeSistema("TRANSACCION EXITOSA");
		 * salida.setMensajeUsuario("TRANSACCION EXITOSA"); }else {
		 * salida.setMensajeSistema(response.getMensajeSistema());
		 * salida.setMensajeUsuario(response.getMensajeUsuario()); }
		 * salida.setCodigoError(response.getCodigoError());
		 * regCiv.setCodigoDactilar(""); regCiv.setFoto(""); } }else {
		 * regCiv.setCodigoDactilar(""); regCiv.setFoto("");
		 * if(salida.getCodigoError().equals("0")) {
		 * salida.setMensajeSistema("TRANSACCION EXITOSA");
		 * salida.setMensajeUsuario("TRANSACCION EXITOSA"); }else {
		 * salida.setMensajeSistema(response.getMensajeSistema());
		 * salida.setMensajeUsuario(response.getMensajeUsuario()); }
		 * salida.setCodigoError(response.getCodigoError()); } } }
		 */

		regCiv.setLocal(true);
		salida.setRegistroCivil(regCiv);
		elapsedTime.stop();
		return salida;

		// }

	}

	public MensajeSalidaValidarClientesInhabilitadosArchivosNegativos validarClientesInhabilitadosArchivosNegativos(
			MensajeEntradaValidarClientesInhabilitadosArchivosNegativos mensaje, Exchange exchange) {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaValidarClientesInhabilitadosArchivosNegativos salida = new MensajeSalidaValidarClientesInhabilitadosArchivosNegativos();
		elapsedTime.start();

		Map<?, ?> out = personapasDAO.validarClientesInhabilitadosArchivosNegativos(mensaje.getIdentificacion(),
				mensaje.getTipoIdentificacion(), mensaje.getCliente(), mensaje.getTipoCuentaPadre(),
				mensaje.getCuentaPadre(), mensaje.getCategoriaCuenta());

		if (out.get("o_error") == null) {
			salida.setCodigoError("");
		} else {
			salida.setCodigoError(out.get("o_error").toString().trim());
		}

		if (out.get("o_msjerror") == null) {
			salida.setMensajeSistema("");
			salida.setMensajeUsuario("");
		} else {
			System.out.println("out.get(\"o_msjerror\")" + out.get("o_msjerror"));
			salida.setMensajeSistema(out.get("o_msjerror").toString().trim());
			salida.setMensajeUsuario(out.get("o_msjerror").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	/*
	 * public ResponseConsultaCedulado
	 * consultarRegistroCivil(MensajeEntradaValidarRegistroCivil entradaParam)
	 * throws Exception { ResponseConsultaCedulado response = null;
	 * 
	 * String userNameService=""; String passwordService="";
	 * 
	 * try {
	 * 
	 * //OBTENER CREDENCIALES SERVICE RCG FrameworkPtt frameworkPtt =
	 * this.config.clienteFramework();
	 * 
	 * com.bolivariano.frameworkseguridadtypes.LoginAplicacionIn request= new
	 * LoginAplicacionIn();
	 * com.bolivariano.frameworkseguridadtypes.LoginAplicacionTypeIn inLogin = new
	 * com.bolivariano.frameworkseguridadtypes.LoginAplicacionTypeIn();
	 * 
	 * inLogin.setIdAplicacion(frameworkApp); inLogin.setUsuario(frameworkusrApl);
	 * 
	 * request.setLoginAplicacionIn(inLogin);
	 * 
	 * LoginAplicacionOut responseFramework = frameworkPtt.loginAplicacion(request);
	 * 
	 * 
	 * if(responseFramework.getLoginAplicacionOut().getCampo().size()>0) {
	 * 
	 * for (int i = 0; i <
	 * responseFramework.getLoginAplicacionOut().getCampo().size(); i++) {
	 * 
	 * if(responseFramework.getLoginAplicacionOut().getCampo().get(i).getNombre().
	 * equals("ACT_server_user_rgcm")) {
	 * userNameService=responseFramework.getLoginAplicacionOut().getCampo().get(i).
	 * getValue(); }
	 * 
	 * if(responseFramework.getLoginAplicacionOut().getCampo().get(i).getNombre().
	 * equals("ACT_server_clave_rgcm")) {
	 * passwordService=responseFramework.getLoginAplicacionOut().getCampo().get(i).
	 * getValue(); }
	 * 
	 * }
	 * 
	 * try {
	 * 
	 * response = new ResponseConsultaCedulado();
	 * 
	 * org.tempuri.IServiceConsultaRCG registrocivil = config.registrocivil();
	 * org.tempuri.ConsultarPersona entradaClienteRegistroCivil = new
	 * ConsultarPersona();
	 * 
	 * //CONSULTAR SERVICIO RCG if(!userNameService.equals("") &&
	 * !passwordService.equals("")) {
	 * entradaClienteRegistroCivil.setCedula(entradaParam.getIdentificacion());
	 * response=registrocivil.consultarPersona(entradaClienteRegistroCivil.getCedula
	 * (),userNameService,passwordService); }
	 * 
	 * } catch (Exception exc) { response = new ResponseConsultaCedulado();
	 * response.setCodigoError("2");
	 * response.setMensajeSistema("Registro Civil se encuentre fuera de servicio.");
	 * response.setMensajeUsuario("Error en el servidor intentelo más tarde.");
	 * //System.out.println("exc.getMessage() consultarRegistroCivil  " +
	 * exc.getMessage()); }
	 * 
	 * }
	 * 
	 * } catch (Exception e) { System.out.println("\033[36m  CATCH RCG " +
	 * e.getMessage()); }
	 * 
	 * return response; }
	 */

	/**
	 * Metodo que se encarga de validar y llenar los datos del cliente del registro
	 * civil. </br>
	 * En caso de no estar activo o falla de conexion del servicio del registro
	 * civil entonces buscara en ambiente local los datos del cliente
	 * 
	 * @apiNote 1.- 2020/08/20 MMARCILY ONBEVOL-68 Validaciones de cedulas de
	 *          menores de edad. </br>
	 * @see #consultarRegistroCivilLocal(MensajeEntradaValidarRegistroCivil,
	 *      Exchange)
	 * @param entradaParam MensajeEntradaValidarRegistroCivil
	 * @param exchange
	 * @return datos del cliente del registro civil o de DB local
	 * @throws Exception
	 * @author LMORA
	 * @since 2019
	 */
	public MensajeSalidaValidarRegistroCivil validarRegistroCivil(MensajeEntradaValidarRegistroCivil entradaParam,
			Exchange exchange) throws Exception {
		JsonResponsePersona jsonResponsePersona = null;
		ResponseConsultaCedulado response = null;
		MensajeSalidaValidarRegistroCivil salidaRCG = null;
		String[] ArrayNombre;
		String nombres = "";

		/* OBTENER CREDENCIALES SERVICE RCG */
		try {

			response = new ResponseConsultaCedulado();
			salidaRCG = new MensajeSalidaValidarRegistroCivil();

			org.tempuri.IServiceConsultaRCG registrocivil = config.registrocivil();
			org.tempuri.ConsultarPersona entradaClienteRegistroCivil = new ConsultarPersona();

			/* CONSULTAR SERVICIO RCG */
			if (!this.serverUserRgcm.equals("") && !this.serverClaveRgcm.equals("")) {

				entradaClienteRegistroCivil.setCedula(entradaParam.getIdentificacion());
				response = registrocivil.consultarPersona(entradaClienteRegistroCivil.getCedula(), this.serverUserRgcm,
						this.serverClaveRgcm);

				com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil regCiv = new com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil();
				jsonResponsePersona = new JsonResponsePersona();

				if (response != null && response.getData() != null
						&& response.getData().getJsonResponsePersona() != null
						&& response.getData().getJsonResponsePersona().size() > 0) {

					jsonResponsePersona = response.getData().getJsonResponsePersona().get(0);

					if (jsonResponsePersona.getCodigoDactilar().equals("")) {
						salidaRCG.setCodigoError("222");
						salidaRCG.setMensajeSistema(
								"Para continuar con tu requerimiento, acércate a cualquiera de nuestras agencias.");
						salidaRCG.setMensajeUsuario(
								"Para continuar con tu requerimiento, acércate a cualquiera de nuestras agencias.");
						return salidaRCG;
					}

					if (!jsonResponsePersona.getCodigoDactilar().equals(entradaParam.getCodigoDactilar())) {
						salidaRCG.setCodigoError("111");
						salidaRCG.setMensajeSistema(
								"Tu código dactilar está incorrecto, por favor ingrésalo nuevamente.");
						salidaRCG.setMensajeUsuario(
								"Tu código dactilar está incorrecto, por favor ingrésalo nuevamente.");
						return salidaRCG;
					}

					regCiv.setCodigoDactilar(jsonResponsePersona.getCodigoDactilar());
					regCiv.setNombre(jsonResponsePersona.getNombresYApellidos());

					if (!regCiv.getNombre().trim().equals("")) {

						ArrayNombre = regCiv.getNombre().split(" ");
						if (ArrayNombre.length > 4) {
							for (int j = 0; j < ArrayNombre.length; j++) {
								regCiv.setApellidos(ArrayNombre[0] + " " + ArrayNombre[1]);
								if (j != 0 && j != 1)
									nombres += ArrayNombre[j] + " ";
								regCiv.setNombres(nombres);
							}
						} else {
							if (ArrayNombre.length == 4) {
								regCiv.setNombres(ArrayNombre[2] + " " + ArrayNombre[3]);
								regCiv.setApellidos(ArrayNombre[0] + " " + ArrayNombre[1]);
							}
							if (ArrayNombre.length == 3) {
								regCiv.setNombres(ArrayNombre[2]);
								regCiv.setApellidos(ArrayNombre[0] + " " + ArrayNombre[1]);
							}
							if (ArrayNombre.length == 2) {
								regCiv.setNombres(ArrayNombre[1]);
								regCiv.setApellidos(ArrayNombre[0]);
							}
							if (ArrayNombre.length == 1) {
								regCiv.setNombres(ArrayNombre[0]);
							}
						}

					}

					regCiv.setFechaNacimiento(jsonResponsePersona.getFechaNacimiento());
					regCiv.setSexo(jsonResponsePersona.getSexo());

					if (jsonResponsePersona.getSexo().equals("MUJER")) {
						regCiv.setSexo("F");
					} else {
						if (jsonResponsePersona.getSexo().equals("HOMBRE")) {
							regCiv.setSexo("M");
						}
					}

					regCiv.setNacionalidad(jsonResponsePersona.getNacionalidad());
					regCiv.setEstadoCivil(jsonResponsePersona.getEstadoCivil());
					regCiv.setIdentificacion(jsonResponsePersona.getCedulaCompleta());
					regCiv.setInstruccion(jsonResponsePersona.getInstruccion());
					regCiv.setNacionalidad(jsonResponsePersona.getNacionalidad());
					regCiv.setConyuge(jsonResponsePersona.getNombreConyugue());
					regCiv.setProfesion(jsonResponsePersona.getProfesionOcupacion());
					regCiv.setFechaFallecimiento(jsonResponsePersona.getFallecimiento());
					regCiv.setFechaCedulacion(jsonResponsePersona.getFechaEmision());
					regCiv.setDomicilio(jsonResponsePersona.getDomicilio());

					regCiv.setFoto(jsonResponsePersona.getFoto());
					System.out.println("getNacionalidad " + jsonResponsePersona.getNacionalidad());
					System.out.println("getProfesionOcupacion " + jsonResponsePersona.getProfesionOcupacion());

					HomologacionRegistroCivil homRegCivMun = new HomologacionRegistroCivil();
					homRegCivMun = this.obtenerCatalogoHomologacionRC(jsonResponsePersona.getSexo(),
							jsonResponsePersona.getEstadoCivil(), jsonResponsePersona.getNacionalidad(),
							jsonResponsePersona.getProfesionOcupacion());

					System.out.println("homRegCivMun.getCodHomNacionalidad() " + homRegCivMun.getCodHomNacionalidad());
					System.out.println("homRegCivMun.getCodHomProfesion() " + homRegCivMun.getCodHomProfesion());
					regCiv.setHomMISNacionalidad(homRegCivMun.getCodHomNacionalidad());
					regCiv.setHomMISProfesion(homRegCivMun.getCodHomProfesion());
					System.out.println("homRegCivMun.getNombreProfesion() " + homRegCivMun.getNombreProfesion());
					regCiv.setNombreProfesion(homRegCivMun.getNombreProfesion());

					regCiv.setNumeroCasa("");

					logger.debug(
							"regCiv.getNacionalidad().substring(0, 5)-" + regCiv.getNacionalidad().substring(0, 5));

					if (regCiv.getNacionalidad().substring(0, 5).toUpperCase().equals("ECUAT")) {
						regCiv.setHomMISNacionalidad("1");
						logger.info("Ha colocado nacionalidad");
					}

					salidaRCG.setCodigoError(response.getCodigoError());
					salidaRCG.setMensajeSistema(response.getMensajeSistema());
					salidaRCG.setMensajeUsuario(response.getMensajeUsuario());

					regCiv.setLocal(false);

					// ref. 1 - INICIO
					regCiv.setClaseCedula((jsonResponsePersona.getClaseCedula() != null
							&& !jsonResponsePersona.getClaseCedula().isEmpty())
									? jsonResponsePersona.getClaseCedula().trim().toUpperCase()
									: "");
					logger.debug("regCiv.getClaseCedula() " + regCiv.getClaseCedula());
					// ref. 1 - FIN

					salidaRCG.setRegistroCivil(regCiv);
					return salidaRCG;

				} else
					return consultarRegistroCivilLocal(entradaParam, null);
			} else
				return consultarRegistroCivilLocal(entradaParam, null);

		} catch (Exception exc) {
			response = new ResponseConsultaCedulado();
			System.out.println("exc " + exc.getMessage());
			response.setCodigoError("2");
			response.setMensajeSistema("Registro Civil se encuentre fuera de servicio.");
			response.setMensajeUsuario("Error en el servidor intentelo más tarde.");
			return consultarRegistroCivilLocal(entradaParam, null);
		}

	}

	/**
	 * Metodo que obtiene los datos del cliente si el servicio de registro civil no
	 * responde
	 * 
	 * @apiNote 1.- 2020/08/25 MMARCILY ONBEVOL-68 Validaciones de cedulas de
	 *          menores de edad. </br>
	 * @param mensaje  MensajeEntradaValidarRegistroCivil
	 * @param exchange
	 * @return datos del cliente de DB local
	 * @throws Exception
	 * @author DGARAICOA
	 * @since 2019
	 */
	public MensajeSalidaValidarRegistroCivil consultarRegistroCivilLocal(MensajeEntradaValidarRegistroCivil mensaje,
			Exchange exchange) throws Exception {

		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaValidarRegistroCivil salida = new MensajeSalidaValidarRegistroCivil();
		elapsedTime.start();
		Util utilitario = new Util();
		com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil regCiv = new com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil();

		Map<?, ?> out = personapasDAO.buscarRegistroCivilLocalSybase(mensaje.getIdentificacion());

		String codigoError = out.get("s_codigo_error").toString().trim();
		salida.setCodigoError(out.get("s_codigo_error").toString().trim());
		ResponseConsultaCedulado response = new ResponseConsultaCedulado();

		regCiv.setNombre("");
		regCiv.setNombres("");
		regCiv.setApellidos("");

		if (out.get("s_nombre") != null)
			regCiv.setNombre(utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nombre").toString().toLowerCase()));

		if (out.get("s_nombres") != null)
			regCiv.setNombres(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nombres").toString().toLowerCase()));

		if (out.get("s_apellidos") != null)
			regCiv.setApellidos(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_apellidos").toString().toLowerCase()));

		Date date = new Date();
		XMLGregorianCalendar xmlDate = null;
		GregorianCalendar gc = new GregorianCalendar();

		gc.setTime(date);

		try {
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		regCiv.setFechaNacimiento("");
		salida.setCodigoError("");
		salida.setMensajeSistema("");
		salida.setMensajeUsuario("");
		regCiv.setFechaNacimiento("");
		regCiv.setSexo("");
		regCiv.setNacionalidad("");
		regCiv.setEstadoCivil("");
		regCiv.setIdentificacion("");
		regCiv.setInstruccion("");
		regCiv.setHomMISNacionalidad("");
		regCiv.setHomMISProfesion("");
		regCiv.setFechaCedulacion("");
		regCiv.setClaseCedula("");

		String anio = "";
		String mes = "";
		String dia = "";
		String fecha = "";
		if (out.get("s_fecha_nacimiento") != null) {

			fecha = out.get("s_fecha_nacimiento").toString().toLowerCase();
			logger.debug("anio " + anio);
			mes = fecha.substring(0, 3);
			logger.debug("mes " + mes);

			if (mes.equalsIgnoreCase("Jan")) {

				mes = "01";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Feb")) {
				mes = "02";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Mar")) {
				mes = "03";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Apr")) {
				mes = "04";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("May")) {
				mes = "05";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jun")) {
				mes = "06";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jul")) {
				mes = "07";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Aug")) {
				mes = "08";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Sep")) {
				mes = "09";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Oct")) {
				mes = "10";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Nov")) {
				mes = "11";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Dec")) {
				mes = "12";
				logger.debug("mes " + mes);
			}

			dia = fecha.substring(4, 6);
			logger.debug("dia " + dia);
			dia = dia.trim();
			if (dia.length() == 1) {
				dia = "0" + dia;
			}
			anio = fecha.substring(7, 11);
			logger.debug("anio " + anio);

			regCiv.setFechaNacimiento(anio + "-" + mes + "-" + dia);
		}

		if (out.get("s_codigo_error") != null)
			salida.setCodigoError(out.get("s_codigo_error").toString().trim());

		if (out.get("s_msg_err") != null) {
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
		}

		if (out.get("s_sexo") != null)
			regCiv.setSexo(out.get("s_sexo").toString());

		if (out.get("s_nacionalidad") != null)
			regCiv.setNacionalidad(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nacionalidad").toString().toLowerCase()));

		if (out.get("s_estado_civil") != null)
			regCiv.setEstadoCivil(
					utilitario.convertirCadaPalabraEnMayuscula(out.get("s_estado_civil").toString().toLowerCase()));

		if (out.get("e_identificacion") != null)
			regCiv.setIdentificacion(mensaje.getIdentificacion());

		if (out.get("s_niv_instruccion") != null)
			regCiv.setInstruccion(out.get("s_niv_instruccion").toString());

		if (out.get("s_hn_profesion") != null)
			regCiv.setHomMISProfesion(out.get("s_hn_profesion").toString());

		if (out.get("s_hn_cod_nacionalidad") != null)
			regCiv.setHomMISNacionalidad(out.get("s_hn_cod_nacionalidad").toString().toLowerCase());

		if (out.get("s_fecha_exped") != null) {

			fecha = out.get("s_fecha_exped").toString().toLowerCase();

			logger.debug("anio " + anio);
			mes = fecha.substring(0, 3);
			logger.debug("mes " + mes);

			if (mes.equalsIgnoreCase("Jan")) {

				mes = "01";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Feb")) {
				mes = "02";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Mar")) {
				mes = "03";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Apr")) {
				mes = "04";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("May")) {
				mes = "05";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jun")) {
				mes = "06";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Jul")) {
				mes = "07";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Aug")) {
				mes = "08";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Sep")) {
				mes = "09";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Oct")) {
				mes = "10";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Nov")) {
				mes = "11";
				logger.debug("mes " + mes);
			} else if (mes.equalsIgnoreCase("Dec")) {
				mes = "12";
				logger.debug("mes " + mes);
			}

			dia = fecha.substring(4, 6);
			logger.debug("dia " + dia);
			dia = dia.trim();
			if (dia.length() == 1) {
				dia = "0" + dia;
			}
			anio = fecha.substring(7, 11);
			logger.debug("anio " + anio);

			regCiv.setFechaCedulacion(anio + "-" + mes + "-" + dia);
		}

		// ref. 1 - INICIO
		if (out.get("s_ciudadania") != null)
			regCiv.setClaseCedula(out.get("s_ciudadania").toString().toUpperCase());
		// ref. 1 - FIN
		regCiv.setLocal(true);
		salida.setRegistroCivil(regCiv);
		elapsedTime.stop();
		return salida;
	}

	public MensajeSalidaValidarRegistroCivil consultarRegistroCivilLocal2(MensajeEntradaValidarRegistroCivil mensaje,
			String codigoErrorRCG, Exchange exchange) throws Exception {

		StopWatch elapsedTime = new StopWatch();
		System.out.println("CONSULTA ---- -----BASE LOCAL" + codigoErrorRCG);
		MensajeSalidaValidarRegistroCivil salida = new MensajeSalidaValidarRegistroCivil();
		elapsedTime.start();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		Util utilitario = new Util();

		com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil regCiv = new com.bolivariano.pasivos.mensajepersonapas.v1.MensajeSalidaValidarRegistroCivil.RegistroCivil();

		Map<?, ?> out = personapasDAO.buscarRegistroCivilLocalSybase(mensaje.getIdentificacion());

		String codigoError = out.get("s_codigo_error").toString().trim();

		ResponseConsultaCedulado response = new ResponseConsultaCedulado();

		if (codigoError != null && !codigoError.equals("0")) {

			mensaje.setLocal(false);
			regCiv.setNombre("");
			regCiv.setNombres("");
			regCiv.setApellidos("");
			System.out.println("NO ENCONTRADO LOCAL");
			salida.setCodigoError(codigoErrorRCG);
			salida.setMensajeSistema("CEDULA NO EXISTE");
			salida.setMensajeUsuario("CEDULA NO EXISTE");

			return salida;

		} else {

			regCiv.setNombre("");
			regCiv.setNombres("");
			regCiv.setApellidos("");

			if (out.get("s_nombre") != null) {
				regCiv.setNombre(
						utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nombre").toString().toLowerCase()));
			}

			if (out.get("s_nombres") != null) {
				regCiv.setNombres(
						utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nombres").toString().toLowerCase()));
			}

			if (out.get("s_apellidos") != null) {
				regCiv.setApellidos(
						utilitario.convertirCadaPalabraEnMayuscula(out.get("s_apellidos").toString().toLowerCase()));
			}

			Date date = new Date();
			XMLGregorianCalendar xmlDate = null;
			GregorianCalendar gc = new GregorianCalendar();

			gc.setTime(date);

			try {
				xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			regCiv.setFechaNacimiento("");
			salida.setCodigoError("");
			salida.setMensajeSistema("");
			salida.setMensajeUsuario("");
			regCiv.setFechaNacimiento("");
			regCiv.setSexo("");
			regCiv.setNacionalidad("");
			regCiv.setEstadoCivil("");
			regCiv.setIdentificacion("");
			regCiv.setInstruccion("");
			regCiv.setHomMISNacionalidad("");
			regCiv.setHomMISProfesion("");
			regCiv.setFechaCedulacion("");

			if (out.get("s_fecha_nacimiento") != null) {
				String FormatFecha = out.get("s_fecha_nacimiento").toString().toLowerCase();
				FormatFecha = FormatFecha.replace(" 12:00am", "");
				FormatFecha = FormatFecha.replace(" 12:00pm", "");
				regCiv.setFechaNacimiento(DATE_FORMAT.format(new Date(FormatFecha)));
			}

			if (out.get("s_codigo_error") != null) {
				salida.setCodigoError(out.get("s_codigo_error").toString().trim());
			}

			if (out.get("s_msg_err") != null) {
				salida.setMensajeSistema(out.get("s_msg_err").toString().trim());
				salida.setMensajeUsuario(out.get("s_msg_err").toString().trim());
			}

			if (out.get("s_fecha_nacimiento") != null) {

				String FormatFecha = out.get("s_fecha_nacimiento").toString().toLowerCase();
				FormatFecha = FormatFecha.replace(" 12:00am", "");
				FormatFecha = FormatFecha.replace(" 12:00pm", "");
				regCiv.setFechaNacimiento(DATE_FORMAT.format(new Date(FormatFecha)));

				// regCiv.setFechaNacimiento(out.get("s_fecha_nacimiento").toString());
			}

			if (out.get("s_sexo") != null) {
				regCiv.setSexo(out.get("s_sexo").toString());
			}

			if (out.get("s_nacionalidad") != null) {
				regCiv.setNacionalidad(
						utilitario.convertirCadaPalabraEnMayuscula(out.get("s_nacionalidad").toString().toLowerCase()));
			}

			if (out.get("s_estado_civil") != null) {
				regCiv.setEstadoCivil(
						utilitario.convertirCadaPalabraEnMayuscula(out.get("s_estado_civil").toString().toLowerCase()));
			}
			// if (out.get("e_identificacion") != null) {
			regCiv.setIdentificacion(mensaje.getIdentificacion());
			// }

			if (out.get("s_niv_instruccion") != null) {
				regCiv.setInstruccion(out.get("s_niv_instruccion").toString());
			}

			if (out.get("s_hn_profesion") != null) {
				regCiv.setHomMISProfesion(out.get("s_hn_profesion").toString());
			}

			if (out.get("s_hn_cod_nacionalidad") != null) {
				regCiv.setHomMISNacionalidad(out.get("s_hn_cod_nacionalidad").toString().toLowerCase());
			}

			regCiv.setNombreProfesion("");

			if (out.get("s_fecha_exped") != null) {
				String FormatFecha = out.get("s_fecha_exped").toString();
				FormatFecha = FormatFecha.replace(" 12:00AM", "");
				FormatFecha = FormatFecha.replace(" 12:00PM", "");
				regCiv.setFechaCedulacion(DATE_FORMAT.format(new Date(FormatFecha)));
			}

			regCiv.setCodigoDactilar("");
			regCiv.setFoto("");

			regCiv.setLocal(true);
			salida.setCodigoError(codigoErrorRCG);
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
			salida.setRegistroCivil(regCiv);
			elapsedTime.stop();
			return salida;

		}

	}

	public MensajeSalidaOtenerCatalogoHomologacionRC otenerCatalogoHomologacionRC(
			MensajeEntradaOtenerCatalogoHomologacionRC mensaje, Exchange exchange) throws CustomException, Exception {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaOtenerCatalogoHomologacionRC salida = new MensajeSalidaOtenerCatalogoHomologacionRC();
		elapsedTime.start();
		System.out.println("mensaje.getCodCatalogo(): " + mensaje.getCodCatalogo());
		System.out.println("mensaje.getDescriOrigen(): " + mensaje.getDescriOrigen());
		Map<?, ?> out = personapasDAO.obtenerCatalogoHomologacion(mensaje.getCodCatalogo(), mensaje.getDescriOrigen());

		// System.out.println("out.get(\"codigo_hm\").toString()"+out.get("codigo_hm").toString());
		if (out.get("codigo_hm") == null) {
			salida.setCodigo("");
		} else {
			salida.setCodigo(out.get("codigo_hm").toString());
		}

		if (out.get("descri_hm") == null) {
			salida.setDescripcion("");
		} else {
			salida.setDescripcion(out.get("descri_hm").toString());
		}

		if (out.get("codigo_error") == null) {
			salida.setCodigoError("");
		} else {
			salida.setCodigoError(out.get("codigo_error").toString().trim());
		}

		if (out.get("msg_err") == null) {
			salida.setMensajeSistema("");
			salida.setMensajeUsuario("");
		} else {
			System.out.println("out.get(\"msg_err\")" + out.get("msg_err"));
			salida.setMensajeSistema(out.get("msg_err").toString().trim());
			salida.setMensajeUsuario(out.get("msg_err").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	public HomologacionRegistroCivil obtenerCatalogoHomologacionRC(String genero, String estadoCivil,
			String nacionalidad, String profesion) throws CustomException, Exception {
		StopWatch elapsedTime = new StopWatch();
		HomologacionRegistroCivil salida = new HomologacionRegistroCivil();
		elapsedTime.start();

		Map<?, ?> out = personapasDAO.obtenerCatalogoHomologacion2(genero, estadoCivil, nacionalidad, profesion);

		if (out.get("s_cod_hm_genero") == null) {
			salida.setCodHomGenero("");
		} else {
			salida.setCodHomGenero(out.get("s_cod_hm_genero").toString());
		}

		// System.out.println("out.get(\"codigo_hm\").toString()"+out.get("codigo_hm").toString());
		if (out.get("s_cod_hm_est_civil") == null) {
			salida.setCodHomEstadoCivil("");
		} else {
			salida.setCodHomEstadoCivil(out.get("s_cod_hm_est_civil").toString());
		}

		if (out.get("s_cod_hm_nacionalidad") == null) {
			salida.setCodHomNacionalidad("");
		} else {
			salida.setCodHomNacionalidad(out.get("s_cod_hm_nacionalidad").toString());
		}

		System.out.println("s_cod_hm_profesion " + out.get("s_cod_hm_profesion"));

		if (out.get("s_cod_hm_profesion") == null) {
			salida.setCodHomProfesion("999");
			salida.setNombreProfesion("OTRAS");
			System.out.println("Va a setear 999 en PROFESION");
		} else {
			salida.setCodHomProfesion(out.get("s_cod_hm_profesion").toString());
			salida.setNombreProfesion(out.get("s_nom_hm_profesion").toString());
			System.out.println("Va a setear ALGO en PROFESION");
		}

		if (out.get("codigo_error") == null) {
			salida.setCodigoError("");
		} else {
			salida.setCodigoError(out.get("codigo_error").toString().trim());
		}

		if (out.get("msg_err") == null) {
			salida.setMensajeSistema("");
			salida.setMensajeUsuario("");
		} else {
			System.out.println("out.get(\"msg_err\")" + out.get("msg_err"));
			salida.setMensajeSistema(out.get("msg_err").toString().trim());
			salida.setMensajeUsuario(out.get("msg_err").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	/**
	 * Metodo que se encarga de validar si existe medios (telefono y correo) creados
	 * en DB. </br>
	 * 
	 * @apiNote 1.- 2020/08/18 ONBEVOL-8 Validación de Medios de Envío contra el
	 *          MIS. </br>
	 * @param entradaParam MensajeEntradaValidarRegistroCivil
	 * @param exchange
	 * @return existeCorreo: S - si existe correo / N - no existe correo </br>
	 *         existeTelefono: S - si existe telefono / N - no existe telefono
	 * @throws Exception
	 * @author mmarcily
	 */
	public MensajeSalidaValidarExistenciaMedios validarExistenciaMedios(MensajeEntradaValidarExistenciaMedios mensaje,
			Exchange exchange) {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaValidarExistenciaMedios salida = new MensajeSalidaValidarExistenciaMedios();
		elapsedTime.start();

		Map<?, ?> out = personapasDAO.validarExistenciaMedios(mensaje.getCorreo(), mensaje.getTelefono());

		if (out.get("s_existe_correo") != null) {
			salida.setExisteCorreo(out.get("s_existe_correo").toString().trim().toUpperCase());
		}

		if (out.get("s_existe_telefono") != null) {
			salida.setExisteTelefono(out.get("s_existe_telefono").toString().trim().toUpperCase());
		}

		if (out.get("s_error") == null) {
			salida.setCodigoError("0");
		} else {
			salida.setCodigoError(out.get("s_cod_error").toString().trim());
		}

		if (out.get("s_msg_err") == null) {
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
		} else {
			salida.setMensajeSistema(out.get("s_msj_error").toString().trim());
			salida.setMensajeUsuario(out.get("s_msj_error").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	public MensajeSalidaValidarClienteImpedimento validarClienteImpedimento(
			MensajeEntradaValidarClienteImpedimento mensaje, Exchange exchange) {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaValidarClienteImpedimento salida = new MensajeSalidaValidarClienteImpedimento();
		elapsedTime.start();

		Map<?, ?> out = personapasDAO.validarClienteImpedimento(mensaje.getEnte());

		System.out.println(out.toString());
		if (out.get("s_cod_error") == null) {
			salida.setCodigoError("0");
		} else {
			salida.setCodigoError(out.get("s_cod_error").toString().trim());
		}

		if (out.get("s_msj_error") == null) {
			salida.setImpedimento("N");
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
		} else {
			salida.setImpedimento("S");
			salida.setMensajeSistema(out.get("s_msj_error").toString().trim());
			salida.setMensajeUsuario(out.get("s_msj_error").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

	/**
	 * Metodo que se encarga de obtener informacion inicial del cliente en las
	 * tablas del mis. </br>
	 * 
	 * @apiNote 1.- LMORA INICIAL. </br>
	 *          2.- 2020/10/16 MMARCILY ONBEVOL-332 Apertura de Cuenta: Datos
	 *          Laborables Independiente. </br>
	 * @param entradaParam MensajeSalidaObtenerInformacionCliente
	 * @param exchange
	 * @return lista de direcciones, lista de direcciones_email, lista de
	 *         referencias laborales
	 * @throws Exception
	 * @author LMORA
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MensajeSalidaObtenerInformacionCliente obtenerInformacioCliente(
			MensajeEntradaObtenerInformacionCliente request) {

		MensajeSalidaObtenerInformacionCliente response = new MensajeSalidaObtenerInformacionCliente();
		MensajeSalidaObtenerInformacionCliente.Cliente cliente = new Cliente();

		Map<?, ?> datosClienteMap = personapasDAO.obtenerInformacionCliente(request);

		if (datosClienteMap == null) {
			response.setCodigoError("9999");
			response.setMensajeUsuario("Ha surgido un problema intentelo más tarde");
			response.setMensajeSistema("Ha surgido un problema intentelo más tarde");
		}

		// List<DatosCliente> clienteList = (List) datosClienteMap.get("result");
		List<DatosCliente> datosEmailList = (List) datosClienteMap.get("result");// #result-set-2
		// List<DatosCliente> lstRefSec = (List) datosClienteMap.get("#result-set-3");

		/*
		 * if (clienteList != null && clienteList.size() > 0) {
		 * 
		 * clienteList.stream().forEach(datosClientebean -> {
		 * 
		 * MensajeSalidaObtenerInformacionCliente.Cliente.DatosDirecciones datosCliente
		 * = new DatosDirecciones();
		 * 
		 * datosCliente.setDireccion(datosClientebean.getDireccion());
		 * datosCliente.setFechaNacimiento(datosClientebean.getFechaNacimiento());
		 * datosCliente.setSecuenciaDireccion(datosClientebean.getSecuenciaDireccion());
		 * datosCliente.setTipoDireccion(datosClientebean.getTipoDireccion());
		 * datosCliente.setTipoNacionalidad(datosClientebean.getTipoNacionalidad());
		 * datosCliente.setEnvioCorrespondencia(datosClientebean.getEnvioCorrespondencia
		 * ()); datosCliente.setCanton(datosClientebean.getCanton());
		 * datosCliente.setCiudad(datosClientebean.getCiudad());
		 * datosCliente.setParroquia(datosClientebean.getParroquia());
		 * 
		 * cliente.getDatosDirecciones().add(datosCliente); }); }
		 */

		if (datosEmailList != null && datosEmailList.size() > 0) {

			datosEmailList.stream().forEach(datosClienteEmail -> {
				MensajeSalidaObtenerInformacionCliente.Cliente.DatosMedios datosClienteMail = new DatosMedios();
				datosClienteMail.setMedio(datosClienteEmail.getMedio());
				datosClienteMail.setSecuenciaMedio(datosClienteEmail.getSecuenciaMedio());
				datosClienteMail.setTipoMedio(datosClienteEmail.getTipoMedio());

				cliente.getDatosMedios().add(datosClienteMail);
			});
		}

		/*
		 * if (lstRefSec != null && lstRefSec.size() > 0) {
		 * 
		 * lstRefSec.stream().forEach(datosRefSec -> {
		 * MensajeSalidaObtenerInformacionCliente.Cliente.DatosRefLaboral
		 * datosRefLaboral = new DatosRefLaboral();
		 * datosRefLaboral.setCodigoTrabajoCobis(datosRefSec.getCodigoTrabajoCobis());
		 * datosRefLaboral.setSecuenciaTrabajo(datosRefSec.getSecuenciaTrabajo());
		 * datosRefLaboral.setNombreEmpresa(datosRefSec.getNombreEmpresa());
		 * 
		 * cliente.getDatosRefLaboral().add(datosRefLaboral); }); }
		 */

		response.setCliente(cliente);
		response.setCodigoError("0");
		response.setMensajeUsuario("TRANSACCION EXITOSA");
		response.setMensajeSistema("TRANSACCION EXITOSA");

		return response;
	}

	public MensajeSalidaVerificarAviso24Activo verificarAviso24Activo(MensajeEntradaVerificarAviso24Activo request) {

		MensajeSalidaObtenerPaquetesMensajeriaActivos responseAviso24 = new MensajeSalidaObtenerPaquetesMensajeriaActivos();

		MensajeEntradaObtenerPaquetesMensajeriaActivos requestAviso24 = new MensajeEntradaObtenerPaquetesMensajeriaActivos();

		MensajeSalidaVerificarAviso24Activo response = new MensajeSalidaVerificarAviso24Activo();
		

		try {

			Date fecha = null;
			GregorianCalendar cal = new GregorianCalendar();
			XMLGregorianCalendar fechaGre = null;

			fecha = new Date();
			cal.setTime(fecha);

			fechaGre = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

			requestAviso24.setIdentificacion(request.getIdentificacion());
			requestAviso24
					.setTipoIdentificacion(request.getTipoIdentificacion().equals("C") ? TipoIdentificacionPersona.C
							: TipoIdentificacionPersona.P);

			requestAviso24.setFecha(fechaGre);
			requestAviso24.setOficina(0);
			requestAviso24.setIdFuncionalidad(new BigInteger("2087"));
			requestAviso24.setTransaccion(45222);
			requestAviso24.setCanal(request.getCanal());
			requestAviso24.setUsuario("onboarding");

			responseAviso24 = this.config.clienteAvisos24().obtenerPaquetesMensajeriaActiva(requestAviso24);

			response.setTieneAvisos24(false);

			if (responseAviso24.getCodigoError() == null) {

				response.setCodigoError("9999");
				response.setMensajeUsuario("Ha surgido un problema intentelo más tarde.");
				response.setMensajeSistema(responseAviso24.getMensajeSistema());

				return response;
			}

			if (!responseAviso24.getCodigoError().equals("0")) {

				if (responseAviso24.getCodigoError().equals("22525656")) {

					response.setTieneAvisos24(false);
					response.setCodigoError("0");
					response.setMensajeUsuario("TRANSACCION EXITOSA");
					response.setMensajeSistema(responseAviso24.getMensajeSistema());

				} else {
					response.setCodigoError("9999");
					response.setMensajeUsuario("Ha surgido un problema intentelo más tarde.");
					response.setMensajeSistema(responseAviso24.getMensajeSistema());
				}

				return response;
			}

			response.setCodigoError(responseAviso24.getCodigoError());
			response.setMensajeUsuario(responseAviso24.getMensajeUsuario());
			response.setMensajeSistema(responseAviso24.getMensajeSistema());

			if (responseAviso24.getServicios() != null) {
				if (responseAviso24.getEstado().equals("ACTIVO") || responseAviso24.getEstado().equals("INACTIVO"))
					response.setTieneAvisos24(true);
			}

		} catch (Exception error) {
			// TODO Auto-generated catch block
			response.setCodigoError("9999");
			response.setMensajeUsuario("Ha surgido un problema intentelo más tarde.");
			response.setMensajeSistema(error.getMessage() + "");
			error.printStackTrace();
		}

		return response;
	}
	
	/**
	 * ENCERAR DATOS DE CLIENTE EN EL MIS. </br>
	 * 
	 * @apiNote 1.- 2020/11/23 MMARCILY ONBEVOL-425 Apertura de Cuenta: Encerar el
	 *          Perfil Transaccional. </br>
	 * @param mensaje
	 * @param exchange
	 * @return 
	 * @throws Exception
	 * @author mmarcily
	 */
	public MensajeSalidaEncerarDatosClientesMis encerarDatosClientesMis(MensajeEntradaEncerarDatosClientesMis mensaje,
			Exchange exchange) {
		StopWatch elapsedTime = new StopWatch();
		MensajeSalidaEncerarDatosClientesMis salida = new MensajeSalidaEncerarDatosClientesMis();
		elapsedTime.start();
	
		Map<String, Object> mapDataExtra = new HashMap<String, Object>();
		mapDataExtra.put("trn1", this.trn1);
		mapDataExtra.put("trn2", this.trn2);
		mapDataExtra.put("trn3", this.trn3);
		mapDataExtra.put("trn4", this.trn4);
		mapDataExtra.put("trn5", this.trn5);
		mapDataExtra.put("trn6", this.trn6);
		mapDataExtra.put("srv", this.srv);
		mapDataExtra.put("lsrv", this.lsrv);
		mapDataExtra.put("ente", mensaje.getEnte());
		mapDataExtra.put("terminal", mensaje.getTerminal());

		Map<?, ?> out = personapasDAO.encerarDatosClientesMis(mapDataExtra);

		if (out.get("s_cod_error") == null) {
			salida.setCodigoError("0");
		} else {
			salida.setCodigoError(out.get("s_cod_error").toString().trim());
		}

		if (out.get("s_msj_error") == null) {
			salida.setMensajeSistema("TRANSACCION EXITOSA");
			salida.setMensajeUsuario("TRANSACCION EXITOSA");
		} else {
			salida.setMensajeSistema(out.get("s_msj_error").toString().trim());
			salida.setMensajeUsuario(out.get("s_msj_error").toString().trim());
		}
		elapsedTime.stop();
		return salida;
	}

}
