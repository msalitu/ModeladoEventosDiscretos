package desmoj.mcBurger.eventos;

import desmoj.core.simulator.*;

public class EventoLLegadaCliente extends Event<Cliente> {

	private McBurgerEventModel myModel;

	
	public EventoLLegadaCliente(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// store a reference to the model this event is associated with
		myModel = (McBurgerEventModel)owner;
	}

	public void eventRoutine(Cliente cliente) {

		// cliente entra restaurante
		myModel.colaClientes.insert(cliente);
		sendTraceNote("Longitud cola clientes: "+ myModel.colaClientes.length());

		// busca dependiente ocioso
		if (!myModel.colaDependientesOciosos.isEmpty()) {
			// si hay uno

			// empleo al primer dependieente ocioso
			Dependiente dependiente = myModel.colaDependientesOciosos.first();
			// lo elimino de la cola de dependientes ociosos porque tiene trabajo
			myModel.colaDependientesOciosos.remove(dependiente);

			// elimino al cliente de la cola porque le atiendo
			myModel.colaClientes.remove(cliente);

			// creamos el evento de ser atendido
			EventoServicio servicio = new EventoServicio (myModel, "Evento Servicio", true);

			// lo programamos en la lista de eventos
			servicio.schedule(dependiente, cliente, new TimeSpan(myModel.getTiempoServicio()));

		}

	}

}