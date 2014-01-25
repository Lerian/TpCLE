package main;

public class Person {
	private String name;
	private String city;

	@Override
	public String toString() {
		return "Person [name=" + name + ", city=" + city + "]";
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public void setCity(String newCity) {
		city = newCity;
	}
}
