package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;
import edu.psu.geovista.app.spreadsheet.util.Debug;

/*
 * Description:
 * <code>SIN</code><br>
 *   usage: <code>=SIN(parameter)</code><br>
 *   accepts only one literal or address<br>
 *   returns the sine of the specified parameter (in radians)<br>
 *   example: <code>=SIN(45)</code> returns <code>0.8509035</code>
 * Date: Mar 25, 2003
 * Time: 10:39:53 AM
 * @author Jin Chen
 */


public class FunctionSin extends Function {

    public Number evaluate( Node node) throws ParserException,NoReferenceException {
        Debug.showNode(node," FunctionSin() show node "+node);
        return new Float(Math.sin(getSingleParameter( node)));
    }


    public String getUsage() {
	return "SIN(value)";
    }

    public String getDescription() {
	return "Returns the sine of an angle.";
    }
}


