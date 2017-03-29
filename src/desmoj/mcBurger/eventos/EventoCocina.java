package desmoj.mcBurger.eventos;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;

public class EventoCocina extends EventOf2Entities<Cocinero, Dependiente> {

	private McBurgerEventModel myModel;

	public EventoCocina(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		myModel = (McBurgerEventModel)owner;
	}

	@Override
	public void eventRoutine(Cocinero cocinero, Dependiente dependiente) {
		
		// Entregar al dependiente que estaba esperandome
		// El dependiente cobra
		EventoPago event = new EventoPago(myModel, "Evento Pago", true);
		// lo programamos en la lista de eventos
		event.schedule(dependiente, new TimeSpan(myModel.getTiempoPago()));
		
		
		//Atender al siguiente
		if (!myModel.colaPedidosPendientes.isEmpty()) {
			Dependiente nextDependiente = myModel.colaPedidosPendientes.first();
			// Y eliminar al dependiente de la cola de espera
			myModel.colaPedidosPendientes.remove(nextDependiente);
			
			// creamos un nuevo evento para que cocine
			EventoCocina nextEvent = new EventoCocina(myModel, "Evento Cocina", true);
 			// se programa el tiempo necesario para que termine de cocinar
			nextEvent.schedule(cocinero, nextDependiente, new TimeSpan(myModel.getTiempoCocina(), TimeUnit.MINUTES));
		}else {
			// NO, no hay ningun pedido esperando

			// el dependiente se incluye en la cola de dependientes ociosos
			myModel.colaCocinerosOciosos.insert(cocinero);

			// ahora esta esperando a que llegue algun dependiente
		}
	}
	
}