package com.br.log4b.crawler;

import java.util.Arrays;

public class ScrapingController {

	public static void main(String[] args) throws Exception {
		HnVisaScraping hnVisaScraping = new HnVisaScraping(Arrays.asList(args));
		hnVisaScraping.start();
	}
}
