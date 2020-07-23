package guru.learningjournal.kafka.examples;
import java.io.File;
import java.io.IOException;
class AppConfigs {
    final static String applicationID = "Multi-Threaded-Producer";
    final static String topicName = "nse-eod-topic";
    final static String path = "LearningJournal/03-multi-threading/";
    final static String kafkaConfigFileLocation = path+"kafka.properties";
    final static String[] eventFiles = {path+"data/NSE05NOV2018BHAV.csv",path+"data/NSE06NOV2018BHAV.csv"};
}
