package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:47:32 PM
 * @author Jin Chen
 */

public class FunctionSqrt extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.sqrt(getSingleParameter(node)));
    }

    public String getUsage() {
        return "SQRT(value)";
    }

    public String getDescription() {
        return "Returns a square root of a number.";
    }
}
