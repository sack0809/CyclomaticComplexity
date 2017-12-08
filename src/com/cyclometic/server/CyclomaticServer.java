package com.cyclometic.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/CyclometicServer")
public class CyclomaticServer {

	private static final String REMOTE_URL = "https://github.com/sack0809/ScalableComputing.git";

	public void getRepo() throws IOException, GitAPIException {

		try (Git result = Git.cloneRepository().setURI(REMOTE_URL)
				.setDirectory(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repo")).setCloneAllBranches(true)

				.call()) {
			// Note: the call() returns an opened repository already which needs to be
			// closed to avoid file handle leaks!
			System.out.println("Having repository: " + result.getRepository().getDirectory());

		}

	}

	@SuppressWarnings("deprecation")

	@GET
	@Path("/main")
	@Produces(MediaType.APPLICATION_JSON)
	public String listCommits() throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException,
			IOException, GitAPIException {
		// TODO Auto-generated method stub
		Stack<String> commList = new Stack<String>();
		commList.push("END");
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File("/Users/playsafe/Desktop/Java/Assignment2/src/repo/.git"))
				.readEnvironment().findGitDir().build();
		PrintStream out = new PrintStream(new FileOutputStream("OutFile.txt"));
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

				RevCommit targetCommit = walk.parseCommit(repository.resolve(commit.getName()));

				for (Map.Entry<String, Ref> e : repository.getAllRefs().entrySet()) {
					if (e.getKey().startsWith(Constants.R_HEADS)) {
						if (walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {
							String foundInBranch = e.getValue().getName();
							if (branchName.equals(foundInBranch)) {
								foundInThisBranch = true;
								RevTree tree = targetCommit.getTree();
								TreeWalk treeWalk = new TreeWalk(repository);
								treeWalk.addTree(tree);
								treeWalk.setRecursive(false);
								System.out.println("Commit found: " + commit.getName());
								while (treeWalk.next()) {
									System.out.println("found: " + treeWalk.getPathString());

								}
								break;
							}
						}
					}
				}

				if (foundInThisBranch) {
					commList.push(commit.getName());

				}
			}

		}
		/// repository.close();
		return commList.pop();
		// return "0c1fe1361b9b966ec8cf5cdefcb99c48fad39bdf";

	}

	public HashMap<Integer, String> positionList = new HashMap<Integer, String>();

	@POST
	@Path("/graph")
	@Consumes("application/json")
	public Response addLocationMap(InputStream incomingData) {
		

		BufferedReader in = null;
		StringBuilder JSONBuilder = new StringBuilder();
		try {

			in = new BufferedReader(new InputStreamReader(incomingData));

			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				process(line);
				JSONBuilder.append(line + "\n");
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return Response.status(201).entity(JSONBuilder.toString()).build();

	}

	public void process(String s) {
		String[] p;
		p = s.split(":");
		String commitName = p[0];
		int cumuComp = Integer.parseInt(p[1]);

		positionList.put(cumuComp, commitName);
		System.out.println(commitName + "  " + cumuComp);
		GraphBuilder GB = new GraphBuilder("Cyclomatic Complexity", ("using Commit"), positionList);
		GB.dostuff();
	}

}
