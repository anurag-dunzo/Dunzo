package coffeemachine;

import entities.Beverage;
import entities.Machine;
import input.InputParser;
import input.IParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Class that simulates the coffee machine
 */
public class CoffeeMachineApp {

    private static final int ONE_DAY_IN_SECONDS = 86400;

	public static void main(String[] args) throws IOException, InterruptedException {
        CoffeeMachineApp app = new CoffeeMachineApp();
        CoffeeMachineAppUtil util = new CoffeeMachineAppUtil();
        List<String> testFileNames = util.parseArguments(args);
        app.processAllInputFiles(util, testFileNames);
    }

    /**
     * Run each input scenario one by one and Show Indicator for low running items after each run.
     *
     * @param util
     * @param testFiles
     * @throws IOException
     * @throws InterruptedException
     */
    public void processAllInputFiles(CoffeeMachineAppUtil util, List<String> testFileNames) throws IOException, InterruptedException {
        for (String fileName : testFileNames) {
            Machine machine = getMachineFromInputFile(fileName);
            ExecutorService executorService = Executors.newFixedThreadPool(getNumberOfOutlets(machine));
            printMessage("machine warming up!");
            CoffeeMachine coffeeMachine = util.initialize(machine);
            printMessage("ready to serve you some hot beverage!");
            processBeverages(executorService, coffeeMachine, machine.getBeverages());
            executorService.shutdown();
            try {
                executorService.awaitTermination(ONE_DAY_IN_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw e;
            }
            printLowRunningIngredients(coffeeMachine);
        }
    }

    /*
     * default number of outlets is 3, 
     */
	private int getNumberOfOutlets(Machine machine) {
		return machine.getOutlets() != null ? machine.getOutlets().getCount_n() : 3;
	}

	private Machine getMachineFromInputFile(String fileName) throws IOException, JsonProcessingException {
		String rawFileContent = new String(Files.readAllBytes(Paths.get(fileName)));
		IParser<String, Machine> inputParser = new InputParser();
		System.out.println("Processing input : " + fileName);
		Machine machine = inputParser.parse(rawFileContent);
		return machine;
	}

    private static void printLowRunningIngredients(CoffeeMachine coffeeMachine) {
        if (coffeeMachine.getInventory().getLowRunningIngredients() != null && !coffeeMachine.getInventory().getLowRunningIngredients().isEmpty()) {
            printMessage("running low on the following ingredients! please stock up!");
            printMessage("ingredient | quantity left");
            coffeeMachine.getInventory().getLowRunningIngredients().forEach(item -> {
            		printMessage(item.getName() + " | " + item.getQuantity());
            });
        }
    }

    private static void processBeverages(ExecutorService executorService, final CoffeeMachine coffeeMachine, Map<String, Map<String, Integer>> beverages) {
        beverages.forEach((key, value) -> {
            Beverage beverage = Beverage.createBeveragesItem(key, value);
            executorService.execute(
                    () -> {
                    		coffeeMachine.prepare(beverage);
                    }
            );
        });
    }
    
    /*
     * this prints to the console now, can be changed to write to a log file as well.
     */
    private static void printMessage(String message) {
    		System.out.println(message);
    }


}
