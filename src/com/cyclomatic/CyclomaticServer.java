package com.cyclomatic;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
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
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement; 
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;  

@Path("/CyclometicServer") 
public class CyclomaticServer {
  
	private static final String REMOTE_URL = "https://github.com/sack0809/ScalableComputing.git";
	   
	   @GET 
	   @Path("/repo") 
	   @Produces(MediaType.TEXT_PLAIN) 
	
    public static String getRepo() throws IOException , GitAPIException{
    	
    	 try (Git result = Git.cloneRepository()
                 .setURI(REMOTE_URL)
                 .setDirectory(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repo"))
                  .setCloneAllBranches(true)
                  
                 .call()) {
 	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
 	        System.out.println("Having repository: " + result.getRepository().getDirectory());
 	        
         }
    	
    	
    	
    	FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setGitDir(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repo/.git")).readEnvironment()
                .findGitDir().build();

        listRepositoryContents(repository);

        repository.close();
		return "OK";
    	
    	}
    
	@SuppressWarnings("deprecation")
	
	
	private static void listRepositoryContents(Repository repository) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException, GitAPIException {
		// TODO Auto-generated method stub
		
		
		PrintStream out = new PrintStream(new FileOutputStream(
		          "OutFile.txt"));
		@SuppressWarnings("deprecation")
		Git git = new Git(repository);
		Ref head = repository.getRef("HEAD");

        // a RevWalk allows to walk over commits based on some filtering that is defined
        RevWalk walk = new RevWalk(repository);

        
        List<Ref> branches = git.branchList().call();
        for (Ref branch : branches) {
            String branchName = branch.getName();

            System.out.println("Commits of branch: " + branch.getName());
            System.out.println("-------------------------------------");

            Iterable<RevCommit> commits = git.log().all().call();

            for (RevCommit commit : commits) {
                boolean foundInThisBranch = false;

                RevCommit targetCommit = walk.parseCommit(repository.resolve(
                        commit.getName()));
                
                for (Map.Entry<String, Ref> e : repository.getAllRefs().entrySet()) {
                    if (e.getKey().startsWith(Constants.R_HEADS)) {
                        if (walk.isMergedInto(targetCommit, walk.parseCommit(
                                e.getValue().getObjectId()))) {
                            String foundInBranch = e.getValue().getName();
                            if (branchName.equals(foundInBranch)) {
                                foundInThisBranch = true;
                                RevTree tree = targetCommit.getTree();
                                TreeWalk treeWalk = new TreeWalk(repository);
                                treeWalk.addTree(tree);
                                treeWalk.setRecursive(false);
                                out.println("Commit found: "+ commit.getName());
                                while (treeWalk.next()) {
                                   out.println("found: " +treeWalk.getPathString());
                                   
                                   ObjectId objectId = treeWalk.getObjectId(0);
                                   ObjectLoader loader = repository.open(objectId);
                                   loader.copyTo(System.out);
                                    
                                }
                                break;
                            }
                        }
                    }
                }

                if (foundInThisBranch) {
                    System.out.println(commit.getName());
                    //System.out.println(commit.getAuthorIdent().getName());
                    //System.out.println(new Date(commit.getCommitTime()));
                    //System.out.println(commit.getFullMessage());
                }
            }
       

        
        
    
        
       
    }
	}
    
}
        
