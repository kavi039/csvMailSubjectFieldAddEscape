import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CustomFileWriter extends PrintWriter {

    public static String ESCAPE_CHAR = "\\\\";
    public static String ESCAPED_ESCAPE_CHAR = "\\\\\\\\";

    CustomFileWriter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    @Override
    public void println(String x) {
        synchronized (lock) {
            print(changeDelimiter(x));
            println();
        }
    }

    String changeDelimiter(String outputLine) {
        String finalOutputLine = outputLine.replaceAll(ESCAPE_CHAR, ESCAPED_ESCAPE_CHAR)
                .replaceAll(",", ESCAPE_CHAR + ",")
                .replaceAll("::", ",");
        return finalOutputLine;

    }
}
