package model;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
/**
 * This class represents the truck arrival event
 * in the EventsExample model.
 * It occurs when a truck arrives at the terminal
 * to request loading of a container.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class ClientArrivalEvent extends Event<Client> {

	/** a reference to the model this event is a part of.
	 * Useful shortcut to access the model's static components
	 */
	private EventsExample myModel;

	/**
	 * Constructor of the truck arrival event
	 *
	 * Used to create a new truck arrival event
	 *
	 * @param owner the model this event belongs to
	 * @param name this event's name
	 * @param showInTrace flag to indicate if this event shall produce output for the trace
	 */
	public ClientArrivalEvent(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// store a reference to the model this event is associated with
		myModel = (EventsExample)owner;
	}

	public void eventRoutine(Client client) {

		// truck enters parking-lot
		myModel.clientQueue.insert(client);
		sendTraceNote("ClientQueueLength: "+ myModel.clientQueue.length());

		// check if a waiter is available
		if (!myModel.idleWaiterQueue.isEmpty()) {
			// yes, it is

			// get a reference to the first VC from the idle VC queue
			Waiter waiter = myModel.idleWaiterQueue.first();
			// remove it from the queue
			myModel.idleWaiterQueue.remove(waiter);

			// remove the truck from the queue
			myModel.clientQueue.remove(client);

			// create a service end event
			ServiceEndEvent serviceEnd = new ServiceEndEvent (myModel, "ServiceEndEvent", true);

			// and place it on the event list
			serviceEnd.schedule(waiter, new TimeSpan(myModel.getServiceTime(), TimeUnit.MINUTES));

		}

	}
}