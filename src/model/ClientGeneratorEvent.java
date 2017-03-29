package model;

import desmoj.core.simulator.*;

import java.util.concurrent.TimeUnit;

public class ClientGeneratorEvent extends ExternalEvent {

	public ClientGeneratorEvent(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	public void eventRoutine() {

		// get a reference to the model
		EventsExample model = (EventsExample) getModel();

		// create a new Client
		Client Client = new Client(model, "Client", true);
		// create a new Client arrival event
		ClientArrivalEvent ClientArrival = new ClientArrivalEvent(model, "ClientArrivalEvent", true);
		// and schedule it for the current point in time
		ClientArrival.schedule(Client, new TimeSpan(0.0));

		// schedule this Client generator again for the next Client arrival time
		schedule(new TimeSpan(model.getClientArrivalTime(), TimeUnit.MINUTES));
		// from inside to outside...
		// draw a new inter-arrival time value
		// wrap it in a TimeSpan object
		// and schedule this event for the current point in time + the
		// inter-arrival time
	}
}
