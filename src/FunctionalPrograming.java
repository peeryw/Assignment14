
/*
 * Functional Programming in Java - Assignment 14
- write a Java program in the functional style (no loops or iteration)
- answers questions about the data in this file "Movie-Data.txt"
- The file contains data on the top grossing films as of 2012 in inflation-adjusted terms.
- The format of the file is film name | studio | inflation-adjusted total life-time gross earnings in millions of $ | year released.
- example line from the file:
		Lethal Weapon 3|WB|273.0718|1992
- Write functional code to answer the following questions:
	1. What is the top-grossing film of all time?
	2. Did film studios make more money in the 70's or 80's?
	3. Which movies grossed more than $500 million?
	4. [Extra credit] Which studio made the most money off of blockbusters in the 60s? 
	(Hint:collect() and Collectors.toMap())

- should be able to answer each of the following questions with a short code fragment that 
	contains no loops or iteration.
	
Hints:
What do I mean by writing a Java program "in the functional style"? For this assignment it means to
open the file, convert it to a stream, call methods on the stream such as filter(), map(), reduce(),
sorted(), findFirst() and collect() in order to reveal the answers to the questions given.
You might want to write helper functions to extract specific data points from a line of data. For
example:
private static String getRevenue(String s) {
return s.split("[|]")[2]; // split will split a string into an array
of strings based on given regular expression
}
Example:
getRevenue("Lethal Weapon 3|WB|273.0718|1992")
would return: 273.0718
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FunctionalPrograming {

	static List<String> movieData;
	static List<String> movies70;
	static List<String> movies80;

	public static void main(String[] args) {

		/***Open file containing data***/
		Path path = Paths.get("Movie-Data.txt");
		
		/*** Read in values contained in file***/
		try {
			movieData = Files.readAllLines(path);
		} catch (IOException e) {
			System.out.println("MESSAGE:" + e);
		}
		
		/**** Find movie which grossed the most all time *****/
		/****Print Question 1 from assignment****/
		System.out.println("What is the top grossing film of all time?");
		
		Optional<String> first = movieData.stream().sorted(
				(line1, line2) -> -(Double.valueOf(getAmount(line1)).compareTo(Double.valueOf(getAmount(line2)))))
				.findFirst();
		
		System.out.println(first.orElse("No record found"));
		
		/*** Find which decade grossed more, 70's or 80's***/
		/***Print Question 2 from assignment***/
		System.out.println("\nDid the film industry make more money in the 70's or 80's?\nANSWER:");
		
		/***Get all movies from 70's and compile total gross amount***/
		double movies70s = movieData.stream().filter((line) -> (Integer.valueOf(getYear(line)) < 1980))
				.filter((line) -> (Integer.valueOf(getYear(line)) > 1969))
				.mapToDouble(line -> Double.valueOf(getAmount(line))).sum();
		
		/***Get all movies from 80's and compile total gross amount***/
		double movies80s = movieData.stream().filter((line) -> (Integer.valueOf(getYear(line)) < 1990))
				.filter((line) -> (Integer.valueOf(getYear(line)) > 1979))
				.mapToDouble(line -> Double.valueOf(getAmount(line))).sum();
		
		/***Compare 70's total to 80's total***/
		if (movies70s > movies80s)
			System.out.println("The film industry made more money in the 70's.");
		else
			System.out.println("The film industry made more money in the 80's.");
		
		/****Find all movies that grossed more than 500 million****/
		/****Print Question 3****/
		System.out.println("\nWhich movies grossed more than 500 million?\nANSWER:");
		
		/****Filter line by line amounts greater than 500****/
		movieData.stream().filter((line) ->  (Double.valueOf(getAmount(line)) > 500)).forEach((line) -> System.out.println("\t" + line));
		
		/****Find movie company that made most money in the 60's****/
		/****Print Question 4****/
		System.out.println("\nWhich studio made the most money off of blockbusters in the 60s?");
		
		/****Filter out studios and sum there total gross****/
		movieData.stream().filter((line) -> (Integer.valueOf(getYear(line)) < 1970))
		.filter((line) -> (Integer.valueOf(getYear(line)) > 1959))
		.sorted((line1,line2) -> -(Double.valueOf(getAmount(line1)).compareTo(Double.valueOf(getAmount(line2)))))
		.findFirst();
		
	}

	private static String getStudio(String line) {
		return line.split("[|]")[1];
	}

	private static String getYear(String line) {
		return line.split("[|]")[3];
	}

	private static String getAmount(String line) {
		return line.split("[|]")[2];
	}

}
