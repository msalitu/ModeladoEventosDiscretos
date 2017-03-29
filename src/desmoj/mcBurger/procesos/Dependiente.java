package desmoj.mcBurger.procesos;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Dependiente extends SimProcess{
	
	private McBurgerModel miModelo;

	public Dependiente(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		miModelo = (McBurgerModel) owner;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		//el dependiente siempre está en servicio, no descansa
		while (true) {
			//comprueba si hay alguien esperando
			if (miModelo.colaClientes.isEmpty()) { // NO,no hay nadie

				// me meto en la cola de dependientes libres
				miModelo.colaDependientesOciosos.insert(this);

				// espero a que ocurra algo
				passivate();
				
			} else { //SI, hay un cliente esperando

				//cojo al cliente que este primero en la cola
				Cliente cliente = miModelo.colaClientes.first();
				miModelo.colaClientes.remove(cliente);
				cliente.endWait();

				//tomo nota de su pedido
				hold(new TimeSpan(miModelo.getTiempoServicio()));
				
				//me pongo en la cola de pedidos para cocina
				miModelo.colaPedidosPendientes.insert(this);
				sendTraceNote("El dependiente encarga el pedido a cocina");
				sendTraceNote("Longitud de la cola de pedidos para cocina: " + miModelo.colaPedidosPendientes.length());
				
				//compruebo que hay algun cocinero libre
				if(miModelo.colaCocinerosOciosos.isEmpty()){	//NO, los cocineros estan ocupados
					// espero a que me atiendan
					passivate();
				}else{											//SI, hay un cocinero libre
					//cojo al primer cocinero disponible
					Cocinero cocinero = miModelo.colaCocinerosOciosos.first();
					miModelo.colaCocinerosOciosos.remove(cocinero);
					//me aseguro de que lo siguiente que va a hacer 
					//es preparar el pedido que le acabo de solicitar
					cocinero.activateAfter(this);
					//me siento a esperar a que lo termine
					passivate();
				}
				
				// Cobro el pedido al cliente
				hold(new TimeSpan(miModelo.getClienteTiempoPago()));
				sendTraceNote("El dependiente cobra el pedido");
				
				//entrego el pedido al cliente
				cliente.activate(new TimeSpan(0.0));
				//vuelvo a mirar a ver si hay nuevos clientes en la cola
			}
		}
		
	}

}
