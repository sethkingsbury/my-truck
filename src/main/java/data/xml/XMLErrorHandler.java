package data.xml;

import org.xml.sax.*;
import java.io.*;

/**
 * XMLErrorHandler provides implementation for error handling when reading XML files
 */
public class XMLErrorHandler implements ErrorHandler {

	/**
	 * We might want to get clever about where the error output should
	 * go.  Maybe it should pop up a dialog box, go down a pipe, be
	 * written to a log etc.
	 */
	private PrintStream errDest;

	XMLErrorHandler(PrintStream errDest) {
		this.errDest = errDest;
	}

	/**
	 * Format some of the available information about or current problem.
	 */
	private String getParseExceptionInfo(SAXParseException spe) {
		/*
		 * The system ID will generally be a URL
		 */
		String systemId = spe.getSystemId();
		if (systemId == null) {
			systemId = "null";
		}
		String parseExceptionInfo = spe.getMessage() + "\nat line "
			+ spe.getLineNumber() + " in " + systemId;
		return parseExceptionInfo;
	}

	/* The warning, fatalError and error methods are from the SAX
	 * org.xml.sax.ErrorHandler interface.  We might want to take
	 * specific action (e.g. exit) or pass the exception on for
	 * someone further up the line to handle.
	 */

	public void warning(SAXParseException spe) throws SAXException {
		errDest.println("Warning: " + getParseExceptionInfo(spe));
	}

	public void error(SAXParseException spe) throws SAXException {
		String errorMessage = "Error: " + getParseExceptionInfo(spe);
		throw new SAXException(errorMessage);
	}

	public void fatalError(SAXParseException spe) throws SAXException {
		String fatalErrorMessage = "Fatal Error: "
			+ getParseExceptionInfo(spe);
		throw new SAXException(fatalErrorMessage);
	}
}
