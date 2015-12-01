/**
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import models.Argument;
import models.FunctionCallExpression;

import org.junit.Before;
import org.junit.Test;

import parsers.Parser;
import algorithms.Unifier;

/**
 * @author MacBookAir
 *
 */
public class UnificationTest {

	public ArrayList<TestCaseWrapper> testCases;
	public String fileName = "unification-cases.txt";

	public void loadTestCases() {
		System.out.println("Loading test cases from file.");
		testCases = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = br.readLine();
			int numberOfCases = Integer.parseInt(line);
			for (int i = 0; i < numberOfCases; i++) {
				line = br.readLine();
				String[] expressions = line.split("=");
				String firstExpression = expressions[0].trim();
				String secondExpression = expressions[1].trim();
				TestCaseWrapper testCase = new TestCaseWrapper(firstExpression,
						secondExpression);
				testCases.add(testCase);
				int numberOfUnifiers = Integer.parseInt(br.readLine());
				testCase.setAreUnifiable(numberOfUnifiers > 0);
				for (int j = 0; j < numberOfUnifiers; j++) {
					line = br.readLine();
					String[] unifiers = line.split("=");
					testCase.addUnifier(unifiers[0].trim(), unifiers[1].trim());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		loadTestCases();
		System.out.println("SetUp");
	}

	@Test
	public void test() {
		for (TestCaseWrapper testCaseWrapper : testCases) {
			System.out.println("================ test ================");
			FunctionCallExpression p1 = Parser
					.parseFunctionCall(testCaseWrapper.input1);
			FunctionCallExpression p2 = Parser
					.parseFunctionCall(testCaseWrapper.input2);
			assertEquals("Parsing " + testCaseWrapper.input1
					+ " ended up to be: " + p1, testCaseWrapper.input1,
					p1.toString());
			assertEquals("Parsing " + testCaseWrapper.input2
					+ " ended up to be: " + p2, testCaseWrapper.input2,
					p2.toString());
			System.out.println("================ finished parsing");
			Unifier u = new Unifier(p1, p2);
			if (testCaseWrapper.areUnifiable) {
				assertTrue("FunctionCallExpressions: " + p1 + ", " + p2
						+ " should be unifiable", u.unify());
			} else {
				assertFalse("FunctionCallExpressions: " + p1 + ", " + p2
						+ " cannot be unified", u.unify());
			}
			System.out.println("================ finished unifying");
			if (testCaseWrapper.areUnifiable) {
				for (Argument key : u.unifiers.keySet()) {
					assertTrue(
							"Result Unifiers: " + u.unifiers + " != "
									+ testCaseWrapper.unifiers,
							testCaseWrapper.hasUnifier(key, u.unifiers.get(key)));
				}
			}

		}
	}
}

class TestCaseWrapper {
	public String input1;
	public String input2;
	public boolean areUnifiable;
	public HashMap<String, String> unifiers;

	public TestCaseWrapper(String input1, String input2) {
		this.input1 = input1.replace(" ", "");
		this.input2 = input2.replace(" ", "");
		this.unifiers = new HashMap<>();
	}

	public void setAreUnifiable(boolean areUnifiable) {
		this.areUnifiable = areUnifiable;
	}

	public boolean hasUnifier(Argument key, Argument value) {
		return hasUnifier(key.toString(), value.toString());
	}

	public boolean hasUnifier(String key, String value) {
		return unifiers.containsKey(key) && unifiers.get(key).equals(value);
	}

	public void addUnifier(String toBeReplaced, String replacement) {
		unifiers.put(toBeReplaced, replacement);
	}
}
