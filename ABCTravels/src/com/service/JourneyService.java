package com.service;
import java.time.LocalDate;
import java.time.DayOfWeek;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.user.Journey;
import com.user.Order;
import com.user.Route;

import java.time.DayOfWeek;

public class JourneyService {
    private List<Route> routes;
    private List<Order> orders;

    public JourneyService(List<Route> routes, List<Order> orders) {
        this.routes = routes;
        this.orders = orders;
     }

    public void planJourney() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Plan Journey");

        // Getting journey details from the user
        System.out.print("Enter source : ");
        String source = scanner.nextLine();

        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        System.out.print("Enter journey date (YYYY-MM-DD): ");
        String journeyDateString = scanner.nextLine(); 
        LocalDate journeyDate = LocalDate.parse(journeyDateString, DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.print("Enter number of passengers: ");
        int noOfPassengers = scanner.nextInt();
        scanner.nextLine(); // Consume the leftover new line 
        
        // finding matching routes
        List<Route> matchingRoutes = getRoutes(source , destination , journeyDate , noOfPassengers);
        if(matchingRoutes!=null) {
        	System.out.println("Avaialable Routes : ");
        	for(int i=0;i<matchingRoutes.size();i++) {
        		
        		System.out.println((i+1)+ " : " + matchingRoutes.get(i));
        	}
        	System.out.println("Select a route(number) :");
        	int routeNumber =  scanner.nextInt();
        	Route selectedRoute = matchingRoutes.get(routeNumber-1);
        	// Creating an order 
        	Order newOrder = createOrder(journeyDate , noOfPassengers , selectedRoute);
        	orders.add(newOrder);
        	System.out.println("Journey planned successfully .Order details :" +newOrder);
        	
        }else {
        	System.out.println("No available routes found for the given details ");
        }
        
       
    }
    
  private List<Route> getRoutes(String source , String destination , LocalDate journeyDate, int noOfPassengers){
    	
      List<Route> matchingRoutes = new ArrayList<>();
   for(Route route: routes) {
	   if (route.getSource().equals(source) &&
               route.getDestination().equals(destination) &&
               route.getJourneyDate().isEqual(journeyDate) &&
               route.getNoOfSeatsAvailable() >= noOfPassengers) {
               matchingRoutes.add(route);
           }

   }
   
        
		return routes;
}
  public void reScheduleJourney() {
	  Scanner sc = new Scanner(System.in);
	  System.out.println(" \n ReSchedule Your Journey ");
	  System.out.println(" Enter your order Id");
	  int orderId = sc.nextInt();
	  sc.nextLine();  
	  
	   // Finding the order id by invoking OrderID method
	  Order orderToReSchedule = findOrderById(orderId);
	  if(orderToReSchedule!=null) {
		  System.out.println("\n Enter new Journey Date ");
		  String date = sc.nextLine();
		  LocalDate newDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
	  
	  // Check if the route is available for the new date 
	  List<Route> availableRoutes = getRoutes(orderToReSchedule.getRoute().getSource(),
			  orderToReSchedule.getRoute().getDestination(),
			  newDate,orderToReSchedule.getRequestedJourneyPlan().getNumberOfPassengers());
	  if(availableRoutes!=null) {
		  // updating the journey date
		  orderToReSchedule.getRequestedJourneyPlan().setJourneyDate(newDate);
		  System.out.println("Journey reScheduled Successfully.Updated order details : " + orderToReSchedule);
		   }
		}else {
			System.out.println(" OrderId not found ");
	  } 
  }
  private Order createOrder(LocalDate journeyDate , int noOfPassengers, Route selectedRoute){
	   Order newOrder = new Order(); 
	   double bookingCost = selectedRoute.getTicketPricePerPassenger()*noOfPassengers; 
	   // Adding surge price on weekends 
	   if(journeyDate.getDayOfWeek()==DayOfWeek.SATURDAY||journeyDate.getDayOfWeek()==DayOfWeek.SUNDAY) {
		   bookingCost+=200; //weekend surge 
		   bookingCost += bookingCost*0.1;  // Adding GST 10%
		   
		   
	   }
	  newOrder.setOrderAmount(bookingCost);
	  newOrder.setRoute(selectedRoute);
	  newOrder.setOrderStatus(" Created ");
	  newOrder.setRequestedJourneyPlan(new Journey(journeyDate,noOfPassengers));
	  newOrder.setOrderId(orders.size()+1); //Simple way to generate unique Order ID
	  
	  
	return newOrder;
	  
  }
  private Order findOrderById(int orderId) {
	  
	  for(Order order: orders) {
		  if(order.getOrderId().equals(order)) {
			  return order;
			  
			 }
		  
	  }
	  
	return null; // return null if no Order id Found
	  
  }
    
}