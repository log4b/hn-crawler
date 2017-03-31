package com.br.log4b.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HnVisaScraping {

    private final List<String> searchingKey;

    public HnVisaScraping(List<String> searchingKey) {
        if(null == searchingKey || searchingKey.size() == 0)    {
            throw new IllegalArgumentException("You should specify at least one word to be found");
        }
        this.searchingKey = searchingKey;
    }

    public void start() {
        try {
            //TODO Change the application to find the url itself considering the present month
            final Document rootDocument = Jsoup.connect("https://news.ycombinator.com/item?id=13764728").get();

            final Elements comments = rootDocument.getElementsByClass("comment");

            //TODO Store data somewhere
            comments.parallelStream()
                    .filter(comment -> searchingKey
                            .stream()
                            .anyMatch(key -> comment.text().contains(key)))
                    .collect(Collectors.toList()).forEach(u -> System.out.println(u));

        } catch (IOException e) {
            //TODO Add logback to dependecies and log exception
            e.printStackTrace();
        }
    }

}
