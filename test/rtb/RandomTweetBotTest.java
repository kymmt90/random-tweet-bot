/*
 * Copyright 2015 Kohei Yamamoto
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(Enclosed.class)
public class RandomTweetBotTest {
    public static class JSONファイルsingleを読み込ませた場合 {
        RandomTweetBot sut;
        int retValreadJsonFile;

        @Before
        public void setUp() throws Exception {
            sut = new RandomTweetBot(true);
            retValreadJsonFile = sut.readJsonFile("test/rtb/single.json");
        }

        @Test
        public void readJsonFileで1が返る() throws Exception {
            assertThat(retValreadJsonFile, is(1));
        }

        @Test
        public void nextTweetでテキスト1が返る() throws Exception {
            String actual = sut.nextTweet();
            assertThat(actual, is("1 [0 fav] [1990-01-18]"));
        }

        @Test
        public void getNumTweetsで1が返る() throws Exception {
            int actual = sut.getNumTweets();
            assertThat(actual, is(1));
        }

        @Test
        public void getTweetで1が返る() throws Exception {
            String actual = sut.getTweet(0);
            assertThat(actual, is("1 [0 fav] [1990-01-18]"));
        }

        @Test(expected = IndexOutOfBoundsException.class)
        public void getTweetに引数1でIndexOutOfBoundsExceptionが投げられる() throws Exception {
            sut.getTweet(1);
        }

        @Test(expected = IndexOutOfBoundsException.class)
        public void getTweetに引数2でIndexOutOfBoundsExceptionが投げられる() throws Exception {
            sut.getTweet(2);
        }
    }

    public static class JSONファイルmultipleを読み込ませた場合 {
        RandomTweetBot sut;
        int retValreadJsonFile;

        @Before
        public void setUp() throws Exception {
            sut = new RandomTweetBot(true);
            retValreadJsonFile = sut.readJsonFile("test/rtb/multiple.json");
        }

        @Test
        public void readJsonFileで2が返る() throws Exception {
            assertThat(retValreadJsonFile, is(2));
        }

        @Test
        public void getNumTweetsで2が返る() throws Exception {
            int actual = sut.getNumTweets();
            assertThat(actual, is(2));
        }

        @Test
        public void getTweetに引数0で1が得られる() throws Exception {
            assertThat(sut.getTweet(0), is("1 [0 fav] [1990-01-18]"));
        }

        @Test
        public void getTweetに引数1で2が得られる() throws Exception {
            assertThat(sut.getTweet(1), is("2 [0 fav] [1990-01-18]"));
        }

        @Test
        public void extTweetでテキスト1か2が返る() throws Exception {
            boolean one = false, two = false;
            for (int i = 0; i < 100; ++i) {
                if (sut.nextTweet().equals("1 [0 fav] [1990-01-18]")) one = true;
                if (sut.nextTweet().equals("2 [0 fav] [1990-01-18]")) two = true;
            }
            assertThat(one, is(true));
            assertThat(two, is(true));
        }

        @Test
        public void nextTweetでprevTweetと異なるツイートが返る() throws Exception {
            sut.setPrevTweet("1");
            String actual = sut.nextTweet();
            assertThat(actual, is(not("1")));
        }
    }

    public static class その他の場合 {
        RandomTweetBot sut;

        @Before
        public void setUp() throws Exception {
            sut = new RandomTweetBot(true);
        }

        @Test
        public void インスタンス生成時にtweetsはsize0でprevTweetはnull() throws Exception {
            assertThat(sut.getNumTweets(), is(0));
            assertThat(sut.getPrevTweet(), is(nullValue()));
        }

        @Test(expected = JsonMappingException.class)
        public void readJsonFileにデータがないJSONファイルのパスを渡すとJsonMappingExceptionが投げられる() throws Exception {
            sut.readJsonFile("test/rtb/empty.json");
        }
        @Test(expected = IOException.class)
        public void readJSONFileに存在しないJSONファイルのパスを渡すとIOExceptionが投げられる() throws Exception {
            sut.readJsonFile("test/rtb/notfound.json");
        }

        @Test(expected = JsonProcessingException.class)
        public void readJSONFileにinvalidなJSONファイルのパスを渡すとJsonProcessingExceptionが投げられる() throws Exception {
            sut.readJsonFile("test/rtb/invalid.json");
        }
        
        @Test
        public void reply_test_true() throws Exception {
            assertThat(sut.isReply("@test_user test"), is(true));
        }
        
        @Test
        public void reply_test_false() throws Exception {
            assertThat(sut.isReply(".@test_user test"), is(false));
        }
    }
    
    public static class JSONファイルreplyを読み込ませreplyがtrueの場合 {
        RandomTweetBot sut;
        
        @Before
        public void setUp() throws Exception {
            sut = new RandomTweetBot(true);
            sut.readJsonFile("test/rtb/reply.json");
        }
        
        @Test
        public void test() throws Exception {
            boolean reply = false, notReply = false;
            for (int i = 0; i < 100; ++i) {
                if (sut.nextTweet().equals("@test_user test [0 fav] [1990-01-18]")) reply = true;
                if (sut.nextTweet().equals(".@test_user test [0 fav] [1990-01-18]")) notReply = true;
            }
            assertThat(reply, is(true));
            assertThat(notReply, is(true));
        }
    }
    
    public static class JSONファイルreplyを読み込ませreplyがfalseの場合 {
        RandomTweetBot sut;
        
        @Before
        public void setUp() throws Exception {
            sut = new RandomTweetBot(false);
            sut.readJsonFile("test/rtb/reply.json");
        }
        
        @Test
        public void test_getTweet() throws Exception {
            assertThat(sut.nextTweet(), is(".@test_user test [0 fav] [1990-01-18]"));
        }
        
        @Test
        public void test_acceptTweet() throws Exception {
            assertThat(sut.acceptTweet("@test_user test [0 fav] [1990-01-18"), is(false));
        }
    }
}
