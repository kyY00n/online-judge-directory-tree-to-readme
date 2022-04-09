import directory.ProblemSolvingDirectory;
import judge.OnlineJudgeResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        final String workspace = System.getenv().get("GITHUB_WORKSPACE");
        logger.info("WORKSPACE PATH : " + workspace);
        final String hostRepositoryUrl = System.getenv().get("GITHUB_SERVER_URL") + "/" + System.getenv().get("GITHUB_REPOSITORY") + "/tree" + "/" + System.getenv().get("GITHUB_REF_NAME"); // owner/repositoryName
        logger.info("HOST REPOSITORY URL : " + hostRepositoryUrl);

        final File directory = new File(workspace);
        logger.info("PATH : " + directory.getPath());
        logger.info("ABSOLUTE PATH : " + directory.getAbsolutePath());

        List<ProblemSolvingDirectory> directories = Arrays.stream(directory.listFiles())
                .filter(OnlineJudgeResolver::isImplemented)
                .map(f -> OnlineJudgeResolver.resolve(f, hostRepositoryUrl))
                .toList();

        String markdown = directories.stream()
                .map(ProblemSolvingDirectory::fileTreeToMarkdown)
                .collect(Collectors.joining());

        File readme = new File("README.md");
        try {
            logger.info(markdown);
            Files.writeString(readme.toPath(), markdown, StandardOpenOption.WRITE);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }

    }

}
