package model;

import desmoj.core.simulator.*;
/**
 * The VanCarrier entity encapsulates all data relevant for a van carrier.
 * In our model, it only stores a reference to the truck it is currently
 * (un)loading.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class Chef extends Entity {

	/**
	 * Constructor of the van carrier entity.
	 *
	 * @param owner the model this entity belongs to
	 * @param name this VC's name
	 * @param showInTrace flag to indicate if this entity shall produce output for the trace
	 */
	public Chef(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}
}