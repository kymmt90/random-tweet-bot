/*
 * Copyright 2014 Kohei Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rtb;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RandomTweetBotMain {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar random-tweet-bot-<version>.jar <property_file_name>");
            System.exit(1);
        }

        BotProperties properties = new BotProperties();
        try {
            properties.load(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String filePath = properties.filePath();
        String screenName = properties.screenName();
        final int favThreshold = properties.favCount();
        final long intervalMinutes = properties.intervalMinutes();

        PopularTweetCollector collector = new PopularTweetCollector();
        RandomTweetBot bot = new RandomTweetBot();
        
        Timer timer = new Timer();
        TimerTask collectorTask
            = new PopularTweetCollectorTask(filePath, screenName, favThreshold, collector);
        TimerTask botTask = new RandomTweetBotTask(filePath, bot);
        timer.scheduleAtFixedRate(collectorTask, 0L, TimeUnit.DAYS.toMillis(1L));
        timer.scheduleAtFixedRate(botTask, TimeUnit.SECONDS.toMillis(30L), TimeUnit.MINUTES.toMillis(intervalMinutes));
    }
}
