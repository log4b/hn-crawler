package com.br.log4b.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class HnVisaScraping {

    private final List<String> searchingKey;

    private final Month month;

    private final String finalPath;

    private final String FIELD_SEPARATOR = "||";

    public HnVisaScraping(List<String> searchingKey) {
        month = LocalDate.now().getMonth();

        if(null == searchingKey || searchingKey.size() == 0)    {
            throw new IllegalArgumentException("You should specify at least one word to be found");
        } else {
            this.searchingKey = searchingKey;
        }

        final String path = System.getenv("path");

        if(null == path)    {
            throw new IllegalArgumentException("You should set a 'path' environment variable");
        } else {
            finalPath = path + "/" + month + "/" + "visa.cvs";
        }
    }

    public void start() {
        try {
            //TODO search for the correct URL based on current month/year
            final Document rootDocument = Jsoup.connect("https://news.ycombinator.com/item?id=14023198").get();

            final Elements rootComments = rootDocument.getElementsByClass("athing");

            StringBuilder builder = new StringBuilder();

            rootComments
                    .parallelStream()
                    .forEach(r -> r.getElementsByClass("comment")
                            .parallelStream()
                            .filter(comment -> searchingKey.stream()
                                    .anyMatch(key -> comment.text().contains(key)))
                            .forEach(filtered -> builder.append(r.id())
                                    .append(FIELD_SEPARATOR)
                                    .append(filtered.text().replace("\n", ""))
                                    .append("\n")));

            storePost(builder.toString());
        } catch (IOException e) {
            System.err.println("Cannot connect to the url");
        }
    }

    private void storePost(String content)    {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(finalPath))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("An error has occurred. Cannot store the comments on path");
        }
    }

}
