import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static String outputFileFolder = "/home/kavita/projects/svn/logUploader/loguploader_data/rectify_files/";
    private static int idealLength = 28;

    public static void main(String[] args) {
        File dir = new File("/home/kavita/projects/svn/logUploader/loguploader_data/");
        try {

            Files.list(dir.toPath()).forEach(dirPath -> {
                File file = new File(dirPath.toString());
                File makeDir = new File(outputFileFolder + dirPath.getFileName());
                makeDir.mkdirs();
                try {
                    Files.list(file.toPath()).forEach(filePath -> {
                        System.out.println(filePath);
                        fixFileEscapeIssue(filePath);
                    });
                } catch (Exception ex) {
                    System.out.println("While processing file got error : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("While processing dir got error : " + e.getMessage());
        }

    }

    private static void fixFileEscapeIssue(Path filePath) {
        String filename = filePath.getParent().getFileName() + "/" + filePath.getFileName().toString();
        String outputFileName = outputFileFolder + filename;
        try {
            File file = new File(filePath.toString());
            file.createNewFile();
            CustomFileWriter customFileWriter = new CustomFileWriter(outputFileName);
            System.out.println("fixFileEscapeIssue :: " + outputFileName);

            Files.lines(filePath)
                    .map(line -> line.split(","))
                    .map(contents -> {
                        String content;
                        if (contents.length == idealLength) {
                            content = String.join("::", contents);
                        } else {
                            int lengthDiff = contents.length - idealLength;
                            lengthDiff=9+lengthDiff;
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < contents.length; i++) {
                                stringBuilder.append(contents[i]);
                                if (i != contents.length - 1) {
                                    if (i < 9 || i >= lengthDiff) {
                                        stringBuilder.append("::");
                                    } else {
                                        stringBuilder.append(",");
                                    }
                                }
                            }
                            content = stringBuilder.toString();
                        }
                        return content;
                    })
                    .forEachOrdered(customFileWriter::println);
            customFileWriter.close();
        } catch (Exception ex) {
            System.out.println("fixFileEscapeIssue :: got error while processing - " + ex.getMessage());
        }


    }


}
