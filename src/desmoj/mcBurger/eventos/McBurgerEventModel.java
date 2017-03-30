package desmoj.mcBurger.eventos;


import java.util.concurrent.TimeUnit;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;


public class McBurgerEventModel extends Model {
	
	private final Integer NUM_DEPENDIENTES = 2;
	private final Integer NUM_COCINEROS = 4;
	private ContDistExponential tiempoLlegadaCliente;
	private ContDistExponential tiempoServicio;
	private ContDistExponential tiempoCocina;
	private ContDistExponential tiempoPago;
	protected Queue<Cliente> colaClientes;
	protected Queue<Dependiente> colaDependientesOciosos;
	protected Queue<Cocinero> colaCocinerosOciosos;
	protected Queue<Dependiente> colaPedidosPendientes;

	public McBurgerEventModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
		super(owner, modelName, showInReport, showInTrace);
	}

	@Override
	public String description() {
		return "Descripcion modelo McBurger";
	}

	@Override
	public void doInitialSchedules() {
		// crea el generador de clientes
		   ClienteGeneratorEvent clienteGenerator =
		            new ClienteGeneratorEvent(this, "Cliente Generator", true);
	   // programa el inicio de la simulacion
	   clienteGenerator.schedule(new TimeSpan(0));
		
	}

	@Override
	public void init() {
		 // distributions
		tiempoServicio = new ContDistExponential(this, "Tiempo tomar pedido", 4.0, true, false);
		tiempoCocina = new ContDistExponential(this, "Tiempo cocina", 9.0, true, false);
		tiempoLlegadaCliente = new ContDistExponential(this, "Tiempo entrada clientes", 5.0, true, false);
		tiempoPago = new ContDistExponential(this, "Tiempo pago", 2.0, true, false);

		tiempoLlegadaCliente.setNonNegative(true);
		
		// queues
		colaClientes = new Queue<Cliente>(this, "Cola clientes", true, true);
		colaDependientesOciosos = new Queue<Dependiente>(this, "Cola dependientes ociosos", true, true);
		colaCocinerosOciosos = new Queue<Cocinero>(this, "Cola cocineros ociosos", true, true);
		colaPedidosPendientes = new Queue<Dependiente>(this, "Cola pedidos pendientes", true, true);
		
		// Inicio las colas de dependientes y cocineros
	   Dependiente dependiente;
	   for (int i = 0; i < NUM_DEPENDIENTES ; i++){
	      // se crea un dependiente
		  dependiente = new Dependiente(this, "Dependiente", true);
	      // se pone en la cola de dependientes ociosos
	      colaDependientesOciosos.insert(dependiente);
	   }
	   
	   Cocinero cocinero;
	   for (int i = 0; i < NUM_COCINEROS ; i++){
	      // se crea un cocinero
		  cocinero = new Cocinero(this, "Cocinero", true);
	      // se pone en la cola de cocineros ociosos
	      colaCocinerosOciosos.insert(cocinero);
	   }
	}
	
   public double getTiempoServicio() {
      return tiempoServicio.sample();
   }

   public double getTiempoLlegadaCliente() {
      return tiempoLlegadaCliente.sample();
   }
   
   public double getTiempoCocina(){
   		return tiempoCocina.sample();
   }
   
   public double getTiempoPago(){
	   return tiempoPago.sample();
   }
   
   public static void main(java.lang.String[] args) {

	   Experiment.setEpsilon(java.util.concurrent.TimeUnit.SECONDS);
	   Experiment.setReferenceUnit(java.util.concurrent.TimeUnit.MINUTES);
	   McBurgerEventModel model = new McBurgerEventModel(null,
	                         "McBurger model orientado a eventos", true, true);

	   Experiment exp = new Experiment("McBurger modelo eventos");
	   model.connectToExperiment(exp);
	   
	   exp.setShowProgressBar(true);  
	   exp.stop(new TimeInstant(720, TimeUnit.MINUTES)); 
	   exp.tracePeriod(new TimeInstant(0), new TimeInstant(100, TimeUnit.MINUTES));                                         
	   exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES)); 
	   exp.start();
	   exp.report();
	   exp.finish();
   }

}
