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
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.mashape.unirest.http.JsonNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONObject;

/**
 *
 * @author Santiago
 */
public class DBServices /*implements RequestHandler<ApiGatewayRequest, ApiGatewayProxyResponse>  */{
    
    /*@Override*/
    public static String handleRequest() {
        
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("RealTimeBuses");

        ArrayList <String> itemStr= new ArrayList<String>();
        
        ScanSpec scanSpec = new ScanSpec().withProjectionExpression("IDBus, Latitud, Longitud");

        ItemCollection<ScanOutcome> items = table.scan(null, // FilterExpression
                "IDBus, Latitud, Longitud", // ProjectionExpression
                null, // ExpressionAttributeNames - not used in this example
                null);

            System.out.println("Scan of RealTimeBuses for items ");
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                
                String itemObj= iterator.next().toJSON();
                
                itemStr.add(itemObj);
               
            }
        
        return Arrays.toString(itemStr.toArray());
    }
}
