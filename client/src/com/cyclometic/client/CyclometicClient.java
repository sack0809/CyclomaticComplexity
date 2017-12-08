package com.cyclometic.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

///	import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;




@Path("/CyclometicClient") 
public class CyclometicClient {
	
	private static final String REMOTE_URL = "https://github.com/sack0809/ScalableComputing.git";
	private static final long serialVersionUID = 1L;
	private static final String RESTURL = "http://localhost:8080/ServerRESTAPI/rest/CyclometicServer/graph";
	
	@GET
    @Path("/me")
	@Produces(MediaType.APPLICATION_JSON )

	public int cc() throws GitAPIException {
		    HttpURLConnection connection = null;	
		    BufferedReader reader = null;
		    String json = null;
		    int sum = 0;
		    String test=null;
		   //HashMap<Integer,String> val=new HashMap<Integer,String>();
		    List <Integer> cyclo = new ArrayList();
		    
		    try {			
		      URL resetEndpoint = new URL("http://localhost:8080/ServerRESTAPI/rest/CyclometicServer/main");
		      connection = (HttpURLConnection) resetEndpoint.openConnection();
		      // Set request method to GET as required from the API
		      connection.setRequestMethod("GET");
		      
		      connection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
		     connection.setRequestProperty ("Content-Encoding","enctype=multipart/form-data");	      
		      if (connection.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ connection.getResponseCode());
				}


		      // Read the response
		      BufferedReader br = new BufferedReader(new InputStreamReader(
		  			(connection.getInputStream())));
               
		  
		      
		        String output;
				System.out.println("Output from Server .... \n");
				StringBuilder jsonSb = new StringBuilder();
				String line = br.readLine();
				if(line.equals("END")) {
					//do the exit
				}
				while (line != null) {
					jsonSb.append(line);
				}
				output = jsonSb.toString();

				
				ObjectId newval = ObjectId.fromString(output);
		    	
				try (Git result = Git.cloneRepository()
		                 .setURI(REMOTE_URL)
		                 .setDirectory(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repoClient"))
		                  .setCloneAllBranches(true)
		                  
		                 .call()) {
		 	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
		 	        System.out.println("Having repository: " + result.getRepository().getDirectory());
		 	        
		         }
		    	
				
		       FileRepositoryBuilder builder = new FileRepositoryBuilder();
		        Repository repository = builder
		                .setGitDir(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repo/.git")).readEnvironment()
		                .findGitDir().build();

		        //listRepositoryContents(repository);
		        //getfile(repository);
		        //repository.close();
				
				@SuppressWarnings("resource")
				RevWalk walk = new RevWalk(repository);
	            RevCommit commit = walk.parseCommit(newval);
	            RevTree tree = commit.getTree();
                System.out.println("Having tree: " + tree);
                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    
                    while (treeWalk.next()) {
                       //System.out.println("found: " +treeWalk.getPathString());
                         
                    	ObjectId objectId = treeWalk.getObjectId(0);
                        ObjectLoader loader = repository.open(objectId);
                        File newFile = new File("tempReadFile");
                		  loader.copyTo(new FileOutputStream(newFile));
                		  CyclometicCalculation cal = new CyclometicCalculation();
                		     String temp1= Integer.toString(cal.calculateCC(newFile));
                		      
                		   System.out.println("found: " +treeWalk.getPathString()+"Cyclomatic:"+cal.calculateCC(newFile));
                		  
                		   cyclo.add(cal.calculateCC(newFile));
                		   //val.put(cal.calculateCC(newFile), treeWalk.getPathString());
                		   
                		   // test=temp1+":"+treeWalk.getPathString();
                		   // sendData(test);
                		    
                    }
                    
                   
                    
                   
            	    for(int i=0; i<cyclo.size(); i++){
            	        sum += cyclo.get(i);
            	    }
            	    sendData(sum,output);
                    
                /*ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = repository.open(objectId);
                File newFile = new File("tempReadFile");
        		  loader.copyTo(new FileOutputStream(newFile));
        		  CyclometicCalculation cal = new CyclometicCalculation();
        		  System.out.println(cal.calculateCC(newFile));*/
                
                }
		      /*StringBuilder jsonSb = new StringBuilder();
		      String line = null;
		      while ((line = reader.readLine()) != null) {
		        jsonSb.append(line);
		      }
		      json = jsonSb.toString();
               System.out.println(json);*/
		      // Converts JSON string to Java object
                walk.dispose();
                repository.close();
				connection.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			  }
		    
		    
			
			return sum;

			}
		  
	
	 public static void getRepo() throws IOException , GitAPIException{
	    	
    	 try (Git result = Git.cloneRepository()
                 .setURI(REMOTE_URL)
                 .setDirectory(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repoClient"))
                  .setCloneAllBranches(true)
                  
                 .call()) {
 	       
 	        System.out.println("Having repository: " + result.getRepository().getDirectory());
 	        
         }
    	
    	
    	
       FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setGitDir(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repo/.git")).readEnvironment()
                .findGitDir().build();

    
        repository.close();
		
    	
    	}

	
 
	 
	    /*@POST
	    @Path("/we")
		@Produces(MediaType.APPLICATION_JSON)*/
	 public String sendData(int sum, String output) {
			
			String JSONData = null;
			
			
			try {
				
				URL targetUrl = new URL(RESTURL);

	            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
	            httpConnection.setRequestMethod("POST");
	            httpConnection.setRequestProperty("Content-Type", "application/json");
	            httpConnection.setRequestProperty("Content-Language", "en-US");
	            httpConnection.setUseCaches(false);
	            httpConnection.setDoInput(true);
	            httpConnection .setDoOutput(true);
                httpConnection.connect();
	              ObjectMapper mapper = new ObjectMapper();
				 JSONData = mapper.writeValueAsString(sum);
				 JSONData=output+":"+JSONData;
				
				System.out.println(JSONData);
				OutputStream outputStream = httpConnection.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
	            osw.write(JSONData);
	            osw.flush();
	         
	         osw.close();
	         BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

	            while (in.readLine() != null) {
	            }
	            
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return JSONData;
          
	 }
	 }


