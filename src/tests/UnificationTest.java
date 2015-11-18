/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import algorithms.Unifier;
import parsers.PredicateParser;
import unification.Predicate;

/**
 * @author MacBookAir
 *
 */
public class UnificationTest {

	public ArrayList<CaseWrapper> cases;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cases = new ArrayList<>();
		cases.add(new CaseWrapper("p(f(a),g(X))", "p(Y,Y)", false));
		cases.add(new CaseWrapper("p(a,X,h(g(Z)))", "p(Z,h(Y),h(Y))", true));
		cases.add(new CaseWrapper("P(x,g(x),g(f(a)))", "P(f(u),v,v)", true));
		cases.add(new CaseWrapper("P(a,y,f(y))", "P(z,z,u)", true));
		cases.add(new CaseWrapper("f(x,g(x),x)", "f(g(u),g(g(z)),z)", true));
	}

	@Test
	public void test() {
		for (CaseWrapper caseWrapper : cases) {
			Predicate p1 = PredicateParser.parse(caseWrapper.input1);
			Predicate p2 = PredicateParser.parse(caseWrapper.input2);
			assertEquals("Parsing " + caseWrapper.input1 + " ended up to be: "
					+ p1, caseWrapper.input1, p1.toString());
			assertEquals("Parsing " + caseWrapper.input2 + " ended up to be: "
					+ p2, caseWrapper.input2, p2.toString());
			Unifier u = new Unifier(p1, p2);
			if (caseWrapper.areUnifiable) {
				assertTrue("Predicates: " + p1 + ", " + p2
						+ " should be unifiable", u.unify());
			} else {
				assertFalse("Predicates: " + p1 + ", " + p2
						+ " cannot be unified", u.unify());
			}

		}
	}
}

class CaseWrapper {
	public String input1;
	public String input2;
	public boolean areUnifiable;

	public CaseWrapper(String input1, String input2, boolean areUnifiable) {
		this.input1 = input1;
		this.input2 = input2;
		this.areUnifiable = areUnifiable;
	}
}