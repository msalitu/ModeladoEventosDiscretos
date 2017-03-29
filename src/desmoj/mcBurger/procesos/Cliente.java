package desmoj.mcBurger.procesos;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeOperations;

public class Cliente extends SimProcess{
	
	private McBurgerModel miModelo;

	private TimeInstant empiezaEspera;
	
	private TimeInstant terminaEspera;
	
	public Cliente(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		miModelo = (McBurgerModel) owner;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		// El cliente llega a la cola
		miModelo.colaClientes.insert(this);
		empiezaEspera = presentTime();
		sendTraceNote("Longitud de la cola de clientes: " + miModelo.colaClientes.length());

		// esta disponible el dependiente?
		if (!miModelo.colaDependientesOciosos.isEmpty()) { // esta disponible

			//coge el primer dependiente (y unico) de la cola idle de dependientes
			Dependiente dependiente = miModelo.colaDependientesOciosos.first();
			//elimina al dependiente de la cola de dependientes libres
			miModelo.colaDependientesOciosos.remove(dependiente);

			//coloca al dependiente en la lista de eventos despues de mi
			//para asegurar que voy a ser el siguiente cliente
			dependiente.activateAfter(this);

			//me siento a esperar mi pedido
			passivate();

		} else { // no esta disponible

			// espero a que me atiendan
			passivate();
		}

		// Online otra vez quiere decir que ya me han entregado el pedido y me puedo ir
		sendTraceNote("El cliente fue atendido y sale de McBurger.");
		miModelo.clientesAtendidos.update(++miModelo.numeroClientesAtendidos);
		miModelo.waitTimeHistogram.update(getWaitTime());
		
	}

	public void endWait() {
		terminaEspera = presentTime();
	}
	
	public double getWaitTime() {
		if (empiezaEspera != null && terminaEspera != null) 
			return TimeOperations.diff(empiezaEspera, terminaEspera).getTimeAsDouble();
		else
			return Double.NaN;
	}
}