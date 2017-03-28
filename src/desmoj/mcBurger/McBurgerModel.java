package desmoj.mcBurger;

import java.util.Map;
import java.util.TreeMap;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Histogram;
import desmoj.core.statistic.TimeSeries;
import desmoj.core.util.AccessPoint;
import desmoj.core.util.Parameterizable;
import desmoj.extensions.experimentation.reflect.MutableFieldAccessPoint;

public class McBurgerModel extends Model implements Parameterizable{

	
	private ContDistExponential tiempoLlegadaCliente;
	private ContDistExponential tiempoServicio;
	private ContDistExponential tiempoCocina;
	private ContDistExponential tiempoPago;
	protected ProcessQueue<Cliente> colaClientes;
	protected ProcessQueue<Dependiente> colaDependientesOciosos;
	protected ProcessQueue<Cocinero> colaCocinerosOciosos;
	protected ProcessQueue<Dependiente> colaPedidosPendientes;
	private Integer numeroDependientes;
	private Integer numeroCocineros;
	protected Integer numeroClientesLlegados = 0;
	protected Integer numeroClientesAtendidos = 0;
	protected TimeSeries clientesLlegados;
	protected TimeSeries clientesAtendidos;
	protected Histogram waitTimeHistogram;
	
	public McBurgerModel(Model owner,
			String modelName,
			boolean showInReport,
			boolean showInTrace) {
			super(owner, modelName, showInReport, showInTrace);
			numeroDependientes = 4;
			numeroCocineros = 2;
	}
	
	public McBurgerModel(){
		this(null, "McBurger Model", true, true);
	}


	@Override
	public String description() {
		return "Describe lo que hace el modelo";
	}

	@Override
	public void doInitialSchedules() {
		
		for(int i = 0; i < numeroDependientes; i++){
			Dependiente dependiente = new Dependiente(this, "Dependiente", true);
			dependiente.activate();
		}
		
		for(int i = 0; i < numeroCocineros; i++){
			Cocinero cocinero = new Cocinero(this, "Cocinero", true);
			cocinero.activate();
		}
		
		ClienteGenerator primeraLlegada =
				new ClienteGenerator(this, "Llegada nuevo cliente", false);
		primeraLlegada.schedule(new TimeSpan(getClienteTiempoLlegada()));
		
	}
	
	public double getTiempoServicio(){
		return tiempoServicio.sample();
	}
	
	public double getTiempoCocina(){
		return tiempoCocina.sample();
	}
	
	public double getClienteTiempoLlegada(){
		return tiempoLlegadaCliente.sample();
	}
	
	public double getClienteTiempoPago(){
		return tiempoPago.sample();
	}
	

	@Override
	public void init() {
		// dater collectors
        clientesLlegados = new TimeSeries(this, "Llega cliente", new TimeInstant(0), new TimeInstant(1500), true, false);
        clientesAtendidos = new TimeSeries(this, "Termina cliente", new TimeInstant(0), new TimeInstant(1500), true, false);
        waitTimeHistogram = new Histogram(this, "Tiempo espera cliente", 0, 16, 10, true, false);

        // distributions
		tiempoServicio = new ContDistExponential(this, "Tiempo tomar pedido", 4.0, true, false);
		tiempoCocina = new ContDistExponential(this, "Tiempo cocina", 9.0, true, false);
		tiempoLlegadaCliente = new ContDistExponential(this, "Tiempo entrada clientes", 5.0, true, false);
		tiempoPago = new ContDistExponential(this, "Tiempo pago", 2.0, true, false);

		// queues
		colaClientes = new ProcessQueue<Cliente>(this, "Cola clientes", true, false);
		colaDependientesOciosos = new ProcessQueue<Dependiente>(this, "Cola dependientes ociosos", true, false);
		colaCocinerosOciosos = new ProcessQueue<Cocinero>(this, "Cola cocineros ociosos", true, false);
		colaPedidosPendientes = new ProcessQueue<Dependiente>(this, "Cola pedidos pendientes", true, false);
	}
	
	
	@Override
	public Map<String, AccessPoint> createParameters() {
		Map<String, AccessPoint> pm = new TreeMap<String, AccessPoint>();
		pm.put("numeroClientes", new MutableFieldAccessPoint("numeroClientes", this));
		return pm;
	}
	
	public static void main(java.lang.String[] args) {

		// make a new experiment
		// Use as experiment name a OS filename compatible string!!
		// Otherwise your simulation will crash!!
        Experiment.setEpsilon(java.util.concurrent.TimeUnit.SECONDS);
        Experiment.setReferenceUnit(java.util.concurrent.TimeUnit.MINUTES);
        Experiment experiment =
            new Experiment("McBurger Model");

		// make a new model
		// null as first parameter because it is the main model and has no mastermodel
		McBurgerModel vc_1st_p_Model =
			new McBurgerModel(
				null,
				"McBurger Model",
				true,
				false);

		// connect Experiment and Model
		vc_1st_p_Model.connectToExperiment(experiment);

        // set trace
		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(100));

		// now set the time this simulation should stop at 
		// let him work 1500 Minutes
		experiment.stop(new TimeInstant(1500));
		experiment.setShowProgressBar(false);

		// start the Experiment with start time 0.0
		experiment.start();

		// --> now the simulation is running until it reaches its ending criteria
		// ...
		// ...
		// <-- after reaching ending criteria, the main thread returns here

		// print the report about the already existing reporters into the report file
		experiment.report();

		// stop all threads still alive and close all output files
		experiment.finish();
	}

}
