package model;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the service end event in the EventsExample model. It
 * occurs when a van carrier finishes loading a truck.
 * 
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class CookEndEvent extends EventOf2Entities<Waiter, Chef> {

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
	public CookEndEvent(Model owner, String name, boolean showInTrace) {
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
	public void eventRoutine(Waiter waiter, Chef chef) {

		// pass the departure of the truck to the trace
		sendTraceNote("ClientQueueLength: " + myModel.clientQueue.length());
		myModel.idleChefQueue.insert(chef);
		PaymentEndEvent paymentEnd = new PaymentEndEvent(myModel, "PaymentEndEvent", true);
		paymentEnd.schedule(waiter, new TimeSpan(myModel.getPaymentTime(), TimeUnit.MINUTES));

	}

}