package model;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.TimeUnit;

public class EventsExample extends Model {

	protected static int NUM_WAITERS = 5;
	protected static int NUM_CHEFS = 1;
	private ContDistExponential clientArrivalTime;
	private ContDistExponential serviceTime;
	private ContDistExponential cookTime;
	private ContDistExponential paymentTime;
	protected Queue<Client> clientQueue;
	protected Queue<Waiter> idleWaiterQueue;
	protected Queue<Chef> idleChefQueue;

	public EventsExample(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
		super(owner, modelName, showInReport, showInTrace);
	}

	public String description() {
		return "McBurger";
	}

	public void doInitialSchedules() {

		// create the ClientGeneratorEvent
		ClientGeneratorEvent clientGenerator = new ClientGeneratorEvent(this, "ClientGenerator", true);

		// schedule for start of simulation
		clientGenerator.schedule(new TimeSpan(0));
	}

	public void init() {

		clientArrivalTime = new ContDistExponential(this, "ClientArrivalTimeStream", 5.0, true, false);
		serviceTime = new ContDistExponential(this, "ServiceTimeStream", 4.0, true, false);
		cookTime = new ContDistExponential(this, "CookTimeStream", 9.0, true, false);
		paymentTime = new ContDistExponential(this, "PaymentTimeStream", 2.0, true, false);

		clientArrivalTime.setNonNegative(true);
		serviceTime.setNonNegative(true);
		cookTime.setNonNegative(true);
		paymentTime.setNonNegative(true);

		clientQueue = new Queue<Client>(this, "Client Queue", true, true);

		idleWaiterQueue = new Queue<Waiter>(this, "idle Waiter Queue", true, true);
		idleChefQueue = new Queue<Chef>(this, "idle Chef Queue", true, true);

		Waiter waiter;
		for (int i = 0; i < NUM_WAITERS; i++) {
			waiter = new Waiter(this, "Waiter", true);
			idleWaiterQueue.insert(waiter);
		}

		Chef chef;
		for (int i = 0; i < NUM_CHEFS; i++) {
			chef = new Chef(this, "Chef", true);
			idleChefQueue.insert(chef);
		}
	}

	public double getServiceTime() {
		return serviceTime.sample();
	}

	public double getClientArrivalTime() {
		return clientArrivalTime.sample();
	}

	public double getCookTime() {
		return cookTime.sample();
	}

	public double getPaymentTime() {
		return paymentTime.sample();
	}

	/**
	 * Runs the model.
	 *
	 * In DESMO-J used to - instantiate the experiment - instantiate the model -
	 * connect the model to the experiment - steer length of simulation and
	 * outputs - set the ending criterion (normally the time) - start the
	 * simulation - initiate reporting - clean up the experiment
	 *
	 * @param args
	 *            is an array of command-line arguments (will be ignored here)
	 */
	public static void main(java.lang.String[] args) {
		EventsExample model = new EventsExample(null, "EventsExample", true, true);
		Experiment.setReferenceUnit(TimeUnit.MINUTES);
		Experiment.setEpsilon(TimeUnit.SECONDS);
		Experiment exp = new Experiment("EventExampleExperiment");
		model.connectToExperiment(exp);
		exp.setShowProgressBar(true); // display a progress bar (or not)
		exp.stop(new TimeInstant(1500, TimeUnit.MINUTES)); // set end of
		exp.tracePeriod(new TimeInstant(0), new TimeInstant(100, TimeUnit.MINUTES)); // set
		exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES)); // and
		exp.start();
		exp.report();
		exp.finish();
	}
} /* end of model class */
