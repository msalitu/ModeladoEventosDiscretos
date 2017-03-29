package desmoj.mcBurger.eventos;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class ClienteGenerator extends ExternalEvent{
	
	private McBurgerModel miModelo;

	public ClienteGenerator(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		miModelo = (McBurgerModel) owner;
	}

	@Override
	public void eventRoutine() throws SuspendExecution {
		
		Cliente nuevoCliente = new Cliente(miModelo, "Cliente", true);
		nuevoCliente.activate();
		schedule(new TimeSpan(miModelo.getClienteTiempoLlegada()));
		miModelo.clientesLlegados.update(++miModelo.numeroClientesLlegados);
	}

}
