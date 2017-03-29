package desmoj.mcBurger.eventos;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

public class ClienteGeneratorEvent extends ExternalEvent {


	public ClienteGeneratorEvent(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}
	
	public void eventRoutine() {

		// get a reference to the model
		McBurgerEventModel model = (McBurgerEventModel)getModel();

		// create a new client
		Cliente cliente = new Cliente(model, "Cliente", true);
		// nueva llegada cliente
		EventoLLegadaCliente llegadaCliente = new EventoLLegadaCliente(model, "Evento llegada cliente", true);
		llegadaCliente.schedule(cliente, new TimeSpan(0.0));

		// programar cuando llegara el proximo cliente
		schedule(new TimeSpan(model.getTiempoLlegadaCliente(), TimeUnit.MINUTES));
	

	}
}
