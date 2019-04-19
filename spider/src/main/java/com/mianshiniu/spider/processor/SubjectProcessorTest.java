package com.mianshiniu.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Vim 2019/2/20 10:10
 *
 * @author Vim
 */
public class SubjectProcessorTest implements PageProcessor {

    private Site site = Site.me().setDomain("www.nowcoder.com");

    @Override
    public void process(Page page) {
        String txt = getTxt("E:/subject.txt");
        page.setRawText(txt);

        page.getHtml().regex("");
    }

    private String getTxt(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new SubjectProcessorTest()).addUrl("https://www.nowcoder.com/test/question/done?tid=21218449&qid=313049#summary")
                .addPipeline(new ConsolePipeline()).run();
    }
}
