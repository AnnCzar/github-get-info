package org.example.githubgetinfo.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GitHubUserNotFoundException extends RuntimeException {
    public GitHubUserNotFoundException(String message) {
        super(message);
    }

    public static ResponseStatusException create(String username) {
      GitHubUserNotFoundException exception = new GitHubUserNotFoundException(String.format("Github user '%s' not found", username));
      return new ResponseStatusException(HttpStatusCode.valueOf(404), exception.getMessage(), exception);
    }
}
