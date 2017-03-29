package desmoj.mcBurger.eventos;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;


public class EventoServicio extends EventOf2Entities<Dependiente,Cliente> {

	private McBurgerEventModel myModel;

	public EventoServicio(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		myModel = (McBurgerEventModel)owner;
	}

	@Override
	public void eventRoutine(Dependiente dependiente, Cliente cliente) {
	
        sendTraceNote(cliente + " es atendido y ha solicitado su hamburguesa");
        myModel.colaPedidosPendientes.insert(dependiente);
        
		// comprueba si hay cocineros ociosos
		if (!myModel.colaCocinerosOciosos.isEmpty())
		{
			// SI, hay un cocinero libre

			// el primer cocinero deja de estar ocioso para cocinar
			Cocinero cocinero = myModel.colaCocinerosOciosos.first();
			myModel.colaCocinerosOciosos.remove(cocinero);
			
			// el dependiente ya no esta pendiente
			myModel.colaPedidosPendientes.remove(dependiente);

			// creamos un nuevo evento para que cocine
			EventoCocina event = new EventoCocina(myModel, "Evento Cocina", true);
 			// se programa el tiempo necesario para que termine de cocinar
			event.schedule(cocinero, dependiente, new TimeSpan(myModel.getTiempoCocina(), TimeUnit.MINUTES));
		}
		
	}
}