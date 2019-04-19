package com.mianshiniu.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by Vim 2019/2/20 10:04
 *
 * @author Vim
 */
public class SpiderProcessor implements PageProcessor {
    private Site site = Site.me().setDomain("my.oschina.net");

    @Override
    public void process(Page page) {
        List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
        page.addTargetRequests(links);
        page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
        page.putField("content", page.getHtml().$("div.content").toString());
        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new SpiderProcessor()).addUrl("http://my.oschina.net/flashsword/blog")
                .addPipeline(new ConsolePipeline()).run();
    }
}
