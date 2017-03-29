package desmoj.mcBurger.procesos;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Cocinero extends SimProcess{
	
	private McBurgerModel miModelo;

	public Cocinero(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		miModelo = (McBurgerModel) owner;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		
		// el cocinero nunca descansa
		while(true){
			// compruebo si hay algun pedido pendiente
			if (miModelo.colaPedidosPendientes.isEmpty()){	//NO, no hay ningun pedido pendiente
				miModelo.colaCocinerosOciosos.insert(this);
				passivate();
			}else{											//SI, hay un dependiente, hagamosle caso...
				//Cojo el pedido que me trae el dependiente
				Dependiente dependiente = miModelo.colaPedidosPendientes.first();
				miModelo.colaPedidosPendientes.remove(dependiente);
				//Cocino
				hold(new TimeSpan(miModelo.getTiempoCocina()));
				//Se lo entrego
				sendTraceNote("El cocinero entrega la comida al dependiente");
				dependiente.activate(new TimeSpan(0.0));
				// Vuelvo a mirar si tengo pedidos pendientes
			}
		}
	}

}
