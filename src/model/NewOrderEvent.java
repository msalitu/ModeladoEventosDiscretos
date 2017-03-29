package model;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the truck arrival event in the EventsExample model. It
 * occurs when a truck arrives at the terminal to request loading of a
 * container.
 * 
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class NewOrderEvent extends EventOf2Entities<Waiter, Chef> {

	/**
	 * a reference to the model this event is a part of. Useful shortcut to
	 * access the model's static components
	 */
	private EventsExample myModel;

	/**
	 * Constructor of the truck arrival event
	 *
	 * Used to create a new truck arrival event
	 *
	 * @param owner
	 *            the model this event belongs to
	 * @param name
	 *            this event's name
	 * @param showInTrace
	 *            flag to indicate if this event shall produce output for the
	 *            trace
	 */
	public NewOrderEvent(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// store a reference to the model this event is associated with
		myModel = (EventsExample) owner;
	}

	public void eventRoutine(Waiter waiter, Chef chef) {

		// myModel.clientQueue.insert(chef);
		sendTraceNote("ClientQueueLength: " + myModel.clientQueue.length());
		CookEndEvent cookEnd = new CookEndEvent(myModel, "CookEndEvent", true);
		cookEnd.schedule(waiter, chef, new TimeSpan(myModel.getCookTime(), TimeUnit.MINUTES));

	}

}