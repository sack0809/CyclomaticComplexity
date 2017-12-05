package com.cyclometic.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
@Path("/CyclometicClient") 
public class CyclometicClient {
	
	private static final String REMOTE_URL = "https://github.com/sack0809/ScalableComputing.git";
	
	
	@GET
    @Path("/")
	@Produces({ MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
	public HashMap<Integer, String> cc() throws GitAPIException {
		    HttpURLConnection connection = null;
		    BufferedReader reader = null;
		    String json = null;
		    int cc = 0;
		    HashMap<Integer,String> val=new HashMap<Integer,String>();
		    try {			
		      URL resetEndpoint = new URL("http://localhost:8080/ServerRESTAPI/rest/CyclometicServer/main");
		      connection = (HttpURLConnection) resetEndpoint.openConnection();
		      // Set request method to GET as required from the API
		      connection.setRequestMethod("GET");
		      connection.setRequestProperty("Accept", "application/json");
		      
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
				String line = null;
				while ((line = br.readLine()) != null) {
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
                		  
                		   cc=cal.calculateCC(newFile);
                		   System.out.println("found: " +treeWalk.getPathString()+"Cyclomatic:"+cc);
                		   val.put(cc, treeWalk.getPathString());
                		   
                          
                    }
                
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
			
			return val;

			}
		  
	
	 public static void getRepo() throws IOException , GitAPIException{
	    	
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
        getfile(repository);
        repository.close();
		
    	
    	}

	
  public static  void getfile(Repository repository) throws InvalidRemoteException, TransportException, GitAPIException, IOException {
	  
	  
       
            

	  
  }
  

}


