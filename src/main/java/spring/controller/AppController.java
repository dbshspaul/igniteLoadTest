package spring.controller;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.transactions.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by debasish paul on 07-05-2018.
 */
@RestController
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    @Autowired
    Ignite ignite;

    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello(){
        return "hello world";
    }

    @GetMapping(value = "/cache", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllCacheData(@RequestParam("cache") String cacheName) {
        Map<Object, Object> data = new HashMap<>();
        try {
            LOGGER.info("Reading cache "+cacheName);
            IgniteCache<String, Object> cache = ignite.getOrCreateCache(cacheName);
            Iterator<Cache.Entry<String, Object>> iter = cache.iterator();
            while (iter.hasNext()) {
                Cache.Entry<String, Object> entry = iter.next();
                data.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("Error: " + e.getMessage());
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (data.isEmpty()) {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Object>(data, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/putString", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> putCacheData(@RequestParam(value = "key") String key,
                                          @RequestParam(value = "value") String value,
                                          @RequestParam("cache") String cacheName) {
        return putDataInCache(key, value, cacheName);
    }

    private ResponseEntity<?> putDataInCache(String key, Object value, String cacheName) {
        Map<Object, Object> data = new HashMap<>();
        String errMsg = "";
        try {
            IgniteCache<String, Object> cache = ignite.getOrCreateCache(cacheName);
            cache.put(key, value);
            LOGGER.info("Data inserted in cache successfully.");
        } catch (TransactionException e) {
            errMsg = e.getMessage();
            LOGGER.error("Data insertion failed. ", e.getMessage());
        }

        if (errMsg != "") {
            data.put("msg", errMsg);
            return new ResponseEntity<Object>(data, HttpStatus.NOT_MODIFIED);
        } else {
            data.put("msg", "Data inserted in cache successfully.");
            return new ResponseEntity<Object>(data, HttpStatus.OK);
        }
    }
}
