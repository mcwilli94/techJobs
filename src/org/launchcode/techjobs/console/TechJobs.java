package org.launchcode.techjobs.console;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by LaunchCode
 */
public class TechJobs {

    private static Scanner in = new Scanner(System.in);

    public static void main (String[] args) {

        // Initialize our field map with key/name pairs
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // Top-level menu options
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to search until they manually quit
        while (true) {

            String actionChoice = getUserSelection("View jobs by:", actionChoices);

            if (actionChoice.equals("list")) {

                String columnChoice = getUserSelection("List", columnChoices);

                if (columnChoice.equals("all")) {
                    printJobs(JobData.findAll());
                } else {

                    ArrayList<String> results = JobData.findAll(columnChoice);

                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");

                    // Print list of skills, employers, etc
                    for (String item : results) {
                        System.out.println(item);
                    }
                }

            } else { // choice is "search"

                // How does the user want to search (e.g. by skill or employer)
                String searchField = getUserSelection("Search by:", columnChoices);


                // Determines the search term
                System.out.println("\nSearch term: ");
                String searchTerm = in.nextLine();


                // Determines which columns to search the term under
                if (searchField.equals("all") || searchField.equals("0")) {
                    findByValue(JobData.findAll(), searchTerm);
                } else {
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }


            }
        }
    }

    // ﻿Returns the key of the selected item from the choices Dictionary
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        Integer choiceIdx;
        Boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        // Put the choices in an ordered structure so we can
        // associate an integer with each one
        Integer i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }

        do {

            System.out.println("\n" + menuHeader);

            // Print available choices
            for (Integer j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }

            choiceIdx = in.nextInt();
            in.nextLine();

            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while(!validChoice);

        return choiceKeys[choiceIdx];
    }

    // Print a list of jobs
    private static void printJobs(ArrayList<HashMap<String, String>> someJobs) {
        // Initialize variables
        HashMap<String, String> jobs = new HashMap<>();

        // Print out error message if search term not found in database
        if (someJobs.isEmpty() == true){
            System.out.println("There are no results for that term");

        } else {
            // print out all jobs matching the search term
            for (int i = 0; i < someJobs.size(); i++) {
                jobs = someJobs.get(i);
                for (String k : jobs.keySet()) {
                    System.out.println(k + " : " + jobs.get(k));
                }
                System.out.println("\n");
            }
        }

    }

    private static void findByValue(ArrayList<HashMap<String, String>> someJobs, String searchTerm) {
        // Initialize Variables
        HashMap<String, String> jobs = new HashMap<>();
        Object[] matches;
        int count = 0;


        // Find and add to an array the cases matching the search term
        for (int i = 0; i < someJobs.size(); i++) {
            jobs = someJobs.get(i);
            for (String k : jobs.keySet()){

                if (jobs.get(k).toLowerCase().contains(searchTerm.toLowerCase())){
                    count += 1;
                    matches = jobs.entrySet().toArray();
                    for (int j = 0; j < matches.length; j++){

                        // Prints the matching job openings properly
                        int x = matches[j].toString().indexOf("=");
                        System.out.println(matches[j].toString().substring(0,x) + " : " + matches[j].toString().substring(x+1,matches[j].toString().length()));
                    }
                    System.out.println("\n");
                    break;  // prevents the same job from appearing more than once
                }

            }

        }

        // Display error message if results not found
        if (count == 0){
            System.out.println("\nThere are not results for that term");
        }

    }

}
