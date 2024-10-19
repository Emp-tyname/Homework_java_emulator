package com.example.newMock.Controller;
//nohup java -jar newMock-0.0.1-SNAPSHOT.jar --server.port=8081 Xms1g Xmx1g> newMock_8081.log -- СХОРОНИЛ

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency;
            String rquid = requestDTO.getRqUID();


            if (firstDigit == '8'){
                maxLimit = new BigDecimal(2000);
                currency = "USD";
            }
            else if (firstDigit == '9'){
                maxLimit = new BigDecimal(1000);
                currency = "EU";
            }
            else {
                maxLimit  = new BigDecimal(10000);
                currency = "RUB";
            }
            double balance = Integer.valueOf(maxLimit.intValue()) * Math.random();
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setRqUID(rquid);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount()); // Можно так вытащить
            responseDTO.setBalance(new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP));
            responseDTO.setMaxLimit(maxLimit);
            responseDTO.setCurrency(currency);
            log.info("******** RequestDTO ********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("******** ResponseDTO ********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));
            return responseDTO;
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
