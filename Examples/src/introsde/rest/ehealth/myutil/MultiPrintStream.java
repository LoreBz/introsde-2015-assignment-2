package introsde.rest.ehealth.myutil;

import java.io.PrintStream;
import java.util.List;

public class MultiPrintStream {

	List<PrintStream> streams;

	public MultiPrintStream(List<PrintStream> streams) {
		super();
		this.streams = streams;
	}

	public void println(String s) {
		if (!streams.isEmpty()) {
			for (PrintStream out : streams) {
				out.println(s);
			}
		}
	}

	public void close() {
		for (PrintStream printStream : streams) {
			if (!printStream.equals(System.out)) {
				printStream.close();
			}
		}
	}

}
