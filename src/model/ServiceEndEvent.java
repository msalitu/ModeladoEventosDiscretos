package model;

import desmoj.core.simulator.*;

/**
 * This class represents the service end event in the EventsExample model. It
 * occurs when a van carrier finishes loading a truck.
 * 
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class ServiceEndEvent extends Event<Waiter> {

	/**
	 * A reference to the model this event is a part of. Useful shortcut to
	 * access the model's static components
	 */
	private EventsExample myModel;

	/**
	 * Constructor of the service end event
	 *
	 * Used to create a new service end event
	 *
	 * @param owner
	 *            the model this event belongs to
	 * @param name
	 *            this event's name
	 * @param showInTrace
	 *            flag to indicate if this event shall produce output for the
	 *            trace
	 */
	public ServiceEndEvent(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// store a reference to the model this event is associated with
		myModel = (EventsExample) owner;
	}

	/**
	 * This eventRoutine() describes what happens when a van carrier finishes
	 * loading a truck.
	 *
	 * The truck leaves the system. The van carrier will then check if there is
	 * another truck waiting for service. If there is another truck waiting it
	 * will service it. If not it will wait on its parking spot for the next
	 * customer to arrive.
	 */
	public void eventRoutine(Waiter waiter) {

		// pass the departure of the truck to the trace
		sendTraceNote(waiter + " leaves the service area");
		if (!myModel.idleChefQueue.isEmpty()) {
			Chef chef = myModel.idleChefQueue.first();
			myModel.idleChefQueue.remove(chef);
			NewOrderEvent newOrder = new NewOrderEvent(myModel, "NewOrderEvent", true);
			newOrder.schedule(waiter, chef, new TimeSpan(0.0));
		}

	}

}