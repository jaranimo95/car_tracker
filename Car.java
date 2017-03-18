// Christian Jarani
// Professor Farnan
// CS 1501
// Project 3

public class Car {

	private String	vin, make, model, colour;
	private int 			  price, mileage;
	private boolean 	  			hideFlag;

	public Car () {
		this.vin = null;
		this.make = null;
		this.model = null;
		this.colour = null;
		this.price = 0;
		this.mileage = 0;
	}

	public Car (String vin, String make, String model, String colour, int price, int mileage) {
		this.vin = vin;
		this.make = make;
		this.model = model;
		this.colour = colour;
		this.price = price;
		this.mileage = mileage;
	}

	public void setVin (String vin) {
		this.vin = vin;
	}

	public String getVin () {
		return this.vin;
	}

	public void setMake (String make) {
		this.make = make;
	}

	public String getMake () {
		return this.make;
	}

	public void setModel (String model) {
		this.model = model;
	}

	public String getModel () {
		return this.model;
	}

	public void setColour (String colour) {
		this.colour = colour;
	}

	public String getColour () {
		return this.colour;
	}

	public void setPrice (int price) {
		this.price = price;
	}

	public int getPrice () {
		return this.price;
	}

	public void setMileage (int mileage) {
		this.mileage = mileage;
	}

	public int getMileage () {
		return this.mileage;
	}

}
