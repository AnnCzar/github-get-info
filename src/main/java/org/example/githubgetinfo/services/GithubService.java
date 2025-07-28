package org.example.githubgetinfo.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.githubgetinfo.exceptions.GitHubUserNotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GithubService {
    public GithubService() {
    }
    private static final String GITHUB_TOKEN = "github_token_here";
    // GitHub Personal Access Token - if we make too
    // many requests we need to authorize

    public String listNonForkRepositories(String username) throws Exception {


        try{
            String reposUrl = String.format("https://api.github.com/users/%s/repos", username);
            URL url = new URL(reposUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Authorization - uncomment if you need it
//            if (GITHUB_TOKEN != null && !GITHUB_TOKEN.isEmpty()) {
//                con.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
//            }

            int status = con.getResponseCode();
            if (status == 404) {
                throw  GitHubUserNotFoundException.create(username);
            }else if (status == 403) {
                throw new RuntimeException("Access forbidden: API rate limit exceeded or insufficient permissions");
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                String json = response.toString();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> repos = gson.fromJson(json, listType);
                List<Map<String, Object>> nonForkRepos = repos.stream()
                        .filter(repo -> !(Boolean) repo.get("fork"))
                        .toList();


                List<Map<String, String>> finalinfo = new ArrayList<>();

                for (Map<String, Object> repo : nonForkRepos) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", (String) repo.get("name"));
                    Map<String, Object> owner = (Map<String, Object>) repo.get("owner");
                    map.put("userlogin", (String) owner.get("login"));
                    String repoName = (String) repo.get("name");
                    List<Map<String, String>> branches = getBranches((String) owner.get("login"), repoName);
                    map.put("branches", branches.toString());

                    finalinfo.add(map);
                }

                return finalinfo.toString();
        }

        } catch (IOException e) {
            throw new RuntimeException("Error connecting to GitHub API", e);
        }
    }


    public List<Map<String, String>> getBranches(String owner, String repoName) {
        String branchesUrl = String.format("https://api.github.com/repos/%s/%s/branches", owner, repoName);

        try {
            URL url = new URL(branchesUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // Authorization - uncomment if you need it
//            if (GITHUB_TOKEN != null && !GITHUB_TOKEN.isEmpty()) {
//                con.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
//            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                String json = response.toString();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                List<Map<String, Object>> branches = gson.fromJson(json, listType);

                List<Map<String, String>> branchInfo = new ArrayList<>();
                for (Map<String, Object> branch : branches) {
                    Map<String, String> info = new HashMap<>();
                    info.put("name", (String) branch.get("name"));

                    Map<String, Object> commit = (Map<String, Object>) branch.get("commit");
                    info.put("lastCommitSha", (String) commit.get("sha"));

                    branchInfo.add(info);
                }
                return branchInfo;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching branches for repo: " + repoName, e);
        }
    }
}

