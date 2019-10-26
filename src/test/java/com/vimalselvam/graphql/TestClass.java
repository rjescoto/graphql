package com.vimalselvam.graphql;

import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.testng.Assert;
import org.testng.annotations.Test;

import okhttp3.*;

/**
 * Test
 * http://www.vimalselvam.com/2019/06/02/introducing-test-graphql-java/
 * Let’s test the Pokemon GraphQL API. We’re going to test the following query:

query pokemon {
  pokemon(name: "Pikachu") {
    name
  }
}

 */
public class TestClass {
    private static final OkHttpClient client = new OkHttpClient();
    private final String graphqlUri = "https://graphql-pokemon.now.sh/graphql";

    private Response prepareResponse(String graphqlPayload) throws IOException {
		@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.  create(MediaType.get("application/json; charset=utf-8"), graphqlPayload);
        Request request = new Request.Builder().url(graphqlUri).post(body).build();
        return client.newCall(request).execute();
    }
    
/*
    private void ExecuteTestType(String tt){
    	switch(tt) {
    	case "get member by name":ExecuteByName();break;
    	case  "get member by id":ExecuteById();break;
    	default:IllegalTestType();break;
    	}
    }
    
    private void ExecuteByName(){}
    private void ExecuteById(){}  
    */
    
    @Test
    public void testGraphqlWithInputStream() throws IOException {
        // Read a graphql file as an input stream
        InputStream iStream = TestClass.class.getResourceAsStream("/graphql/pokemon.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", "Pikachu");

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(iStream, variables);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        Assert.assertEquals(jsonNode.get("data").get("pokemon").get("name").asText(), "Pikachu");
    }

    @Test
    public void testGraphqlWithFileAndVariables() throws IOException {
        // Read a graphql file
        File file = new File("src/test/resources/graphql/pokemon.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", "Pikachu");

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        Assert.assertEquals(jsonNode.get("data").get("pokemon").get("name").asText(), "Pikachu");
    }

    @Test
    public void testGraphqlFirstNWithFileAndVariables() throws IOException {
        // Read a graphql file
        File file = new File("src/test/resources/graphql/pokemons-first-n.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("first", 1000);

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        //  strng to search:   "number":"001","name":"Bulbasaur"
        
        String jsonData = response.body().string();
        String quote="\"";
        String fragment="number"+quote + ":"+quote+"001"+quote +","+quote + "name"+quote+":"+quote+"Bulbasaur";
        boolean hasFragment=jsonData.contains(fragment);
        System.out.println(jsonData);
       //test that a known member exissts
        Assert.assertTrue(hasFragment);
        //test that at least n (10)  members were returned  "number":"010"
        boolean hasCount=jsonData.contains(quote+"number"+quote+":"+quote+"010"+quote);      
        Assert.assertTrue(hasCount);
    }

    
    @Test
    public void testGraphqlWithNoVariables() throws IOException {
        // Read a graphql file
        File file = new File("src/test/resources/graphql/pokemon-with-no-variable.graphql");

        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), 200, "Response Code Assertion");

        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        Assert.assertEquals(jsonNode.get("data").get("pokemon").get("name").asText(), "Pikachu");
    }
  
    /*  
    @Test(dataProvider="TestDataProvider")
    public void testMethod(String author,String searchKey) throws InterruptedException{

    public void testGraphqlViaTDD(String test_type , String IN1, String IN2,String EO1,String EO2,String EO3,int EO4) throws IOException {
        // Read a graphql file
        File file = new File("src/test/resources/graphql/pokemon-with-no-variable.graphql");

        //GetParamsFromTestData()
        //ExecuteTestType(test_type,IN1,IN2,EO1,EO2,EO3);
        // Now parse the graphql file to a request payload string
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload);

        Assert.assertEquals(response.code(), EO4, "Response Code Assertion");

        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        Assert.assertEquals(jsonNode.get("data").get("pokemon").get("name").asText(), "Pikachu");
    }
*/
    
}
