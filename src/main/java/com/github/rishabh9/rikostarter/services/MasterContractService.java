package com.github.rishabh9.rikostarter.services;

import com.github.rishabh9.riko.upstox.common.constants.Exchanges;
import com.github.rishabh9.riko.upstox.users.UserService;
import com.github.rishabh9.rikostarter.models.MasterContract;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static com.github.rishabh9.rikostarter.services.MasterContractService.MasterContractHeaders.*;

@Log4j2
@Service
public class MasterContractService {

    @Autowired
    private UserService userService;

    public void downloadIndividualMasterContract() {
        log.info("Downloading master contract for ACC");
        userService.getMasterContract(Exchanges.NSE_EQUITY, "ACC", null)
                .thenAccept(response -> log.info("Downloaded contract for ACC: {}", response.getData()));
    }

    //@Scheduled(fixedDelay = 600000, initialDelay = 1000)
    public void downloadAllContracts() {
        log.info("Downloading all master contract for {}", Exchanges.NSE_EQUITY);
        userService.getAllMasterContracts(Exchanges.NSE_EQUITY)
                .whenComplete(((inputStream, throwable) -> {
                    if (null != throwable) {
                        log.fatal("An error occurred while downloading all master contracts", throwable);
                    } else {
                        try {
                            readAndSaveJsonStream(inputStream);
                        } catch (IOException e) {
                            log.fatal("Error in reading response input stream", e);
                        }
                    }
                }));
    }

    private void readAndSaveJsonStream(final InputStream in) throws IOException {
        final Gson gson = new Gson();
        final JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        boolean flag = false;
        while (reader.hasNext()) {
            JsonToken nextToken = reader.peek();
            switch (nextToken) {
                case BEGIN_ARRAY:
                    reader.beginArray();
                    break;
                case BEGIN_OBJECT:
                    reader.beginObject();
                    break;
                case END_ARRAY:
                    reader.endArray();
                    break;
                case END_OBJECT:
                    reader.endObject();
                    break;
                case END_DOCUMENT:
                    reader.close();
                    break;
                case NAME:
                    flag = "data".equalsIgnoreCase(reader.nextName());
                    break;
                case BOOLEAN:
                    // Need to consume, to proceed to next token
                    reader.nextBoolean();
                    break;
                case STRING:
                    // Need to consume, to proceed to next token
                    reader.nextString();
                    break;
                case NUMBER:
                    // Need to consume, to proceed to next token
                    reader.nextDouble();
                    break;
                case NULL:
                    // Need to consume, to proceed to next token
                    reader.nextNull();
                    break;
                default:
                    break;
            }
            if (flag) {
                // exit while loop
                break;
            }
        }

        boolean isHeader = true;
        String[] header = new String[0];
        String[] record;

        reader.beginArray();
        while (reader.hasNext()) {
            final String message = gson.fromJson(reader, String.class);
            if (isHeader) {
                log.info("Master Contract header: {}", message);
                header = message.split(",");
                isHeader = false;
                continue;
            }

            final MasterContract masterContract = new MasterContract();
            record = message.split(",");
            // Parse all headers and columns for the record
            for (int i = 0; i < record.length; i++) {
                buildMasterContract(masterContract, header[i], record[i]);
            }
            log.info("Master Contract row: {}", masterContract);
        }
        reader.endArray();
        reader.close();
    }

    private void buildMasterContract(final MasterContract document, final String header, final String value) {
        switch (header) {
            case STRIKE_PRICE:
                if (!Strings.isNullOrEmpty(value))
                    document.setStrikePrice(new BigDecimal(value));
                break;
            case CLOSING_PRICE:
                if (!Strings.isNullOrEmpty(value))
                    document.setClosingPrice(new BigDecimal(value));
                break;
            case LOT_SIZE:
                if (!Strings.isNullOrEmpty(value))
                    document.setLotSize(Integer.parseInt(value));
                break;
            case TICK_SIZE:
                if (!Strings.isNullOrEmpty(value))
                    document.setTickSize(Float.parseFloat(value));
                break;
            case EXCHANGE:
                document.setExchange(value.trim());
                break;
            case TOKEN:
                document.setToken(value.trim());
                break;
            case PARENT_TOKEN:
                document.setParentToken(value.trim());
                break;
            case SYMBOL:
                document.setSymbol(value.trim());
                break;
            case NAME:
                document.setName(value.trim());
                break;
            case EXPIRY:
                document.setExpiry(value.trim());
                break;
            case INSTRUMENT_TYPE:
                document.setInstrumentType(value.trim());
                break;
            case ISIN:
                document.setIsin(value.trim());
        }
    }

    final class MasterContractHeaders {
        static final String STRIKE_PRICE = "strike_price";
        static final String CLOSING_PRICE = "closing_price";
        static final String LOT_SIZE = "lot_size";
        static final String TICK_SIZE = "tick_size";
        static final String EXCHANGE = "exchange";
        static final String TOKEN = "token";
        static final String PARENT_TOKEN = "parent_token";
        static final String SYMBOL = "symbol";
        static final String NAME = "name";
        static final String EXPIRY = "expiry";
        static final String INSTRUMENT_TYPE = "instrument_type";
        static final String ISIN = "isin";
    }
}
