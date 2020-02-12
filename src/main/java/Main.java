import download.CloudStorageClient;
import download.MongoDBClient;
import org.apache.log4j.Logger;
import service.LogService;
import service.VerificationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;


@SuppressWarnings("WeakerAccess")

public class Main {
   // private static final String PROPS_PATH = "/s3_backup_verification/configs/";
    private static final String PROPS_PATH = "/tmp/configs/";
    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {

        try {
            Properties appProps = loadProperties();

            MongoDBClient mongoDBClient = new MongoDBClient(appProps);
            LogService logService = new LogService();
            CloudStorageClient cloudStorageClient = new CloudStorageClient(appProps);

            VerificationService verificationService = new VerificationService(mongoDBClient, cloudStorageClient, logService);

            log.info("IS COMPLETED: " + cloudStorageClient.isCompletedVerification());
            verificationService.verify();
            //upload report to AWS
            cloudStorageClient.uploadReportLogToAws(logService.getLogFileName());
        } catch (IOException e) {
            log.error(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }


    private static Properties loadProperties() throws IOException {
        File file = new File(PROPS_PATH + "application.properties");
        String appPath;
        if (file.exists()) {
            appPath = file.getPath();
        } else {
            appPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.properties")).getPath();
        }
        System.out.println("appPath = " + appPath);
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appPath));
        log.info("(Additional info) AppProps ready");
        return appProps;
    }

}
