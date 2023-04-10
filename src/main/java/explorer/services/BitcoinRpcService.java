package explorer.services;


import explorer.model.BlockIncludingTransactionDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BitcoinRpcService {

    @Value("${bitcoin.node.url}")
    String bitcoin_node_url;

    public Integer getBlockCount()  {
        HttpResponse<String> response = Unirest.post(bitcoin_node_url)
                .header("content-type", "text/plain;")
                .body("{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\": []}")
                .asString();
        return (Integer) new JSONObject(response.getBody()).get("result");
    }

    public String getBlockHash(int height)  {
        HttpResponse<String> response = Unirest.post(bitcoin_node_url)
                .header("content-type", "text/plain;")
                .body("{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"getblockhash\", \"params\": [ " + height + "]}")
                .asString();
        return (String) new JSONObject(response.getBody()).get("result");
    }

    public BlockIncludingTransactionDetails getBlockIncludingTransactions(String blockhash) {
        HttpResponse<String> response = Unirest.post(bitcoin_node_url)
                .header("content-type", "text/plain;")
                .body("{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"getblock\", \"params\": [\"" + blockhash +  "\" " + ",2" + "]}")
                .asString();
        try {
            return new ObjectMapper().readValue(new JSONObject(response.getBody()).get("result").toString(), BlockIncludingTransactionDetails.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
