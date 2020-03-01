package com.pailsom.coronavirustracker;

import com.pailsom.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoronaVirusService {

    private static final String VIRUS_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    private List<LocationStats> locationStats = Arrays.asList();
    private CloseableHttpClient client = HttpClients.createDefault();
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetch() throws IOException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpGet request = new HttpGet(VIRUS_URL);
        try(CloseableHttpResponse response = client.execute(request)){
            System.out.println(response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity!=null){
                Reader reader = new InputStreamReader(entity.getContent());
                Iterable<CSVRecord> records =  CSVFormat.DEFAULT.withFirstRecordAsHeader().parse( reader);
                for (CSVRecord csvRecord : records){
                    LocationStats locationStats = new LocationStats();
                    locationStats.setState(csvRecord.get("Province/State"));
                    locationStats.setCountry(csvRecord.get("Country/Region"));
                    int totalCases = Integer.parseInt(csvRecord.get(csvRecord.size()-1));
                    int prevDayTotalCases = Integer.parseInt(csvRecord.get(csvRecord.size()-2));
                    locationStats.setLastTotalCases(totalCases);
                    locationStats.setDiffFromPreDay(totalCases-prevDayTotalCases);
                    newStats.add(locationStats);
                }
                this.locationStats=newStats;
            }
        }
    }

    public List<LocationStats> getStats() {
        return locationStats;
    }
}
