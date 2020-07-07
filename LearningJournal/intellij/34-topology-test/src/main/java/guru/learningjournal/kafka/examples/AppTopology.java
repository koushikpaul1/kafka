package guru.learningjournal.kafka.examples;

import guru.learningjournal.kafka.examples.serde.AppSerdes;
import guru.learningjournal.kafka.examples.types.AdClick;
import guru.learningjournal.kafka.examples.types.AdImpression;
import guru.learningjournal.kafka.examples.types.CampaignPerformance;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppTopology {
    private static final Logger logger = LogManager.getLogger();

    static void withBuilder(StreamsBuilder builder) {
        KStream<String, AdImpression> KS0 = builder.stream(AppConfigs.impressionTopic,
            Consumed.with(AppSerdes.String(), AppSerdes.AdImpression()));

        KTable<String, Long> adImpressionCount = KS0.groupBy((k, v) -> v.getCampaigner(),
            Grouped.with(AppSerdes.String(), AppSerdes.AdImpression()))
            .count();

        KStream<String, AdClick> KS1 = builder.stream(AppConfigs.clicksTopic,
            Consumed.with(AppSerdes.String(), AppSerdes.AdClick()));

        KTable<String, Long> adClickCount = KS1.groupBy((k, v) -> v.getCampaigner(),
            Grouped.with(AppSerdes.String(), AppSerdes.AdClick()))
            .count();

        KTable<String, CampaignPerformance> campaignPerformance = adImpressionCount.leftJoin(
            adClickCount, (impCount, clkCount) -> new CampaignPerformance()
                .withAdImpressions(impCount)
                .withAdClicks(clkCount))
            .mapValues((k, v) -> v.withCampaigner(k));

        campaignPerformance.toStream().foreach((k, v) -> logger.info(v));
    }
}
