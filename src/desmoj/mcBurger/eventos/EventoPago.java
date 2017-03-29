package desmoj.mcBurger.eventos;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;
import desmoj.tutorial1.EventsExample.ServiceEndEvent;
import desmoj.tutorial1.EventsExample.Truck;

public class EventoPago extends Event<Dependiente> {

	private McBurgerEventModel myModel;

	public EventoPago(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		myModel = (McBurgerEventModel)owner;
	}

	@Override
	public void eventRoutine(Dependiente dependiente) {
		

		// el dependiente comprueba si hay clientes esperando
		if (!myModel.colaClientes.isEmpty())
		{
			// Sí, por lo menos uno

			// lo atendemos
			Cliente nextCliente = myModel.colaClientes.first();
			myModel.colaClientes.remove(nextCliente);

			// creamos un nuevo evento de servicio
			EventoServicio event = new EventoServicio(myModel, "Servicio evento", true);
 			// se programa con el tiempo de servicio
			event.schedule(dependiente, nextCliente, new TimeSpan(myModel.getTiempoServicio(), TimeUnit.MINUTES));
		}
		else {
			// NO, no hay ningun cliente esperando

			// el dependiente se incluye en la cola de dependientes ociosos
			myModel.colaDependientesOciosos.insert(dependiente);

			// ahora esta esperando a que llegue algun cliente
		}
	}
}