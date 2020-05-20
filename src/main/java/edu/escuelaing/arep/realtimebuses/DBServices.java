/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arep.realtimebuses;
//import com.mashape.unirest.http.JsonNode;
import com.amazonaws.regions.Regions;
import edu.escuelaing.arep.realtimebuses.apigateway.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
//import org.json.JSONObject;
//import java.net.*;
import java.util.Map;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

/**
 *
 * @author Santiago
 */
public class DBServices implements RequestHandler<ApiGatewayRequest, ApiGatewayProxyResponse>  {
    
    @Override
    public ApiGatewayProxyResponse handleRequest(ApiGatewayRequest i, Context cntxt) {
        
        Map<String, String> parameters = i.getQueryStringParameters();
        
        String ID = parameters.get("IDBus"); 
        String lati = parameters.get("Latitud"); 
        String longi = parameters.get("Longitud"); 
        
        double lat = Double.parseDouble(lati);
        double lon = Double.parseDouble(longi);
        
        
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("RealTimeBuses");

        try {

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("IDBus", ID)
                .withUpdateExpression("set Latitud= :val1 , Longitud= :val2")
                .withValueMap(new ValueMap().withNumber(":val1", lat).withNumber(":val2", lon))
                .withReturnValues(ReturnValue.UPDATED_NEW);
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after adding new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Failed to add new attribute in " + table);
            System.err.println(e.getMessage());
            Item item = new Item().withPrimaryKey("IDBus", 120)
                    .withNumber("Latitud", lat)
                    .withNumber("Longitud", lon);
            table.putItem(item);
            
        }
        
        
        
        Map<String, String> headers=i.getHeaders();
        ApiGatewayProxyResponse response = new ApiGatewayProxyResponse(200,headers,ID+" "+Double.toString(lat)+" "+Double.toString(lon));
        System.out.println(i.toString());
        return response;
    }
}
