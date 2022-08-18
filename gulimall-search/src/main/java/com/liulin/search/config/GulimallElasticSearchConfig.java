package com.liulin.search.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GulimallElasticSearchConfig {

    @Bean
     public RestHighLevelClient esRestClient(){
         RestHighLevelClient client = new RestHighLevelClient(
                 RestClient.builder(new HttpHost("192.168.137.14", 9200, "http")));
         return  client;
     }


}
