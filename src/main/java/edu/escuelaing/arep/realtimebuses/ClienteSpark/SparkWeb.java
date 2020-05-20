package edu.escuelaing.arep.realtimebuses.ClienteSpark;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spark.*;
import static spark.Spark.*;
/**
 *
 * @author santiago.vega-r
 */
public class SparkWeb {
    public static void main( String[] args )
    {
        port(getPort());
          
        get("/", (req, res) -> inputDataPage(req, res));
        get("/results", (req, res) -> resultsPage(req, res));
   
    }
    
     private static String inputDataPage(Request req, Response res) {
        String pageContent
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<h2>Planeación de rutas Transmilenio</h2>"
                + "<form action=\"/results\">"
                + "  Ingrese numero identificación del bus <br>"
                + "  <input type=\"text\" name=\"number\" >"
                + "  <br><br>"
				+ "  Ingrese numero identificación de la ruta <br>"
	            + "  <input type=\"text\" name=\"number1\" >"
	            + "  <input type=\"submit\" value=\"Submit\"> "
                + "  <br><br>"
                + "</form>"
                + "</body>"
                + "</html>";
        return pageContent;
    }

    private static String resultsPage(Request req, Response res) throws IOException, InterruptedException {
        
        BufferedReader in = null;
        
        /**ArrayList Longitud = new ArrayList();
        ArrayList Latitud = new ArrayList();*/
        
        List<?> Latitud = new ArrayList<>(Arrays.asList(4.755457,4.747224,4.728404,4.724769,4.722101,4.715146,4.705795,4.697984,4.694586,4.685802,4.677188,4.672927,4.666853,4.659138,4.651337,4.645482,4.638397,4.626211,4.622228,4.617293));
        List<?> Longitud = new ArrayList<>(Arrays.asList(-74.046128,-74.047179,-74.050375,-74.051024,-74.051469,-74.052665,-74.054194,-74.055476,-74.056064,-74.057582,-74.062104,-74.069169, -74.074458, -74.077446,-74.078363,-74.078830,-74.079372,-74.081856,-74.085284, -74.089420));
        
       
       
       String request= req.queryParams("number");
       String request1= req.queryParams("number1");
       
       for(int i=0;i<20;i++) {
    	//   System.out.println(Longitud.get(i));
       
    		   
    	   URL API = new URL("https://9i6rk1q1dl.execute-api.us-east-1.amazonaws.com/beta?IDBus="+request+"&Latitud="+Latitud.get(i)+"&Longitud="+Longitud.get(i));
		
        URLConnection con = API.openConnection();
        
        String result = "";
        in = new BufferedReader(new InputStreamReader( con.getInputStream()));
		
		System.err.println("Conectado");
		BufferedReader stdIn = new BufferedReader(
		new InputStreamReader(System.in));
		String line;
		
		while ((line = in.readLine()) != null) { 
                        result=result+line;
			System.out.println(line); 
		}
		
		Thread.sleep(8000);
       } 
        
        String pageContent
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<h3>Ruta: "+ request1+" </h3>"
                +"<p>Iniciada Correctamente</p>"
                + "</body>"
                + "</html>";
        
        return pageContent;
    }
    
    
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }        
           
        return 4567; //returns default port if heroku-port isn't set(i.e. on localhost)    }
    }
}
