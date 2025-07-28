package org.example.githubgetinfo.controllers;


import org.example.githubgetinfo.services.GithubService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/getInfo")
@CrossOrigin(origins = "*")
public class GithubController {
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    public String getUserLogin(@PathVariable String username) throws Exception {
        return githubService.listNonForkRepositories(username);


    }

}
