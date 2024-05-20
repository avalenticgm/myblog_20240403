package it.cgmconsulting.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BannerService {

    public ResponseEntity<String> getBanner(String campaignId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", campaignId);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = "http://localhost:8099/banner/api/campaigns";
        ResponseEntity<String> banner = null;
        try{
            banner = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return banner;
    }
}
